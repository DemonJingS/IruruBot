package com.jing.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author：DemonJing
 * @Package：com.jing.utils
 * @Project：IruruBot
 * @name：IdempotentLockManager
 * @Date：2024/9/22 下午8:25
 * @Filename：IdempotentLockManager
 */
public class IdempotentLockManager {
    private static final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    public static ReentrantLock getLock(String requestId) {
        return locks.computeIfAbsent(requestId, k -> new ReentrantLock());
    }
}
