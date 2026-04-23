package com.company.ems.service;

import com.company.ems.dto.AssignmentDTO;
import com.company.ems.entity.Assignment;
import com.company.ems.entity.Department;
import com.company.ems.entity.Employee;
import com.company.ems.entity.Position;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.AssignmentMapper;
import com.company.ems.mapper.DepartmentMapper;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.mapper.PositionMapper;
import com.company.ems.vo.AssignmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 员工分配业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class AssignmentService {
    
    @Autowired
    private AssignmentMapper assignmentMapper;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Autowired
    private PositionMapper positionMapper;
    
    /**
     * 新增员工分配
     * 
     * @param assignmentDTO 分配数据
     * @return 分配ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addAssignment(AssignmentDTO assignmentDTO) {
        // 1. 验证员工是否存在
        Employee employee = employeeMapper.findById(assignmentDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        if (!"active".equals(employee.getStatus())) {
            throw new BusinessException("员工状态不是active，无法分配");
        }
        
        // 2. 验证部门是否存在
        Department department = departmentMapper.findById(assignmentDTO.getDeptId());
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        if (!"active".equals(department.getStatus())) {
            throw new BusinessException("部门状态不是active，无法分配");
        }
        
        // 3. 验证岗位是否存在
        Position position = positionMapper.findById(assignmentDTO.getPositionId());
        if (position == null) {
            throw new BusinessException("岗位不存在");
        }
        if (!"active".equals(position.getStatus())) {
            throw new BusinessException("岗位状态不是active，无法分配");
        }
        
        // 4. 如果是主部门，需要将该员工的其他主部门设置为非主部门
        if (assignmentDTO.getIsPrimary()) {
            assignmentMapper.updatePrimaryToFalse(assignmentDTO.getEmployeeId());
        }
        
        // 5. 检查员工是否已经在该部门（避免重复分配）
        int count = assignmentMapper.countByEmployeeAndDept(
            assignmentDTO.getEmployeeId(), 
            assignmentDTO.getDeptId()
        );
        if (count > 0) {
            throw new BusinessException("员工已在该部门，无法重复分配");
        }
        
        // 6. 构造分配对象
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(assignmentDTO, assignment);
        assignment.setStatus("active");
        
        // 7. 插入数据库
        int rows = assignmentMapper.insert(assignment);
        if (rows == 0) {
            throw new BusinessException("新增员工分配失败");
        }
        
        log.info("新增员工分配成功：员工ID={}, 部门ID={}, 岗位ID={}", 
                 assignmentDTO.getEmployeeId(), 
                 assignmentDTO.getDeptId(), 
                 assignmentDTO.getPositionId());
        return assignment.getAssignmentId();
    }
    
    /**
     * 员工调动（保留历史记录）
     * 将旧的分配记录设置为inactive，新增新的分配记录
     * 
     * @param assignmentDTO 新的分配数据
     * @return 新分配ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer transferEmployee(AssignmentDTO assignmentDTO) {
        // 1. 验证员工是否存在
        Employee employee = employeeMapper.findById(assignmentDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        if (!"active".equals(employee.getStatus())) {
            throw new BusinessException("员工状态不是active，无法调动");
        }
        
        // 2. 验证部门是否存在
        Department department = departmentMapper.findById(assignmentDTO.getDeptId());
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        if (!"active".equals(department.getStatus())) {
            throw new BusinessException("部门状态不是active，无法调动");
        }
        
        // 3. 验证岗位是否存在
        Position position = positionMapper.findById(assignmentDTO.getPositionId());
        if (position == null) {
            throw new BusinessException("岗位不存在");
        }
        if (!"active".equals(position.getStatus())) {
            throw new BusinessException("岗位状态不是active，无法调动");
        }
        
        // 4. 将员工的旧分配记录设置为inactive
        assignmentMapper.inactivateOldAssignments(assignmentDTO.getEmployeeId());
        
        // 5. 新增新的分配记录
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(assignmentDTO, assignment);
        assignment.setStatus("active");
        assignment.setIsPrimary(true); // 调动后默认为主部门
        
        int rows = assignmentMapper.insert(assignment);
        if (rows == 0) {
            throw new BusinessException("员工调动失败");
        }
        
        log.info("员工调动成功：员工ID={}, 新部门ID={}, 新岗位ID={}, 调动原因={}", 
                 assignmentDTO.getEmployeeId(), 
                 assignmentDTO.getDeptId(), 
                 assignmentDTO.getPositionId(),
                 assignmentDTO.getChangeReason());
        return assignment.getAssignmentId();
    }
    
    /**
     * 查询员工当前有效的分配记录
     * 
     * @param employeeId 员工ID
     * @return 分配列表
     */
    public List<AssignmentVO> getActiveAssignments(Integer employeeId) {
        // 验证员工是否存在
        Employee employee = employeeMapper.findById(employeeId);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        return assignmentMapper.findActiveByEmployeeId(employeeId);
    }
    
    /**
     * 查询员工的主部门分配
     * 
     * @param employeeId 员工ID
     * @return 主部门分配信息
     */
    public AssignmentVO getPrimaryAssignment(Integer employeeId) {
        // 验证员工是否存在
        Employee employee = employeeMapper.findById(employeeId);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        AssignmentVO primaryAssignment = assignmentMapper.findPrimaryByEmployeeId(employeeId);
        if (primaryAssignment == null) {
            throw new BusinessException("员工没有主部门分配");
        }
        
        return primaryAssignment;
    }
    
    /**
     * 查询员工的历史分配记录
     * 
     * @param employeeId 员工ID
     * @return 历史分配列表
     */
    public List<AssignmentVO> getAssignmentHistory(Integer employeeId) {
        // 验证员工是否存在
        Employee employee = employeeMapper.findById(employeeId);
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        return assignmentMapper.findHistoryByEmployeeId(employeeId);
    }
    
    /**
     * 根据ID查询分配详情
     * 
     * @param assignmentId 分配ID
     * @return 分配详情
     */
    public AssignmentVO getAssignmentDetail(Integer assignmentId) {
        AssignmentVO assignmentVO = assignmentMapper.findById(assignmentId);
        if (assignmentVO == null) {
            throw new BusinessException("分配记录不存在");
        }
        return assignmentVO;
    }
    
    /**
     * 删除员工分配（将状态设置为inactive）
     * 
     * @param assignmentId 分配ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssignment(Integer assignmentId) {
        // 1. 验证分配记录是否存在
        AssignmentVO assignment = assignmentMapper.findById(assignmentId);
        if (assignment == null) {
            throw new BusinessException("分配记录不存在");
        }
        
        // 2. 如果是主部门，不允许删除
        if (assignment.getIsPrimary()) {
            throw new BusinessException("主部门分配不允许删除，请先设置其他部门为主部门");
        }
        
        // 3. 更新状态为inactive
        int rows = assignmentMapper.updateStatus(assignmentId, "inactive");
        if (rows == 0) {
            throw new BusinessException("删除分配记录失败");
        }
        
        log.info("删除员工分配成功：分配ID={}", assignmentId);
    }
}
