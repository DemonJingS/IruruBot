package com.jing.utils;

import com.jing.bot.botEnum.BotAttribute;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

/**
 * @Author：DemonJing
 * @Package：com.jing.utils
 * @Project：lingJingBot
 * @name：MessageUtils
 * @Date：2024/9/8 下午1:09
 * @Filename：MessageUtils
 */
public class MessageUtils {

    public static boolean isCommand(Update update) {
        Message message = update.getMessage();
        if (message == null) {
            return false;
        }
        if (message.hasEntities()) {
            for (MessageEntity entity : message.getEntities()) {
                if (StringUtils.equals(BotAttribute.IS_BOT_COMMAND.getType(), entity.getType())) {
                    return true;
                }
            }
        }
        return false;
    }
}
