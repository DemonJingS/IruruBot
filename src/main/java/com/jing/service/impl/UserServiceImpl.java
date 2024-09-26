package com.jing.service.impl;

import com.jing.bot.botEnum.BotResponse;
import com.jing.bot.botEnum.UserEnum;
import com.jing.constant.UserConstant;
import com.jing.controller.dto.PlayerInDTO;
import com.jing.controller.dto.ResponseOutDTO;
import com.jing.controller.dto.UserInfoInDTO;
import com.jing.mapper.UserMapper;
import com.jing.service.UserService;
import com.jing.utils.UserUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Update;
import com.jing.mapper.entity.User;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：UserServiceImpl
 * @Date：2024/9/16 下午9:36
 * @Filename：UserServiceImpl
 */

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean registerUser(Update update) {
        String userName = UserUtils.combineUsername(update);
        String userId = update.getMessage().getFrom().getId().toString();
        String tgUserName = update.getMessage().getFrom().getUserName();
        User user = userMapper.selectUserByUserId(userId);
        if (user == null) {
            user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setTgUserName(tgUserName);
            user.setFlag(UserEnum.USER_FLAG_BETA.getCode());
            user.setStatus(UserEnum.USER_STATUS_BAN.getCode());
            user.setAddTimeNum(UserConstant.USER_ADD_TIME_NUM);
            user.setMinusTimeNum(UserConstant.USER_MINUS_TIME_NUM);
            userMapper.insertUser(user);
            log.info("UserService.registerUser用户注册成功，用户名：{}，用户ID：{}", userName, userId);
            return true;

        }
        return StringUtils.equals(user.getStatus(), UserEnum.USER_STATUS_NORMAL.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserInfo(UserInfoInDTO userInfoInDTO) {
        String userId = userInfoInDTO.getUserId();
        if (StringUtils.isNotBlank(userInfoInDTO.getXpInfo()) && UserUtils.containsSpecialCharacters(userInfoInDTO.getXpInfo())) {
            userMapper.updateUserInfoXP(userId, userInfoInDTO.getXpInfo());
        }
        if (StringUtils.isNotBlank(userInfoInDTO.getClothes()) && UserUtils.containsSpecialCharacters(userInfoInDTO.getClothes())) {
            userMapper.updateUserInfoClothes(userId, userInfoInDTO.getClothes());
        }
        if (StringUtils.isNotBlank(userInfoInDTO.getToys()) && UserUtils.containsSpecialCharacters(userInfoInDTO.getToys())) {
            userMapper.updateUserInfoToys(userId, userInfoInDTO.getToys());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public  ResponseOutDTO queryAndReplaceUser(PlayerInDTO playerInDTO) {
        User user = userMapper.selectUserByUserId(playerInDTO.getPlayerId());
        ResponseOutDTO responseOutDTO = new ResponseOutDTO();
        if (user != null) {
            if (user.getStatus().equals(UserEnum.USER_STATUS_NORMAL.getCode())) {
                responseOutDTO.setCode(BotResponse.SUCCESS.getCode());
                responseOutDTO.setMessage(BotResponse.SUCCESS.getMsg());
                responseOutDTO.setUrl("/main");
                return responseOutDTO;
            }
            if (user.getStatus().equals(UserEnum.USER_STATUS_BAN.getCode())) {
                responseOutDTO.setCode(BotResponse.USER_IS_BAN.getCode());
                responseOutDTO.setMessage(BotResponse.USER_IS_BAN.getMsg());
                responseOutDTO.setUrl("/error");
                return responseOutDTO;
            }
        }
        responseOutDTO.setCode(BotResponse.USER_NOT_EXIST.getCode());
        responseOutDTO.setMessage(BotResponse.USER_NOT_EXIST.getMsg());
        responseOutDTO.setUrl("/error");
        return responseOutDTO;
    }

    @Override
    public User queryUserByUserId(String userId) {
        User user = userMapper.selectUserByUserId(userId);
        if (user == null) {
            return null;
        }
        return getUser(userId, user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAddTimeNum(String userId, int addTimeNum) {
        userMapper.updateAddTimeNum(userId, addTimeNum);
    }

    @Override
    public void banUser(String addUserId) {
        userMapper.updateUserStatus(addUserId);
    }

    @Override
    public void updateMinusTimeNum(String userId, int minusTimeNum) {
        userMapper.updateMinusTimeNum(userId, minusTimeNum);
    }

    @Nullable
    private User getUser(String userId, User user) {
        if (StringUtils.equals(user.getStatus(), UserEnum.USER_STATUS_NORMAL.getCode())) {
            return user;
        }
        StringUtils.equals(user.getStatus(), UserEnum.USER_STATUS_BAN.getCode());
        return null;
    }

    public User getUserByUserId(String userId) {
        return userMapper.selectUserByUserId(userId);
    }
}
