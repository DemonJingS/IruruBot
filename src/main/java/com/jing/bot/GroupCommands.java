package com.jing.bot;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot
 * @Project：lingJingBot
 * @name：GroupCommands
 * @Date：2024/9/14 下午12:47
 * @Filename：GroupCommands
 */
public class GroupCommands {

    public List<BotCommand> setGroupCommands() {
        return List.of(
                new BotCommand("/seelock","发送或回复显示自己或他人带锁信息"),
                new BotCommand("/vote","投票解锁"),
                new BotCommand("/chinese","中文语言包")
        );
    }
}
