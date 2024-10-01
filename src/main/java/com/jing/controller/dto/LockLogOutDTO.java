package com.jing.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LockLogOutDTO implements Serializable {

    private String addName;

    private String time;

    public LockLogOutDTO() {
    }
}
