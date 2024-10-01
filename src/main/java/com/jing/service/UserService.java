package com.jing.service;

import com.jing.controller.dto.LoginInDTO;
import com.jing.controller.dto.PlayerInDTO;
import com.jing.controller.dto.ResponseOutDTO;
import com.jing.controller.dto.UserInfoInDTO;
import com.jing.mapper.entity.User;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.concurrent.ThreadLocalRandom;


/**
 * @Author：DemonJing
 * @Package：com.jing.service
 * @Project：IruruBot
 * @name：UserService
 * @Date：2024/9/16 下午9:36
 * @Filename：UserService
 */
public interface UserService {

    boolean registerUser(Update update);

    void saveUserInfo(UserInfoInDTO userInfoInDTO);

    ResponseOutDTO queryAndReplaceUser(PlayerInDTO playerInDTO);

    User queryUserByUserId(String userId);

    void updateAddTimeNum(String userId, int addTimeNum);

    void banUser(String addUserId);

    void updateMinusTimeNum(String userId, int minusTimeNum);

    boolean checkUser(LoginInDTO loginInDTO);

    String generateOrObtainCode(PlayerInDTO playerInDTO);

}
