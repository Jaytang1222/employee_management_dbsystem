package com.company.ems.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 部门视图对象
 * 用于返回部门详细信息（包含经理姓名）
 * 
 * @author EMS Team
 */
@Data
public class DepartmentVO {
    
    /**
     * 部门ID
     */
    private Integer deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 上级部门ID
     */
    private Integer parentDeptId;
    
    /**
     * 上级部门名称
     */
    private String parentDeptName;
    
    /**
     * 部门经理员工ID
     */
    private Integer managerEmployeeId;
    
    /**
     * 部门经理姓名
     */
    private String managerName;
    
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
