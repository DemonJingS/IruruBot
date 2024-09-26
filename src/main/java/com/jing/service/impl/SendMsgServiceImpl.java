package com.jing.service.impl;

import com.jing.service.SendMsgService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：SendMsgServiceImpl
 * @Date：2024/9/21 下午11:05
 * @Filename：SendMsgServiceImpl
 */
@Service
public class SendMsgServiceImpl implements SendMsgService {
    @SneakyThrows
    @Override
    public Message sendHtmlMsg(TelegramClient telegramClient, String chatId, String msg) {
        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(msg).parseMode(ParseMode.HTML).build();
        return telegramClient.execute(sendMessage);
    }

    @SneakyThrows
    @Override
    public Message sendWithButtonMsg(TelegramClient telegramClient, InlineKeyboardMarkup inlineKeyboardMarkup, String chatId, String msg) {
        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(msg).replyMarkup(inlineKeyboardMarkup).build();
        Message message = telegramClient.execute(sendMessage);
        return message;
    }


    @Override
    public void sendEditMessage(TelegramClient telegramClient, EditMessageText editMessageText) throws TelegramApiException {
        telegramClient.execute(editMessageText);
    }

    @SneakyThrows
    @Override
    public Message sendChinesePack(TelegramClient telegramClient, Update update) {
        SendMessage sendMessage =SendMessage.builder().chatId(update.getMessage().getChatId()).
                replyToMessageId(update.getMessage().getMessageId()).text("简体中文语言包：tg://setlanguage?lang=zhcncc").parseMode(ParseMode.HTML).build();
        Message message = telegramClient.execute(sendMessage);
        return message;

    }
}
