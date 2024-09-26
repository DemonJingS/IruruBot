package com.jing.bot.botEnum;

import lombok.Getter;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot.botEnum
 * @Project：IruruBot
 * @name：UserStatus
 * @Date：2024/9/22 上午12:05
 * @Filename：UserStatus
 */
@Getter
public enum UserStatus {

    USER_STATUS_NORMAL("1", "正常"),

    USER_STATUS_BAN("2", "封禁");

    private final String status;

    private final String msg;

    UserStatus(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static String getMsgByStatus(String status) {
        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.status.equals(status)) {
                return userStatus.msg;
            }
        }
        return null;
    }
}
