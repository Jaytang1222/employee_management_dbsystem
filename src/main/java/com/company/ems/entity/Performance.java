package com.company.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 绩效实体类
 * 对应数据库表：performance_info
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Performance {
    
    /**
     * 绩效ID
     */
    private Integer performanceId;
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
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
