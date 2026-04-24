package com.company.ems.service;

import com.company.ems.dto.AttendanceQueryDTO;
import com.company.ems.dto.EmployeeQueryDTO;
import com.company.ems.dto.PayrollQueryDTO;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.AttendanceMapper;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.mapper.PayrollMapper;
import com.company.ems.util.ExcelUtil;
import com.company.ems.vo.AttendanceVO;
import com.company.ems.vo.EmployeeVO;
import com.company.ems.vo.PayrollVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据导出业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class ExportService {
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private AttendanceMapper attendanceMapper;
    
    @Autowired
    private PayrollMapper payrollMapper;
    
    /**
     * 导出员工列表
     * 
     * @param queryDTO 查询条件（可选）
     * @param response HTTP响应对象
     */
    public void exportEmployees(EmployeeQueryDTO queryDTO, HttpServletResponse response) {
        log.info("开始导出员工列表");
        
        // 1. 查询所有员工数据（不分页）
        queryDTO.setPageNum(0);
        queryDTO.setPageSize(Integer.MAX_VALUE);
        List<EmployeeVO> employees = employeeMapper.findList(queryDTO);
        
        if (employees == null || employees.isEmpty()) {
            throw new BusinessException("没有可导出的员工数据");
        }
        
        // 2. 定义表头
        String[] headers = {
            "员工ID", "姓名", "性别", "身份证号", "手机号", "邮箱",
            "出生日期", "入职日期", "学历", "主部门", "主岗位",
            "地址", "紧急联系人", "状态"
        };
        
        // 3. 转换数据
        List<Object[]> dataList = new ArrayList<>();
        for (EmployeeVO emp : employees) {
            Object[] row = {
                emp.getEmployeeId(),
                emp.getName(),
                emp.getGender(),
                emp.getIdCard(),
                emp.getPhone(),
                emp.getEmail(),
                emp.getBirthDate(),
                emp.getHireDate(),
                emp.getEducationLevel(),
                emp.getPrimaryDeptName(),
                emp.getPrimaryPositionName(),
                emp.getAddress(),
                emp.getEmergencyContact(),
                emp.getStatus()
            };
            dataList.add(row);
        }
        
        // 4. 导出Excel
        String fileName = "员工列表_" + System.currentTimeMillis();
        ExcelUtil.exportExcel(headers, dataList, fileName, response);
        
        log.info("员工列表导出完成，共{}条记录", employees.size());
    }
    
    /**
     * 导出考勤记录
     * 
     * @param queryDTO 查询条件（必须包含月份）
     * @param response HTTP响应对象
     */
    public void exportAttendances(AttendanceQueryDTO queryDTO, HttpServletResponse response) {
        log.info("开始导出考勤记录");
        
        // 1. 验证参数
        if (queryDTO.getStartDate() == null && queryDTO.getEndDate() == null) {
            throw new BusinessException("请指定考勤日期范围");
        }
        
        // 2. 查询所有考勤数据（不分页）
        queryDTO.setPageNum(0);
        queryDTO.setPageSize(Integer.MAX_VALUE);
        List<AttendanceVO> attendances = attendanceMapper.findList(queryDTO);
        
        if (attendances == null || attendances.isEmpty()) {
            throw new BusinessException("没有可导出的考勤数据");
        }
        
        // 3. 定义表头
        String[] headers = {
            "考勤ID", "员工ID", "员工姓名", "考勤日期", "签到时间", "签退时间",
            "考勤状态", "迟到分钟", "早退分钟", "加班小时", "备注"
        };
        
        // 4. 转换数据
        List<Object[]> dataList = new ArrayList<>();
        for (AttendanceVO att : attendances) {
            Object[] row = {
                att.getAttendanceId(),
                att.getEmployeeId(),
                att.getEmployeeName(),
                att.getAttendanceDate(),
                att.getCheckInTime(),
                att.getCheckOutTime(),
                getAttendanceStatusText(att.getAttendanceStatus()),
                att.getLateMin(),
                att.getEarlyLeaveMin(),
                att.getOvertimeHours(),
                att.getRemark()
            };
            dataList.add(row);
        }
        
        // 5. 导出Excel
        String fileName = "考勤记录_" + System.currentTimeMillis();
        ExcelUtil.exportExcel(headers, dataList, fileName, response);
        
        log.info("考勤记录导出完成，共{}条记录", attendances.size());
    }
    
    /**
     * 导出薪资记录
     * 
     * @param queryDTO 查询条件（必须包含月份）
     * @param response HTTP响应对象
     */
    public void exportPayrolls(PayrollQueryDTO queryDTO, HttpServletResponse response) {
        log.info("开始导出薪资记录");
        
        // 1. 验证参数
        if (queryDTO.getPayMonth() == null || queryDTO.getPayMonth().isEmpty()) {
            throw new BusinessException("请指定发薪月份");
        }
        
        // 2. 查询所有薪资数据（不分页）
        queryDTO.setPageNum(0);
        queryDTO.setPageSize(Integer.MAX_VALUE);
        List<PayrollVO> payrolls = payrollMapper.findList(queryDTO);
        
        if (payrolls == null || payrolls.isEmpty()) {
            throw new BusinessException("没有可导出的薪资数据");
        }
        
        // 3. 定义表头
        String[] headers = {
            "薪资ID", "员工ID", "员工姓名", "发薪月份", "基本工资", "津贴",
            "奖金", "扣款", "社保", "税", "实发工资", "发薪日期"
        };
        
        // 4. 转换数据
        List<Object[]> dataList = new ArrayList<>();
        for (PayrollVO pay : payrolls) {
            Object[] row = {
                pay.getPayrollId(),
                pay.getEmployeeId(),
                pay.getEmployeeName(),
                pay.getPayMonth(),
                pay.getBasePay(),
                pay.getAllowance(),
                pay.getBonus(),
                pay.getDeduction(),
                pay.getSocialInsurance(),
                pay.getTax(),
                pay.getNetPay(),
                pay.getPayDate()
            };
            dataList.add(row);
        }
        
        // 5. 导出Excel
        String fileName = "薪资记录_" + queryDTO.getPayMonth() + "_" + System.currentTimeMillis();
        ExcelUtil.exportExcel(headers, dataList, fileName, response);
        
        log.info("薪资记录导出完成，共{}条记录", payrolls.size());
    }
    
    /**
     * 转换考勤状态为中文
     */
    private String getAttendanceStatusText(String status) {
        if (status == null) {
            return "";
        }
        switch (status) {
            case "normal":
                return "正常";
            case "late":
                return "迟到";
            case "early_leave":
                return "早退";
            case "absent":
                return "缺勤";
            case "leave":
                return "请假";
            default:
                return status;
        }
    }
}
