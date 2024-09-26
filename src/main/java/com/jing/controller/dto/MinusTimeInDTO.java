package com.jing.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：MinusTimeInDTO
 * @Date：2024/9/22 下午4:25
 * @Filename：MinusTimeInDTO
 */
@Getter
@Setter
public class MinusTimeInDTO {

    @NotEmpty
    @Length(min = 1, max = 15)
    @Pattern(regexp = "^[1-9]\\d*$",message = "攻击者id")
    private String userId;

    @NotEmpty
    @Length(min = 17, max = 25)
    private String lockId;

    public MinusTimeInDTO() {

    }
}
