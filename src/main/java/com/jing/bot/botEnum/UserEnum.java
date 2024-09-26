package com.jing.bot.botEnum;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot.botEnum
 * @Project：IruruBot
 * @name：UserEnum
 * @Date：2024/9/16 下午9:44
 * @Filename：UserEnum
 */
public enum UserEnum {


    USER_FLAG_BETA("1", "内测用户"),

    USER_FLAG_PUBLIC("2", "公测用户"),

    USER_STATUS_NORMAL("1", "正常"),

    USER_STATUS_BAN("2", "封禁");

    private final String code;

    private final String desc;

    UserEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
