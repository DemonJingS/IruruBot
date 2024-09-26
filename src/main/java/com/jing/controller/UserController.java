package com.jing.controller;

import com.jing.bot.botEnum.BotResponse;
import com.jing.bot.gameEnum.LockGameStatus;
import com.jing.constant.GameConstant;
import com.jing.controller.dto.PlayerInDTO;
import com.jing.controller.dto.ResponseOutDTO;
import com.jing.controller.dto.UserInfoInDTO;
import com.jing.controller.dto.UserInfoOutDTO;
import com.jing.mapper.entity.LockGame;
import com.jing.mapper.entity.User;
import com.jing.service.LockGameService;
import com.jing.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author：DemonJing
 * @Package：com.jing.controller
 * @Project：IruruBot
 * @name：UserController
 * @Date：2024/9/17 14:06
 * @Filename：UserController
 */

@Slf4j
@Controller
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private LockGameService lockGameService;

    @GetMapping("/")
    public String welcomePage() {
        return "welcome";
    }

    @GetMapping("/user")
    public String userPage() {
        return "user";
    }

    @GetMapping("/ceshi")
    public String ceshi(){
        return "111";
    }

    @PostMapping("/checkPlayer")
    public @ResponseBody ResponseOutDTO getUser(@RequestBody @Valid PlayerInDTO playerInDTO) {
        ResponseOutDTO responseOutDTO = new ResponseOutDTO();
        if (playerInDTO == null) {
            responseOutDTO.setCode(BotResponse.ERROR.getCode());
            responseOutDTO.setMessage(BotResponse.ERROR.getMsg());
            responseOutDTO.setUrl("/error");
        } else {
            responseOutDTO = userService.queryAndReplaceUser(playerInDTO);
        }
        return responseOutDTO;
    }


    @ResponseBody
    @PostMapping("/user")
    public UserInfoOutDTO getUserInfo(@RequestBody @Valid PlayerInDTO playerInDTO) {
        User user = userService.queryUserByUserId(playerInDTO.getPlayerId());
        LockGame lockGame = lockGameService.queryGameByGamerId(playerInDTO.getPlayerId());
        if (user != null) {
            UserInfoOutDTO userInfoOutDTO = new UserInfoOutDTO();
            userInfoOutDTO.setXpInfo(user.getXpInfo());
            userInfoOutDTO.setClothes(user.getClothes());
            userInfoOutDTO.setToys(user.getToys());
            userInfoOutDTO.setJoinTime(user.getJoinTime());
            if (lockGame != null) {
                userInfoOutDTO.setStatusMsg(LockGameStatus.getMsgByStatus(lockGame.getStatus()));
                userInfoOutDTO.setAddTimeNum(String.valueOf(user.getAddTimeNum()));
                userInfoOutDTO.setMinusTimeNum(String.valueOf(user.getMinusTimeNum()));
            }
            if (StringUtils.equals(user.getFlag(), GameConstant.BETA_FLAG)) {
                userInfoOutDTO.setBetaId(user.getBetaId());
            }
            return userInfoOutDTO;  //
        }
        return null;
    }


    @ResponseBody
    @PostMapping("/user/update")
    public ResponseOutDTO savUserInfo(@RequestBody @Valid UserInfoInDTO userInfoInDTO) {
        ResponseOutDTO responseOutDTO = new ResponseOutDTO();
        String xpInfo = StringUtils.deleteWhitespace(userInfoInDTO.getXpInfo());
        String clothes = StringUtils.deleteWhitespace(userInfoInDTO.getClothes());
        String toys = StringUtils.deleteWhitespace(userInfoInDTO.getToys());
        if (StringUtils.isNotBlank(xpInfo) || StringUtils.isNotBlank(clothes) || StringUtils.isNotBlank(toys)) {
            userService.saveUserInfo(userInfoInDTO);
            responseOutDTO.setCode(BotResponse.SUCCESS.getCode());
            responseOutDTO.setFlag(true);
            return responseOutDTO;
        }
        responseOutDTO.setCode(BotResponse.ERROR.getCode());
        responseOutDTO.setFlag(false);
        return responseOutDTO;
    }
}
