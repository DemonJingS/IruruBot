package com.jing.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.jing.bot.botEnum.BotCommands;
import com.jing.config.IruruThreadPool;
import com.jing.constant.BotInfo;
import com.jing.constant.GameConstant;
import com.jing.mapper.GroupMapper;
import com.jing.mapper.entity.GroupInfo;
import com.jing.mapper.entity.User;
import com.jing.service.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：IruruBotServiceImpl
 * @Date：2024/9/16 下午8:33
 * @Filename：IruruBotServiceImpl
 */

@Slf4j
@Service
public class IruruBotServiceImpl implements IruruBotService {
    @Resource
    private DeleteMsgService deleteMsgService;

    @Resource
    private UserService userService;

    @Resource
    private GroupMapper groupMapper;

    @Resource
    private LockGameService lockGameService;

    @Resource
    private IruruThreadPool iruruThreadPool;

    @Resource
    private VoteService voteService;

    @Resource
    private SendMsgService sendMsgService;

    @Override
    public void mainFunction(TelegramClient telegramClient, Update update) {
        /*  iruruThreadPool.iruruThreadPool().execute(() -> {*/
        if (update.hasMessage()&&update.getMessage().hasText() && update.getMessage().isCommand()) {
            log.info("IruruBotService.mainFunction:{}", JSONObject.toJSONString(update));
            if (update.getMessage().isCommand() && update.getMessage().getChat().isUserChat()) {
                if (StringUtils.equals(BotCommands.START.getCommand(), update.getMessage().getText())) {
                    startRegisterUser(telegramClient, update);
                }
                return;
            }
            GroupInfo groupInfo = groupMapper.selectByGroupId(update.getMessage().getChatId().toString());
            if (groupInfo == null || !groupInfo.getStatus().equals("1")) {
                return;
            }
            deleteMsgService.deleteMsgAndCommand(telegramClient,update.getMessage());
            if (update.getMessage().getChat().isGroupChat() || update.getMessage().getChat().isSuperGroupChat()) {
                String command = StringUtils.deleteWhitespace(update.getMessage().getText());
                //查看带锁信息
                if (StringUtils.equals(command, BotCommands.SEE_LOCK.getCommand()) || StringUtils.equals(command, BotCommands.SEE_LOCK.getCommand() + BotInfo.BOT_NAME)) {
                    lockGameService.seeLockGameInfAndSendAddTimeButton(telegramClient, update);
                    return;
                }
                //投票
                if (StringUtils.equals(command, BotCommands.VOTE.getCommand()) || StringUtils.equals(command, BotCommands.VOTE.getCommand() + BotInfo.BOT_NAME)) {
                    lockGameService.voteUnLock(telegramClient, update);
                    return;
                }
                //中文
                if (StringUtils.equals(command, BotCommands.CHINESE.getCommand()) || StringUtils.equals(command, BotCommands.CHINESE.getCommand() + BotInfo.BOT_NAME)){
                    sendMsgService.sendChinesePack(telegramClient,update);
                    return;
                }
            }
            return;
        }
        log.info("IruruBotService.mainFunction:{}", JSONObject.toJSONString(update));
        if (update.hasCallbackQuery()) {
            //加时
            if (StringUtils.startsWith(update.getCallbackQuery().getData(), GameConstant.LOCK_GAME_ADD_TIME)) {
                lockGameService.addTime(telegramClient, update);
                return;
            }
            if (StringUtils.startsWith(update.getCallbackQuery().getData(), GameConstant.LOCK_GAME_AGREE) || StringUtils.startsWith(update.getCallbackQuery().getData(), GameConstant.LOCK_GAME_REFUSE)) {
                lockGameService.agreeOrRefuseUnlock(telegramClient, update);
                return;
            }

        }
        //  });
    }

    @Override
    public void startRegisterUser(TelegramClient telegramClient, Update update) {
        if (userService.registerUser(update)) {
            User user = userService.queryUserByUserId(update.getMessage().getFrom().getId().toString());
            if (user==null){
                return;
            }
            // 创建按钮并设置 Web App
            InlineKeyboardButton keyboardButton = InlineKeyboardButton.builder().text("请点击!").webApp(new WebAppInfo("https://noticeably-positive-bird.ngrok-free.app")).build();
            // 设置按钮行
            InlineKeyboardRow row = new InlineKeyboardRow();
            row.add(keyboardButton);
            // 创建键盘并设置到消息中
            InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(row).build();
            SendMessage sendMessage = SendMessage.builder().replyMarkup(inlineKeyboardMarkup).chatId(update.getMessage().getChatId().toString()).text("点击按钮开始使用").build();
            try {
                Message message = telegramClient.execute(sendMessage);
                deleteMsgService.deleteButton(telegramClient, message);
            } catch (TelegramApiException e) {
                log.error("IruruBotService.startRegisterUser error: {}", JSONObject.toJSONString(e));
            }
        }
    }
}
