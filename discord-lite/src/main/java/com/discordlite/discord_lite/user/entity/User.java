package com.discordlite.discord_lite.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    @Column(length = 255)
    private String displayName;
    @Column(nullable = false, unique = true, length = 255)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
    @Column
    private String avatar;
    @Column(nullable = false)
    private boolean enabled;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.enabled = true;
    }
}
