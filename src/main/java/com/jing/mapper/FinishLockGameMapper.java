package com.jing.mapper;

import com.jing.mapper.entity.LockGame;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinishLockGameMapper {

    void insertFinishLockGame(LockGame lockGame);


    List<LockGame> selectByUserId (@Param("userId") String userId);
}