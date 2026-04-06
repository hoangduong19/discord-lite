package com.discordlite.discord_lite.channel.entity;

import java.util.ArrayList;
import java.util.List;

import com.discordlite.discord_lite.channel.enums.ChannelType;
import com.discordlite.discord_lite.channelUser.entity.ChannelUser;
import com.discordlite.discord_lite.server.entity.Server;
import com.discordlite.discord_lite.user.entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "channels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id", nullable = false, updatable = false)
    private Long channelId;

    @Column(name = "channel_name", nullable = false, length = 255)
    private String channelName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChannelType type;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChannelUser> members = new ArrayList<>();

    public void addMember(User user) {
        ChannelUser channelUser = new ChannelUser(this, user);
        this.members.add(channelUser);
    }

    public static Channel createDirectMessage(User a, User b) {
        Channel channel = new Channel();
        channel.setType(ChannelType.DIRECT);
        channel.setChannelName("DM"); // Hoặc logic tên: a.getName() + " & " + b.getName()
        channel.setServer(null);
        
        // Tận dụng luôn hàm addMember đã viết
        channel.addMember(a);
        channel.addMember(b);
        
        return channel;
    }
}
