package com.jing.mapper;

import com.jing.mapper.entity.Pause;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper
 * @Project：IruruBot
 * @name：PauseMapper
 * @Date：2024/9/20 下午9:47
 * @Filename：PauseMapper
 */
@Repository
public interface PauseMapper {

    void insertPause(Pause pause);

    Pause selectPause(@Param("lockId") String lockId, @Param("userId") String userId);


    List<Pause> selectPauses(@Param("userId") String userId, @Param("lockId") String lockId);

    void updateContinueTime(@Param("userId") String userId, @Param("lockId") String lockId);
}
