package com.company.ems.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工视图对象
 * 用于返回员工详细信息（包含关联的部门和岗位信息）
 * 
 * @author EMS Team
 */
@Data
public class EmployeeVO {
    
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
    
    /**
     * 主部门名称
     */
    private String primaryDeptName;
    
    /**
     * 主岗位名称
     */
    private String primaryPositionName;
}
