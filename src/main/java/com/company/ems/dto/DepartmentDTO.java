package com.company.ems.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 部门数据传输对象
 * 用于新增和更新部门
 * 
 * @author EMS Team
 */
@Data
public class DepartmentDTO {
    
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Size(min = 2, max = 100, message = "部门名称长度必须在2-100个字符之间")
    private String deptName;
    
    /**
     * 上级部门ID（根部门为NULL）
     */
    private Integer parentDeptId;
    
    /**
     * 部门经理员工ID
     */
    private Integer managerEmployeeId;
}
