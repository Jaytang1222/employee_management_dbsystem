package com.company.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工分配实体类
 * 对应数据库表：assignment_info
 * 用于记录员工-部门-岗位的关联关系和调动历史
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {
    
    /**
     * 分配ID
     */
    private Integer assignmentId;
    
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
     * 生效日期
     */
    private LocalDate startDate;
    
    /**
     * 是否主部门
     */
    private Boolean isPrimary;
    
    /**
     * 调动原因
     */
    private String changeReason;
    
    /**
     * 状态：active/inactive
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
