package com.company.ems.controller;

import com.company.ems.dto.PerformanceDTO;
import com.company.ems.dto.PerformanceQueryDTO;
import com.company.ems.service.PerformanceService;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.PerformanceRankingVO;
import com.company.ems.vo.PerformanceVO;
import com.company.ems.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 绩效控制器
 * 
 * @author EMS Team
 */
@Slf4j
@RestController
@RequestMapping("/api/performances")
public class PerformanceController {
    
    @Autowired
    private PerformanceService performanceService;
    
    /**
     * 新增绩效记录
     * 
     * @param performanceDTO 绩效数据
     * @return 绩效ID
     */
    @PostMapping
    public Result<Integer> addPerformance(@Validated @RequestBody PerformanceDTO performanceDTO) {
        log.info("新增绩效记录请求：员工ID={}, 考核周期={}", 
                 performanceDTO.getEmployeeId(), 
                 performanceDTO.getEvalDate());
        Integer performanceId = performanceService.addPerformance(performanceDTO);
        return Result.success("新增绩效记录成功", performanceId);
    }
    
    /**
     * 更新绩效记录
     * 
     * @param performanceId 绩效ID
     * @param performanceDTO 绩效数据
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public Result<Void> updatePerformance(@PathVariable("id") Integer performanceId, 
                                          @Validated @RequestBody PerformanceDTO performanceDTO) {
        log.info("更新绩效记录请求：绩效ID={}", performanceId);
        performanceService.updatePerformance(performanceId, performanceDTO);
        return Result.success("更新绩效记录成功", null);
    }
    
    /**
     * 删除绩效记录
     * 
     * @param performanceId 绩效ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePerformance(@PathVariable("id") Integer performanceId) {
        log.info("删除绩效记录请求：绩效ID={}", performanceId);
        performanceService.deletePerformance(performanceId);
        return Result.success("删除绩效记录成功", null);
    }
    
    /**
     * 根据ID查询绩效详情
     * 
     * @param performanceId 绩效ID
     * @return 绩效详情
     */
    @GetMapping("/{id}")
    public Result<PerformanceVO> getPerformanceDetail(@PathVariable("id") Integer performanceId) {
        log.info("查询绩效详情：绩效ID={}", performanceId);
        PerformanceVO performanceVO = performanceService.getPerformanceDetail(performanceId);
        return Result.success(performanceVO);
    }
    
    /**
     * 查询绩效列表（分页+条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    @GetMapping
    public Result<PageResult<PerformanceVO>> getPerformanceList(PerformanceQueryDTO queryDTO) {
        log.info("查询绩效列表：pageNum={}, pageSize={}", queryDTO.getPageNum(), queryDTO.getPageSize());
        PageResult<PerformanceVO> pageResult = performanceService.getPerformanceList(queryDTO);
        return Result.success(pageResult);
    }
    
    /**
     * 查询绩效排名
     * 
     * @param evalDate 考核周期（格式：YYYY-MM-DD，可选）
     * @return 绩效排名列表
     */
    @GetMapping("/ranking")
    public Result<List<PerformanceRankingVO>> getPerformanceRanking(
            @RequestParam(required = false) String evalDate) {
        log.info("查询绩效排名：考核周期={}", evalDate);
        List<PerformanceRankingVO> rankings = performanceService.getPerformanceRanking(evalDate);
        return Result.success(rankings);
    }
}
