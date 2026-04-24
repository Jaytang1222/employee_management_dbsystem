package com.company.ems.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门树形结构视图对象
 * 用于返回部门树形层级结构
 * 
 * @author EMS Team
 */
@Data
public class DepartmentTreeVO {
    
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
     * 子部门列表
     */
    private List<DepartmentTreeVO> children = new ArrayList<>();
}
