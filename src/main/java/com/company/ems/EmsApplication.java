package com.company.ems;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 企业员工管理系统 - 启动类
 * 
 * @author EMS Team
 * @since 2024-01-19
 */
@SpringBootApplication
@MapperScan("com.company.ems.mapper")
public class EmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmsApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("企业员工管理系统启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("========================================\n");
    }
}
