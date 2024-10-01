package com.jing.service;

import com.jing.controller.dto.*;
import com.jing.mapper.entity.LockGame;
import com.jing.mapper.entity.LockGameLog;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

/**
 * @Author：DemonJing
 * @Package：com.jing.service
 * @Project：IruruBot
 * @name：LockGameService
 * @Date：2024/9/18 下午11:49
 * @Filename：LockGameService
 */
public interface LockGameService {

    /**
     * 查询玩家游戏
     * @param gamerId
     * @return
     */
    LockGame queryGameByGamerId(String gamerId);

    /**
     * 保存玩家游戏
     * @param lockGameInDTO
     * @return
     */
    LockGameOutDTO saveLockGameMode(LockGameInDTO lockGameInDTO);

    void saveOldLockGame(LockGame lockGame);

    /**
     * 删除并保存旧的游戏记录
     * @param lockGame
     */
    void delAndSaveOldLockGame(LockGame lockGame);

    /**
     * 显示游戏模式信息
     * @param lockGame
     * @return
     */
    LockGameOutDTO lockGameMode(LockGame lockGame);

    /**
     * 显示倒计时模式信息
     * @param lockGame
     * @return
     */
    LockGameOutDTO lockCountdown(LockGame lockGame);

    /**
     * 发送带锁信息增加按钮
     * @param telegramClient
     * @param update
     */
    void seeLockGameInfAndSendAddTimeButton(TelegramClient telegramClient , Update update);

    /**
     * 增加时间
     * @param telegramClient
     * @param update
     */
    void addTime(TelegramClient telegramClient , Update update) ;

    /**
     * 减少时间
     * @param minusTimeInDTO
     * @return
     */
    ResponseOutDTO minusTime(MinusTimeInDTO minusTimeInDTO);

    /**
     * 投票解锁
     * @param telegramClient
     * @param update
     */
    void voteUnLock(TelegramClient telegramClient, Update update);

    /**
     * 同意或拒绝解锁
     * @param telegramClient
     * @param update
     */
    void agreeOrRefuseUnlock(TelegramClient telegramClient, Update update);

    /**
     * 继续挑战
     * @param continueChallengeInDTO
     * @return
     */
    ResponseOutDTO continueChallenge(ContinueChallengeInDTO continueChallengeInDTO);

    CiDuoOutDTO ciDuo(CiDuoInDTO ciDuoInDTO);

    List<LockGameLog> showLockGameLog(String playerId);
}
