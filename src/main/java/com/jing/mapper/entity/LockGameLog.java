package com.jing.mapper.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper.entity
 * @Project：IruruBot
 * @name：LockGameLog
 * @Date：2024/9/22 上午12:25
 * @Filename：LockGameLog
 */
@Getter
@Setter
public class LockGameLog {
    private String chatId;

    private String chatTitle;

    private String lockId;

    private String lockUserId;

    private String lockUserName;

    private String lockTgUserName;

    private String otherUserId;

    private String otherUserName;

    private String otherTgUserName;

    private String overtime;

    private String operationTime;

    public LockGameLog() {
    }
}
