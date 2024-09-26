package com.jing.mapper.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LockGame {

    private String lockId;

    private String userId;

    private String userName;

    private String tgUserName;

    private String status;

    private String modeId;

    private String pauseCount;

    private String difficulty;

    private String joinTime;

    private String startTime;

    private String endTime;

    private String pauseTime;


}