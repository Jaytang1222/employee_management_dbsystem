package com.company.ems.service;

import com.company.ems.dto.DepartmentDTO;
import com.company.ems.entity.Department;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.DepartmentMapper;
import com.company.ems.vo.DepartmentTreeVO;
import com.company.ems.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class DepartmentService {
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    /**
     * 新增部门
     * 
     * @param departmentDTO 部门数据
     * @return 部门ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addDepartment(DepartmentDTO departmentDTO) {
        // 1. 验证上级部门是否存在
        if (departmentDTO.getParentDeptId() != null) {
            Department parentDept = departmentMapper.findById(departmentDTO.getParentDeptId());
            if (parentDept == null) {
                throw new BusinessException("上级部门不存在");
            }
        }
        
        // 2. 构造部门对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        department.setStatus("active");
        
        // 3. 插入数据库
        int rows = departmentMapper.insert(department);
        if (rows == 0) {
            throw new BusinessException("新增部门失败");
        }
        
        log.info("新增部门成功：{} - {}", department.getDeptId(), department.getDeptName());
        return department.getDeptId();
    }
    
    /**
     * 更新部门
     * 
     * @param deptId 部门ID
     * @param departmentDTO 部门数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateDepartment(Integer deptId, DepartmentDTO departmentDTO) {
        // 1. 验证部门是否存在
        Department existDept = departmentMapper.findById(deptId);
        if (existDept == null) {
            throw new BusinessException("部门不存在");
        }
        
        // 2. 验证上级部门是否存在
        if (departmentDTO.getParentDeptId() != null) {
            // 不能将自己设置为上级部门
            if (departmentDTO.getParentDeptId().equals(deptId)) {
                throw new BusinessException("不能将自己设置为上级部门");
            }
            
            Department parentDept = departmentMapper.findById(departmentDTO.getParentDeptId());
            if (parentDept == null) {
                throw new BusinessException("上级部门不存在");
            }
            
            // 验证是否会形成循环引用（防止A->B->C->A的情况）
            if (isCircularReference(deptId, departmentDTO.getParentDeptId())) {
                throw new BusinessException("不能设置为下级部门的子部门，会形成循环引用");
            }
        }
        
        // 3. 构造部门对象
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        department.setDeptId(deptId);
        
        // 4. 更新数据库
        int rows = departmentMapper.updateById(department);
        if (rows == 0) {
            throw new BusinessException("更新部门失败");
        }
        
        log.info("更新部门成功：{} - {}", deptId, department.getDeptName());
    }
    
    /**
     * 删除部门（软删除）
     * 
     * @param deptId 部门ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartment(Integer deptId) {
        // 1. 验证部门是否存在
        Department department = departmentMapper.findById(deptId);
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        
        // 2. 检查是否有子部门
        int childCount = departmentMapper.countByParentId(deptId);
        if (childCount > 0) {
            throw new BusinessException("该部门下有子部门，不能删除");
        }
        
        // 3. 检查是否有员工
        int employeeCount = departmentMapper.countEmployeesByDeptId(deptId);
        if (employeeCount > 0) {
            throw new BusinessException("该部门下有员工，不能删除");
        }
        
        // 4. 软删除
        int rows = departmentMapper.deleteById(deptId);
        if (rows == 0) {
            throw new BusinessException("删除部门失败");
        }
        
        log.info("删除部门成功：{} - {}", deptId, department.getDeptName());
    }
    
    /**
     * 根据ID查询部门详情
     * 
     * @param deptId 部门ID
     * @return 部门详情
     */
    public DepartmentVO getDepartmentDetail(Integer deptId) {
        DepartmentVO departmentVO = departmentMapper.findDetailById(deptId);
        if (departmentVO == null) {
            throw new BusinessException("部门不存在");
        }
        return departmentVO;
    }
    
    /**
     * 查询部门列表
     * 
     * @return 部门列表
     */
    public List<DepartmentVO> getDepartmentList() {
        return departmentMapper.findList();
    }
    
    /**
     * 查询部门树形结构（★核心功能）
     * 
     * @return 部门树形结构
     */
    public List<DepartmentTreeVO> getDepartmentTree() {
        // 1. 查询所有部门
        List<DepartmentTreeVO> allDepts = departmentMapper.findAllForTree();
        
        // 2. 构建部门Map（key: deptId, value: DepartmentTreeVO）
        Map<Integer, DepartmentTreeVO> deptMap = allDepts.stream()
                .collect(Collectors.toMap(DepartmentTreeVO::getDeptId, dept -> dept));
        
        // 3. 构建树形结构
        List<DepartmentTreeVO> rootDepts = new ArrayList<>();
        for (DepartmentTreeVO dept : allDepts) {
            if (dept.getParentDeptId() == null) {
                // 根部门
                rootDepts.add(dept);
            } else {
                // 子部门，添加到父部门的children中
                DepartmentTreeVO parentDept = deptMap.get(dept.getParentDeptId());
                if (parentDept != null) {
                    parentDept.getChildren().add(dept);
                }
            }
        }
        
        log.info("查询部门树形结构成功，根部门数量：{}", rootDepts.size());
        return rootDepts;
    }
    
    /**
     * 验证是否会形成循环引用
     * 
     * @param deptId 当前部门ID
     * @param parentDeptId 要设置的上级部门ID
     * @return true-会形成循环引用，false-不会
     */
    private boolean isCircularReference(Integer deptId, Integer parentDeptId) {
        // 从parentDeptId开始，向上查找，如果找到deptId，说明会形成循环
        Integer currentId = parentDeptId;
        while (currentId != null) {
            if (currentId.equals(deptId)) {
                return true;
            }
            Department dept = departmentMapper.findById(currentId);
            if (dept == null) {
                break;
            }
            currentId = dept.getParentDeptId();
        }
        return false;
    }
}
