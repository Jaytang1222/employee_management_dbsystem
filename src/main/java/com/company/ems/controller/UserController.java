package com.company.ems.controller;

import com.company.ems.dto.LoginDTO;
import com.company.ems.entity.User;
import com.company.ems.service.UserService;
import com.company.ems.vo.LoginVO;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户登录
     * 
     * @param loginDTO 登录参数
     * @return 登录结果（包含Token）
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@Validated @RequestBody LoginDTO loginDTO) {
        log.info("用户登录请求：{}", loginDTO.getUsername());
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success("登录成功", loginVO);
    }
    
    /**
     * 用户登出
     * 
     * @return 操作结果
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // JWT是无状态的，登出只需要前端删除Token即可
        // 这里只是提供一个接口，实际不需要做任何操作
        log.info("用户登出");
        return Result.success("登出成功", null);
    }
    
    /**
     * 获取当前登录用户信息
     * 
     * @param request HTTP请求对象
     * @return 用户信息
     */
    @GetMapping("/userinfo")
    public Result<User> getUserInfo(HttpServletRequest request) {
        // 从请求属性中获取用户ID（由拦截器设置）
        Integer userId = (Integer) request.getAttribute("userId");
        User user = userService.getUserInfo(userId);
        return Result.success(user);
    }
}
