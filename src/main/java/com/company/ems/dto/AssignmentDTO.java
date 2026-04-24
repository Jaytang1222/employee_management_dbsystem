package com.company.ems.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 员工分配数据传输对象
 * 用于新增和更新员工分配
 * 
 * @author EMS Team
 */
@Data
public class AssignmentDTO {
    
    /**
     * 员工ID
     */
    @NotNull(message = "员工ID不能为空")
    private Integer employeeId;
    
    /**
     * 部门ID
     */
    @NotNull(message = "部门ID不能为空")
    private Integer deptId;
    
    /**
     * 岗位ID
     */
    @NotNull(message = "岗位ID不能为空")
    private Integer positionId;
    
    /**
     * 生效日期
     */
    @NotNull(message = "生效日期不能为空")
    private LocalDate startDate;
    
    /**
     * 是否主部门
     */
    @NotNull(message = "是否主部门不能为空")
    private Boolean isPrimary;
    
    /**
     * 调动原因
     */
    private String changeReason;
}
