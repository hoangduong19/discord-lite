package com.discordlite.discord_lite.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;

    public void sendOtp(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Chatty - Email Verification");
        message.setText("""
                Your verification code is: %s

                This code will expire in 5 minutes.
                """.formatted(code));
        javaMailSender.send(message);
    }
}
