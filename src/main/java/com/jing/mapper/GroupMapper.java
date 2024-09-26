package com.jing.mapper;

import com.jing.mapper.entity.GroupInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper
 * @Project：IruruBot
 * @name：GroupMapper
 * @Date：2024/9/20 下午10:50
 * @Filename：GroupMapper
 */
@Repository
public interface GroupMapper {

        GroupInfo selectByGroupId(@Param("groupId") String groupId);
}
