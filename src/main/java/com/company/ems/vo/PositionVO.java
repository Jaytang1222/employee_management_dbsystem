package com.company.ems.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位视图对象
 * 用于返回岗位详细信息
 * 
 * @author EMS Team
 */
@Data
public class PositionVO {
    
    /**
     * 岗位ID
     */
    private Integer positionId;
    
    /**
     * 岗位名称
     */
    private String positionName;
    
    /**
     * 岗位级别：初级/中级/高级/专家
     */
    private String positionLevel;
    
    /**
     * 基本工资
     */
    private BigDecimal baseSalary;
    
    /**
     * 岗位描述
     */
    private String description;
    
    /**
     * 状态：active/inactive/deleted
     */
    private String status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 该岗位的员工数量
     */
    private Integer employeeCount;
}
