package com.jing.utils;

import com.jing.constant.GameConstant;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author：DemonJing
 * @Package：com.jing.utils
 * @Project：lingJingBot
 * @name：UserUtils
 * @Date：2024/9/8 下午9:21
 * @Filename：UserUtils
 */
public class UserUtils {

    /**
     * 合并用户名
     * 根据传入的Update对象，获取并合并用户的名字和姓氏
     * 如果用户没有姓氏，直接返回名字
     *
     * @param update 更新对象，包含用户的消息和用户信息
     * @return 用户的名字或名字与姓氏的组合
     */
    public static String combineUsername(Update update) {

        String firstName = update.getMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getFrom().getLastName();
        if (StringUtils.isBlank(lastName)) {
            return firstName;
        }else {
            return firstName + lastName;
        }
    }


    /**
     * 组合回复用户的用户名
     * 当用户回复一条消息并调用此方法时，它会获取被回复用户的姓和名
     * 如果姓或名不存在，则只返回存在的名字或姓加名字的组合
     *
     * @param update Telegram API的Update对象，用于获取回复消息的用户信息
     * @return 组合后的用户名，如果只有名没有姓，则只返回名
     */
    public static String combineReplyToUsername(Update update) {

        String firstName = update.getMessage().getReplyToMessage().getFrom().getFirstName();
        String lastName = update.getMessage().getReplyToMessage().getFrom().getLastName();
        if (StringUtils.isBlank(lastName)) {
            return firstName;
        } else {
            return firstName + lastName;
        }
    }

    public static String callbackQueryUsername(Update update) {

        String firstName = update.getCallbackQuery().getFrom().getFirstName();
        String lastName = update.getCallbackQuery().getFrom().getLastName();

        if (StringUtils.isBlank(lastName)) {
            return firstName;
        } else {
            return firstName + lastName;
        }
    }

    public static String entitiesUsername(Update update) {
        Message message = (Message) update.getCallbackQuery().getMessage();
        String firstName = message.getEntities().get(0).getUser().getFirstName();
        String lastName = message.getEntities().get(0).getUser().getLastName();

        if (StringUtils.isBlank(lastName)) {
            return firstName;
        } else {
            return firstName + lastName;
        }
    }


    public static boolean containsSpecialCharacters(String input) {
        Pattern pattern = Pattern.compile(GameConstant.SPECIAL_CHAR_REGEX);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }
}
