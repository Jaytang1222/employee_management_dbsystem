package com.company.ems.config;

import com.company.ems.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置
 * 注册拦截器
 * 
 * @author EMS Team
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    private LoginInterceptor loginInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // 拦截所有请求
                .addPathPatterns("/**")
                // 排除登录接口
                .excludePathPatterns("/api/login", "/api/logout")
                // 排除静态资源
                .excludePathPatterns("/static/**", "/public/**")
                // 排除Swagger文档（如果后续添加）
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**");
    }
}
