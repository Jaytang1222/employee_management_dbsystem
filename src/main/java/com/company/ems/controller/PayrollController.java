package com.company.ems.controller;

import com.company.ems.dto.PayrollDTO;
import com.company.ems.dto.PayrollQueryDTO;
import com.company.ems.service.PayrollService;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.PayrollVO;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 薪资控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/payrolls")
public class PayrollController {
    
    @Autowired
    private PayrollService payrollService;
    
    /**
     * 新增薪资记录
     * 
     * @param payrollDTO 薪资数据
     * @return 薪资ID
     */
    @PostMapping
    public Result<Integer> addPayroll(@Validated @RequestBody PayrollDTO payrollDTO) {
        log.info("新增薪资记录请求：员工ID={}, 月份={}", 
                 payrollDTO.getEmployeeId(), 
                 payrollDTO.getPayMonth());
        Integer payrollId = payrollService.addPayroll(payrollDTO);
        return Result.success("新增薪资记录成功", payrollId);
    }
    
    /**
     * 更新薪资记录
     * 
     * @param payrollId 薪资ID
     * @param payrollDTO 薪资数据
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Void> updatePayroll(@PathVariable("id") Integer payrollId, 
                                      @Validated @RequestBody PayrollDTO payrollDTO) {
        log.info("更新薪资记录请求：薪资ID={}", payrollId);
        payrollService.updatePayroll(payrollId, payrollDTO);
        return Result.success("更新薪资记录成功", null);
    }
    
    /**
     * 删除薪资记录
     * 
     * @param payrollId 薪资ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePayroll(@PathVariable("id") Integer payrollId) {
        log.info("删除薪资记录请求：薪资ID={}", payrollId);
        payrollService.deletePayroll(payrollId);
        return Result.success("删除薪资记录成功", null);
    }
    
    /**
     * 根据ID查询薪资详情
     * 
     * @param payrollId 薪资ID
     * @return 薪资详情
     */
    @GetMapping("/{id}")
    public Result<PayrollVO> getPayrollDetail(@PathVariable("id") Integer payrollId) {
        log.info("查询薪资详情：薪资ID={}", payrollId);
        PayrollVO payrollVO = payrollService.getPayrollDetail(payrollId);
        return Result.success(payrollVO);
    }
    
    /**
     * 查询薪资列表（分页+条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<PayrollVO>> getPayrollList(PayrollQueryDTO queryDTO) {
        log.info("查询薪资列表：pageNum={}, pageSize={}", queryDTO.getPageNum(), queryDTO.getPageSize());
        PageResult<PayrollVO> pageResult = payrollService.getPayrollList(queryDTO);
        return Result.success(pageResult);
    }
}
