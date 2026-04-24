package com.company.ems.controller;

import com.company.ems.dto.AttendanceQueryDTO;
import com.company.ems.dto.EmployeeQueryDTO;
import com.company.ems.dto.PayrollQueryDTO;
import com.company.ems.service.ExportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 数据导出控制器
 * 提供Excel格式的数据导出功能
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/export")
public class ExportController {
    
    @Autowired
    private ExportService exportService;
    
    /**
     * 导出员工列表
     * 支持条件筛选：姓名、性别、状态等
     * 
     * @param queryDTO 查询条件（可选）
     * @param response HTTP响应对象
     */
    @GetMapping("/employees")
    public void exportEmployees(EmployeeQueryDTO queryDTO, HttpServletResponse response) {
        log.info("导出员工列表请求：查询条件={}", queryDTO);
        exportService.exportEmployees(queryDTO, response);
    }
    
    /**
     * 导出考勤记录
     * 必须指定日期范围
     * 
     * @param queryDTO 查询条件（必须包含startDate和endDate）
     * @param response HTTP响应对象
     */
    @GetMapping("/attendances")
    public void exportAttendances(AttendanceQueryDTO queryDTO, HttpServletResponse response) {
        log.info("导出考勤记录请求：查询条件={}", queryDTO);
        exportService.exportAttendances(queryDTO, response);
    }
    
    /**
     * 导出薪资记录
     * 必须指定发薪月份
     * 
     * @param queryDTO 查询条件（必须包含payMonth）
     * @param response HTTP响应对象
     */
    @GetMapping("/payrolls")
    public void exportPayrolls(PayrollQueryDTO queryDTO, HttpServletResponse response) {
        log.info("导出薪资记录请求：查询条件={}", queryDTO);
        exportService.exportPayrolls(queryDTO, response);
    }
    
    /**
     * 导出绩效排名
     * 可选指定考核日期
     * 
     * @param evalDate 考核日期（可选）
     * @param response HTTP响应对象
     */
    @GetMapping("/performance-ranking")
    public void exportPerformanceRanking(@RequestParam(required = false) String evalDate, 
                                         HttpServletResponse response) {
        log.info("导出绩效排名请求：考核日期={}", evalDate);
        exportService.exportPerformanceRanking(evalDate, response);
    }
}
