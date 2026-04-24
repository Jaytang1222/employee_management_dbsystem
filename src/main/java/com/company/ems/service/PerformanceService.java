package com.company.ems.service;

import com.company.ems.dto.PerformanceDTO;
import com.company.ems.dto.PerformanceQueryDTO;
import com.company.ems.entity.Employee;
import com.company.ems.entity.Performance;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.EmployeeMapper;
import com.company.ems.mapper.PerformanceMapper;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.PerformanceRankingVO;
import com.company.ems.vo.PerformanceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 绩效业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class PerformanceService {
    
    @Autowired
    private PerformanceMapper performanceMapper;
    
    @Autowired
    private EmployeeMapper employeeMapper;
    
    /**
     * 新增绩效记录
     * 
     * @param performanceDTO 绩效数据
     * @return 绩效ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addPerformance(PerformanceDTO performanceDTO) {
        // 1. 验证员工是否存在
        Employee employee = employeeMapper.findById(performanceDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        if (!"active".equals(employee.getStatus())) {
            throw new BusinessException("员工状态不是active，无法添加绩效记录");
        }
        
        // 2. 构造绩效对象
        Performance performance = new Performance();
        BeanUtils.copyProperties(performanceDTO, performance);
        
        // 3. 根据分数自动计算等级
        calculateGrade(performance);
        
        // 4. 插入数据库
        int rows = performanceMapper.insert(performance);
        if (rows == 0) {
            throw new BusinessException("新增绩效记录失败");
        }
        
        log.info("新增绩效记录成功：员工ID={}, 考核周期={}, 分数={}, 等级={}", 
                 performanceDTO.getEmployeeId(), 
                 performanceDTO.getEvalDate(), 
                 performance.getScore(),
                 performance.getGrade());
        return performance.getPerformanceId();
    }
    
    /**
     * 更新绩效记录
     * 
     * @param performanceId 绩效ID
     * @param performanceDTO 绩效数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePerformance(Integer performanceId, PerformanceDTO performanceDTO) {
        // 1. 验证绩效记录是否存在
        Performance existPerformance = performanceMapper.findById(performanceId);
        if (existPerformance == null) {
            throw new BusinessException("绩效记录不存在");
        }
        
        // 2. 验证员工是否存在
        Employee employee = employeeMapper.findById(performanceDTO.getEmployeeId());
        if (employee == null) {
            throw new BusinessException("员工不存在");
        }
        
        // 3. 构造绩效对象
        Performance performance = new Performance();
        BeanUtils.copyProperties(performanceDTO, performance);
        performance.setPerformanceId(performanceId);
        
        // 4. 根据分数自动计算等级
        calculateGrade(performance);
        
        // 5. 更新数据库
        int rows = performanceMapper.updateById(performance);
        if (rows == 0) {
            throw new BusinessException("更新绩效记录失败");
        }
        
        log.info("更新绩效记录成功：绩效ID={}, 员工ID={}, 分数={}, 等级={}", 
                 performanceId, performanceDTO.getEmployeeId(), 
                 performance.getScore(), performance.getGrade());
    }
    
    /**
     * 删除绩效记录
     * 
     * @param performanceId 绩效ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePerformance(Integer performanceId) {
        // 1. 验证绩效记录是否存在
        Performance performance = performanceMapper.findById(performanceId);
        if (performance == null) {
            throw new BusinessException("绩效记录不存在");
        }
        
        // 2. 删除记录
        int rows = performanceMapper.deleteById(performanceId);
        if (rows == 0) {
            throw new BusinessException("删除绩效记录失败");
        }
        
        log.info("删除绩效记录成功：绩效ID={}", performanceId);
    }
    
    /**
     * 根据ID查询绩效详情
     * 
     * @param performanceId 绩效ID
     * @return 绩效详情
     */
    public PerformanceVO getPerformanceDetail(Integer performanceId) {
        PerformanceVO performanceVO = performanceMapper.findDetailById(performanceId);
        if (performanceVO == null) {
            throw new BusinessException("绩效记录不存在");
        }
        return performanceVO;
    }
    
    /**
     * 查询绩效列表（分页）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    public PageResult<PerformanceVO> getPerformanceList(PerformanceQueryDTO queryDTO) {
        // 1. 计算分页偏移量
        int offset = (queryDTO.getPageNum() - 1) * queryDTO.getPageSize();
        queryDTO.setPageNum(offset);
        
        // 2. 查询总数
        Long total = performanceMapper.countList(queryDTO);
        
        // 3. 查询列表
        List<PerformanceVO> records = performanceMapper.findList(queryDTO);
        
        // 4. 构造分页结果
        return new PageResult<>(total, records, queryDTO.getPageNum() / queryDTO.getPageSize() + 1, queryDTO.getPageSize());
    }
    
    /**
     * 查询绩效排名
     * 
     * @param evalDate 考核周期（格式：YYYY-MM-DD）
     * @return 绩效排名列表
     */
    public List<PerformanceRankingVO> getPerformanceRanking(String evalDate) {
        // 验证日期格式
        if (evalDate != null && !evalDate.isEmpty() && !evalDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new BusinessException("考核周期格式不正确，应为：YYYY-MM-DD");
        }
        
        List<PerformanceRankingVO> rankings = performanceMapper.getPerformanceRanking(evalDate);
        
        log.info("查询绩效排名：考核周期={}, 结果数量={}", evalDate, rankings.size());
        return rankings;
    }
    
    /**
     * 根据分数自动计算等级
     * 规则：
     * - A：90-100分
     * - B：80-89分
     * - C：60-79分
     * - D：<60分
     * 
     * @param performance 绩效对象
     */
    private void calculateGrade(Performance performance) {
        BigDecimal score = performance.getScore();
        String grade;
        
        if (score.compareTo(new BigDecimal("90")) >= 0) {
            grade = "A";
        } else if (score.compareTo(new BigDecimal("80")) >= 0) {
            grade = "B";
        } else if (score.compareTo(new BigDecimal("60")) >= 0) {
            grade = "C";
        } else {
            grade = "D";
        }
        
        performance.setGrade(grade);
    }
}
