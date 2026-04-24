package com.company.ems.dto;

import lombok.Data;

/**
 * 员工分配查询参数
 * 用于员工分配列表的条件查询和分页
 * 
 * @author EMS Team
 */
@Data
public class AssignmentQueryDTO {
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 部门ID
     */
    private Integer deptId;
    
    /**
     * 岗位ID
     */
    private Integer positionId;
    
    /**
     * 页码（默认第1页）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小（默认10条）
     */
    private Integer pageSize = 10;
}
