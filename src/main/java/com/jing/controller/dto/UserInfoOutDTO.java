package com.jing.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：UserInfoOutDTO
 * @Date：2024/9/18 下午5:24
 * @Filename：UserInfoOutDTO
 */
@Getter
@Setter
public class UserInfoOutDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -232374015837778993L;

    private String xpInfo;

    private String clothes;

    private String toys;

    private String betaId;

    private String statusMsg;

    private String addTimeNum;

    private String minusTimeNum;

    private String joinTime;

    public UserInfoOutDTO() {
    }
}
