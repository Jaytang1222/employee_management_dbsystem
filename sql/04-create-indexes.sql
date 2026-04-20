-- ============================================
-- 企业员工管理系统 - 索引创建脚本
-- ============================================
-- 数据库名称：employee_management_system
-- 数据库类型：OpenGauss
-- 创建日期：2024-01-19
-- 说明：创建索引以提升查询性能
-- ============================================

-- 确保连接到正确的数据库
\c employee_management_system

-- ============================================
-- 1. employee_info（员工表）索引
-- ============================================
-- 姓名查询索引（频繁按姓名搜索）
CREATE INDEX idx_employee_name ON employee_info(name);

-- 状态查询索引（频繁按状态筛选）
CREATE INDEX idx_employee_status ON employee_info(status);

-- 入职日期索引（用于统计分析）
CREATE INDEX idx_employee_hire_date ON employee_info(hire_date);

-- 手机号索引（用于查询和验证）
CREATE INDEX idx_employee_phone ON employee_info(phone);

-- ============================================
-- 2. department_info（部门表）索引
-- ============================================
-- 上级部门索引（用于树形查询）
CREATE INDEX idx_dept_parent ON department_info(parent_dept_id);

-- 部门经理索引（用于关联查询）
CREATE INDEX idx_dept_manager ON department_info(manager_employee_id);

-- 部门名称索引（用于搜索）
CREATE INDEX idx_dept_name ON department_info(dept_name);

-- ============================================
-- 3. position_info（岗位表）索引
-- ============================================
-- 岗位名称索引（用于搜索）
CREATE INDEX idx_position_name ON position_info(position_name);

-- 岗位级别索引（用于筛选）
CREATE INDEX idx_position_level ON position_info(position_level);

-- ============================================
-- 4. user_info（用户表）索引
-- ============================================
-- 关联员工索引
CREATE INDEX idx_user_employee ON user_info(employee_id);

-- 用户状态索引
CREATE INDEX idx_user_status ON user_info(status);

-- ============================================
-- 5. assignment_info（员工分配表）索引
-- ============================================
-- 员工分配索引（最常用的查询）
CREATE INDEX idx_assignment_employee ON assignment_info(employee_id);

-- 部门分配索引（按部门查询员工）
CREATE INDEX idx_assignment_dept ON assignment_info(dept_id);

-- 岗位分配索引（按岗位查询员工）
CREATE INDEX idx_assignment_position ON assignment_info(position_id);

-- 员工状态组合索引（查询员工当前分配）
CREATE INDEX idx_assignment_emp_status ON assignment_info(employee_id, status);

-- 部门状态组合索引（查询部门当前员工）
CREATE INDEX idx_assignment_dept_status ON assignment_info(dept_id, status);

-- 主部门标识索引（查询主部门）
CREATE INDEX idx_assignment_primary ON assignment_info(is_primary);

-- ============================================
-- 6. attendance_info（考勤表）索引
-- ============================================
-- 员工考勤日期组合索引（最常用的查询）
CREATE INDEX idx_attendance_emp_date ON attendance_info(employee_id, attendance_date);

-- 考勤日期索引（按日期统计）
CREATE INDEX idx_attendance_date ON attendance_info(attendance_date);

-- 考勤状态索引（按状态筛选）
CREATE INDEX idx_attendance_status ON attendance_info(attendance_status);

-- 员工ID索引（按员工查询）
CREATE INDEX idx_attendance_employee ON attendance_info(employee_id);

-- ============================================
-- 7. payroll_info（薪资记录表）索引
-- ============================================
-- 员工薪资月份组合索引（最常用的查询）
CREATE INDEX idx_payroll_emp_month ON payroll_info(employee_id, pay_month);

-- 发薪月份索引（按月份统计）
CREATE INDEX idx_payroll_month ON payroll_info(pay_month);

-- 员工ID索引（按员工查询）
CREATE INDEX idx_payroll_employee ON payroll_info(employee_id);

-- 发薪日期索引（按日期查询）
CREATE INDEX idx_payroll_pay_date ON payroll_info(pay_date);

-- ============================================
-- 8. performance_info（绩效考核表）索引
-- ============================================
-- 员工ID索引（按员工查询）
CREATE INDEX idx_performance_employee ON performance_info(employee_id);

-- 考核日期索引（按日期查询）
CREATE INDEX idx_performance_date ON performance_info(eval_date);

-- 员工考核日期组合索引（查询员工某期绩效）
CREATE INDEX idx_performance_emp_date ON performance_info(employee_id, eval_date);

-- 绩效等级索引（按等级筛选）
CREATE INDEX idx_performance_grade ON performance_info(grade);

-- 绩效分数索引（用于排名）
CREATE INDEX idx_performance_score ON performance_info(score DESC);

-- ============================================
-- 索引创建完成提示
-- ============================================
SELECT '所有索引创建成功！' as message;

-- 查看所有索引
SELECT 
    schemaname,
    tablename,
    indexname,
    indexdef
FROM pg_indexes
WHERE schemaname = 'public'
ORDER BY tablename, indexname;

-- 统计索引数量
SELECT 
    tablename,
    COUNT(*) as index_count
FROM pg_indexes
WHERE schemaname = 'public'
GROUP BY tablename
ORDER BY tablename;

-- ============================================
-- 执行说明：
-- 1. 确保已执行02-create-tables.sql和03-insert-test-data.sql
-- 2. 执行命令：\i /path/to/04-create-indexes.sql
-- 3. 确认所有索引创建成功
-- 4. 可以使用EXPLAIN ANALYZE查看查询计划，验证索引效果
-- ============================================

-- ============================================
-- 性能优化建议：
-- 1. 定期执行ANALYZE更新统计信息
-- 2. 监控慢查询日志
-- 3. 根据实际查询模式调整索引
-- 4. 考勤表数据量大时可考虑分区
-- ============================================
