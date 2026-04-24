package com.company.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 岗位实体类
 * 对应数据库表：position_info
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    
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
}
