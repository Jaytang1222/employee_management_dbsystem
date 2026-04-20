-- ============================================
-- 企业员工管理系统 - 测试数据插入脚本
-- ============================================
-- 数据库名称：employee_management_system
-- 数据库类型：OpenGauss
-- 创建日期：2024-01-19
-- 说明：插入测试数据（30个员工及相关数据）
-- ============================================

-- 确保连接到正确的数据库
\c employee_management_system

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
('110101199001011234', '张伟', '男', '1990-01-01', '13800138001', 'zhangwei@company.com', '北京市朝阳区', '2020-01-15', 'active', '本科', '李娜 13900139001'),
('110101198502022345', '李娜', '女', '1985-02-02', '13800138002', 'lina@company.com', '北京市海淀区', '2019-03-20', 'active', '硕士', '张伟 13800138001'),
('110101199203033456', '王芳', '女', '1992-03-03', '13800138003', 'wangfang@company.com', '北京市西城区', '2021-05-10', 'active', '本科', '王强 13900139003'),
('110101198804044567', '刘强', '男', '1988-04-04', '13800138004', 'liuqiang@company.com', '北京市东城区', '2018-07-01', 'active', '硕士', '刘芳 13900139004'),
('110101199505055678', '赵敏', '女', '1995-05-05', '13800138005', 'zhaomin@company.com', '北京市丰台区', '2022-02-15', 'active', '本科', '赵刚 13900139005'),
('110101199106066789', '孙丽', '女', '1991-06-06', '13800138006', 'sunli@company.com', '北京市石景山区', '2020-08-20', 'active', '大专', '孙强 13900139006'),
('110101198707077890', '周杰', '男', '1987-07-07', '13800138007', 'zhoujie@company.com', '北京市通州区', '2019-11-05', 'active', '本科', '周芳 13900139007'),
('110101199408088901', '吴磊', '男', '1994-08-08', '13800138008', 'wulei@company.com', '北京市昌平区', '2021-01-10', 'active', '本科', '吴娜 13900139008'),
('110101199009099012', '郑爽', '女', '1990-09-09', '13800138009', 'zhengshuang@company.com', '北京市大兴区', '2020-04-15', 'active', '硕士', '郑强 13900139009'),
('110101198610101123', '王磊', '男', '1986-10-10', '13800138010', 'wanglei@company.com', '北京市房山区', '2018-06-20', 'active', '本科', '王丽 13900139010'),
('110101199311111234', '陈静', '女', '1993-11-11', '13800138011', 'chenjing@company.com', '北京市门头沟区', '2021-09-01', 'active', '本科', '陈强 13900139011'),
('110101198912121345', '杨洋', '男', '1989-12-12', '13800138012', 'yangyang@company.com', '北京市平谷区', '2019-12-10', 'active', '大专', '杨丽 13900139012'),
('110101199601011456', '黄晓明', '男', '1996-01-01', '13800138013', 'huangxiaoming@company.com', '北京市怀柔区', '2022-03-15', 'active', '本科', '黄丽 13900139013'),
('110101199202021567', '赵丽颖', '女', '1992-02-02', '13800138014', 'zhaoliying@company.com', '北京市密云区', '2020-07-20', 'active', '本科', '赵强 13900139014'),
('110101198803031678', '邓超', '男', '1988-03-03', '13800138015', 'dengchao@company.com', '北京市延庆区', '2019-05-25', 'active', '硕士', '邓丽 13900139015'),
('110101199504041789', '孙俪', '女', '1995-04-04', '13800138016', 'sunli2@company.com', '北京市朝阳区', '2021-11-30', 'active', '本科', '孙强 13900139016'),
('110101199105051890', '胡歌', '男', '1991-05-05', '13800138017', 'huge@company.com', '北京市海淀区', '2020-10-05', 'active', '本科', '胡丽 13900139017'),
('110101198706061901', '刘诗诗', '女', '1987-06-06', '13800138018', 'liushishi@company.com', '北京市西城区', '2019-08-15', 'active', '大专', '刘强 13900139018'),
('110101199407072012', '吴京', '男', '1994-07-07', '13800138019', 'wujing@company.com', '北京市东城区', '2021-06-20', 'active', '本科', '吴丽 13900139019'),
('110101199008082123', '谢娜', '女', '1990-08-08', '13800138020', 'xiena@company.com', '北京市丰台区', '2020-12-25', 'active', '本科', '谢强 13900139020'),
('110101198609092234', '何炅', '男', '1986-09-09', '13800138021', 'hejiong@company.com', '北京市石景山区', '2018-09-30', 'active', '硕士', '何丽 13900139021'),
('110101199310102345', '李冰冰', '女', '1993-10-10', '13800138022', 'libingbing@company.com', '北京市通州区', '2021-04-05', 'active', '本科', '李强 13900139022'),
('110101198911112456', '范冰冰', '女', '1989-11-11', '13800138023', 'fanbingbing@company.com', '北京市昌平区', '2019-10-10', 'active', '大专', '范强 13900139023'),
('110101199612122567', '杨幂', '女', '1996-12-12', '13800138024', 'yangmi@company.com', '北京市大兴区', '2022-01-15', 'active', '本科', '杨强 13900139024'),
('110101199201012678', '刘亦菲', '女', '1992-01-01', '13800138025', 'liuyifei@company.com', '北京市房山区', '2020-11-20', 'active', '本科', '刘强 13900139025'),
('110101198802022789', '成龙', '男', '1988-02-02', '13800138026', 'chenglong@company.com', '北京市门头沟区', '2019-07-25', 'active', '高中', '成丽 13900139026'),
('110101199503032890', '李连杰', '男', '1995-03-03', '13800138027', 'lilianjie@company.com', '北京市平谷区', '2021-08-30', 'active', '本科', '李丽 13900139027'),
('110101199104042901', '甄子丹', '男', '1991-04-04', '13800138028', 'zhenzidan@company.com', '北京市怀柔区', '2020-09-05', 'active', '本科', '甄丽 13900139028'),
('110101198705053012', '周星驰', '男', '1987-05-05', '13800138029', 'zhouxingchi@company.com', '北京市密云区', '2019-06-10', 'active', '大专', '周丽 13900139029'),
('110101199406063123', '林志玲', '女', '1994-06-06', '13800138030', 'linzhiling@company.com', '北京市延庆区', '2021-07-15', 'active', '本科', '林强 13900139030');

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
('zhangwei', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHdUoeLN.nh8.Uh.0Vc.Oy', 1, 'active'),
('lina', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHdUoeLN.nh8.Uh.0Vc.Oy', 2, 'active'),
('wangfang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHdUoeLN.nh8.Uh.0Vc.Oy', 3, 'active'),
('liuqiang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHdUoeLN.nh8.Uh.0Vc.Oy', 4, 'active'),
('zhaomin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHdUoeLN.nh8.Uh.0Vc.Oy', 5, 'active'),
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z2EHdUoeLN.nh8.Uh.0Vc.Oy', 1, 'active');

-- ============================================
-- 6. 插入员工分配数据（员工-部门-岗位关联）
-- ============================================
INSERT INTO assignment_info (employee_id, dept_id, position_id, start_date, is_primary, change_reason, status) VALUES
(1, 1, 1, '2020-01-15', true, '入职', 'active'),
(2, 2, 2, '2019-03-20', true, '入职', 'active'),
(3, 3, 3, '2021-05-10', true, '入职', 'active'),
(4, 4, 4, '2018-07-01', true, '入职', 'active'),
(5, 5, 5, '2022-02-15', true, '入职', 'active'),
(6, 6, 6, '2020-08-20', true, '入职', 'active'),
(7, 6, 7, '2019-11-05', true, '入职', 'active'),
(8, 6, 8, '2021-01-10', true, '入职', 'active'),
(9, 7, 6, '2020-04-15', true, '入职', 'active'),
(10, 7, 7, '2018-06-20', true, '入职', 'active'),
(11, 7, 8, '2021-09-01', true, '入职', 'active'),
(12, 8, 10, '2019-12-10', true, '入职', 'active'),
(13, 8, 10, '2022-03-15', true, '入职', 'active'),
(14, 9, 11, '2020-07-20', true, '入职', 'active'),
(15, 9, 12, '2019-05-25', true, '入职', 'active'),
(16, 9, 12, '2021-11-30', true, '入职', 'active'),
(17, 10, 13, '2020-10-05', true, '入职', 'active'),
(18, 10, 12, '2019-08-15', true, '入职', 'active'),
(19, 4, 14, '2021-06-20', true, '入职', 'active'),
(20, 4, 14, '2020-12-25', true, '入职', 'active'),
(21, 5, 15, '2018-09-30', true, '入职', 'active'),
(22, 5, 15, '2021-04-05', true, '入职', 'active'),
(23, 2, 9, '2019-10-10', true, '入职', 'active'),
(24, 6, 7, '2022-01-15', true, '入职', 'active'),
(25, 7, 7, '2020-11-20', true, '入职', 'active'),
(26, 8, 10, '2019-07-25', true, '入职', 'active'),
(27, 9, 12, '2021-08-30', true, '入职', 'active'),
(28, 10, 12, '2020-09-05', true, '入职', 'active'),
(29, 3, 11, '2019-06-10', true, '入职', 'active'),
(30, 6, 8, '2021-07-15', true, '入职', 'active');

-- ============================================
-- 7. 插入考勤数据（每个员工10条记录）
-- 这里只插入部分示例数据，实际应该有更多
-- ============================================
-- 员工1的考勤记录
INSERT INTO attendance_info (employee_id, attendance_date, check_in_time, check_out_time, attendance_status, late_min, early_leave_min, overtime_hours, remark) VALUES
(1, '2024-01-02', '08:55:00', '18:05:00', 'normal', 0, 0, 0.5, NULL),
(1, '2024-01-03', '09:10:00', '18:00:00', 'late', 10, 0, 0, '地铁延误'),
(1, '2024-01-04', '08:50:00', '18:30:00', 'normal', 0, 0, 1.0, NULL),
(1, '2024-01-05', '08:58:00', '18:00:00', 'normal', 0, 0, 0, NULL),
(1, '2024-01-08', '09:00:00', '17:45:00', 'early_leave', 0, 15, 0, '家中有事'),
(1, '2024-01-09', '08:55:00', '18:00:00', 'normal', 0, 0, 0, NULL),
(1, '2024-01-10', '08:50:00', '19:00:00', 'normal', 0, 0, 2.0, '项目加班'),
(1, '2024-01-11', '09:05:00', '18:00:00', 'late', 5, 0, 0, NULL),
(1, '2024-01-12', '08:58:00', '18:00:00', 'normal', 0, 0, 0, NULL),
(1, '2024-01-15', NULL, NULL, 'leave', 0, 0, 0, '年假');

-- 为其他员工批量插入考勤数据（简化版本）
INSERT INTO attendance_info (employee_id, attendance_date, check_in_time, check_out_time, attendance_status, late_min, early_leave_min, overtime_hours) 
SELECT 
    e.employee_id,
    '2024-01-02'::date + (n || ' days')::interval,
    '09:00:00'::time,
    '18:00:00'::time,
    'normal',
    0,
    0,
    0
FROM employee_info e
CROSS JOIN generate_series(0, 9) as n
WHERE e.employee_id BETWEEN 2 AND 30;

-- ============================================
-- 8. 插入薪资数据（每个员工3个月）
-- ============================================
INSERT INTO payroll_info (employee_id, pay_month, base_pay, allowance, bonus, deduction, social_insurance, tax, net_pay, pay_date) VALUES
-- 员工1（总经理）
(1, '2023-11', 30000.00, 2000.00, 5000.00, 0, 3000.00, 4500.00, 29500.00, '2023-12-05'),
(1, '2023-12', 30000.00, 2000.00, 10000.00, 0, 3000.00, 5800.00, 33200.00, '2024-01-05'),
(1, '2024-01', 30000.00, 2000.00, 3000.00, 0, 3000.00, 4200.00, 27800.00, '2024-02-05'),
-- 员工2（技术总监）
(2, '2023-11', 25000.00, 1500.00, 3000.00, 0, 2500.00, 3600.00, 23400.00, '2023-12-05'),
(2, '2023-12', 25000.00, 1500.00, 5000.00, 0, 2500.00, 4200.00, 24800.00, '2024-01-05'),
(2, '2024-01', 25000.00, 1500.00, 2000.00, 0, 2500.00, 3400.00, 22600.00, '2024-02-05'),
-- 员工3（市场总监）
(3, '2023-11', 22000.00, 1500.00, 2500.00, 0, 2200.00, 3100.00, 20700.00, '2023-12-05'),
(3, '2023-12', 22000.00, 1500.00, 4000.00, 0, 2200.00, 3600.00, 21700.00, '2024-01-05'),
(3, '2024-01', 22000.00, 1500.00, 1500.00, 0, 2200.00, 2900.00, 19900.00, '2024-02-05');

-- 为其他员工批量插入薪资数据
INSERT INTO payroll_info (employee_id, pay_month, base_pay, allowance, bonus, deduction, social_insurance, tax, net_pay, pay_date)
SELECT 
    e.employee_id,
    to_char('2023-11-01'::date + (n || ' months')::interval, 'YYYY-MM'),
    p.base_salary,
    500.00,
    CASE WHEN n = 1 THEN 2000.00 ELSE 1000.00 END,
    0,
    p.base_salary * 0.1,
    p.base_salary * 0.12,
    p.base_salary + 500.00 + CASE WHEN n = 1 THEN 2000.00 ELSE 1000.00 END - p.base_salary * 0.1 - p.base_salary * 0.12,
    ('2023-12-05'::date + (n || ' months')::interval)
FROM employee_info e
JOIN assignment_info a ON e.employee_id = a.employee_id AND a.is_primary = true AND a.status = 'active'
JOIN position_info p ON a.position_id = p.position_id
CROSS JOIN generate_series(0, 2) as n
WHERE e.employee_id BETWEEN 4 AND 30;

-- ============================================
-- 9. 插入绩效数据（每个员工2条记录）
-- ============================================
INSERT INTO performance_info (employee_id, eval_date, score, grade, comment, evaluate_date) VALUES
(1, '2023-11-01', 95.00, 'A', '工作表现优秀，领导能力强', '2023-11-30'),
(1, '2023-12-01', 92.00, 'A', '持续保持优秀表现', '2023-12-30'),
(2, '2023-11-01', 90.00, 'A', '技术能力突出，团队管理到位', '2023-11-30'),
(2, '2023-12-01', 88.00, 'B', '表现良好，需加强沟通', '2023-12-30'),
(3, '2023-11-01', 85.00, 'B', '市场拓展能力强', '2023-11-30'),
(3, '2023-12-01', 87.00, 'B', '业绩稳步提升', '2023-12-30');

-- 为其他员工批量插入绩效数据
INSERT INTO performance_info (employee_id, eval_date, score, grade, comment, evaluate_date)
SELECT 
    employee_id,
    eval_date,
    score,
    CASE 
        WHEN score >= 90 THEN 'A'
        WHEN score >= 80 THEN 'B'
        WHEN score >= 60 THEN 'C'
        ELSE 'D'
    END as grade,
    comment,
    evaluate_date
FROM (
    SELECT 
        e.employee_id,
        '2023-11-01'::date + (n || ' months')::interval as eval_date,
        (70.00 + (random() * 25))::numeric(5,2) as score,
        '工作表现正常' as comment,
        ('2023-11-30'::date + (n || ' months')::interval) as evaluate_date
    FROM employee_info e
    CROSS JOIN generate_series(0, 1) as n
    WHERE e.employee_id BETWEEN 4 AND 30
) subquery;

-- ============================================
-- 数据插入完成提示
-- ============================================
SELECT '测试数据插入成功！' as message;

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
-- 2. 执行命令：\i /path/to/03-insert-test-data.sql
-- 3. 确认数据插入成功
-- 4. 测试登录账号：
--    用户名：admin / zhangwei / lina / wangfang / liuqiang / zhaomin
--    密码：password123（所有账号统一密码）
-- ============================================
