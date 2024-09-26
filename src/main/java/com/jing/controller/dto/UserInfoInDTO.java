package com.jing.controller.dto;

import jakarta.validation.constraints.NotEmpty;
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
 * @name：UserInfoInDTO
 * @Date：2024/9/16 下午10:15
 * @Filename：UserInfoInDTO
 */

@Getter
@Setter
public class UserInfoInDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8235695756846093187L;

    @NotEmpty
    @Length(min = 1, max = 15)
    private String userId;

    @Length(max = 30)
    private String xpInfo;

    @Length(max = 30)
    private String clothes;

    @Length(max = 30)
    private String toys;

    public UserInfoInDTO() {
    }
}
