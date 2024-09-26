package com.jing.service.impl;

import com.jing.mapper.PauseMapper;
import com.jing.mapper.entity.LockGame;
import com.jing.mapper.entity.Pause;
import com.jing.service.PauseService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：PauseServiceImpl
 * @Date：2024/9/21 上午12:20
 * @Filename：PauseServiceImpl
 */
@Slf4j
@Service
public class PauseServiceImpl implements PauseService {

    @Resource
    private PauseMapper pauseMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePause(LockGame lockGame) {
        Pause pause = new Pause();
        pause.setLockId(lockGame.getLockId());
        pause.setUserId(lockGame.getUserId());
        pauseMapper.insertPause(pause);
    }

    @Override
    public void replaceContinueTime(String userId, String lockId) {
        pauseMapper.updateContinueTime(userId,lockId);
    }
}
