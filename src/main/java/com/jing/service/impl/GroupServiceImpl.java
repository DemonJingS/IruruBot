package com.jing.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.jing.bot.GroupCommands;
import com.jing.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeAllGroupChats;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：GroupServiceImpl
 * @Date：2024/9/18 下午9:40
 * @Filename：GroupServiceImpl
 */

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {



    @Override
    public boolean checkGroup(String groupId) {

        return false;
    }

    @Override
    public void setGroupCommands(TelegramClient telegramClient) {
        try {
            GroupCommands groupCommands = new GroupCommands();
            List<BotCommand> botCommands = groupCommands.setGroupCommands();
            BotCommandScopeAllGroupChats botCommandScopeAllGroupChats = BotCommandScopeAllGroupChats.builder().build();
            SetMyCommands setAnyOneCommands = SetMyCommands.builder().commands(botCommands).scope(botCommandScopeAllGroupChats).build();
            telegramClient.execute(setAnyOneCommands);
        } catch (TelegramApiException e) {
            log.error("GroupService.setGroupCommands错误:{}", JSONObject.toJSONString(e));
        }
    }
}
