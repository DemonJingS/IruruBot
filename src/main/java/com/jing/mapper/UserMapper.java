package com.jing.mapper;

import com.jing.mapper.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author：DemonJing
 * @Package：com.jing.mapper
 * @Project：lingJingBot
 * @name：UserMapper
 * @Date：2024/9/8 下午5:13
 * @Filename：UserMapper
 */
@Repository
public interface UserMapper {

    void insertUser(User user);

    User selectUserByUserId(@Param("userId") String userId);

    void updateUserStatus(@Param("userId") String userId);

    void updateAddTimeNum(@Param("userId") String userId, @Param("addTimeNum") int addTimeNum);

    void updateMinusTimeNum(@Param("userId") String userId, @Param("minusTimeNum") int minusTimeNum);

    void updateAddAndMinusTimeNum();


    void updateUserInfoXP(@Param("userId") String userId, @Param("xpInfo") String xpInfo);

    void updateUserInfoClothes(@Param("userId") String userId, @Param("clothes") String clothes);

    void updateUserInfoToys(@Param("userId") String userId, @Param("toys") String toys);

    User selectUserByUserIdForUpate(@Param("userId") String userId);
}
