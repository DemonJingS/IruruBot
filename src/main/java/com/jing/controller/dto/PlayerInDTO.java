package com.jing.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
 * @name：PlayerInDTO
 * @Date：2024/9/16 下午11:16
 * @Filename：PlayerInDTO
 */
@Getter
@Setter
public class PlayerInDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1476060173813429444L;

    @NotEmpty
    @Length(min = 1, max = 15)
    private String playerId;

    @NotEmpty
    @Length(min = 1, max = 30)
    private String playerName;

    @Length(max = 15)
    private String tgName;

    public PlayerInDTO() {
    }
}
