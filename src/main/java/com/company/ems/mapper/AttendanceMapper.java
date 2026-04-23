package com.company.ems.mapper;

import com.company.ems.dto.AttendanceQueryDTO;
import com.company.ems.entity.Attendance;
import com.company.ems.vo.AttendanceReportVO;
import com.company.ems.vo.AttendanceVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 考勤Mapper接口
 * 
 * @author EMS Team
 */
@Mapper
public interface AttendanceMapper {
    
    /**
     * 新增考勤记录
     * 
     * @param attendance 考勤对象
     * @return 影响行数
     */
    int insert(Attendance attendance);
    
    /**
     * 根据ID更新考勤记录
     * 
     * @param attendance 考勤对象
     * @return 影响行数
     */
    int updateById(Attendance attendance);
    
    /**
     * 根据ID删除考勤记录
     * 
     * @param attendanceId 考勤ID
     * @return 影响行数
     */
    int deleteById(@Param("attendanceId") Integer attendanceId);
    
    /**
     * 根据ID查询考勤记录
     * 
     * @param attendanceId 考勤ID
     * @return 考勤对象
     */
    Attendance findById(@Param("attendanceId") Integer attendanceId);
    
    /**
     * 根据ID查询考勤详情（包含员工信息）
     * 
     * @param attendanceId 考勤ID
     * @return 考勤详情
     */
    AttendanceVO findDetailById(@Param("attendanceId") Integer attendanceId);
    
    /**
     * 查询考勤列表（带分页和条件查询）
     * 
     * @param queryDTO 查询参数
     * @return 考勤列表
     */
    List<AttendanceVO> findList(AttendanceQueryDTO queryDTO);
    
    /**
     * 统计考勤总数（带条件）
     * 
     * @param queryDTO 查询参数
     * @return 总数
     */
    Long countList(AttendanceQueryDTO queryDTO);
    
    /**
     * 检查员工在指定日期是否已有考勤记录
     * 
     * @param employeeId 员工ID
     * @param attendanceDate 考勤日期
     * @return 考勤记录（如果存在）
     */
    Attendance findByEmployeeAndDate(@Param("employeeId") Integer employeeId, 
                                     @Param("attendanceDate") String attendanceDate);
    
    /**
     * 查询员工月度考勤报表
     * 
     * @param employeeId 员工ID
     * @param month 月份（格式：YYYY-MM）
     * @return 考勤月报
     */
    AttendanceReportVO getMonthlyReport(@Param("employeeId") Integer employeeId, 
                                        @Param("month") String month);
}
