package com.jing.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Getter
@Setter
public class LoginInDTO  implements Serializable {

    @NotEmpty
    @Length(min = 1, max = 15)
    private String userId;

    @NotEmpty
    @Length(min = 6,max = 6)
    private String code;
}
