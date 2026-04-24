package com.company.ems.controller;

import com.company.ems.dto.AssignmentDTO;
import com.company.ems.dto.AssignmentQueryDTO;
import com.company.ems.service.AssignmentService;
import com.company.ems.vo.AssignmentVO;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工分配控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    
    @Autowired
    private AssignmentService assignmentService;
    
    /**
     * 新增员工分配
     * 
     * @param assignmentDTO 分配数据
     * @return 分配ID
     */
    @PostMapping
    public Result<Integer> addAssignment(@Validated @RequestBody AssignmentDTO assignmentDTO) {
        log.info("新增员工分配请求：员工ID={}, 部门ID={}, 岗位ID={}", 
                 assignmentDTO.getEmployeeId(), 
                 assignmentDTO.getDeptId(), 
                 assignmentDTO.getPositionId());
        Integer assignmentId = assignmentService.addAssignment(assignmentDTO);
        return Result.success("新增员工分配成功", assignmentId);
    }
    
    /**
     * 员工调动（保留历史记录）
     * 
     * @param assignmentDTO 新的分配数据
     * @return 新分配ID
     */
    @PostMapping("/transfer")
    public Result<Integer> transferEmployee(@Validated @RequestBody AssignmentDTO assignmentDTO) {
        log.info("员工调动请求：员工ID={}, 新部门ID={}, 新岗位ID={}", 
                 assignmentDTO.getEmployeeId(), 
                 assignmentDTO.getDeptId(), 
                 assignmentDTO.getPositionId());
        Integer assignmentId = assignmentService.transferEmployee(assignmentDTO);
        return Result.success("员工调动成功", assignmentId);
    }
    
    /**
     * 查询员工当前有效的分配记录
     * 
     * @param employeeId 员工ID
     * @return 分配列表
     */
    @GetMapping("/employee/{employeeId}")
    public Result<List<AssignmentVO>> getActiveAssignments(@PathVariable Integer employeeId) {
        log.info("查询员工当前分配：员工ID={}", employeeId);
        List<AssignmentVO> assignments = assignmentService.getActiveAssignments(employeeId);
        return Result.success(assignments);
    }
    
    /**
     * 查询员工的主部门分配
     * 
     * @param employeeId 员工ID
     * @return 主部门分配信息
     */
    @GetMapping("/employee/{employeeId}/primary")
    public Result<AssignmentVO> getPrimaryAssignment(@PathVariable Integer employeeId) {
        log.info("查询员工主部门分配：员工ID={}", employeeId);
        AssignmentVO assignment = assignmentService.getPrimaryAssignment(employeeId);
        return Result.success(assignment);
    }
    
    /**
     * 查询员工的历史分配记录
     * 
     * @param employeeId 员工ID
     * @return 历史分配列表
     */
    @GetMapping("/employee/{employeeId}/history")
    public Result<List<AssignmentVO>> getAssignmentHistory(@PathVariable Integer employeeId) {
        log.info("查询员工历史分配：员工ID={}", employeeId);
        List<AssignmentVO> history = assignmentService.getAssignmentHistory(employeeId);
        return Result.success(history);
    }
    
    /**
     * 查询员工分配列表（分页+条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<AssignmentVO>> getAssignmentList(AssignmentQueryDTO queryDTO) {
        log.info("查询员工分配列表：pageNum={}, pageSize={}", queryDTO.getPageNum(), queryDTO.getPageSize());
        PageResult<AssignmentVO> pageResult = assignmentService.getAssignmentList(queryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 根据ID查询分配详情
     * 
     * @param assignmentId 分配ID
     * @return 分配详情
     */
    @GetMapping("/{id}")
    public Result<AssignmentVO> getAssignmentDetail(@PathVariable("id") Integer assignmentId) {
        log.info("查询分配详情：分配ID={}", assignmentId);
        AssignmentVO assignment = assignmentService.getAssignmentDetail(assignmentId);
        return Result.success(assignment);
    }
    
    /**
     * 删除员工分配（将状态设置为inactive）
     * 
     * @param assignmentId 分配ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteAssignment(@PathVariable("id") Integer assignmentId) {
        log.info("删除员工分配请求：分配ID={}", assignmentId);
        assignmentService.deleteAssignment(assignmentId);
        return Result.success("删除员工分配成功", null);
    }
}
