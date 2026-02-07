package com.discordlite.discord_lite.auth.service;

import com.discordlite.discord_lite.auth.entity.EmailVerification;
import com.discordlite.discord_lite.auth.enums.VerificationPurpose;
import com.discordlite.discord_lite.auth.repository.EmailVerificationRepository;
import com.discordlite.discord_lite.user.entity.User;
import com.discordlite.discord_lite.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class EmailVerificationService {
    private final MailService mailService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserService userService;

    @Transactional
    public void sendOtp(String email, VerificationPurpose verificationPurpose) {
        emailVerificationRepository.invalidateActiveOtps(email, verificationPurpose);

        String code = String.valueOf(
                new SecureRandom().nextInt(900000) + 100000
        );

        EmailVerification emailVerification = new EmailVerification();
        emailVerification.setEmail(email);
        emailVerification.setVerificationPurpose(verificationPurpose);
        emailVerification.setCode(code);
        emailVerification.setExpiresAt(Instant.now().plusSeconds(300));

        emailVerificationRepository.save(emailVerification);
        mailService.sendOtp(email, code);
    }

    @Transactional
    public void verifyOtp(String username, String code, VerificationPurpose purpose) {
        User user = userService.findByUsername(username);
        EmailVerification emailVerification = emailVerificationRepository.findLatestActiveOtp(user.getEmail() , code, purpose)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        if (emailVerification.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Otp Expires");
        }
        emailVerification.setUsed(true);
        emailVerificationRepository.save(emailVerification);
    }
}
