package com.company.ems.mapper;

import com.company.ems.entity.Department;
import com.company.ems.vo.DepartmentTreeVO;
import com.company.ems.vo.DepartmentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门Mapper接口
 * 
 * @author EMS Team
 */
public interface DepartmentMapper {
    
    /**
     * 新增部门
     * 
     * @param department 部门对象
     * @return 影响行数
     */
    int insert(Department department);
    
    /**
     * 根据ID更新部门
     * 
     * @param department 部门对象
     * @return 影响行数
     */
    int updateById(Department department);
    
    /**
     * 根据ID删除部门（软删除）
     * 
     * @param deptId 部门ID
     * @return 影响行数
     */
    int deleteById(@Param("deptId") Integer deptId);
    
    /**
     * 根据ID查询部门
     * 
     * @param deptId 部门ID
     * @return 部门对象
     */
    Department findById(@Param("deptId") Integer deptId);
    
    /**
     * 查询部门列表
     * 
     * @return 部门列表
     */
    List<DepartmentVO> findList();
    
    /**
     * 根据ID查询部门详情（包含上级部门和经理信息）
     * 
     * @param deptId 部门ID
     * @return 部门详情
     */
    DepartmentVO findDetailById(@Param("deptId") Integer deptId);
    
    /**
     * 查询所有部门（用于构建树形结构）
     * 
     * @return 部门列表
     */
    List<DepartmentTreeVO> findAllForTree();
    
    /**
     * 根据上级部门ID查询子部门数量
     * 
     * @param parentDeptId 上级部门ID
     * @return 子部门数量
     */
    int countByParentId(@Param("parentDeptId") Integer parentDeptId);
    
    /**
     * 根据部门ID查询员工数量
     * 
     * @param deptId 部门ID
     * @return 员工数量
     */
    int countEmployeesByDeptId(@Param("deptId") Integer deptId);
    
    /**
     * 按部门统计员工数量
     * 统计每个部门的主部门员工数量（is_primary = true）
     * 
     * @return 部门员工统计列表
     */
    List<com.company.ems.vo.EmployeeCountByDeptVO> countEmployeeByDepartment();
}
