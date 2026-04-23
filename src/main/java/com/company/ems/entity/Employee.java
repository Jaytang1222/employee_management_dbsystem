package com.company.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类
 * 对应数据库表：employee_info
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 身份证号
     */
    private String idCard;
    
    /**
     * 姓名
     */
    private String name;
    
    /**
     * 性别：男/女
     */
    private String gender;
    
    /**
     * 出生日期
     */
    private LocalDate birthDate;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 入职日期
     */
    private LocalDate hireDate;
    
    /**
     * 状态：active/inactive/deleted
     */
    private String status;
    
    /**
     * 学历：高中/大专/本科/硕士/博士
     */
    private String educationLevel;
    
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
