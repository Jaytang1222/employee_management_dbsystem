package com.company.ems.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 绩效数据传输对象
 * 用于新增和更新绩效记录
 * 
 * @author EMS Team
 */
@Data
public class PerformanceDTO {
    
    /**
     * 员工ID
     */
    @NotNull(message = "员工ID不能为空")
    private Integer employeeId;
    
    /**
     * 考核周期（月份）
     */
    @NotNull(message = "考核周期不能为空")
    @PastOrPresent(message = "考核周期不能是未来日期")
    private LocalDate evalDate;
    
    /**
     * 绩效分数（0-100）
     */
    @NotNull(message = "绩效分数不能为空")
    @DecimalMin(value = "0.0", message = "绩效分数不能小于0")
    @DecimalMax(value = "100.0", message = "绩效分数不能大于100")
    private BigDecimal score;
    
    /**
     * 评价意见
     */
    private String comment;
    
    /**
     * 评估日期
     */
    private LocalDate evaluateDate;
}
