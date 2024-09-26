package com.jing.service.impl;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.*;

/**
 * @Author：DemonJing
 * @Package：com.jing.service.impl
 * @Project：IruruBot
 * @name：VoteService
 * @Date：2024/9/23 下午6:25
 * @Filename：VoteService
 */
@Service
public class VoteService {

    // 为每个投票创建独立的投票计数和已投用户记录
    // 使用Map来存储同意投票结果
    private final Map<String, Integer> agreeVotesMap = new HashMap<>();
    // 使用Map来存储拒绝投票结果
    private final Map<String, Integer> rejectVotesMap = new HashMap<>();
    // 使用Map来存储已投票用户
    private final Map<String, Set<String>> votedUsersMap = new HashMap<>();

    public void agreeOrReVote(TelegramClient telegramClient, Update update) {

        String data = update.getCallbackQuery().getData();
        String userid = update.getCallbackQuery().getFrom().getId().toString();
        String[] parts = data.split(":");
        String voteType = parts[0];  // 'agree' or 'reject'
        String voteKey = parts[1];
        if (votedUsersMap.containsKey(voteKey) && votedUsersMap.get(voteKey).contains(userid)) {
            System.out.println("用户已投票");
        } else {
            handleVote(userid, voteKey, voteType.equals("agree") ? agreeVotesMap : rejectVotesMap, voteType);
        }
    }

    private void handleVote(String userId, String voteKey, Map<String, Integer> voteMap, String voteType ) {
        // 增加投票计数
        voteMap.putIfAbsent(voteKey, 0);
        voteMap.put(voteKey, voteMap.get(voteKey) + 1);
        // 记录该用户已投票
        Set<String> votedUsers = votedUsersMap.computeIfAbsent(voteKey, k -> new HashSet<>());
        votedUsers.add(userId);
        // 发送当前投票计数
        // 检查是否达到结束条件
    }
    /**
     * 初始化三个map
     */
    private void initMaps() {

    }

}
