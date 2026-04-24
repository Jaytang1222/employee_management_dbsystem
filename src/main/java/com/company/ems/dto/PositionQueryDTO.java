package com.company.ems.dto;

import lombok.Data;

/**
 * 岗位查询参数
 * 用于岗位列表的条件查询和分页
 * 
 * @author EMS Team
 */
@Data
public class PositionQueryDTO {
    
    /**
     * 岗位名称（模糊查询）
     */
    private String positionName;
    
    /**
     * 岗位级别
     */
    private String positionLevel;
    
    /**
     * 状态：active/inactive/deleted
     */
    private String status;
    
    /**
     * 页码（默认第1页）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小（默认10条）
     */
    private Integer pageSize = 10;
}
