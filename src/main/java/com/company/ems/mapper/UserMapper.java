package com.company.ems.mapper;

import com.company.ems.entity.User;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 * 
 * @author EMS Team
 */
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    User findByUsername(@Param("username") String username);
    
    /**
     * 根据用户ID查询用户
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    User findById(@Param("userId") Integer userId);
}
