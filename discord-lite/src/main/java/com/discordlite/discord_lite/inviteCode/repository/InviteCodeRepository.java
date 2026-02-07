package com.discordlite.discord_lite.inviteCode.repository;

import com.discordlite.discord_lite.inviteCode.entity.InviteCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteCodeRepository extends JpaRepository<InviteCode, Long> {

    Optional<InviteCode> findByInviteCodeLink(String inviteCodeLink);
}
