package com.company.ems.dto;

import lombok.Data;

/**
 * 薪资查询数据传输对象
 * 用于薪资列表的条件查询和分页
 * 
 * @author EMS Team
 */
@Data
public class PayrollQueryDTO {
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 员工姓名（模糊查询）
     */
    private String employeeName;
    
    /**
     * 发薪月份（格式：YYYY-MM）
     */
    private String payMonth;
    
    /**
     * 开始月份（格式：YYYY-MM）
     */
    private String startMonth;
    
    /**
     * 结束月份（格式：YYYY-MM）
     */
    private String endMonth;
    
    /**
     * 页码（从1开始）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
