package com.company.ems.mapper;

import com.company.ems.dto.EmployeeQueryDTO;
import com.company.ems.entity.Employee;
import com.company.ems.vo.EmployeeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工Mapper接口
 * 
 * @author EMS Team
 */
public interface EmployeeMapper {
    
    /**
     * 新增员工
     * 
     * @param employee 员工对象
     * @return 影响行数
     */
    int insert(Employee employee);
    
    /**
     * 根据ID更新员工
     * 
     * @param employee 员工对象
     * @return 影响行数
     */
    int updateById(Employee employee);
    
    /**
     * 根据ID删除员工（软删除）
     * 
     * @param employeeId 员工ID
     * @return 影响行数
     */
    int deleteById(@Param("employeeId") Integer employeeId);
    
    /**
     * 根据ID查询员工
     * 
     * @param employeeId 员工ID
     * @return 员工对象
     */
    Employee findById(@Param("employeeId") Integer employeeId);
    
    /**
     * 根据身份证号查询员工
     * 
     * @param idCard 身份证号
     * @return 员工对象
     */
    Employee findByIdCard(@Param("idCard") String idCard);
    
    /**
     * 查询员工列表（带分页和条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 员工列表
     */
    List<EmployeeVO> findList(EmployeeQueryDTO queryDTO);
    
    /**
     * 统计员工总数（带条件）
     * 
     * @param queryDTO 查询参数
     * @return 总数
     */
    Long countList(EmployeeQueryDTO queryDTO);
    
    /**
     * 根据ID查询员工详情（包含部门和岗位信息）
     * 
     * @param employeeId 员工ID
     * @return 员工详情
     */
    EmployeeVO findDetailById(@Param("employeeId") Integer employeeId);
}
