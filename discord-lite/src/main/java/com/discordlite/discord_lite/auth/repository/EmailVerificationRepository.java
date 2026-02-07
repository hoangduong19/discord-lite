package com.discordlite.discord_lite.auth.repository;

import com.discordlite.discord_lite.auth.entity.EmailVerification;
import com.discordlite.discord_lite.auth.enums.VerificationPurpose;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    @Query("""
        SELECT ev FROM EmailVerification ev
        WHERE ev.email = :email
          AND ev.verificationPurpose = :purpose
          AND ev.used = false
          AND ev.code = :code
        ORDER BY ev.expiresAt DESC
    """)
    Optional<EmailVerification> findLatestActiveOtp(
            @Param("email") String email,
            @Param("code") String code,
            @Param("purpose") VerificationPurpose purpose
    );

    @Modifying
    @Transactional
    @Query("""
        UPDATE EmailVerification ev
        SET ev.used = true
        WHERE ev.email = :email
          AND ev.verificationPurpose = :purpose
          AND ev.used = false
    """)
    void invalidateActiveOtps(
            @Param("email") String email,
            @Param("purpose") VerificationPurpose purpose
    );

}
