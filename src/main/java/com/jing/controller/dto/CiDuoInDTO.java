package com.jing.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：CiDuoInDTO
 * @Date：2024/9/25 下午3:51
 * @Filename：CiDuoInDTO
 */
@Getter
@Setter
public class CiDuoInDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5748616646008055082L;

    private String userId;

    private String page;

    private String prevPage;

    private String nextPage;

    private String jumpPage;
}
