package com.jing.mapper;

import com.jing.mapper.entity.LockGame;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface LockGameMapper {


    LockGame selectByGamerId(@Param("userId") String userId);

    void deleteFinishLockGame(@Param("userId")String userId);

    void insertRegisterLockGame(LockGame lockGame);

    void updateLockGameEndTime(@Param("userId")String userId, @Param("endTime") String newEndTime);

    void updatePauseStatusAndPauseTime(@Param("userId")String userId, @Param("status") String status ,@Param("pauseCount")String pauseCount);

    void updateContinueChallenge(String userId, String endTime);
}