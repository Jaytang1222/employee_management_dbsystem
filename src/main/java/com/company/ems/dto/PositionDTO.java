package com.company.ems.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * 岗位数据传输对象
 * 用于新增和更新岗位
 * 
 * @author EMS Team
 */
@Data
public class PositionDTO {
    
    /**
     * 岗位名称
     */
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 2, max = 100, message = "岗位名称长度必须在2-100个字符之间")
    private String positionName;
    
    /**
     * 岗位级别：初级/中级/高级/专家
     */
    @Pattern(regexp = "^(初级|中级|高级|专家)$", message = "岗位级别只能是：初级、中级、高级、专家")
    private String positionLevel;
    
    /**
     * 基本工资
     */
    @DecimalMin(value = "0.0", inclusive = false, message = "基本工资必须大于0")
    @Digits(integer = 8, fraction = 2, message = "基本工资格式不正确")
    private BigDecimal baseSalary;
    
    /**
     * 岗位描述
     */
    @Size(max = 500, message = "岗位描述长度不能超过500个字符")
    private String description;
}
