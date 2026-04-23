package com.company.ems.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 考勤查询数据传输对象
 * 用于考勤列表的条件查询和分页
 * 
 * @author EMS Team
 */
@Data
public class AttendanceQueryDTO {
    
    /**
     * 员工ID（可选）
     */
    private Integer employeeId;
    
    /**
     * 员工姓名（可选，模糊查询）
     */
    private String employeeName;
    
    /**
     * 考勤状态（可选）
     */
    private String attendanceStatus;
    
    /**
     * 开始日期（可选，格式：yyyy-MM-dd）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    /**
     * 结束日期（可选，格式：yyyy-MM-dd）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    /**
     * 页码（默认1）
     */
    private Integer pageNum = 1;
    
    /**
     * 每页大小（默认10）
     */
    private Integer pageSize = 10;
}
