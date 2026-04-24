package com.company.ems.mapper;

import com.company.ems.entity.Position;
import com.company.ems.vo.PositionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位Mapper接口
 * 
 * @author EMS Team
 */
public interface PositionMapper {
    
    /**
     * 新增岗位
     * 
     * @param position 岗位对象
     * @return 影响行数
     */
    int insert(Position position);
    
    /**
     * 根据ID更新岗位
     * 
     * @param position 岗位对象
     * @return 影响行数
     */
    int updateById(Position position);
    
    /**
     * 根据ID删除岗位（软删除）
     * 
     * @param positionId 岗位ID
     * @return 影响行数
     */
    int deleteById(@Param("positionId") Integer positionId);
    
    /**
     * 根据ID查询岗位
     * 
     * @param positionId 岗位ID
     * @return 岗位对象
     */
    Position findById(@Param("positionId") Integer positionId);
    
    /**
     * 查询岗位列表
     * 
     * @return 岗位列表
     */
    List<PositionVO> findList();
    
    /**
     * 根据ID查询岗位详情（包含员工数量）
     * 
     * @param positionId 岗位ID
     * @return 岗位详情
     */
    PositionVO findDetailById(@Param("positionId") Integer positionId);
    
    /**
     * 根据岗位ID查询员工数量
     * 
     * @param positionId 岗位ID
     * @return 员工数量
     */
    int countEmployeesByPositionId(@Param("positionId") Integer positionId);
}
