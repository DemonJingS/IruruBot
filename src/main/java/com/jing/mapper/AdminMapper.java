package com.jing.mapper;


import com.jing.mapper.entity.Admin;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminMapper {

    Admin selectByUserId(String userId);


}
