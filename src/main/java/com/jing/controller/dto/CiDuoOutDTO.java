package com.jing.controller.dto;

import com.jing.mapper.entity.LockGame;
import com.jing.mapper.entity.LockGameLog;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：CiDuoOutDTO
 * @Date：2024/9/25 下午5:02
 * @Filename：CiDuoOutDTO
 */
@Getter
@Setter
public class CiDuoOutDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5097089866413869827L;

    private List<LockGame> lockGames;

    private List<LockGameLog> lockGameLogs;

    private String msg;

    public CiDuoOutDTO() {
    }
}
