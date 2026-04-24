package com.company.ems.service;

import com.company.ems.dto.PositionDTO;
import com.company.ems.dto.PositionQueryDTO;
import com.company.ems.entity.Position;
import com.company.ems.exception.BusinessException;
import com.company.ems.mapper.PositionMapper;
import com.company.ems.vo.PageResult;
import com.company.ems.vo.PositionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 岗位业务逻辑层
 * 
 * @author EMS Team
 */
@Slf4j
@Service
public class PositionService {
    
    @Autowired
    private PositionMapper positionMapper;
    
    /**
     * 新增岗位
     * 
     * @param positionDTO 岗位数据
     * @return 岗位ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer addPosition(PositionDTO positionDTO) {
        // 1. 构造岗位对象
        Position position = new Position();
        BeanUtils.copyProperties(positionDTO, position);
        position.setStatus("active");
        
        // 2. 插入数据库
        int rows = positionMapper.insert(position);
        if (rows == 0) {
            throw new BusinessException("新增岗位失败");
        }
        
        log.info("新增岗位成功：{} - {}", position.getPositionId(), position.getPositionName());
        return position.getPositionId();
    }
    
    /**
     * 更新岗位
     * 
     * @param positionId 岗位ID
     * @param positionDTO 岗位数据
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePosition(Integer positionId, PositionDTO positionDTO) {
        // 1. 验证岗位是否存在
        Position existPosition = positionMapper.findById(positionId);
        if (existPosition == null) {
            throw new BusinessException("岗位不存在");
        }
        
        // 2. 构造岗位对象
        Position position = new Position();
        BeanUtils.copyProperties(positionDTO, position);
        position.setPositionId(positionId);
        
        // 3. 更新数据库
        int rows = positionMapper.updateById(position);
        if (rows == 0) {
            throw new BusinessException("更新岗位失败");
        }
        
        log.info("更新岗位成功：{} - {}", positionId, position.getPositionName());
    }
    
    /**
     * 删除岗位（软删除）
     * 
     * @param positionId 岗位ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deletePosition(Integer positionId) {
        // 1. 验证岗位是否存在
        Position position = positionMapper.findById(positionId);
        if (position == null) {
            throw new BusinessException("岗位不存在");
        }
        
        // 2. 检查是否有员工在该岗位
        int employeeCount = positionMapper.countEmployeesByPositionId(positionId);
        if (employeeCount > 0) {
            throw new BusinessException("该岗位下有员工，不能删除");
        }
        
        // 3. 软删除
        int rows = positionMapper.deleteById(positionId);
        if (rows == 0) {
            throw new BusinessException("删除岗位失败");
        }
        
        log.info("删除岗位成功：{} - {}", positionId, position.getPositionName());
    }
    
    /**
     * 根据ID查询岗位详情
     * 
     * @param positionId 岗位ID
     * @return 岗位详情
     */
    public PositionVO getPositionDetail(Integer positionId) {
        PositionVO positionVO = positionMapper.findDetailById(positionId);
        if (positionVO == null) {
            throw new BusinessException("岗位不存在");
        }
        return positionVO;
    }
    
    /**
     * 查询岗位列表（分页）
     * 
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    public PageResult<PositionVO> getPositionList(PositionQueryDTO queryDTO) {
        // 1. 保存原始页码
        int currentPage = queryDTO.getPageNum();
        
        // 2. 计算分页偏移量
        int offset = (currentPage - 1) * queryDTO.getPageSize();
        queryDTO.setPageNum(offset);
        
        // 3. 查询总数
        Long total = positionMapper.countList(queryDTO);
        
        // 4. 查询列表
        List<PositionVO> records = positionMapper.findList(queryDTO);
        
        // 5. 使用原始页码构造分页结果
        return new PageResult<>(total, records, currentPage, queryDTO.getPageSize());
    }
    
    /**
     * 查询所有岗位（不分页）
     * 
     * @return 岗位列表
     */
    public List<PositionVO> getAllPositions() {
        return positionMapper.findAll();
    }
}
