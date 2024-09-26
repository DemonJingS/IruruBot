package com.jing.service;

import com.jing.mapper.entity.LockGame;
import com.jing.mapper.entity.Pause;

/**
 * @Author：DemonJing
 * @Package：com.jing.service
 * @Project：IruruBot
 * @name：PauseService
 * @Date：2024/9/21 上午12:20
 * @Filename：PauseService
 */
public interface PauseService {

    void savePause(LockGame lockGame);

    void replaceContinueTime(String userId,String lockId);
}
