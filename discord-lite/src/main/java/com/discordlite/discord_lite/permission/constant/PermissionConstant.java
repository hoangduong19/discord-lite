package com.discordlite.discord_lite.permission.constant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class PermissionConstant {
    // Server
    public static final String VIEW_SERVER = "VIEW_SERVER";
    public static final String MANAGE_SERVER = "MANAGE_SERVER";
    public static final String CREATE_INVITE = "CREATE_INVITE";
    public static final String MANAGE_ROLES = "MANAGE_ROLES";
    public static final String KICK_MEMBER = "KICK_MEMBER";

    // Channel
    public static final String VIEW_CHANNEL = "VIEW_CHANNEL";
    public static final String CREATE_CHANNEL = "CREATE_CHANNEL";
    public static final String DELETE_CHANNEL = "DELETE_CHANNEL";
    public static final String MANAGE_CHANNEL = "MANAGE_CHANNEL";

    // Message
    public static final String SEND_MESSAGE = "SEND_MESSAGE";
    public static final String READ_MESSAGE = "READ_MESSAGE";
    public static final String DELETE_MESSAGE = "DELETE_MESSAGE";

    // Admin
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
}
