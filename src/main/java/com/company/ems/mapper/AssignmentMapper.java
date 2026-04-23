package com.company.ems.mapper;

import com.company.ems.entity.Assignment;
import com.company.ems.vo.AssignmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 员工分配Mapper接口
 * 
 * @author EMS Team
 */
@Mapper
public interface AssignmentMapper {
    
    /**
     * 新增员工分配
     * 
     * @param assignment 分配信息
     * @return 影响行数
     */
    int insert(Assignment assignment);
    
    /**
     * 根据ID查询分配信息
     * 
     * @param assignmentId 分配ID
     * @return 分配信息
     */
    AssignmentVO findById(@Param("assignmentId") Integer assignmentId);
    
    /**
     * 查询员工当前有效的分配记录
     * 
     * @param employeeId 员工ID
     * @return 分配列表
     */
    List<AssignmentVO> findActiveByEmployeeId(@Param("employeeId") Integer employeeId);
    
    /**
     * 查询员工的主部门分配
     * 
     * @param employeeId 员工ID
     * @return 主部门分配信息
     */
    AssignmentVO findPrimaryByEmployeeId(@Param("employeeId") Integer employeeId);
    
    /**
     * 查询员工的历史分配记录
     * 
     * @param employeeId 员工ID
     * @return 历史分配列表
     */
    List<AssignmentVO> findHistoryByEmployeeId(@Param("employeeId") Integer employeeId);
    
    /**
     * 更新分配状态
     * 
     * @param assignmentId 分配ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(@Param("assignmentId") Integer assignmentId, @Param("status") String status);
    
    /**
     * 将员工的所有主部门设置为非主部门
     * 
     * @param employeeId 员工ID
     * @return 影响行数
     */
    int updatePrimaryToFalse(@Param("employeeId") Integer employeeId);
    
    /**
     * 将员工的旧分配记录设置为inactive
     * 
     * @param employeeId 员工ID
     * @return 影响行数
     */
    int inactivateOldAssignments(@Param("employeeId") Integer employeeId);
    
    /**
     * 统计员工在某部门的分配数量
     * 
     * @param employeeId 员工ID
     * @param deptId 部门ID
     * @return 数量
     */
    int countByEmployeeAndDept(@Param("employeeId") Integer employeeId, @Param("deptId") Integer deptId);
}
