package com.jing.bot.botEnum;

import lombok.Getter;

/**
 * @Author：DemonJing
 * @Package：com.jing.utils
 * @Project：lingJingBot
 * @name：BotCommands
 * @Date：2024/9/8 下午1:34
 * @Filename：BotCommands
 */

@Getter
public enum BotCommands {

    START("/start"),

    VOTE("/vote"),

    CHINESE("/chinese"),

    SEE_LOCK("/seelock");


    private final String command;

    BotCommands(String command) {
        this.command = command;
    }
}
