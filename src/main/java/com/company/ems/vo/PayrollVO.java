package com.company.ems.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 薪资视图对象
 * 用于返回薪资详细信息（包含员工信息）
 * 
 * @author EMS Team
 */
@Data
public class PayrollVO {
    
    /**
     * 薪资ID
     */
    private Integer payrollId;
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 员工姓名
     */
    private String employeeName;
    
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
