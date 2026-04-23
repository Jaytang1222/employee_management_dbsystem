package com.company.ems.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 考勤实体类
 * 对应数据库表：attendance_info
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    
    /**
     * 考勤ID
     */
    private Integer attendanceId;
    
    /**
     * 员工ID
     */
    private Integer employeeId;
    
    /**
     * 考勤日期
     */
    private LocalDate attendanceDate;
    
    /**
     * 签到时间
     */
    private LocalTime checkInTime;
    
    /**
     * 签退时间
     */
    private LocalTime checkOutTime;
    
    /**
     * 考勤状态：normal/late/early_leave/absent/leave
     */
    private String attendanceStatus;
    
    /**
     * 迟到分钟数
     */
    private Integer lateMin;
    
    /**
     * 早退分钟数
     */
    private Integer earlyLeaveMin;
    
    /**
     * 加班小时数
     */
    private BigDecimal overtimeHours;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
