package com.company.ems.vo;

import lombok.Data;

/**
 * 按部门统计员工数量视图对象
 * 用于返回部门员工统计信息
 * 
 * @author EMS Team
 */
@Data
public class EmployeeCountByDeptVO {
    
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
     * 员工数量（主部门）
     */
    private Integer employeeCount;
    
    /**
     * 部门经理姓名
     */
    private String managerName;
}
