package com.jing.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.jing.service.DeleteMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：lingJingBot
 * @name：DeleteMsgServiceImpl
 * @Date：2024/9/12 下午9:32
 * @Filename：DeleteMsgServiceImpl
 */

@Slf4j
@Service
public class DeleteMsgServiceImpl implements DeleteMsgService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * 删除消息
     *
     * @param telegramClient
     * @param message
     */
    @Override
    public void deleteMsgAndCommand(TelegramClient telegramClient, Message message) {
        scheduler.schedule(() -> {
            DeleteMessage deleteMessage = DeleteMessage.builder().chatId(message.getChatId()).messageId(message.getMessageId()).build();
            try {
                telegramClient.execute(deleteMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }, 10, TimeUnit.SECONDS);

    }

    @Override
    public void deleteBotCommandMessage(TelegramClient telegramClient, Message message) {
        scheduler.schedule(() -> {
            DeleteMessage deleteBotCommand = DeleteMessage.builder().chatId(message.getChatId()).messageId(message.getMessageId()).build();
            try {
                telegramClient.execute(deleteBotCommand);
            } catch (TelegramApiException e) {
                log.error("DeleteMessageService.deleteBotCommandMessage删除消息失败:{}", JSONObject.toJSONString(e));
            }
        }, 60, TimeUnit.SECONDS);

    }


    @Override
    public void deleteButton(TelegramClient telegramClient, Message message) {
        scheduler.schedule(() -> {
            try {
                EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                        .chatId(message.getChatId().toString())
                        .messageId(message.getMessageId())
                        .replyMarkup(null) // 移除按钮
                        .build();
                telegramClient.execute(editMessageReplyMarkup);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }, 60, TimeUnit.SECONDS);
    }

    @Override
    public void deleteNowButton(TelegramClient telegramClient, Message message) {
        scheduler.schedule(() -> {
            try {
                EditMessageReplyMarkup editMessageReplyMarkup = EditMessageReplyMarkup.builder()
                        .chatId(message.getChatId().toString())
                        .messageId(message.getMessageId())
                        .replyMarkup(null) // 移除按钮
                        .build();
                telegramClient.execute(editMessageReplyMarkup);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }

        }, 5, TimeUnit.SECONDS);
    }
}
