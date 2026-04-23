package com.company.ems.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 考勤月报视图对象
 * 用于返回员工月度考勤统计信息
 * 
 * @author EMS Team
 */
@Data
public class AttendanceReportVO {
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 员工姓名
     */
    private String employeeName;
    
    /**
     * 统计月份（格式：YYYY-MM）
     */
    private String reportMonth;
    
    /**
     * 出勤天数
     */
    private Integer attendanceDays;
    
    /**
     * 正常天数
     */
    private Integer normalDays;
    
    /**
     * 迟到次数
     */
    private Integer lateCount;
    
    /**
     * 迟到总分钟数
     */
    private Integer totalLateMin;
    
    /**
     * 早退次数
     */
    private Integer earlyLeaveCount;
    
    /**
     * 早退总分钟数
     */
    private Integer totalEarlyLeaveMin;
    
    /**
     * 缺勤天数
     */
    private Integer absentDays;
    
    /**
     * 请假天数
     */
    private Integer leaveDays;
    
    /**
     * 加班总小时数
     */
    private BigDecimal totalOvertimeHours;
}
