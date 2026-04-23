package com.company.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 薪资实体类
 * 对应数据库表：payroll_info
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {
    
    /**
     * 薪资ID
     */
    private Integer payrollId;
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 发薪月份（格式：YYYY-MM）
     */
    private String payMonth;
    
    /**
     * 基本工资
     */
    private BigDecimal basePay;
    
    /**
     * 津贴
     */
    private BigDecimal allowance;
    
    /**
     * 奖金
     */
    private BigDecimal bonus;
    
    /**
     * 扣款
     */
    private BigDecimal deduction;
    
    /**
     * 社保
     */
    private BigDecimal socialInsurance;
    
    /**
     * 税
     */
    private BigDecimal tax;
    
    /**
     * 实发工资
     */
    private BigDecimal netPay;
    
    /**
     * 发薪日期
     */
    private LocalDate payDate;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
