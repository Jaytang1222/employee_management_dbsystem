package com.company.ems.controller;

import com.company.ems.dto.PositionDTO;
import com.company.ems.dto.PositionQueryDTO;
import com.company.ems.service.PositionService;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.PositionVO;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/positions")
public class PositionController {
    
    @Autowired
    private PositionService positionService;
    
    /**
     * 新增岗位
     * 
     * @param positionDTO 岗位数据
     * @return 岗位ID
     */
    @PostMapping
    public Result<Integer> addPosition(@Validated @RequestBody PositionDTO positionDTO) {
        log.info("新增岗位请求：{}", positionDTO.getPositionName());
        Integer positionId = positionService.addPosition(positionDTO);
        return Result.success("新增岗位成功", positionId);
    }
    
    /**
     * 更新岗位
     * 
     * @param positionId 岗位ID
     * @param positionDTO 岗位数据
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Void> updatePosition(@PathVariable("id") Integer positionId, 
                                       @Validated @RequestBody PositionDTO positionDTO) {
        log.info("更新岗位请求：{}", positionId);
        positionService.updatePosition(positionId, positionDTO);
        return Result.success("更新岗位成功", null);
    }
    
    /**
     * 删除岗位（软删除）
     * 
     * @param positionId 岗位ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePosition(@PathVariable("id") Integer positionId) {
        log.info("删除岗位请求：{}", positionId);
        positionService.deletePosition(positionId);
        return Result.success("删除岗位成功", null);
    }
    
    /**
     * 根据ID查询岗位详情
     * 
     * @param positionId 岗位ID
     * @return 岗位详情
     */
    @GetMapping("/{id}")
    public Result<PositionVO> getPositionDetail(@PathVariable("id") Integer positionId) {
        log.info("查询岗位详情：{}", positionId);
        PositionVO positionVO = positionService.getPositionDetail(positionId);
        return Result.success(positionVO);
    }
    
    /**
     * 查询岗位列表（分页+条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<PositionVO>> getPositionList(PositionQueryDTO queryDTO) {
        log.info("查询岗位列表：pageNum={}, pageSize={}", queryDTO.getPageNum(), queryDTO.getPageSize());
        PageResult<PositionVO> pageResult = positionService.getPositionList(queryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 查询所有岗位（不分页，用于下拉选择）
     * 
     * @return 岗位列表
     */
    @GetMapping("/all")
    public Result<List<PositionVO>> getAllPositions() {
        log.info("查询所有岗位");
        List<PositionVO> list = positionService.getAllPositions();
        return Result.success(list);
    }
}
