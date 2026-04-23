package com.company.ems.controller;

import com.company.ems.dto.AttendanceDTO;
import com.company.ems.dto.AttendanceQueryDTO;
import com.company.ems.service.AttendanceService;
import com.company.ems.vo.AttendanceReportVO;
import com.company.ems.vo.AttendanceVO;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 考勤控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {
    
    @Autowired
    private AttendanceService attendanceService;
    
    /**
     * 新增考勤记录
     * 
     * @param attendanceDTO 考勤数据
     * @return 考勤ID
     */
    @PostMapping
    public Result<Integer> addAttendance(@Validated @RequestBody AttendanceDTO attendanceDTO) {
        log.info("新增考勤记录请求：员工ID={}, 日期={}", 
                 attendanceDTO.getEmployeeId(), 
                 attendanceDTO.getAttendanceDate());
        Integer attendanceId = attendanceService.addAttendance(attendanceDTO);
        return Result.success("新增考勤记录成功", attendanceId);
    }
    
    /**
     * 更新考勤记录
     * 
     * @param attendanceId 考勤ID
     * @param attendanceDTO 考勤数据
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Void> updateAttendance(@PathVariable("id") Integer attendanceId, 
                                         @Validated @RequestBody AttendanceDTO attendanceDTO) {
        log.info("更新考勤记录请求：考勤ID={}", attendanceId);
        attendanceService.updateAttendance(attendanceId, attendanceDTO);
        return Result.success("更新考勤记录成功", null);
    }
    
    /**
     * 删除考勤记录
     * 
     * @param attendanceId 考勤ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAttendance(@PathVariable("id") Integer attendanceId) {
        log.info("删除考勤记录请求：考勤ID={}", attendanceId);
        attendanceService.deleteAttendance(attendanceId);
        return Result.success("删除考勤记录成功", null);
    }
    
    /**
     * 根据ID查询考勤详情
     * 
     * @param attendanceId 考勤ID
     * @return 考勤详情
     */
    @GetMapping("/{id}")
    public Result<AttendanceVO> getAttendanceDetail(@PathVariable("id") Integer attendanceId) {
        log.info("查询考勤详情：考勤ID={}", attendanceId);
        AttendanceVO attendanceVO = attendanceService.getAttendanceDetail(attendanceId);
        return Result.success(attendanceVO);
    }
    
    /**
     * 查询考勤列表（分页+条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<AttendanceVO>> getAttendanceList(AttendanceQueryDTO queryDTO) {
        log.info("查询考勤列表：pageNum={}, pageSize={}", queryDTO.getPageNum(), queryDTO.getPageSize());
        PageResult<AttendanceVO> pageResult = attendanceService.getAttendanceList(queryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 查询员工月度考勤报表
     * 
     * @param employeeId 员工ID
     * @param month 月份（格式：YYYY-MM）
     * @return 考勤月报
     */
    @GetMapping("/report")
    public Result<AttendanceReportVO> getMonthlyReport(@RequestParam Integer employeeId, 
                                                       @RequestParam String month) {
        log.info("查询员工月度考勤报表：员工ID={}, 月份={}", employeeId, month);
        AttendanceReportVO report = attendanceService.getMonthlyReport(employeeId, month);
        return Result.success(report);
    }
}
