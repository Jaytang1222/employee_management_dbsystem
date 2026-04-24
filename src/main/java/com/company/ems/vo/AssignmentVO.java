package com.company.ems.vo;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工分配视图对象
 * 用于返回员工分配详细信息（包含关联的员工、部门、岗位信息）
 * 
 * @author EMS Team
 */
@Data
public class AssignmentVO {
    
    /**
     * 分配ID
     */
    private Integer assignmentId;
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 员工姓名
     */
    private String employeeName;
    
    /**
     * 部门ID
     */
    private Integer deptId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 岗位ID
     */
    private Integer positionId;
    
    /**
     * 岗位名称
     */
    private String positionName;
    
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
