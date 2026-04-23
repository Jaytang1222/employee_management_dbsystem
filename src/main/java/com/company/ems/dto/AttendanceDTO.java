package com.company.ems.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 考勤数据传输对象
 * 用于新增和更新考勤记录
 * 
 * @author EMS Team
 */
@Data
public class AttendanceDTO {
    
    /**
     * 员工ID
     */
    @NotNull(message = "员工ID不能为空")
    private Integer employeeId;
    
    /**
     * 考勤日期
     */
    @NotNull(message = "考勤日期不能为空")
    @PastOrPresent(message = "考勤日期不能是未来日期")
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
    @NotBlank(message = "考勤状态不能为空")
    @Pattern(regexp = "^(normal|late|early_leave|absent|leave)$", 
             message = "考勤状态只能是：normal、late、early_leave、absent、leave")
    private String attendanceStatus;
    
    /**
     * 迟到分钟数
     */
    @Min(value = 0, message = "迟到分钟数不能小于0")
    private Integer lateMin;
    
    /**
     * 早退分钟数
     */
    @Min(value = 0, message = "早退分钟数不能小于0")
    private Integer earlyLeaveMin;
    
    /**
     * 加班小时数
     */
    @DecimalMin(value = "0.0", message = "加班小时数不能小于0")
    @DecimalMax(value = "24.0", message = "加班小时数不能大于24")
    private BigDecimal overtimeHours;
    
    /**
     * 备注
     */
    private String remark;
}
