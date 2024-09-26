package com.jing.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：ResponseOutDTO
 * @Date：2024/9/18 下午5:03
 * @Filename：ResponseOutDTO
 */
@Getter
@Setter
public class ResponseOutDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8398092596015060627L;

    private String code;

    private String url;

    private String message;

    private boolean flag;

    public ResponseOutDTO() {
    }

    public ResponseOutDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
