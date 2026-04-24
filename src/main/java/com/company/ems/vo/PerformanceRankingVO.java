package com.company.ems.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 绩效排名视图对象
 * 用于返回绩效排名信息
 * 
 * @author EMS Team
 */
@Data
public class PerformanceRankingVO {
    
    /**
     * 排名
     */
    private Integer ranking;
    
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
}
