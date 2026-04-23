package com.company.ems.service;

import com.company.ems.dto.AttendanceDTO;
import com.company.ems.dto.AttendanceQueryDTO;
import com.company.ems.entity.Attendance;
import com.company.ems.entity.Employee;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.AttendanceMapper;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.vo.AttendanceReportVO;
import com.company.ems.vo.AttendanceVO;
import com.company.ems.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * 考勤业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class AttendanceService {
    
    @Autowired
    private AttendanceMapper attendanceMapper;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    // 标准上班时间：9:00
    private static final LocalTime STANDARD_CHECK_IN_TIME = LocalTime.of(9, 0);
    
    // 标准下班时间：18:00
    private static final LocalTime STANDARD_CHECK_OUT_TIME = LocalTime.of(18, 0);
    
    /**
     * 新增考勤记录
     * 
     * @param attendanceDTO 考勤数据
     * @return 考勤ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addAttendance(AttendanceDTO attendanceDTO) {
        // 1. 验证员工是否存在
        Employee employee = employeeMapper.findById(attendanceDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        if (!"active".equals(employee.getStatus())) {
            throw new BusinessException("员工状态不是active，无法添加考勤记录");
        }
        
        // 2. 检查该员工当天是否已有考勤记录
        Attendance existAttendance = attendanceMapper.findByEmployeeAndDate(
            attendanceDTO.getEmployeeId(), 
            attendanceDTO.getAttendanceDate().toString()
        );
        if (existAttendance != null) {
            throw new BusinessException("该员工当天已有考勤记录，请使用更新功能");
        }
        
        // 3. 验证签退时间必须晚于签到时间
        if (attendanceDTO.getCheckInTime() != null && attendanceDTO.getCheckOutTime() != null) {
            if (attendanceDTO.getCheckOutTime().isBefore(attendanceDTO.getCheckInTime())) {
                throw new BusinessException("签退时间必须晚于签到时间");
            }
        }
        
        // 4. 自动计算迟到、早退、加班时间（如果提供了签到签退时间）
        calculateAttendanceTime(attendanceDTO);
        
        // 5. 构造考勤对象
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceDTO, attendance);
        
        // 6. 设置默认值
        if (attendance.getLateMin() == null) {
            attendance.setLateMin(0);
        }
        if (attendance.getEarlyLeaveMin() == null) {
            attendance.setEarlyLeaveMin(0);
        }
        if (attendance.getOvertimeHours() == null) {
            attendance.setOvertimeHours(BigDecimal.ZERO);
        }
        
        // 7. 插入数据库
        int rows = attendanceMapper.insert(attendance);
        if (rows == 0) {
            throw new BusinessException("新增考勤记录失败");
        }
        
        log.info("新增考勤记录成功：员工ID={}, 日期={}, 状态={}", 
                 attendanceDTO.getEmployeeId(), 
                 attendanceDTO.getAttendanceDate(), 
                 attendanceDTO.getAttendanceStatus());
        return attendance.getAttendanceId();
    }
    
    /**
     * 更新考勤记录
     * 
     * @param attendanceId 考勤ID
     * @param attendanceDTO 考勤数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateAttendance(Integer attendanceId, AttendanceDTO attendanceDTO) {
        // 1. 验证考勤记录是否存在
        Attendance existAttendance = attendanceMapper.findById(attendanceId);
        if (existAttendance == null) {
            throw new BusinessException("考勤记录不存在");
        }
        
        // 2. 验证员工是否存在
        Employee employee = employeeMapper.findById(attendanceDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 3. 验证签退时间必须晚于签到时间
        if (attendanceDTO.getCheckInTime() != null && attendanceDTO.getCheckOutTime() != null) {
            if (attendanceDTO.getCheckOutTime().isBefore(attendanceDTO.getCheckInTime())) {
                throw new BusinessException("签退时间必须晚于签到时间");
            }
        }
        
        // 4. 自动计算迟到、早退、加班时间
        calculateAttendanceTime(attendanceDTO);
        
        // 5. 构造考勤对象
        Attendance attendance = new Attendance();
        BeanUtils.copyProperties(attendanceDTO, attendance);
        attendance.setAttendanceId(attendanceId);
        
        // 6. 设置默认值
        if (attendance.getLateMin() == null) {
            attendance.setLateMin(0);
        }
        if (attendance.getEarlyLeaveMin() == null) {
            attendance.setEarlyLeaveMin(0);
        }
        if (attendance.getOvertimeHours() == null) {
            attendance.setOvertimeHours(BigDecimal.ZERO);
        }
        
        // 7. 更新数据库
        int rows = attendanceMapper.updateById(attendance);
        if (rows == 0) {
            throw new BusinessException("更新考勤记录失败");
        }
        
        log.info("更新考勤记录成功：考勤ID={}, 员工ID={}", attendanceId, attendanceDTO.getEmployeeId());
    }
    
    /**
     * 删除考勤记录
     * 
     * @param attendanceId 考勤ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAttendance(Integer attendanceId) {
        // 1. 验证考勤记录是否存在
        Attendance attendance = attendanceMapper.findById(attendanceId);
        if (attendance == null) {
            throw new BusinessException("考勤记录不存在");
        }
        
        // 2. 删除记录
        int rows = attendanceMapper.deleteById(attendanceId);
        if (rows == 0) {
            throw new BusinessException("删除考勤记录失败");
        }
        
        log.info("删除考勤记录成功：考勤ID={}", attendanceId);
    }
    
    /**
     * 根据ID查询考勤详情
     * 
     * @param attendanceId 考勤ID
     * @return 考勤详情
     */
    public AttendanceVO getAttendanceDetail(Integer attendanceId) {
        AttendanceVO attendanceVO = attendanceMapper.findDetailById(attendanceId);
        if (attendanceVO == null) {
            throw new BusinessException("考勤记录不存在");
        }
        return attendanceVO;
    }
    
    /**
     * 查询考勤列表（分页）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    public PageResult<AttendanceVO> getAttendanceList(AttendanceQueryDTO queryDTO) {
        // 1. 计算分页偏移量
        int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        queryDTO.setPageNum(offset);
        
        // 2. 查询总数
        Long total = attendanceMapper.countList(queryDTO);
        
        // 3. 查询列表
        List<AttendanceVO> records = attendanceMapper.findList(queryDTO);
        
        // 4. 构造分页结果
        return new PageResult<>(total, records, queryDTO.getPageNum() / queryDTO.getPageSize() + 1, queryDTO.getPageSize());
    }
    
    /**
     * 查询员工月度考勤报表
     * 
     * @param employeeId 员工ID
     * @param month 月份（格式：YYYY-MM）
     * @return 考勤月报
     */
    public AttendanceReportVO getMonthlyReport(Integer employeeId, String month) {
        // 1. 验证员工是否存在
        Employee employee = employeeMapper.findById(employeeId);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 2. 验证月份格式
        if (!month.matches("^\\d{4}-\\d{2}$")) {
            throw new BusinessException("月份格式不正确，应为：YYYY-MM");
        }
        
        // 3. 查询月度报表
        AttendanceReportVO report = attendanceMapper.getMonthlyReport(employeeId, month);
        
        // 4. 如果没有考勤记录，返回空报表
        if (report == null) {
            report = new AttendanceReportVO();
            report.setEmployeeId(employeeId);
            report.setEmployeeName(employee.getName());
            report.setReportMonth(month);
            report.setAttendanceDays(0);
            report.setNormalDays(0);
            report.setLateCount(0);
            report.setTotalLateMin(0);
            report.setEarlyLeaveCount(0);
            report.setTotalEarlyLeaveMin(0);
            report.setAbsentDays(0);
            report.setLeaveDays(0);
            report.setTotalOvertimeHours(BigDecimal.ZERO);
        }
        
        return report;
    }
    
    /**
     * 自动计算迟到、早退、加班时间
     * 
     * @param attendanceDTO 考勤数据
     */
    private void calculateAttendanceTime(AttendanceDTO attendanceDTO) {
        // 如果没有提供签到签退时间，不进行计算
        if (attendanceDTO.getCheckInTime() == null || attendanceDTO.getCheckOutTime() == null) {
            return;
        }
        
        LocalTime checkInTime = attendanceDTO.getCheckInTime();
        LocalTime checkOutTime = attendanceDTO.getCheckOutTime();
        
        // 计算迟到分钟数
        if (checkInTime.isAfter(STANDARD_CHECK_IN_TIME)) {
            int lateMinutes = (int) java.time.Duration.between(STANDARD_CHECK_IN_TIME, checkInTime).toMinutes();
            attendanceDTO.setLateMin(lateMinutes);
            
            // 如果迟到，自动设置状态为late（如果当前状态是normal）
            if ("normal".equals(attendanceDTO.getAttendanceStatus())) {
                attendanceDTO.setAttendanceStatus("late");
            }
        } else {
            attendanceDTO.setLateMin(0);
        }
        
        // 计算早退分钟数
        if (checkOutTime.isBefore(STANDARD_CHECK_OUT_TIME)) {
            int earlyLeaveMinutes = (int) java.time.Duration.between(checkOutTime, STANDARD_CHECK_OUT_TIME).toMinutes();
            attendanceDTO.setEarlyLeaveMin(earlyLeaveMinutes);
            
            // 如果早退，自动设置状态为early_leave（如果当前状态是normal）
            if ("normal".equals(attendanceDTO.getAttendanceStatus())) {
                attendanceDTO.setAttendanceStatus("early_leave");
            }
        } else {
            attendanceDTO.setEarlyLeaveMin(0);
        }
        
        // 计算加班小时数（下班时间晚于18:00）
        if (checkOutTime.isAfter(STANDARD_CHECK_OUT_TIME)) {
            long overtimeMinutes = java.time.Duration.between(STANDARD_CHECK_OUT_TIME, checkOutTime).toMinutes();
            BigDecimal overtimeHours = BigDecimal.valueOf(overtimeMinutes).divide(BigDecimal.valueOf(60), 2, BigDecimal.ROUND_HALF_UP);
            attendanceDTO.setOvertimeHours(overtimeHours);
        } else {
            attendanceDTO.setOvertimeHours(BigDecimal.ZERO);
        }
    }
}
