package com.discordlite.discord_lite.channel.repository;

import com.discordlite.discord_lite.channel.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    @Query("""
            SELECT c
            FROM Channel c
            JOIN ChannelUser cu1 ON cu1.channel = c
            JOIN ChannelUser cu2 ON cu2.channel = c
            WHERE c.type = com.discordlite.discord_lite.channel.enums.ChannelType.DIRECT
                AND cu1.user.userId = :userA
                AND cu2.user.userId = :userB
            """)
    Optional<Channel> findDirectChannelBetween(
            @Param("userA") Long userA,
            @Param("userB") Long userB
    );

    List<Channel> findByServer_ServerId(Long serverId);

}