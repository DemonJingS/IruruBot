package com.jing.bot.gameEnum;

import lombok.Getter;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot.gameEnum
 * @Project：IruruBot
 * @name：LockEnum
 * @Date：2024/9/19 下午8:03
 * @Filename：LockEnum
 */

@Getter
public enum LockGameDifficulty {

    LOCK_SIMPLE("1", "简单"),
    LOCK_ORDINARY("2", "普通"),
    LOCK_HARD("3", "困难"),
    LOCK_HELL("4", "地狱");

    private final String code;
    private final String msg;

    LockGameDifficulty(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getMsgByDifficulty(String difficulty) {
        for (LockGameDifficulty lockEnum : LockGameDifficulty.values()) {
            if (lockEnum.code.equals(difficulty)) {
                return lockEnum.msg;
            }
        }
        return null;
    }
}
