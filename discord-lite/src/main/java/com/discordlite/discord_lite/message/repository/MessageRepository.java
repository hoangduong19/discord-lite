package com.discordlite.discord_lite.message.repository;

import com.discordlite.discord_lite.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("""
            SELECT m
            FROM Message m
            WHERE m.channel.channelId = :channelId
            ORDER BY m.createdAt ASC
            """
    )
    List<Message> findByChannelId(Long channelId);
}
