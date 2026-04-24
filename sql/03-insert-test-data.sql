-- ============================================
-- 企业员工管理系统 - 测试数据插入脚本
-- ============================================
-- 数据库名称：employee_management_system
-- 数据库类型：OpenGauss
-- 创建日期：2024-01-19
-- 说明：插入测试数据（30个员工及相关数据）
-- ============================================

-- ============================================
-- 0. 清除旧数据
-- ============================================
-- 按照外键依赖顺序删除数据
DELETE FROM performance_info;
DELETE FROM payroll_info;
DELETE FROM attendance_info;
DELETE FROM assignment_info;
DELETE FROM user_info;
DELETE FROM employee_info;
DELETE FROM position_info;
DELETE FROM department_info;

-- 重置序列（使用setval函数）
SELECT setval('employee_info_employee_id_seq', 1, false);
SELECT setval('department_info_dept_id_seq', 1, false);
SELECT setval('position_info_position_id_seq', 1, false);
SELECT setval('user_info_user_id_seq', 1, false);
SELECT setval('assignment_info_assignment_id_seq', 1, false);
SELECT setval('attendance_info_attendance_id_seq', 1, false);
SELECT setval('payroll_info_payroll_id_seq', 1, false);
SELECT setval('performance_info_performance_id_seq', 1, false);

-- ============================================
-- 1. 插入部门数据（10个部门，3级结构）
-- ============================================
INSERT INTO department_info (dept_name, parent_dept_id, manager_employee_id, status) VALUES
('总经理办公室', NULL, NULL, 'active'),  -- 1 根部门
('技术部', 1, NULL, 'active'),           -- 2
('市场部', 1, NULL, 'active'),           -- 3
('人力资源部', 1, NULL, 'active'),       -- 4
('财务部', 1, NULL, 'active'),           -- 5
('研发一组', 2, NULL, 'active'),         -- 6 技术部下属
('研发二组', 2, NULL, 'active'),         -- 7
('测试组', 2, NULL, 'active'),           -- 8
('市场推广组', 3, NULL, 'active'),       -- 9 市场部下属
('销售组', 3, NULL, 'active');           -- 10

-- ============================================
-- 2. 插入岗位数据（15个岗位）
-- ============================================
INSERT INTO position_info (position_name, position_level, base_salary, description, status) VALUES
('总经理', '专家', 30000.00, '公司最高管理者', 'active'),
('技术总监', '高级', 25000.00, '技术部门负责人', 'active'),
('市场总监', '高级', 22000.00, '市场部门负责人', 'active'),
('人力资源经理', '中级', 15000.00, '人力资源部门负责人', 'active'),
('财务经理', '中级', 18000.00, '财务部门负责人', 'active'),
('高级Java工程师', '高级', 20000.00, 'Java后端开发', 'active'),
('Java工程师', '中级', 12000.00, 'Java后端开发', 'active'),
('初级Java工程师', '初级', 8000.00, 'Java后端开发', 'active'),
('前端工程师', '中级', 11000.00, '前端开发', 'active'),
('测试工程师', '中级', 10000.00, '软件测试', 'active'),
('产品经理', '中级', 14000.00, '产品设计与规划', 'active'),
('市场专员', '初级', 7000.00, '市场推广', 'active'),
('销售经理', '中级', 13000.00, '销售管理', 'active'),
('人事专员', '初级', 6000.00, '人事管理', 'active'),
('会计', '中级', 9000.00, '财务核算', 'active');

-- ============================================
-- 3. 插入员工数据（30个员工）
-- ============================================
INSERT INTO employee_info (id_card, name, gender, birth_date, phone, email, address, hire_date, status, education_level, emergency_contact) VALUES
('110101199001011234', '张伟', '男', '1990-01-01', '13800138001', 'zhangwei@company.com', '北京市朝阳区', '2026-01-15', 'active', '本科', '李娜 13900139001'),
('110101198502022345', '李娜', '女', '1985-02-02', '13800138002', 'lina@company.com', '北京市海淀区', '2026-01-20', 'active', '硕士', '张伟 13800138001'),
('110101199203033456', '王芳', '女', '1992-03-03', '13800138003', 'wangfang@company.com', '北京市西城区', '2026-02-10', 'active', '本科', '王强 13900139003'),
('110101198804044567', '刘强', '男', '1988-04-04', '13800138004', 'liuqiang@company.com', '北京市东城区', '2026-01-05', 'active', '硕士', '刘芳 13900139004'),
('110101199505055678', '赵敏', '女', '1995-05-05', '13800138005', 'zhaomin@company.com', '北京市丰台区', '2026-02-15', 'active', '本科', '赵刚 13900139005'),
('110101199106066789', '孙丽', '女', '1991-06-06', '13800138006', 'sunli@company.com', '北京市石景山区', '2026-01-20', 'active', '大专', '孙强 13900139006'),
('110101198707077890', '周杰', '男', '1987-07-07', '13800138007', 'zhoujie@company.com', '北京市通州区', '2026-02-05', 'active', '本科', '周芳 13900139007'),
('110101199408088901', '吴磊', '男', '1994-08-08', '13800138008', 'wulei@company.com', '北京市昌平区', '2026-01-10', 'active', '本科', '吴娜 13900139008'),
('110101199009099012', '郑爽', '女', '1990-09-09', '13800138009', 'zhengshuang@company.com', '北京市大兴区', '2026-02-15', 'active', '硕士', '郑强 13900139009'),
('110101198610101123', '王磊', '男', '1986-10-10', '13800138010', 'wanglei@company.com', '北京市房山区', '2026-01-20', 'active', '本科', '王丽 13900139010'),
('110101199311111234', '陈静', '女', '1993-11-11', '13800138011', 'chenjing@company.com', '北京市门头沟区', '2026-03-01', 'active', '本科', '陈强 13900139011'),
('110101198912121345', '杨洋', '男', '1989-12-12', '13800138012', 'yangyang@company.com', '北京市平谷区', '2026-02-10', 'active', '大专', '杨丽 13900139012'),
('110101199601011456', '黄晓明', '男', '1996-01-01', '13800138013', 'huangxiaoming@company.com', '北京市怀柔区', '2026-03-15', 'active', '本科', '黄丽 13900139013'),
('110101199202021567', '赵丽颖', '女', '1992-02-02', '13800138014', 'zhaoliying@company.com', '北京市密云区', '2026-01-20', 'active', '本科', '赵强 13900139014'),
('110101198803031678', '邓超', '男', '1988-03-03', '13800138015', 'dengchao@company.com', '北京市延庆区', '2026-02-25', 'active', '硕士', '邓丽 13900139015'),
('110101199504041789', '孙俪', '女', '1995-04-04', '13800138016', 'sunli2@company.com', '北京市朝阳区', '2026-02-28', 'active', '本科', '孙强 13900139016'),
('110101199105051890', '胡歌', '男', '1991-05-05', '13800138017', 'huge@company.com', '北京市海淀区', '2026-01-05', 'active', '本科', '胡丽 13900139017'),
('110101198706061901', '刘诗诗', '女', '1987-06-06', '13800138018', 'liushishi@company.com', '北京市西城区', '2026-02-15', 'active', '大专', '刘强 13900139018'),
('110101199407072012', '吴京', '男', '1994-07-07', '13800138019', 'wujing@company.com', '北京市东城区', '2026-01-20', 'active', '本科', '吴丽 13900139019'),
('110101199008082123', '谢娜', '女', '1990-08-08', '13800138020', 'xiena@company.com', '北京市丰台区', '2026-02-25', 'active', '本科', '谢强 13900139020'),
('110101198609092234', '何炅', '男', '1986-09-09', '13800138021', 'hejiong@company.com', '北京市石景山区', '2026-01-30', 'active', '硕士', '何丽 13900139021'),
('110101199310102345', '李冰冰', '女', '1993-10-10', '13800138022', 'libingbing@company.com', '北京市通州区', '2026-02-05', 'active', '本科', '李强 13900139022'),
('110101198911112456', '范冰冰', '女', '1989-11-11', '13800138023', 'fanbingbing@company.com', '北京市昌平区', '2026-02-10', 'active', '大专', '范强 13900139023'),
('110101199612122567', '杨幂', '女', '1996-12-12', '13800138024', 'yangmi@company.com', '北京市大兴区', '2026-01-15', 'active', '本科', '杨强 13900139024'),
('110101199201012678', '刘亦菲', '女', '1992-01-01', '13800138025', 'liuyifei@company.com', '北京市房山区', '2026-02-20', 'active', '本科', '刘强 13900139025'),
('110101198802022789', '成龙', '男', '1988-02-02', '13800138026', 'chenglong@company.com', '北京市门头沟区', '2026-02-25', 'active', '高中', '成丽 13900139026'),
('110101199503032890', '李连杰', '男', '1995-03-03', '13800138027', 'lilianjie@company.com', '北京市平谷区', '2026-01-30', 'active', '本科', '李丽 13900139027'),
('110101199104042901', '甄子丹', '男', '1991-04-04', '13800138028', 'zhenzidan@company.com', '北京市怀柔区', '2026-03-05', 'active', '本科', '甄丽 13900139028'),
('110101198705053012', '周星驰', '男', '1987-05-05', '13800138029', 'zhouxingchi@company.com', '北京市密云区', '2026-02-10', 'active', '大专', '周丽 13900139029'),
('110101199406063123', '林志玲', '女', '1994-06-06', '13800138030', 'linzhiling@company.com', '北京市延庆区', '2026-01-15', 'active', '本科', '林强 13900139030');

-- ============================================
-- 4. 更新部门经理（使用已插入的员工ID）
-- ============================================
UPDATE department_info SET manager_employee_id = 1 WHERE dept_id = 1;  -- 张伟任总经理办公室经理
UPDATE department_info SET manager_employee_id = 2 WHERE dept_id = 2;  -- 李娜任技术部经理
UPDATE department_info SET manager_employee_id = 3 WHERE dept_id = 3;  -- 王芳任市场部经理
UPDATE department_info SET manager_employee_id = 4 WHERE dept_id = 4;  -- 刘强任人力资源部经理
UPDATE department_info SET manager_employee_id = 5 WHERE dept_id = 5;  -- 赵敏任财务部经理

-- ============================================
-- 5. 插入用户数据（为部分员工创建登录账号）
-- 密码统一为：password123（实际应用中需要BCrypt加密）
-- ============================================
INSERT INTO user_info (username, password, employee_id, status) VALUES
('zhangwei', 'zhangwei', 1, 'active'),
('lina', 'lina', 2, 'active'),
('wangfang', 'wangfang', 3, 'active'),
('liuqiang', 'liuqiang', 4, 'active'),
('zhaomin', 'zhaomin', 5, 'active'),
('admin', 'admin', 1, 'active');

-- ============================================
-- 6. 插入员工分配数据（员工-部门-岗位关联）
-- ============================================
INSERT INTO assignment_info (employee_id, dept_id, position_id, start_date, is_primary, change_reason, status) VALUES
(1, 1, 1, '2026-01-15', true, '入职', 'active'),
(2, 2, 2, '2026-01-20', true, '入职', 'active'),
(3, 3, 3, '2026-02-10', true, '入职', 'active'),
(4, 4, 4, '2026-01-05', true, '入职', 'active'),
(5, 5, 5, '2026-02-15', true, '入职', 'active'),
(6, 6, 6, '2026-01-20', true, '入职', 'active'),
(7, 6, 7, '2026-02-05', true, '入职', 'active'),
(8, 6, 8, '2026-01-10', true, '入职', 'active'),
(9, 7, 6, '2026-02-15', true, '入职', 'active'),
(10, 7, 7, '2026-01-20', true, '入职', 'active'),
(11, 7, 8, '2026-03-01', true, '入职', 'active'),
(12, 8, 10, '2026-02-10', true, '入职', 'active'),
(13, 8, 10, '2026-03-15', true, '入职', 'active'),
(14, 9, 11, '2026-01-20', true, '入职', 'active'),
(15, 9, 12, '2026-02-25', true, '入职', 'active'),
(16, 9, 12, '2026-02-28', true, '入职', 'active'),
(17, 10, 13, '2026-01-05', true, '入职', 'active'),
(18, 10, 12, '2026-02-15', true, '入职', 'active'),
(19, 4, 14, '2026-01-20', true, '入职', 'active'),
(20, 4, 14, '2026-02-25', true, '入职', 'active'),
(21, 5, 15, '2026-01-30', true, '入职', 'active'),
(22, 5, 15, '2026-02-05', true, '入职', 'active'),
(23, 2, 9, '2026-02-10', true, '入职', 'active'),
(24, 6, 7, '2026-01-15', true, '入职', 'active'),
(25, 7, 7, '2026-02-20', true, '入职', 'active'),
(26, 8, 10, '2026-02-25', true, '入职', 'active'),
(27, 9, 12, '2026-01-30', true, '入职', 'active'),
(28, 10, 12, '2026-03-05', true, '入职', 'active'),
(29, 3, 11, '2026-02-10', true, '入职', 'active'),
(30, 6, 8, '2026-01-15', true, '入职', 'active');

-- ============================================
-- 7. 插入考勤数据（每个员工每月20条记录，共9个月）
-- ============================================
-- 为所有员工批量插入考勤数据（2026-01到2026-09，每月约20天）
INSERT INTO attendance_info (employee_id, attendance_date, check_in_time, check_out_time, attendance_status, late_min, early_leave_min, overtime_hours, remark)
SELECT
    e.employee_id,
    dates.date_val,
    CASE
        WHEN random() < 0.1 THEN
            CAST('09:' || lpad(CAST(LEAST(5 + floor(random() * 25), 59) AS TEXT), 2, '0') || ':00' AS TIME)
        ELSE
            CAST('08:' || lpad(CAST(LEAST(45 + floor(random() * 15), 59) AS TEXT), 2, '0') || ':00' AS TIME)
    END as check_in_time,
    CASE
        WHEN random() < 0.05 THEN
            CAST('17:' || lpad(CAST(LEAST(30 + floor(random() * 25), 59) AS TEXT), 2, '0') || ':00' AS TIME)
        WHEN random() < 0.2 THEN
            CAST(LEAST(18 + floor(random() * 2), 23) || ':' || lpad(CAST(floor(random() * 60) AS TEXT), 2, '0') || ':00' AS TIME)
        ELSE
            CAST('18:' || lpad(CAST(floor(random() * 15) AS TEXT), 2, '0') || ':00' AS TIME)
    END as check_out_time,
    CASE
        WHEN random() < 0.02 THEN 'absent'
        WHEN random() < 0.05 THEN 'leave'
        WHEN random() < 0.15 THEN 'late'
        WHEN random() < 0.18 THEN 'early_leave'
        ELSE 'normal'
    END as attendance_status,
    CASE WHEN random() < 0.15 THEN CAST(floor(random() * 30) AS INTEGER) ELSE 0 END as late_min,
    CASE WHEN random() < 0.05 THEN CAST(floor(random() * 30) AS INTEGER) ELSE 0 END as early_leave_min,
    CASE WHEN random() < 0.2 THEN CAST(random() * 3 AS NUMERIC(4,1)) ELSE 0 END as overtime_hours,
    CASE
        WHEN random() < 0.02 THEN '病假'
        WHEN random() < 0.03 THEN '事假'
        ELSE NULL
    END as remark
FROM employee_info e
CROSS JOIN (
    SELECT CAST(gs AS DATE) as date_val
    FROM generate_series(CAST('2026-01-02' AS DATE), CAST('2026-09-30' AS DATE), CAST('1 day' AS INTERVAL)) as gs
    WHERE EXTRACT(DOW FROM gs) NOT IN (0, 6)
) dates
WHERE e.employee_id BETWEEN 1 AND 30
ORDER BY e.employee_id, dates.date_val;

-- ============================================
-- 8. 插入薪资数据（每个员工9个月：2026-01到2026-09）
-- ============================================
INSERT INTO payroll_info (employee_id, pay_month, base_pay, allowance, bonus, deduction, social_insurance, tax, net_pay, pay_date)
SELECT
    e.employee_id,
    to_char(months.month_date, 'YYYY-MM') as pay_month,
    p.base_salary as base_pay,
    CAST(500.00 + (random() * 500) AS NUMERIC(10,2)) as allowance,
    CASE
        WHEN EXTRACT(MONTH FROM months.month_date) IN (3, 6, 9) THEN CAST(1000.00 + random() * 3000 AS NUMERIC(10,2))
        ELSE CAST(random() * 1000 AS NUMERIC(10,2))
    END as bonus,
    CASE WHEN random() < 0.1 THEN CAST(random() * 500 AS NUMERIC(10,2)) ELSE 0 END as deduction,
    CAST(p.base_salary * 0.105 AS NUMERIC(10,2)) as social_insurance,
    CAST(p.base_salary * 0.12 AS NUMERIC(10,2)) as tax,
    CAST(p.base_salary + 500.00 +
     CASE WHEN EXTRACT(MONTH FROM months.month_date) IN (3, 6, 9) THEN 2000.00 ELSE 500.00 END -
     p.base_salary * 0.105 - p.base_salary * 0.12 AS NUMERIC(10,2)) as net_pay,
    CAST(months.month_date + CAST('1 month' AS INTERVAL) + CAST('4 days' AS INTERVAL) AS DATE) as pay_date
FROM employee_info e
JOIN assignment_info a ON e.employee_id = a.employee_id AND a.is_primary = true AND a.status = 'active'
JOIN position_info p ON a.position_id = p.position_id
CROSS JOIN (
    SELECT CAST(gs AS DATE) as month_date
    FROM generate_series(CAST('2026-01-01' AS DATE), CAST('2026-09-01' AS DATE), CAST('1 month' AS INTERVAL)) as gs
) months
WHERE e.employee_id BETWEEN 1 AND 30
ORDER BY e.employee_id, months.month_date;

-- ============================================
-- 9. 插入绩效数据（每个员工9条记录：2026-01到2026-09）
-- ============================================
INSERT INTO performance_info (employee_id, eval_date, score, grade, comment, evaluate_date)
SELECT
    subquery.employee_id,
    subquery.eval_date,
    subquery.score,
    CASE
        WHEN subquery.score >= 90 THEN 'A'
        WHEN subquery.score >= 80 THEN 'B'
        WHEN subquery.score >= 60 THEN 'C'
        ELSE 'D'
    END as grade,
    CASE
        WHEN subquery.score >= 95 THEN '表现优异，超出预期'
        WHEN subquery.score >= 90 THEN '工作表现优秀'
        WHEN subquery.score >= 85 THEN '工作表现良好，继续保持'
        WHEN subquery.score >= 80 THEN '工作表现良好'
        WHEN subquery.score >= 70 THEN '工作表现合格，需要改进'
        WHEN subquery.score >= 60 THEN '工作表现基本合格'
        ELSE '工作表现不合格，需要重点改进'
    END as comment,
    CAST(subquery.eval_date + CAST('25 days' AS INTERVAL) AS DATE) as evaluate_date
FROM (
    SELECT
        e.employee_id,
        CAST('2026-' || lpad(CAST(months.month_num AS TEXT), 2, '0') || '-01' AS DATE) as eval_date,
        CASE
            WHEN p.position_level IN ('专家', '高级') THEN CAST(85.00 + (random() * 12) AS NUMERIC(5,2))
            WHEN p.position_level = '中级' THEN CAST(75.00 + (random() * 18) AS NUMERIC(5,2))
            ELSE CAST(65.00 + (random() * 25) AS NUMERIC(5,2))
        END as score
    FROM employee_info e
    JOIN assignment_info a ON e.employee_id = a.employee_id AND a.is_primary = true AND a.status = 'active'
    JOIN position_info p ON a.position_id = p.position_id
    CROSS JOIN (
        SELECT generate_series as month_num FROM generate_series(1, 9)
    ) months
    WHERE e.employee_id BETWEEN 1 AND 30
) subquery
ORDER BY subquery.employee_id, subquery.eval_date;

-- ============================================
-- 数据插入完成提示
-- ============================================
-- 统计各表数据量
SELECT 'employee_info' as table_name, COUNT(*) as record_count FROM employee_info
UNION ALL
SELECT 'department_info', COUNT(*) FROM department_info
UNION ALL
SELECT 'position_info', COUNT(*) FROM position_info
UNION ALL
SELECT 'user_info', COUNT(*) FROM user_info
UNION ALL
SELECT 'assignment_info', COUNT(*) FROM assignment_info
UNION ALL
SELECT 'attendance_info', COUNT(*) FROM attendance_info
UNION ALL
SELECT 'payroll_info', COUNT(*) FROM payroll_info
UNION ALL
SELECT 'performance_info', COUNT(*) FROM performance_info
ORDER BY table_name;

-- ============================================
-- 执行说明：
-- 1. 确保已执行02-create-tables.sql创建表结构
-- 2. 在DataGrip中选择employee_management_system数据库
-- 3. 执行本脚本
-- 4. 确认数据插入成功
-- 5. 测试登录账号：
--    用户名：admin / zhangwei / lina / wangfang / liuqiang / zhaomin
--    密码：password123（所有账号统一密码）
-- ============================================
