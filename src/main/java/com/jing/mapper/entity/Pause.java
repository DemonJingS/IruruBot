package com.jing.mapper.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper.entity
 * @Project：IruruBot
 * @name：Pause
 * @Date：2024/9/20 下午9:45
 * @Filename：Pause
 */

@Getter
@Setter
public class Pause {

    private String userId;

    private String lockId;

    private String pauseTime;

    private String continueTime;

    private Integer pauseCount;

    public Pause() {
    }
}
