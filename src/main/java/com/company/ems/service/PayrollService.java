package com.company.ems.service;

import com.company.ems.dto.PayrollDTO;
import com.company.ems.dto.PayrollQueryDTO;
import com.company.ems.entity.Employee;
import com.company.ems.entity.Payroll;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.mapper.PayrollMapper;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.PayrollVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 薪资业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class PayrollService {
    
    @Autowired
    private PayrollMapper payrollMapper;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    /**
     * 新增薪资记录
     * 
     * @param payrollDTO 薪资数据
     * @return 薪资ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addPayroll(PayrollDTO payrollDTO) {
        // 1. 验证员工是否存在
        Employee employee = employeeMapper.findById(payrollDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        if (!"active".equals(employee.getStatus())) {
            throw new BusinessException("员工状态不是active，无法添加薪资记录");
        }
        
        // 2. 检查该员工当月是否已有薪资记录
        Payroll existPayroll = payrollMapper.findByEmployeeAndMonth(
            payrollDTO.getEmployeeId(), 
            payrollDTO.getPayMonth()
        );
        if (existPayroll != null) {
            throw new BusinessException("该员工当月已有薪资记录，请使用更新功能");
        }
        
        // 3. 构造薪资对象
        Payroll payroll = new Payroll();
        BeanUtils.copyProperties(payrollDTO, payroll);
        
        // 4. 设置默认值
        if (payroll.getAllowance() == null) {
            payroll.setAllowance(BigDecimal.ZERO);
        }
        if (payroll.getBonus() == null) {
            payroll.setBonus(BigDecimal.ZERO);
        }
        if (payroll.getDeduction() == null) {
            payroll.setDeduction(BigDecimal.ZERO);
        }
        if (payroll.getSocialInsurance() == null) {
            payroll.setSocialInsurance(BigDecimal.ZERO);
        }
        if (payroll.getTax() == null) {
            payroll.setTax(BigDecimal.ZERO);
        }
        
        // 5. 计算实发工资
        calculateNetPay(payroll);
        
        // 6. 插入数据库
        int rows = payrollMapper.insert(payroll);
        if (rows == 0) {
            throw new BusinessException("新增薪资记录失败");
        }
        
        log.info("新增薪资记录成功：员工ID={}, 月份={}, 实发工资={}", 
                 payrollDTO.getEmployeeId(), 
                 payrollDTO.getPayMonth(), 
                 payroll.getNetPay());
        return payroll.getPayrollId();
    }
    
    /**
     * 更新薪资记录
     * 
     * @param payrollId 薪资ID
     * @param payrollDTO 薪资数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePayroll(Integer payrollId, PayrollDTO payrollDTO) {
        // 1. 验证薪资记录是否存在
        Payroll existPayroll = payrollMapper.findById(payrollId);
        if (existPayroll == null) {
            throw new BusinessException("薪资记录不存在");
        }
        
        // 2. 验证员工是否存在
        Employee employee = employeeMapper.findById(payrollDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 3. 检查唯一性约束（员工+月份）
        Payroll conflictPayroll = payrollMapper.findByEmployeeAndMonth(
            payrollDTO.getEmployeeId(), 
            payrollDTO.getPayMonth()
        );
        // 如果找到记录，且不是当前正在更新的记录，则冲突
        if (conflictPayroll != null && !conflictPayroll.getPayrollId().equals(payrollId)) {
            throw new BusinessException("该员工当月已有薪资记录");
        }
        
        // 4. 构造薪资对象
        Payroll payroll = new Payroll();
        BeanUtils.copyProperties(payrollDTO, payroll);
        payroll.setPayrollId(payrollId);
        
        // 5. 设置默认值
        if (payroll.getAllowance() == null) {
            payroll.setAllowance(BigDecimal.ZERO);
        }
        if (payroll.getBonus() == null) {
            payroll.setBonus(BigDecimal.ZERO);
        }
        if (payroll.getDeduction() == null) {
            payroll.setDeduction(BigDecimal.ZERO);
        }
        if (payroll.getSocialInsurance() == null) {
            payroll.setSocialInsurance(BigDecimal.ZERO);
        }
        if (payroll.getTax() == null) {
            payroll.setTax(BigDecimal.ZERO);
        }
        
        // 6. 重新计算实发工资
        calculateNetPay(payroll);
        
        // 7. 更新数据库
        int rows = payrollMapper.updateById(payroll);
        if (rows == 0) {
            throw new BusinessException("更新薪资记录失败");
        }
        
        log.info("更新薪资记录成功：薪资ID={}, 员工ID={}, 实发工资={}", 
                 payrollId, payrollDTO.getEmployeeId(), payroll.getNetPay());
    }
    
    /**
     * 删除薪资记录
     * 
     * @param payrollId 薪资ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePayroll(Integer payrollId) {
        // 1. 验证薪资记录是否存在
        Payroll payroll = payrollMapper.findById(payrollId);
        if (payroll == null) {
            throw new BusinessException("薪资记录不存在");
        }
        
        // 2. 删除记录
        int rows = payrollMapper.deleteById(payrollId);
        if (rows == 0) {
            throw new BusinessException("删除薪资记录失败");
        }
        
        log.info("删除薪资记录成功：薪资ID={}", payrollId);
    }
    
    /**
     * 根据ID查询薪资详情
     * 
     * @param payrollId 薪资ID
     * @return 薪资详情
     */
    public PayrollVO getPayrollDetail(Integer payrollId) {
        PayrollVO payrollVO = payrollMapper.findDetailById(payrollId);
        if (payrollVO == null) {
            throw new BusinessException("薪资记录不存在");
        }
        return payrollVO;
    }
    
    /**
     * 查询薪资列表（分页）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    public PageResult<PayrollVO> getPayrollList(PayrollQueryDTO queryDTO) {
        // 1. 计算分页偏移量
        int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        queryDTO.setPageNum(offset);
        
        // 2. 查询总数
        Long total = payrollMapper.countList(queryDTO);
        
        // 3. 查询列表
        List<PayrollVO> records = payrollMapper.findList(queryDTO);
        
        // 4. 构造分页结果
        return new PageResult<>(total, records, queryDTO.getPageNum() / queryDTO.getPageSize() + 1, queryDTO.getPageSize());
    }
    
    /**
     * 计算实发工资
     * 公式：实发工资 = 基本工资 + 津贴 + 奖金 - 扣款 - 社保 - 税
     * 
     * @param payroll 薪资对象
     */
    private void calculateNetPay(Payroll payroll) {
        BigDecimal netPay = payroll.getBasePay()
            .add(payroll.getAllowance())
            .add(payroll.getBonus())
            .subtract(payroll.getDeduction())
            .subtract(payroll.getSocialInsurance())
            .subtract(payroll.getTax());
        
        // 确保实发工资不为负数
        if (netPay.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("计算的实发工资不能为负数，请检查各项金额");
        }
        
        payroll.setNetPay(netPay);
    }
}
