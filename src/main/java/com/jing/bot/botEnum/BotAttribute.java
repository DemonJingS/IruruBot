package com.jing.bot.botEnum;

import lombok.Getter;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot.botEnum
 * @Project：IruruBot
 * @name：BotAttribute
 * @Date：2024/9/18 下午8:47
 * @Filename：BotAttribute
 */
@Getter
public enum BotAttribute {
    IS_BOT_COMMAND("bot_command"),

    PRIVATE("private"),

    SUPERGROUP("supergroup");

    private final String type;

    BotAttribute(String type) {
        this.type = type;
    }
}
