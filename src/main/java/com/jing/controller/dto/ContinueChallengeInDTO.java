package com.jing.controller.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller.dto
 * @Project：IruruBot
 * @name：ContinueChallengeInDTO
 * @Date：2024/9/21 下午1:22
 * @Filename：ContinueChallengeInDTO
 */
@Getter
@Setter
public class ContinueChallengeInDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8600399036576828789L;

    private PlayerInDTO playerInDTO;

    @Length(min = 17, max = 17)
    @Pattern(regexp = "\\d+")
    private String lockId;

}
