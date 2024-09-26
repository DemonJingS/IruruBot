package com.jing.service;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @Author：DemonJing
 * @Package：com.jing.service
 * @Project：IruruBot
 * @name：SendMsgService
 * @Date：2024/9/21 下午11:04
 * @Filename：SendMsgService
 */

public interface SendMsgService {

    /**
     * 发送消息
     * @param telegramClient
     * @param chatId
     * @param msg
     * @return
     */
    Message sendHtmlMsg(TelegramClient telegramClient, String chatId, String msg);

    /**
     * 发送带按钮的消息
     * @param telegramClient
     * @param inlineKeyboardMarkup
     * @param chatId
     * @param msg
     * @return
     */
    Message sendWithButtonMsg(TelegramClient telegramClient, InlineKeyboardMarkup inlineKeyboardMarkup, String chatId, String msg);

    /**
     * 发送编辑消息
     * @param telegramClient
     * @param editMessageText
     */
    void sendEditMessage(TelegramClient telegramClient, EditMessageText editMessageText) throws TelegramApiException;

    Message sendChinesePack(TelegramClient telegramClient, Update update);
}
