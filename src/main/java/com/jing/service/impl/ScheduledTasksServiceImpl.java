package com.jing.service.impl;

import com.jing.mapper.UserMapper;
import com.jing.service.ScheduledTasksService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：ScheduledTasksServiceImpl
 * @Date：2024/9/23 下午11:24
 * @Filename：ScheduledTasksServiceImpl
 */
@Slf4j
@Service
public class ScheduledTasksServiceImpl implements ScheduledTasksService {

    @Resource
    private UserMapper userMapper;

    @Override
    @Scheduled(cron = " 0 0 0 * * ? ")
    public void updateUserAddAndMinusTimeNum() {
        userMapper.updateAddAndMinusTimeNum();
    }
}
