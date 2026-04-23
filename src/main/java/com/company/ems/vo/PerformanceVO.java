package com.company.ems.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 绩效视图对象
 * 用于返回绩效详细信息（包含员工信息）
 * 
 * @author EMS Team
 */
@Data
public class PerformanceVO {
    
    /**
     * 绩效ID
     */
    private Integer performanceId;
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 员工姓名
     */
    private String employeeName;
    
    /**
     * 考核周期（月份）
     */
    private LocalDate evalDate;
    
    /**
     * 绩效分数（0-100）
     */
    private BigDecimal score;
    
    /**
     * 绩效等级：A/B/C/D
     */
    private String grade;
    
    /**
     * 评价意见
     */
    private String comment;
    
    /**
     * 评估日期
     */
    private LocalDate evaluateDate;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
