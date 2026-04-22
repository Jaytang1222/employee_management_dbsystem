package com.company.ems.service;

import com.company.ems.dto.LoginDTO;
import com.company.ems.entity.User;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.UserMapper;
import com.company.ems.util.JwtUtil;
import com.company.ems.vo.LoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * 用户登录
     * 
     * @param loginDTO 登录参数
     * @return 登录结果（包含Token）
     */
    public LoginVO login(LoginDTO loginDTO) {
        // 1. 根据用户名查询用户
        User user = userMapper.findByUsername(loginDTO.getUsername());
        
        // 2. 验证用户是否存在
        if (user == null) {
            log.warn("登录失败：用户不存在 - {}", loginDTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }
        
        // 3. 验证密码是否正确（明文比对）
        if (!loginDTO.getPassword().equals(user.getPassword())) {
            log.warn("登录失败：密码错误 - {}", loginDTO.getUsername());
            throw new BusinessException(401, "用户名或密码错误");
        }
        
        // 4. 生成JWT Token
        String token = jwtUtil.generateToken(user.getUserId(), user.getUsername());
        
        // 5. 构造返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUserId(user.getUserId());
        loginVO.setUsername(user.getUsername());
        loginVO.setEmployeeId(user.getEmployeeId());
        
        log.info("用户登录成功：{}", user.getUsername());
        return loginVO;
    }
    
    /**
     * 获取用户信息
     * 
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserInfo(Integer userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 清空密码，不返回给前端
        user.setPassword(null);
        return user;
    }
}
