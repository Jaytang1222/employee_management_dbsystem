package com.company.ems.mapper;

import com.company.ems.dto.PayrollQueryDTO;
import com.company.ems.entity.Payroll;
import com.company.ems.vo.PayrollVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 薪资Mapper接口
 * 
 * @author EMS Team
 */
@Mapper
public interface PayrollMapper {
    
    /**
     * 新增薪资记录
     * 
     * @param payroll 薪资对象
     * @return 影响行数
     */
    int insert(Payroll payroll);
    
    /**
     * 根据ID更新薪资记录
     * 
     * @param payroll 薪资对象
     * @return 影响行数
     */
    int updateById(Payroll payroll);
    
    /**
     * 根据ID删除薪资记录
     * 
     * @param payrollId 薪资ID
     * @return 影响行数
     */
    int deleteById(@Param("payrollId") Integer payrollId);
    
    /**
     * 根据ID查询薪资记录
     * 
     * @param payrollId 薪资ID
     * @return 薪资对象
     */
    Payroll findById(@Param("payrollId") Integer payrollId);
    
    /**
     * 根据ID查询薪资详情（包含员工信息）
     * 
     * @param payrollId 薪资ID
     * @return 薪资详情
     */
    PayrollVO findDetailById(@Param("payrollId") Integer payrollId);
    
    /**
     * 查询薪资列表（带分页和条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 薪资列表
     */
    List<PayrollVO> findList(PayrollQueryDTO queryDTO);
    
    /**
     * 统计薪资总数（带条件）
     * 
     * @param queryDTO 查询参数
     * @return 总数
     */
    Long countList(PayrollQueryDTO queryDTO);
    
    /**
     * 检查员工在指定月份是否已有薪资记录
     * 
     * @param employeeId 员工ID
     * @param payMonth 发薪月份
     * @return 薪资记录（如果存在）
     */
    Payroll findByEmployeeAndMonth(@Param("employeeId") Integer employeeId, 
                                   @Param("payMonth") String payMonth);
}
