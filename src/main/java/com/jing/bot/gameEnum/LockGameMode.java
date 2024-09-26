package com.jing.bot.gameEnum;

import lombok.Getter;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot.gameEnum
 * @Project：IruruBot
 * @name：LockGameMode
 * @Date：2024/9/20 下午7:08
 * @Filename：LockGameMode
 */
@Getter
public enum LockGameMode {

    LOCK_GAME_MODE("1", "游戏模式"),

    LOCK_COUNTDOWN_MODE("2", "倒计时模式");

    private final String mode;
    private final String msg;

    LockGameMode(String mode, String msg) {
        this.mode = mode;
        this.msg = msg;
    }

    public static String getMsgByMode(String mode) {
        for (LockGameMode lockGameMode : LockGameMode.values()) {
            if (lockGameMode.getMode().equals(mode)) {
                return lockGameMode.getMsg();
            }
        }
        return null;
    }
}
