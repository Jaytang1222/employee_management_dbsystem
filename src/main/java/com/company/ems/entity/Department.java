package com.company.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 部门实体类
 * 对应数据库表：department_info
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    
    /**
     * 部门ID
     */
    private Integer deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 上级部门ID（根部门为NULL）
     */
    private Integer parentDeptId;
    
    /**
     * 部门经理员工ID
     */
    private Integer managerEmployeeId;
    
    /**
     * 状态：active/inactive/deleted
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
