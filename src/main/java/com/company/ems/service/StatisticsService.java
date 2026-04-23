package com.company.ems.service;

import com.company.ems.mapper.DepartmentMapper;
import com.company.ems.vo.EmployeeCountByDeptVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 统计报表业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class StatisticsService {
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    /**
     * 按部门统计员工数量
     * 统计每个部门的主部门员工数量（is_primary = true）
     * 
     * @return 部门员工统计列表
     */
    public List<EmployeeCountByDeptVO> countEmployeeByDepartment() {
        log.info("查询按部门统计员工数量");
        List<EmployeeCountByDeptVO> statistics = departmentMapper.countEmployeeByDepartment();
        log.info("按部门统计员工数量完成，共{}个部门", statistics.size());
        return statistics;
    }
}
