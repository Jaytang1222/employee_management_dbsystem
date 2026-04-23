package com.company.ems.dto;

import lombok.Data;

/**
 * 绩效查询数据传输对象
 * 用于绩效列表的条件查询和分页
 * 
 * @author EMS Team
 */
@Data
public class PerformanceQueryDTO {
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 员工姓名（模糊查询）
     */
    private String employeeName;
    
    /**
     * 绩效等级：A/B/C/D
     */
    private String grade;
    
    /**
     * 开始日期
     */
    private String startDate;
    
    /**
     * 结束日期
     */
    private String endDate;
    
    /**
     * 页码（从1开始）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
