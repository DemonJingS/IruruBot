package com.jing.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.jing.bot.botEnum.BotCommands;
import com.jing.bot.botEnum.BotResponse;
import com.jing.bot.botEnum.UserStatus;
import com.jing.bot.gameEnum.LockGameDifficulty;
import com.jing.bot.gameEnum.LockGameMode;
import com.jing.bot.gameEnum.LockGameStatus;
import com.jing.constant.BotInfo;
import com.jing.constant.GameConstant;
import com.jing.constant.UserConstant;
import com.jing.controller.dto.*;
import com.jing.mapper.*;
import com.jing.mapper.entity.LockGame;
import com.jing.mapper.entity.LockGameLog;
import com.jing.mapper.entity.Pause;
import com.jing.mapper.entity.User;
import com.jing.service.*;
import com.jing.utils.UserUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：LockGameServiceImpl
 * @Date：2024/9/19 下午9:21
 * @Filename：LockGameServiceImpl
 */
@Slf4j
@Service
public class LockGameServiceImpl implements LockGameService {
    @Resource
    private LockGameMapper lockGameMapper;

    @Resource
    private FinishLockGameMapper finishLockGameMapper;

    @Resource
    private PauseMapper pauseMapper;

    @Resource
    private SendMsgService sendMsgService;

    @Resource
    private DeleteMsgService deleteMsgService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Resource
    private LockGameLogMapper lockGameLogMapper;

    @Resource
    private PauseService pauseService;

    private static final AtomicLong lockCounter = new AtomicLong(0);

    private final Map<String, Integer> agreeVotesMap = new HashMap<>();
    // 使用Map来存储拒绝投票结果
    private final Map<String, Integer> refuseVotesMap = new HashMap<>();
    // 使用Map来存储已投票用户
    private final Map<String, Set<String>> votedUsersMap = new HashMap<>();

    @Override
    public LockGame queryGameByGamerId(String gamerId) {
        return lockGameMapper.selectByGamerId(gamerId);
    }

    @Override
    public LockGameOutDTO saveLockGameMode(LockGameInDTO lockGameInDTO) {
        log.info("saveLockGameMode:{}", JSONObject.toJSONString(lockGameInDTO));
        LockGame lockGame = lockGameMapper.selectByGamerId(lockGameInDTO.getGamerId());
        LockGameOutDTO lockGameOutDTO = new LockGameOutDTO();
        if (lockGame == null) {

            String lockId = String.valueOf(Instant.now().toEpochMilli() * 10000 + lockCounter.incrementAndGet());
            lockGame = new LockGame();
            lockGame.setLockId(lockId);
            lockGame.setUserId(lockGameInDTO.getGamerId());
            lockGame.setUserName(lockGameInDTO.getGameName());
            lockGame.setTgUserName(lockGameInDTO.getTgUserName());
            lockGame.setModeId(lockGameInDTO.getMode());
            lockGame.setStatus(LockGameStatus.LOCK_GAME_STATUS_START.getStatus());
            lockGame.setPauseCount(GameConstant.LOCK_GAME_PAUSECOUNT_DEFAULT);
            lockGame.setDifficulty(lockGameInDTO.getDifficulty());
            if (LockGameMode.LOCK_GAME_MODE.getMode().equals(lockGameInDTO.getMode())) {
                lockGameOutDTO.setTime(String.valueOf(0));
                lockGameOutDTO.setUrl("/timer");
                lockGameOutDTO.setDifficultyMsg(LockGameDifficulty.getMsgByDifficulty(lockGameInDTO.getDifficulty()));
            }
            if (LockGameMode.LOCK_COUNTDOWN_MODE.getMode().equals(lockGameInDTO.getMode())) {
                lockGameOutDTO.setTime(String.valueOf(Long.parseLong(lockGameInDTO.getTime()) * 60));
                lockGameOutDTO.setUrl("/countdown");
                String endTime = LocalDateTime.now().plusMinutes(Integer.parseInt(lockGameInDTO.getTime())).format(GameConstant.DATE_TIME_FORMATTER);
                lockGame.setEndTime(endTime);
                lockGameOutDTO.setDifficultyMsg(LockGameDifficulty.LOCK_ORDINARY.getCode());
                lockGame.setDifficulty(LockGameDifficulty.LOCK_ORDINARY.getCode());
            }
            lockGameMapper.insertRegisterLockGame(lockGame);
            lockGameOutDTO.setLockId(lockId);
            lockGameOutDTO.setStatusMsg(LockGameStatus.getMsgByStatus(lockGame.getStatus()));
            lockGameOutDTO.setModeMsg(LockGameMode.getMsgByMode(lockGame.getModeId()));
            lockGameOutDTO.setStatus(lockGame.getStatus());
        }
        return lockGameOutDTO;
    }

    @Override
    public void saveOldLockGame(LockGame lockGame) {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAndSaveOldLockGame(LockGame lockGame) {
        lockGame.setStatus(LockGameStatus.LOCK_GAME_STATUS_END.getStatus());
        lockGameMapper.deleteFinishLockGame(lockGame.getUserId());
        finishLockGameMapper.insertFinishLockGame(lockGame);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public LockGameOutDTO lockGameMode(LockGame lockGame) {
        LockGameOutDTO lockGameOutDTO = new LockGameOutDTO();
        if (StringUtils.isNotEmpty(lockGame.getEndTime())) {
            LocalDateTime endTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
            if (endTime.isBefore(LocalDateTime.now())) {
                delAndSaveOldLockGame(lockGame);
                lockGameOutDTO.setUrl("/lockGame");
            } else {
                return getLockGameOutDTO(lockGame, lockGameOutDTO);
            }
        } else {
            return getLockGameOutDTO(lockGame, lockGameOutDTO);
        }
        return lockGameOutDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LockGameOutDTO lockCountdown(LockGame lockGame) {
        LockGameOutDTO lockGameOutDTO = new LockGameOutDTO();
        LocalDateTime endTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
        if (endTime.isBefore(LocalDateTime.now())) {
            delAndSaveOldLockGame(lockGame);
            lockGameOutDTO.setUrl("/lockGame");
        } else {
            lockGameOutDTO.setLockId(lockGame.getLockId());
            lockGameOutDTO.setStatusMsg(LockGameStatus.getMsgByStatus(lockGame.getStatus()));
            lockGameOutDTO.setModeMsg(LockGameMode.getMsgByMode(lockGame.getModeId()));
            lockGameOutDTO.setStatus(lockGame.getStatus());
            lockGameOutDTO.setDifficultyMsg(LockGameDifficulty.getMsgByDifficulty(lockGame.getDifficulty()));
            long seconds = Duration.between(LocalDateTime.now(), endTime).toSeconds();
            lockGameOutDTO.setTime(String.valueOf(seconds));
            lockGameOutDTO.setUrl("/countdown");
        }
        return lockGameOutDTO;
    }

    @Override
    public void seeLockGameInfAndSendAddTimeButton(TelegramClient telegramClient, Update update) {
        String seeLockCommand = StringUtils.deleteWhitespace(update.getMessage().getText());
        if (StringUtils.equals(seeLockCommand, BotCommands.SEE_LOCK.getCommand()) || StringUtils.equals(seeLockCommand, BotCommands.SEE_LOCK.getCommand() + BotInfo.BOT_NAME)) {
            String chatId = update.getMessage().getChatId().toString();
            if (update.getMessage().isReply()) {
                String replyUserId = update.getMessage().getReplyToMessage().getFrom().getId().toString();
                String replyUserName = UserUtils.combineReplyToUsername(update);
                checkAndSendButton(telegramClient, chatId, replyUserId, replyUserName);
                return;
            }
            if (!update.getMessage().isReply()) {
                String userId = update.getMessage().getFrom().getId().toString();
                String userName = UserUtils.combineUsername(update);
                checkAndSendButton(telegramClient, chatId, userId, userName);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addTime(TelegramClient telegramClient, Update update) {
        String addUserId = update.getCallbackQuery().getFrom().getId().toString();
        String addUserName = UserUtils.callbackQueryUsername(update);
        Message message = (Message) update.getCallbackQuery().getMessage();
        String oldText = message.getText();
        String data = update.getCallbackQuery().getData();
        String[] parts = data.split(":");
        String userId = parts[1];
        Integer messageId = message.getMessageId();
        String chatId = message.getChatId().toString();
        if (StringUtils.equals(addUserId, userId)) {
            return;
        }
        User user = userMapper.selectUserByUserIdForUpate(addUserId);
        if (user == null) {
            return;
        }
        if (StringUtils.equals(user.getStatus(), UserStatus.USER_STATUS_BAN.getStatus())) {
            return;
        }

        if (user.getAddTimeNum() >= UserConstant.USER_ADD_TIME_NUM_MAX) {
            return;
        }
        LockGame lockGame = lockGameMapper.selectByGamerId(userId);
        if (lockGame == null) {
            return;
        }
        int addTime = 0;
        String newEndTime = "";
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (StringUtils.equals(lockGame.getModeId(), LockGameMode.LOCK_GAME_MODE.getMode())) {
            if (StringUtils.equals(lockGame.getDifficulty(), LockGameDifficulty.LOCK_SIMPLE.getCode())) {
                addTime = random.nextInt(10, 21);
                if (lockGame.getEndTime() == null) {
                    newEndTime = LocalDateTime.now().plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                } else {
                    LocalDateTime oldEndTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
                    newEndTime = oldEndTime.plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                }
            }
            if (StringUtils.equals(lockGame.getDifficulty(), LockGameDifficulty.LOCK_ORDINARY.getCode())) {
                addTime = random.nextInt(30, 61);
                if (lockGame.getEndTime() == null) {
                    newEndTime = LocalDateTime.now().plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                } else {
                    LocalDateTime oldEndTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
                    newEndTime = oldEndTime.plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                }
            }
            if (StringUtils.equals(lockGame.getDifficulty(), LockGameDifficulty.LOCK_HARD.getCode())) {
                addTime = random.nextInt(90, 241);
                if (lockGame.getEndTime() == null) {
                    newEndTime = LocalDateTime.now().plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                } else {
                    LocalDateTime oldEndTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
                    newEndTime = oldEndTime.plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                }
            }
            if (StringUtils.equals(lockGame.getDifficulty(), LockGameDifficulty.LOCK_HELL.getCode())) {
                addTime = random.nextInt(480, 721);
                if (lockGame.getEndTime() == null) {
                    newEndTime = LocalDateTime.now().plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                } else {
                    LocalDateTime oldEndTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
                    newEndTime = oldEndTime.plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
                }
            }
        }
        if (StringUtils.equals(lockGame.getModeId(), LockGameMode.LOCK_COUNTDOWN_MODE.getMode())) {
            addTime = random.nextInt(30, 61);
            LocalDateTime oldEndTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
            newEndTime = oldEndTime.plusMinutes(addTime).format(GameConstant.DATE_TIME_FORMATTER);
        }
        LockGameLog lockGameLog = new LockGameLog();
        lockGameLog.setChatId(chatId);
        lockGameLog.setChatTitle(message.getChat().getTitle());
        lockGameLog.setLockId(lockGame.getLockId());
        lockGameLog.setLockUserId(lockGame.getUserId());
        lockGameLog.setLockUserName(lockGame.getUserName());
        lockGameLog.setLockTgUserName(lockGame.getTgUserName());
        lockGameLog.setOtherUserId(update.getCallbackQuery().getFrom().getId().toString());
        lockGameLog.setOtherUserName(UserUtils.callbackQueryUsername(update));
        lockGameLog.setOtherTgUserName(update.getCallbackQuery().getFrom().getUserName());
        lockGameLog.setOvertime(String.valueOf(addTime));
        userMapper.updateAddTimeNum(addUserId, user.getAddTimeNum() + 1);
        lockGameMapper.updateLockGameEndTime(userId, newEndTime);
        lockGameLogMapper.insertLockGameLog(lockGameLog);
        String addMsg = String.format("用户：%s 帮你增加了%s分钟!", addUserName, addTime);
        EditMessageText editMessageText = EditMessageText.builder()
                .messageId(messageId)
                .text(oldText + "\n" + addMsg)
                .chatId(chatId).parseMode(ParseMode.HTML).replyMarkup(setButton(userId))
                .build();
        try {
            sendMsgService.sendEditMessage(telegramClient, editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseOutDTO minusTime(MinusTimeInDTO minusTimeInDTO) {
        ResponseOutDTO responseOutDTO = new ResponseOutDTO();
        LockGame lockGame = lockGameMapper.selectByGamerId(minusTimeInDTO.getUserId());
        User user = userMapper.selectUserByUserIdForUpate(minusTimeInDTO.getUserId());
        if (lockGame == null || user == null) {
            responseOutDTO.setCode(BotResponse.UNKNOWN_ERROR.getCode());
            responseOutDTO.setMessage(BotResponse.UNKNOWN_ERROR.getMsg());
            return responseOutDTO;
        }
        if (StringUtils.isEmpty(lockGame.getEndTime())) {
            responseOutDTO.setCode(BotResponse.ERROR.getCode());
            responseOutDTO.setMessage(BotResponse.ERROR.getMsg());
            return responseOutDTO;
        }

        if (LocalDateTime.now().isAfter(LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER))) {
            delAndSaveOldLockGame(lockGame);
            responseOutDTO.setUrl("/main");
            return responseOutDTO;
        }
        if (user.getMinusTimeNum() >= UserConstant.USER_MINUS_TIME_NUM_MAX) {
            responseOutDTO.setMessage("今天没有次数了,明天再来吧");
            return responseOutDTO;
        }

        String msg = "";
        int minusTime = 0;
        LocalDateTime newEndTime = null;
        LocalDateTime oldEndTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_SIMPLE.getCode())) {
            minusTime = random.nextInt(10, 21);
            newEndTime = oldEndTime.minusMinutes(minusTime);
        }
        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_ORDINARY.getCode())) {
            minusTime = random.nextInt(30, 61);
            newEndTime = oldEndTime.minusMinutes(minusTime);
        }
        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_HARD.getCode())) {
            minusTime = random.nextInt(90, 241);
            newEndTime = oldEndTime.minusMinutes(minusTime);
        }

        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_HELL.getCode())) {
            minusTime = random.nextInt(480, 721);
            newEndTime = oldEndTime.minusMinutes(minusTime);
        }
        if (LocalDateTime.now().isAfter(newEndTime)) {
            delAndSaveOldLockGame(lockGame);
            responseOutDTO.setUrl("/main");
            return responseOutDTO;
        }
        if (newEndTime != null) {
            lockGameMapper.updateLockGameEndTime(lockGame.getUserId(), newEndTime.format(GameConstant.DATE_TIME_FORMATTER));
            userMapper.updateMinusTimeNum(lockGame.getUserId(), user.getMinusTimeNum() + 1);
            LockGameLog lockGameLog = new LockGameLog();

            lockGameLog.setLockId(lockGame.getLockId());
            lockGameLog.setLockUserId(lockGame.getUserId());
            lockGameLog.setLockUserName(lockGame.getUserName());
            lockGameLog.setLockTgUserName(lockGame.getTgUserName());
            lockGameLog.setOvertime("-" + minusTime);
            lockGameLogMapper.insertLockGameLog(lockGameLog);
        }
        msg = String.format("减少了%s分钟!", minusTime);
        responseOutDTO.setCode(BotResponse.SUCCESS.getCode());
        responseOutDTO.setMessage(msg);
        return responseOutDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void voteUnLock(TelegramClient telegramClient, Update update) {
        String userId = update.getMessage().getFrom().getId().toString();
        String chatId = update.getMessage().getChatId().toString();
        User user = userService.queryUserByUserId(userId);
        if (user == null) {
            return;
        }
        LockGame lockGame = queryGameByGamerId(userId);
        if (lockGame == null || LockGameStatus.LOCK_GAME_STATUS_PAUSE.getStatus().equals(lockGame.getStatus())) {
            return;
        }
        String endTime;
        if (StringUtils.isEmpty(lockGame.getEndTime())) {
            endTime = "暂时没有结束时间";
        } else {
            if (LocalDateTime.now().isAfter(LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER))) {
                delAndSaveOldLockGame(lockGame);
                return;
            } else {
                endTime = lockGame.getEndTime();
            }

        }
        String countMsg = "";
        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_SIMPLE.getCode())) {
            countMsg = "简单难度，（3/10）即可解锁！\n投票信息：\n";
        }
        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_ORDINARY.getCode())) {
            countMsg = "普通难度，（5/10）即可解锁！\n投票信息：\n";
        }
        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_HARD.getCode())) {
            countMsg = "困难难度，（7/10）即可解锁！\n投票信息：\n";
        }
        if (lockGame.getDifficulty().equals(LockGameDifficulty.LOCK_HELL.getCode())) {
            countMsg = "地狱难度，（9/10）即可解锁！\n投票信息：\n";
        }
        String voteMsg = String.format("用户：%s 请求解锁!\n开始时间：%s\n结束时间：%s\n%s", user.getUserName(), lockGame.getStartTime(), endTime, countMsg);
        InlineKeyboardButton agreeButton = InlineKeyboardButton.builder().text("同意!").callbackData("agree:" + userId).build();
        InlineKeyboardButton refuseButton = InlineKeyboardButton.builder().text("拒绝!").callbackData("refuse:" + userId).build();
        InlineKeyboardRow row = new InlineKeyboardRow(agreeButton, refuseButton);
        InlineKeyboardMarkup inlineKeyboardMarkup = InlineKeyboardMarkup.builder().keyboardRow(row).build();
        sendMsgService.sendWithButtonMsg(telegramClient, inlineKeyboardMarkup, chatId, voteMsg);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agreeOrRefuseUnlock(TelegramClient telegramClient, Update update) {
        String userid = update.getCallbackQuery().getFrom().getId().toString();
        String userName = UserUtils.callbackQueryUsername(update);
        String data = update.getCallbackQuery().getData();
        String[] parts = data.split(":");
        String voteType = parts[0];  // 'agree' or 'reject'
        String initiatedUserId = parts[1];
        if (userid.equals(initiatedUserId)) {
            return;
        }
        User user = userService.queryUserByUserId(userid);
        if (user == null) {
            return;
        }
        LockGame lockGame = queryGameByGamerId(initiatedUserId);
        if (lockGame.getStatus().equals(LockGameStatus.LOCK_GAME_STATUS_PAUSE.getStatus())) {
            return;
        }
        Set<String> votedUsers = votedUsersMap.get(initiatedUserId);
        if (votedUsers != null && votedUsers.size() >= 10) {
            log.info("投票人数已达上限: {}", initiatedUserId);
            return;
        }

        if (votedUsersMap.containsKey(initiatedUserId) && votedUsersMap.get(initiatedUserId).contains(userid)) {
            log.info("initiatedUserId: {},userid: {}", initiatedUserId, userid);
            return;
        } else {
            handleVote(userid, initiatedUserId, voteType.equals("agree") ? agreeVotesMap : refuseVotesMap, voteType);
        }

        String difficulty = lockGame.getDifficulty();
        int requiredAgreeVotes = getRequiredAgreeVotes(difficulty);  // 根据难度获取需要的同意票数
        Integer agreeVotes = agreeVotesMap.get(initiatedUserId);
        if (agreeVotes == null) {
            agreeVotes = 0;  // 如果没有找到对应的值，则设置为0
        }

        // 获取initiatedUserId对应的拒绝票数
        Integer refuseVotes = refuseVotesMap.get(initiatedUserId);
        if (refuseVotes == null) {
            refuseVotes = 0;  // 如果没有找到对应的值，则设置为0
        }
        String msg = "";
        Message message = (Message) update.getCallbackQuery().getMessage();
        if (agreeVotes >= requiredAgreeVotes) {
            // 达到同意票数，结束投票
            log.info("投票通过: initiatedUserId {}", initiatedUserId);
            int pauseCount = Integer.parseInt(lockGame.getPauseCount());
            String strPauseCount = String.valueOf(pauseCount + 1);
            lockGameMapper.updatePauseStatusAndPauseTime(initiatedUserId, LockGameStatus.LOCK_GAME_STATUS_PAUSE.getStatus(), strPauseCount);
            pauseService.savePause(lockGame);
            msg = "投票通过允许开锁！";
            agreeVotesMap.remove(initiatedUserId);
            votedUsersMap.remove(initiatedUserId);
            deleteMsgService.deleteNowButton(telegramClient, message);
            // 实现结束投票逻辑...
        } else if (refuseVotes > 10 - requiredAgreeVotes) {
            // 达到拒绝票数，结束投票
            log.info("投票未通过: initiatedUserId {}", initiatedUserId);
            msg = "不可以开锁哦！";
            refuseVotesMap.remove(initiatedUserId);
            votedUsersMap.remove(initiatedUserId);
            // 实现结束投票逻辑...
            deleteMsgService.deleteNowButton(telegramClient, message);
        }
        String voteTypeMsg;
        if (voteType.equals("agree")) {
            voteTypeMsg = "同意";
        } else {
            voteTypeMsg = "拒绝";
        }
        String oldText = message.getText();
        String format = String.format("%s %s你的请求。", userName, voteTypeMsg);
        String newText = oldText + "\n" + format + "\n" + msg;
        try {
            EditMessageText editMessageText = EditMessageText.builder().chatId(message.getChatId().toString()).messageId(message.getMessageId()).text(newText).replyMarkup(setAgreeAndRejectButton(initiatedUserId)).parseMode(ParseMode.HTML).build();
            sendMsgService.sendEditMessage(telegramClient, editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseOutDTO continueChallenge(ContinueChallengeInDTO continueChallengeInDTO) {
        ResponseOutDTO responseOutDTO = new ResponseOutDTO();
        String userId = continueChallengeInDTO.getPlayerInDTO().getPlayerId();
        LockGame lockGame = lockGameMapper.selectByGamerId(userId);
        LocalDateTime nowTime = LocalDateTime.now();
        if (lockGame == null) {
            responseOutDTO.setCode(BotResponse.ERROR.getCode());
            responseOutDTO.setUrl("/error");

        } else {
            LocalDateTime pauseTime = LocalDateTime.parse(lockGame.getPauseTime(), GameConstant.DATE_TIME_FORMATTER);
            long seconds = Duration.between(pauseTime, nowTime).getSeconds();
            if (lockGame.getEndTime() == null) {
                String endTime = nowTime.plusSeconds(seconds).format(GameConstant.DATE_TIME_FORMATTER);
                lockGameMapper.updateContinueChallenge(userId, endTime);
            } else {
                LocalDateTime endTime = LocalDateTime.parse(lockGame.getEndTime(), GameConstant.DATE_TIME_FORMATTER);
                String newEndTime = endTime.plusSeconds(seconds).format(GameConstant.DATE_TIME_FORMATTER);
                lockGameMapper.updateContinueChallenge(userId, newEndTime);
            }
            pauseMapper.updateContinueTime(userId, lockGame.getLockId());
            responseOutDTO.setCode(BotResponse.SUCCESS.getCode());
            responseOutDTO.setMessage(BotResponse.SUCCESS.getMsg());
            responseOutDTO.setUrl("/main");
        }
        return responseOutDTO;
    }

    @Override
    public CiDuoOutDTO ciDuo(CiDuoInDTO ciDuoInDTO) {
        CiDuoOutDTO ciDuoOutDTO = new CiDuoOutDTO();
        String userId = ciDuoInDTO.getUserId();
        List<LockGame> lockGames = finishLockGameMapper.selectByUserId(userId);
        if (lockGames.isEmpty()) {
            ciDuoOutDTO.setMsg("暂无记录");
            return ciDuoOutDTO;
        }
        return null;
    }

    @Override
    public List<LockGameLog> showLockGameLog(String playerId) {
        LockGame lockGame = lockGameMapper.selectByGamerId(playerId);
        if (lockGame == null) {
            return null;
        }
        return lockGameLogMapper.selectByLockId(lockGame.getLockId());

    }

    // 根据难度返回所需的同意票数
    private int getRequiredAgreeVotes(String difficulty) {
        return switch (difficulty) {
            case "1" -> // 简单难度
                    3;
            case "2" -> // 中等难度
                    5;
            case "3" -> // 困难难度
                    7;
            case "4" -> // 极限难度
                    9;
            default -> throw new IllegalArgumentException("未知难度: " + difficulty);
        };
    }

    private void handleVote(String userId, String initiatedUserId, Map<String, Integer> voteMap, String voteType) {
        voteMap.putIfAbsent(initiatedUserId, 0);
        voteMap.put(initiatedUserId, voteMap.get(initiatedUserId) + 1);
        Set<String> votedUsers = votedUsersMap.computeIfAbsent(initiatedUserId, k -> new HashSet<>());
        votedUsers.add(userId);

    }

    @NotNull
    private LockGameOutDTO getLockGameOutDTO(LockGame lockGame, LockGameOutDTO lockGameOutDTO) {
        lockGameOutDTO.setLockId(lockGame.getLockId());
        lockGameOutDTO.setStatusMsg(LockGameStatus.getMsgByStatus(lockGame.getStatus()));
        lockGameOutDTO.setModeMsg(LockGameMode.getMsgByMode(lockGame.getModeId()));
        lockGameOutDTO.setDifficultyMsg(LockGameDifficulty.getMsgByDifficulty(lockGame.getDifficulty()));
        lockGameOutDTO.setStatus(lockGame.getStatus());
        lockGameOutDTO.setEndTime(lockGame.getEndTime());
        LocalDateTime startTime = LocalDateTime.parse(lockGame.getStartTime(), GameConstant.DATE_TIME_FORMATTER);
        long seconds = Duration.between(startTime, LocalDateTime.now()).toSeconds();

      /*  if (lockGame.getStatus().equals(LockGameStatus.LOCK_GAME_STATUS_START.getStatus())) {

        } else {
            seconds = Duration.between(startTime, LocalDateTime.parse(lockGame.getPauseTime(), GameConstant.DATE_TIME_FORMATTER)).toSeconds();
        }*/

        List<Pause> pauses = pauseMapper.selectPauses(lockGame.getUserId(), lockGame.getLockId());
        if (pauses.isEmpty()) {
            lockGameOutDTO.setTime(String.valueOf(seconds));
        } else {
            long totalDuration = 0;
            for (Pause pause : pauses) {
                LocalDateTime pauseDateTime = LocalDateTime.parse(pause.getPauseTime(), GameConstant.DATE_TIME_FORMATTER);
                LocalDateTime continueDateTime;
                if (pause.getContinueTime() == null) {
                    continueDateTime = LocalDateTime.now();
                } else {
                    continueDateTime = LocalDateTime.parse(pause.getContinueTime(), GameConstant.DATE_TIME_FORMATTER);
                }

                long durationInMillis = Duration.between(pauseDateTime, continueDateTime).toSeconds();
                totalDuration += durationInMillis;
            }
            lockGameOutDTO.setTime(String.valueOf(seconds - totalDuration));
        }
        lockGameOutDTO.setUrl("/timer");
        return lockGameOutDTO;

    }

    private void checkAndSendButton(TelegramClient telegramClient, String chatId, String userId, String userName) {
        String seeLockMsg;
        LockGame lockGame = queryGameByGamerId(userId);
        if (lockGame == null) {
            seeLockMsg = String.format("没有用户：<b><a href=\"tg://user?id=%s\">%s</a></b> 的带锁信息!", userId, userName);
            Message message = sendMsgService.sendHtmlMsg(telegramClient, chatId, seeLockMsg);
            deleteMsgService.deleteMsgAndCommand(telegramClient, message);
            return;
        }
        String gameEndTime = lockGame.getEndTime();
        String starTime = lockGame.getStartTime();
        String statusMsg = LockGameStatus.getMsgByStatus(lockGame.getStatus());
        String modeMsg = LockGameMode.getMsgByMode(lockGame.getModeId());
        String difficultyMsg = LockGameDifficulty.getMsgByDifficulty(lockGame.getDifficulty());
        if (StringUtils.isEmpty(gameEndTime)) {
            seeLockMsg = String.format("用户名：%s\n状态：%s\n模式：%s\n难度：%s\n开始时间：%s\n结束时间：%s\n请给我狠狠加时!",
                    userName, statusMsg, modeMsg, difficultyMsg, starTime, "暂时没有结束时间");
            Message message = sendMsgService.sendWithButtonMsg(telegramClient, setButton(userId), chatId, seeLockMsg);
            deleteMsgService.deleteBotCommandMessage(telegramClient, message);
            return;
        }
        LocalDateTime endTime = LocalDateTime.parse(gameEndTime, GameConstant.DATE_TIME_FORMATTER);
        if (LocalDateTime.now().isAfter(endTime)) {
            lockGame.setStatus(LockGameStatus.LOCK_GAME_STATUS_END.getStatus());
            finishLockGameMapper.insertFinishLockGame(lockGame);
            lockGameMapper.deleteFinishLockGame(lockGame.getUserId());
            seeLockMsg = String.format("用户：<b><a href=\"tg://user?id=%s\">%s</a></b> 已经完成了挑战请重新开始吧!", userId, userName);
            Message message = sendMsgService.sendHtmlMsg(telegramClient, chatId, seeLockMsg);
            deleteMsgService.deleteMsgAndCommand(telegramClient, message);

        } else {
            seeLockMsg = String.format("用户名：%s\n状态：%s\n模式：%s\n难度：%s\n开始时间：%s！\n结束时间：%s！\n请给我狠狠加时!",
                    userName, statusMsg, modeMsg, difficultyMsg, starTime, gameEndTime);
            Message message = sendMsgService.sendWithButtonMsg(telegramClient, setButton(userId), chatId, seeLockMsg);
            deleteMsgService.deleteBotCommandMessage(telegramClient, message);
        }
    }

    private InlineKeyboardMarkup setButton(String userId) {
        InlineKeyboardButton addTimeButton = InlineKeyboardButton.builder().text("请务必帮我狠狠加时!").callbackData("addTime:" + userId).build();
        InlineKeyboardRow row = new InlineKeyboardRow();
        row.add(addTimeButton);
        return InlineKeyboardMarkup.builder().keyboardRow(row).build();
    }

    private InlineKeyboardMarkup setAgreeAndRejectButton(String userId) {
        InlineKeyboardButton agreeButton = InlineKeyboardButton.builder().text("同意!").callbackData("agree:" + userId).build();
        InlineKeyboardButton refuseButton = InlineKeyboardButton.builder().text("拒绝!").callbackData("refuse:" + userId).build();
        InlineKeyboardRow row = new InlineKeyboardRow(agreeButton, refuseButton);
        return InlineKeyboardMarkup.builder().keyboardRow(row).build();
    }

}
