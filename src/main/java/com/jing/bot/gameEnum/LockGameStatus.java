package com.jing.bot.gameEnum;

import lombok.Getter;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot.gameEnum
 * @Project：IruruBot
 * @name：LockGameStatus
 * @Date：2024/9/20 下午7:04
 * @Filename：LockGameStatus
 */
@Getter
public enum LockGameStatus {

    LOCK_GAME_STATUS_START("1","正常状态"),
    LOCK_GAME_STATUS_PAUSE("2","暂停状态"),
    LOCK_GAME_STATUS_END("3","结束状态");

    private final String status;
    private final String msg;

    LockGameStatus(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static String getMsgByStatus(String status) {
        for (LockGameStatus lockGameStatus : LockGameStatus.values()) {
            if (lockGameStatus.getStatus().equals(status)) {
                return lockGameStatus.getMsg();
            }
        }
        return "没有带锁信息";
    }
}
