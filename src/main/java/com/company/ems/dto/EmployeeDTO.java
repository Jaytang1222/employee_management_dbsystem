package com.company.ems.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

/**
 * 员工数据传输对象
 * 用于新增和更新员工
 * 
 * @author EMS Team
 */
@Data
public class EmployeeDTO {
    
    /**
     * 身份证号
     */
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$", 
             message = "身份证号格式不正确")
    private String idCard;
    
    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Size(min = 2, max = 50, message = "姓名长度必须在2-50个字符之间")
    private String name;
    
    /**
     * 性别：男/女
     */
    @Pattern(regexp = "^(男|女)$", message = "性别只能是男或女")
    private String gender;
    
    /**
     * 出生日期
     */
    @Past(message = "出生日期必须是过去的日期")
    private LocalDate birthDate;
    
    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;
    
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
    
    /**
     * 地址
     */
    @Size(max = 200, message = "地址长度不能超过200个字符")
    private String address;
    
    /**
     * 入职日期
     */
    @NotNull(message = "入职日期不能为空")
    @PastOrPresent(message = "入职日期不能晚于当前日期")
    private LocalDate hireDate;
    
    /**
     * 学历：高中/大专/本科/硕士/博士
     */
    @Pattern(regexp = "^(高中|大专|本科|硕士|博士)$", message = "学历只能是：高中、大专、本科、硕士、博士")
    private String educationLevel;
    
    /**
     * 紧急联系人
     */
    @Size(max = 100, message = "紧急联系人长度不能超过100个字符")
    private String emergencyContact;
}
