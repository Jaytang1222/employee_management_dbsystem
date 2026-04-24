package com.company.ems.service;

import com.company.ems.dto.EmployeeDTO;
import com.company.ems.dto.EmployeeQueryDTO;
import com.company.ems.entity.Employee;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.vo.EmployeeVO;
import com.company.ems.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * 员工业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class EmployeeService {
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    /**
     * 新增员工
     * 
     * @param employeeDTO 员工数据
     * @return 员工ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addEmployee(EmployeeDTO employeeDTO) {
        // 1. 验证身份证号是否已存在
        Employee existEmployee = employeeMapper.findByIdCard(employeeDTO.getIdCard());
        if (existEmployee != null) {
            throw new BusinessException("身份证号已存在");
        }
        
        // 2. 验证年龄范围（18-65岁）
        if (employeeDTO.getBirthDate() != null) {
            validateAge(employeeDTO.getBirthDate());
        }
        
        // 3. 构造员工对象
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setStatus("active");
        
        // 4. 插入数据库
        int rows = employeeMapper.insert(employee);
        if (rows == 0) {
            throw new BusinessException("新增员工失败");
        }
        
        log.info("新增员工成功：{} - {}", employee.getEmployeeId(), employee.getName());
        return employee.getEmployeeId();
    }
    
    /**
     * 更新员工
     * 
     * @param employeeId 员工ID
     * @param employeeDTO 员工数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployee(Integer employeeId, EmployeeDTO employeeDTO) {
        // 1. 验证员工是否存在
        Employee existEmployee = employeeMapper.findById(employeeId);
        if (existEmployee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 2. 验证身份证号是否被其他员工使用
        Employee employeeByIdCard = employeeMapper.findByIdCard(employeeDTO.getIdCard());
        if (employeeByIdCard != null && !employeeByIdCard.getEmployeeId().equals(employeeId)) {
            throw new BusinessException("身份证号已被其他员工使用");
        }
        
        // 3. 验证年龄范围（18-65岁）
        if (employeeDTO.getBirthDate() != null) {
            validateAge(employeeDTO.getBirthDate());
        }
        
        // 4. 构造员工对象
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employee.setEmployeeId(employeeId);
        
        // 5. 更新数据库
        int rows = employeeMapper.updateById(employee);
        if (rows == 0) {
            throw new BusinessException("更新员工失败");
        }
        
        log.info("更新员工成功：{} - {}", employeeId, employee.getName());
    }
    
    /**
     * 删除员工（软删除）
     * 
     * @param employeeId 员工ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEmployee(Integer employeeId) {
        // 1. 验证员工是否存在
        Employee employee = employeeMapper.findById(employeeId);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 2. 软删除
        int rows = employeeMapper.deleteById(employeeId);
        if (rows == 0) {
            throw new BusinessException("删除员工失败");
        }
        
        log.info("删除员工成功：{} - {}", employeeId, employee.getName());
    }
    
    /**
     * 根据ID查询员工详情
     * 
     * @param employeeId 员工ID
     * @return 员工详情
     */
    public EmployeeVO getEmployeeDetail(Integer employeeId) {
        EmployeeVO employeeVO = employeeMapper.findDetailById(employeeId);
        if (employeeVO == null) {
            throw new BusinessException("员工不存在");
        }
        return employeeVO;
    }
    
    /**
     * 查询员工列表（分页）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    public PageResult<EmployeeVO> getEmployeeList(EmployeeQueryDTO queryDTO) {
        // 1. 保存原始页码
        int currentPage = queryDTO.getPageNum();
        
        // 2. 计算分页偏移量
        int offset = (currentPage - 1) * queryDTO.getPageSize();
        queryDTO.setPageNum(offset);
        
        // 3. 查询总数
        Long total = employeeMapper.countList(queryDTO);
        
        // 4. 查询列表
        List<EmployeeVO> records = employeeMapper.findList(queryDTO);
        
        // 5. 使用原始页码构造分页结果
        return new PageResult<>(total, records, currentPage, queryDTO.getPageSize());
    }
    
    /**
     * 验证年龄范围（18-65岁）
     * 
     * @param birthDate 出生日期
     */
    private void validateAge(LocalDate birthDate) {
        LocalDate now = LocalDate.now();
        int age = Period.between(birthDate, now).getYears();
        
        if (age < 18) {
            throw new BusinessException("员工年龄不能小于18岁");
        }
        if (age > 65) {
            throw new BusinessException("员工年龄不能大于65岁");
        }
    }
}
