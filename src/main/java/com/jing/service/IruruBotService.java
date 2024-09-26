package com.jing.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @Author：DemonJing
 * @Package：com.jing.service
 * @Project：IruruBot
 * @name：IruruBotService
 * @Date：2024/9/16 下午8:32
 * @Filename：IruruBotService
 */

public interface IruruBotService {

    void mainFunction(TelegramClient telegramClient, Update update);

    void startRegisterUser(TelegramClient telegramClient, Update update);
}
