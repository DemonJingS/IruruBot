package com.jing.service;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @Author：DemonJing
 * @Package：com.jing.service
 * @Project：lingJingBot
 * @name：DeleteMsgService
 * @Date：2024/9/12 下午9:31
 * @Filename：DeleteMsgService
 */
public interface DeleteMsgService {

    void deleteMsgAndCommand(TelegramClient telegramClient, Message message);

    void deleteBotCommandMessage(TelegramClient telegramClient, Message message);

    void deleteButton(TelegramClient telegramClient, Message message);

    void deleteNowButton(TelegramClient telegramClient, Message message);

}
