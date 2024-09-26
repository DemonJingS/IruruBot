package com.jing.mapper.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.Period;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper.entity
 * @Project：IruruBot
 * @name：GroupInfo
 * @Date：2024/9/20 下午10:50
 * @Filename：GroupInfo
 */
@Getter
@Setter
public class GroupInfo {

    private String groupId;

    private String groupName;

    private String groupType;

    private String joinTime;

    private String status;

    public GroupInfo() {
    }
}
