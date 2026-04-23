package com.company.ems.mapper;

import com.company.ems.dto.PerformanceQueryDTO;
import com.company.ems.entity.Performance;
import com.company.ems.vo.PerformanceRankingVO;
import com.company.ems.vo.PerformanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 绩效Mapper接口
 * 
 * @author EMS Team
 */
@Mapper
public interface PerformanceMapper {
    
    /**
     * 新增绩效记录
     * 
     * @param performance 绩效对象
     * @return 影响行数
     */
    int insert(Performance performance);
    
    /**
     * 根据ID更新绩效记录
     * 
     * @param performance 绩效对象
     * @return 影响行数
     */
    int updateById(Performance performance);
    
    /**
     * 根据ID删除绩效记录
     * 
     * @param performanceId 绩效ID
     * @return 影响行数
     */
    int deleteById(@Param("performanceId") Integer performanceId);
    
    /**
     * 根据ID查询绩效记录
     * 
     * @param performanceId 绩效ID
     * @return 绩效对象
     */
    Performance findById(@Param("performanceId") Integer performanceId);
    
    /**
     * 根据ID查询绩效详情（包含员工信息）
     * 
     * @param performanceId 绩效ID
     * @return 绩效详情
     */
    PerformanceVO findDetailById(@Param("performanceId") Integer performanceId);
    
    /**
     * 查询绩效列表（带分页和条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 绩效列表
     */
    List<PerformanceVO> findList(PerformanceQueryDTO queryDTO);
    
    /**
     * 统计绩效总数（带条件）
     * 
     * @param queryDTO 查询参数
     * @return 总数
     */
    Long countList(PerformanceQueryDTO queryDTO);
    
    /**
     * 查询绩效排名
     * 
     * @param evalDate 考核周期（格式：YYYY-MM-DD）
     * @return 绩效排名列表
     */
    List<PerformanceRankingVO> getPerformanceRanking(@Param("evalDate") String evalDate);
}
