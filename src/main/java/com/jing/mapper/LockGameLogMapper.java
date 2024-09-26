package com.jing.mapper;

import com.jing.mapper.entity.LockGameLog;
import org.springframework.stereotype.Repository;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper
 * @Project：IruruBot
 * @name：LockGameLogMapper
 * @Date：2024/9/22 上午12:30
 * @Filename：LockGameLogMapper
 */
@Repository
public interface LockGameLogMapper {

    void insertLockGameLog(LockGameLog lockGameLog);
}
