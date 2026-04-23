package com.company.ems.controller;

import com.company.ems.dto.DepartmentDTO;
import com.company.ems.service.DepartmentService;
import com.company.ems.vo.DepartmentTreeVO;
import com.company.ems.vo.DepartmentVO;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;
    
    /**
     * 新增部门
     * 
     * @param departmentDTO 部门数据
     * @return 部门ID
     */
    @PostMapping
    public Result<Integer> addDepartment(@Validated @RequestBody DepartmentDTO departmentDTO) {
        log.info("新增部门请求：{}", departmentDTO.getDeptName());
        Integer deptId = departmentService.addDepartment(departmentDTO);
        return Result.success("新增部门成功", deptId);
    }
    
    /**
     * 更新部门
     * 
     * @param deptId 部门ID
     * @param departmentDTO 部门数据
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Void> updateDepartment(@PathVariable("id") Integer deptId, 
                                        @Validated @RequestBody DepartmentDTO departmentDTO) {
        log.info("更新部门请求：{}", deptId);
        departmentService.updateDepartment(deptId, departmentDTO);
        return Result.success("更新部门成功", null);
    }
    
    /**
     * 删除部门（软删除）
     * 
     * @param deptId 部门ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDepartment(@PathVariable("id") Integer deptId) {
        log.info("删除部门请求：{}", deptId);
        departmentService.deleteDepartment(deptId);
        return Result.success("删除部门成功", null);
    }
    
    /**
     * 根据ID查询部门详情
     * 
     * @param deptId 部门ID
     * @return 部门详情
     */
    @GetMapping("/{id}")
    public Result<DepartmentVO> getDepartmentDetail(@PathVariable("id") Integer deptId) {
        log.info("查询部门详情：{}", deptId);
        DepartmentVO departmentVO = departmentService.getDepartmentDetail(deptId);
        return Result.success(departmentVO);
    }
    
    /**
     * 查询部门列表
     * 
     * @return 部门列表
     */
    @GetMapping
    public Result<List<DepartmentVO>> getDepartmentList() {
        log.info("查询部门列表");
        List<DepartmentVO> list = departmentService.getDepartmentList();
        return Result.success(list);
    }
    
    /**
     * 查询部门树形结构（★特色功能）
     * 
     * @return 部门树形结构
     */
    @GetMapping("/tree")
    public Result<List<DepartmentTreeVO>> getDepartmentTree() {
        log.info("查询部门树形结构");
        List<DepartmentTreeVO> tree = departmentService.getDepartmentTree();
        return Result.success(tree);
    }
}
