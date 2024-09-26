package com.jing.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：LockGameOutDTO
 * @Date：2024/9/19 下午8:08
 * @Filename：LockGameOutDTO
 */
@Getter
@Setter
public class LockGameOutDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 6775757692740486220L;

    /**
     * 模式信息
     */
    private String modeMsg;

    private String status;
    /**
     * 状态信息
     */
    private String statusMsg;

    /**
     * 时间
     */
    private String time;

    /**
     * 锁编号
     */
    private String lockId;

    /**
     * 难度信息
     */
    private String difficultyMsg;

    /**
     * url
     */
    private String url;

    /**
     * 自定义信息
     */
    private String msg;

    private String endTime;

    public LockGameOutDTO() {
    }
}
