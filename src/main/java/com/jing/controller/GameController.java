package com.jing.controller;

import com.alibaba.fastjson2.JSONObject;
import com.jing.bot.botEnum.BotResponse;
import com.jing.bot.gameEnum.LockGameMode;
import com.jing.controller.dto.*;
import com.jing.mapper.entity.LockGame;
import com.jing.mapper.entity.LockGameLog;
import com.jing.service.LockGameService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller
 * @Project：IruruBot
 * @name：GameController
 * @Date：2024/9/18 下午10:24
 * @Filename：GameController
 */
@Slf4j
@Controller
public class GameController {

    @Resource
    private LockGameService lockGameService;


    @GetMapping("/main")
    public String gameMainPage() {
        return "main";
    }

    @GetMapping("/lockGame")
    public String gamePage() {
        return "lockGame";
    }

    @GetMapping("/timer")
    public String timerPage() {
        return "timer";
    }

    @GetMapping("/countdown")
    public String countdownPage() {
        return "countdown";
    }

    @GetMapping("/bluetooth")
    public String bluetoothPage() {
        return "bluetooth";
    }

    @GetMapping("/ciduo")
    public String ciduoPage() {
        return "ciduo";
    }

    @GetMapping("/roulette")
    public String roulettePage() {
        return "roulette";
    }

    @PostMapping("/showLockGameLog")
    @ResponseBody
    public List<LockLogOutDTO> showLockGameLog(@RequestBody @Valid PlayerInDTO playerInDTO) {
        List<LockGameLog> lockGameLogs = lockGameService.showLockGameLog(playerInDTO.getPlayerId());
        List<LockLogOutDTO> lockLogOutDTOS = new ArrayList<>();
        if (lockGameLogs == null) {
            return Collections.emptyList();
        }
        for (LockGameLog lockGameLog : lockGameLogs) {
            LockLogOutDTO lockLogOutDTO = new LockLogOutDTO();
            lockLogOutDTO.setAddName(lockGameLog.getOtherUserName());
            lockLogOutDTO.setTime(lockGameLog.getOvertime());
            lockLogOutDTOS.add(lockLogOutDTO);
        }
        return lockLogOutDTOS;
    }


    @PostMapping("/lockGameInfo")
    @ResponseBody
    public LockGameOutDTO lockGameCheck(@RequestBody @Valid PlayerInDTO playerInDTO) {
        LockGameOutDTO lockGameOutDTO = new LockGameOutDTO();
        LockGame lockGame = lockGameService.queryGameByGamerId(playerInDTO.getPlayerId());
        if (lockGame == null) {
            lockGameOutDTO.setUrl("/lockGame");
            return lockGameOutDTO;
        }
        if (StringUtils.equals(lockGame.getModeId(), LockGameMode.LOCK_GAME_MODE.getMode())) {
            lockGameOutDTO = lockGameService.lockGameMode(lockGame);
            return lockGameOutDTO;
        }
        if (StringUtils.equals(lockGame.getModeId(), LockGameMode.LOCK_COUNTDOWN_MODE.getMode())) {
            return lockGameService.lockCountdown(lockGame);
        }
        lockGameOutDTO.setUrl("/error");
        return lockGameOutDTO;
    }

    @PostMapping("/startGameMode")
    @ResponseBody
    public LockGameOutDTO startLockGame(@RequestBody @Valid LockGameInDTO lockGameInDTO) {
        return lockGameService.saveLockGameMode(lockGameInDTO);
    }

    @PostMapping("/startCountdown")
    @ResponseBody
    public LockGameOutDTO startCountdown(@RequestBody @Valid LockGameInDTO lockGameInDTO) {
        return lockGameService.saveLockGameMode(lockGameInDTO);
    }


    @PostMapping("/continueChallenge")
    @ResponseBody
    public ResponseOutDTO continueChallenge(@RequestBody @Valid ContinueChallengeInDTO continueChallengeInDTO) {
        return lockGameService.continueChallenge(continueChallengeInDTO);
    }

    @PostMapping("/minusTime")
    @ResponseBody
    public ResponseOutDTO minusTime(@RequestBody @Valid MinusTimeInDTO minusTimeInDTO) {
        log.info("minusTimeInDTO:{}", JSONObject.toJSONString(minusTimeInDTO));
        return lockGameService.minusTime(minusTimeInDTO);
    }
}


