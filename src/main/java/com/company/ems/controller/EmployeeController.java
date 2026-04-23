package com.company.ems.controller;

import com.company.ems.dto.EmployeeDTO;
import com.company.ems.dto.EmployeeQueryDTO;
import com.company.ems.service.EmployeeService;
import com.company.ems.vo.EmployeeVO;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 员工控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeService employeeService;
    
    /**
     * 新增员工
     * 
     * @param employeeDTO 员工数据
     * @return 员工ID
     */
    @PostMapping
    public Result<Integer> addEmployee(@Validated @RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工请求：{}", employeeDTO.getName());
        Integer employeeId = employeeService.addEmployee(employeeDTO);
        return Result.success("新增员工成功", employeeId);
    }
    
    /**
     * 更新员工
     * 
     * @param employeeId 员工ID
     * @param employeeDTO 员工数据
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Void> updateEmployee(@PathVariable("id") Integer employeeId, 
                                       @Validated @RequestBody EmployeeDTO employeeDTO) {
        log.info("更新员工请求：{}", employeeId);
        employeeService.updateEmployee(employeeId, employeeDTO);
        return Result.success("更新员工成功", null);
    }
    
    /**
     * 删除员工（软删除）
     * 
     * @param employeeId 员工ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteEmployee(@PathVariable("id") Integer employeeId) {
        log.info("删除员工请求：{}", employeeId);
        employeeService.deleteEmployee(employeeId);
        return Result.success("删除员工成功", null);
    }
    
    /**
     * 根据ID查询员工详情
     * 
     * @param employeeId 员工ID
     * @return 员工详情
     */
    @GetMapping("/{id}")
    public Result<EmployeeVO> getEmployeeDetail(@PathVariable("id") Integer employeeId) {
        log.info("查询员工详情：{}", employeeId);
        EmployeeVO employeeVO = employeeService.getEmployeeDetail(employeeId);
        return Result.success(employeeVO);
    }
    
    /**
     * 查询员工列表（分页+条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<EmployeeVO>> getEmployeeList(EmployeeQueryDTO queryDTO) {
        log.info("查询员工列表：pageNum={}, pageSize={}", queryDTO.getPageNum(), queryDTO.getPageSize());
        PageResult<EmployeeVO> pageResult = employeeService.getEmployeeList(queryDTO);
        return Result.success(pageResult);
    }
}
