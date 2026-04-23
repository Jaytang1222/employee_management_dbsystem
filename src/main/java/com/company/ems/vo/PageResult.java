package com.company.ems.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果类
 * 
 * @author EMS Team
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页数据列表
     */
    private List<T> records;
    
    /**
     * 当前页码
     */
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    private Integer pageSize;
    
    /**
     * 总页数
     */
    private Integer totalPages;
    
    /**
     * 构造分页结果
     */
    public PageResult(Long total, List<T> records, Integer pageNum, Integer pageSize) {
        this.total = total;
        this.records = records;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
}
