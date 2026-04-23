package com.company.ems.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 薪资数据传输对象
 * 用于新增和更新薪资记录
 * 
 * @author EMS Team
 */
@Data
public class PayrollDTO {
    
    /**
     * 员工ID
     */
    @NotNull(message = "员工ID不能为空")
    private Integer employeeId;
    
    /**
     * 发薪月份（格式：YYYY-MM）
     */
    @NotBlank(message = "发薪月份不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "发薪月份格式必须为：YYYY-MM")
    private String payMonth;
    
    /**
     * 基本工资
     */
    @NotNull(message = "基本工资不能为空")
    @DecimalMin(value = "0.0", message = "基本工资不能小于0")
    private BigDecimal basePay;
    
    /**
     * 津贴
     */
    @DecimalMin(value = "0.0", message = "津贴不能小于0")
    private BigDecimal allowance;
    
    /**
     * 奖金
     */
    @DecimalMin(value = "0.0", message = "奖金不能小于0")
    private BigDecimal bonus;
    
    /**
     * 扣款
     */
    @DecimalMin(value = "0.0", message = "扣款不能小于0")
    private BigDecimal deduction;
    
    /**
     * 社保
     */
    @DecimalMin(value = "0.0", message = "社保不能小于0")
    private BigDecimal socialInsurance;
    
    /**
     * 税
     */
    @DecimalMin(value = "0.0", message = "税不能小于0")
    private BigDecimal tax;
    
    /**
     * 发薪日期
     */
    private LocalDate payDate;
}
