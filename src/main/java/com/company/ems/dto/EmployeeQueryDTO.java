package com.company.ems.dto;

import lombok.Data;

/**
 * 员工查询参数
 * 用于员工列表的条件查询和分页
 * 
 * @author EMS Team
 */
@Data
public class EmployeeQueryDTO {
    
    /**
     * 姓名（模糊查询）
     */
    private String name;
    
    /**
     * 部门ID
     */
    private Integer deptId;
    
    /**
     * 岗位ID
     */
    private Integer positionId;
    
    /**
     * 状态：active/inactive/deleted
     */
    private String status;
    
    /**
     * 页码（默认第1页）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小（默认10条）
     */
    private Integer pageSize = 10;
}
