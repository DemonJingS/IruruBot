package com.jing.bot;

import com.jing.service.GroupService;
import com.jing.service.IruruBotService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @Author：DemonJing
 * @Package：com.jing.bot
 * @Project：IruruBot
 * @name：IruruBot
 * @Date：2024/9/16 下午8:22
 * @Filename：IruruBot
 */
@Getter
@Component
public class IruruBot  implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    @Resource
    private IruruBotService iruruBotService;

    @Resource
    private GroupService groupService;

    private final TelegramClient telegramClient;

    @Value("${telegram.bot.token}")
    private String botToken;

    public IruruBot(@Value("${telegram.bot.token}")String botToken) {
        this.telegramClient = new OkHttpTelegramClient(botToken);
    }

    @PostConstruct
    public void init() {
        groupService.setGroupCommands(this.telegramClient);
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        iruruBotService.mainFunction(telegramClient, update);
    }
}
