package com.company.ems.interceptor;

import com.company.ems.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录拦截器
 * 验证JWT Token
 * 
 * @author EMS Team
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的Token
        String token = request.getHeader("Authorization");
        
        // 如果Token为空或不以"Bearer "开头，返回401
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
            log.warn("请求未携带有效Token: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\",\"data\":null}");
            return false;
        }
        
        // 去除"Bearer "前缀
        token = token.substring(7);
        
        // 验证Token是否有效
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token验证失败: {}", request.getRequestURI());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期\",\"data\":null}");
            return false;
        }
        
        // 将用户信息存入请求属性，供Controller使用
        Integer userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);
        
        log.debug("Token验证成功，用户: {}, ID: {}", username, userId);
        return true;
    }
}
