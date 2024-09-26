package com.jing.mapper.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper.entity
 * @Project：lingJingBot
 * @name：User
 * @Date：2024/9/8 下午4:52
 * @Filename：User
 */

@Getter
@Setter
public class User {

    private String userId;

    private String userName;

    private String tgUserName;

    private String flag;

    private String status;

    private Integer addTimeNum;

    private Integer minusTimeNum;

    private String joinTime;

    private String lastTime;

    private String xpInfo;

    private String clothes;

    private String toys;

    private String betaId;

    private Integer version;

}
