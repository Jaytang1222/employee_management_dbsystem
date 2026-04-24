package com.company.ems.controller;

import com.company.ems.service.AttendanceService;
import com.company.ems.service.PerformanceService;
import com.company.ems.service.StatisticsService;
import com.company.ems.vo.AttendanceReportVO;
import com.company.ems.vo.EmployeeCountByDeptVO;
import com.company.ems.vo.PerformanceRankingVO;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 统计报表控制器
 * 提供各类统计报表接口
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    
    @Autowired
    private StatisticsService statisticsService;
    
    @Autowired
    private AttendanceService attendanceService;
    
    @Autowired
    private PerformanceService performanceService;
    
    /**
     * 按部门统计员工数量
     * 统计每个部门的主部门员工数量
     * 
     * @return 部门员工统计列表
     */
    @GetMapping("/employee-by-dept")
    public Result<List<EmployeeCountByDeptVO>> countEmployeeByDepartment() {
        log.info("查询按部门统计员工数量");
        List<EmployeeCountByDeptVO> statistics = statisticsService.countEmployeeByDepartment();
        return Result.success(statistics);
    }
    
    /**
     * 员工考勤月报
     * 统计指定员工在指定月份的考勤情况
     * 
     * @param employeeId 员工ID
     * @param month 月份（格式：YYYY-MM）
     * @return 考勤月报
     */
    @GetMapping("/attendance-report")
    public Result<AttendanceReportVO> getAttendanceReport(@RequestParam Integer employeeId, 
                                                          @RequestParam String month) {
        log.info("查询员工考勤月报：员工ID={}, 月份={}", employeeId, month);
        AttendanceReportVO report = attendanceService.getMonthlyReport(employeeId, month);
        return Result.success(report);
    }
    
    /**
     * 绩效排名
     * 查询指定考核周期的绩效排名
     * 
     * @param evalDate 考核周期（格式：YYYY-MM-DD，可选）
     * @return 绩效排名列表
     */
    @GetMapping("/performance-ranking")
    public Result<List<PerformanceRankingVO>> getPerformanceRanking(
            @RequestParam(required = false) String evalDate) {
        log.info("查询绩效排名：考核周期={}", evalDate);
        List<PerformanceRankingVO> rankings = performanceService.getPerformanceRanking(evalDate);
        return Result.success(rankings);
    }
}
