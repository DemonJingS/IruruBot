package com.jing.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：LockGameInDTO
 * @Date：2024/9/19 下午8:01
 * @Filename：LockGameInDTO
 */

@Getter
@Setter
public class LockGameInDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8043287382577404190L;

    @NotEmpty
    @Length(min = 1, max = 15)
    @Pattern(regexp = "^[1-9]\\d*$",message = "攻击者id")
    private String gamerId;

    @NotEmpty
    @Length(min = 1, max = 30)
    private String gameName;

    @Length(min = 1, max = 15)
    private String tgUserName;

    @NotEmpty
    private String mode;

    private String difficulty;

    @Length(min = 1, max = 7)
    @Pattern(regexp = "^[1-9]\\d*$",message = "格式错误")
    private String time;

    public LockGameInDTO() {
    }
}
