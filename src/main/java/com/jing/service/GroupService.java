package com.jing.service;

import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @Author：DemonJing
 * @Package：com.jing.service
 * @Project：IruruBot
 * @name：GroupService
 * @Date：2024/9/18 下午9:40
 * @Filename：GroupService
 */
public interface GroupService {

    boolean checkGroup(String groupId);

    /**
     * 设置群组命令
     * @param telegramClient
     */
    void setGroupCommands(TelegramClient telegramClient);
}
