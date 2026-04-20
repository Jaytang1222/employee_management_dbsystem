-- ============================================
-- 企业员工管理系统 - 表结构创建脚本
-- ============================================
-- 数据库名称：employee_management_system
-- 数据库类型：OpenGauss
-- 创建日期：2024-01-19
-- 说明：创建8张核心业务表
-- ============================================

-- 确保连接到正确的数据库
\c employee_management_system

-- ============================================
-- 1. 员工表（employee_info）
-- 说明：存储员工基本信息，系统核心实体
-- ============================================
CREATE TABLE employee_info (
    employee_id SERIAL PRIMARY KEY,
    id_card VARCHAR(18) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10) CHECK (gender IN ('男', '女')),
    birth_date DATE,
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(200),
    hire_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deleted')),
    education_level VARCHAR(50),
    emergency_contact VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE employee_info IS '员工信息表';
COMMENT ON COLUMN employee_info.employee_id IS '员工ID（主键）';
COMMENT ON COLUMN employee_info.id_card IS '身份证号（唯一）';
COMMENT ON COLUMN employee_info.name IS '姓名';
COMMENT ON COLUMN employee_info.gender IS '性别：男/女';
COMMENT ON COLUMN employee_info.birth_date IS '出生日期';
COMMENT ON COLUMN employee_info.phone IS '手机号';
COMMENT ON COLUMN employee_info.email IS '邮箱';
COMMENT ON COLUMN employee_info.address IS '地址';
COMMENT ON COLUMN employee_info.hire_date IS '入职日期';
COMMENT ON COLUMN employee_info.status IS '状态：active/inactive/deleted';
COMMENT ON COLUMN employee_info.education_level IS '学历：高中/大专/本科/硕士/博士';
COMMENT ON COLUMN employee_info.emergency_contact IS '紧急联系人';
COMMENT ON COLUMN employee_info.created_at IS '创建时间';
COMMENT ON COLUMN employee_info.updated_at IS '更新时间';

-- ============================================
-- 2. 部门表（department_info）
-- 说明：存储部门信息，支持树形层级结构
-- ============================================
CREATE TABLE department_info (
    dept_id SERIAL PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL,
    parent_dept_id INTEGER REFERENCES department_info(dept_id) ON DELETE SET NULL,
    manager_employee_id INTEGER,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deleted')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE department_info IS '部门信息表';
COMMENT ON COLUMN department_info.dept_id IS '部门ID（主键）';
COMMENT ON COLUMN department_info.dept_name IS '部门名称';
COMMENT ON COLUMN department_info.parent_dept_id IS '上级部门ID（自关联）';
COMMENT ON COLUMN department_info.manager_employee_id IS '部门经理员工ID';
COMMENT ON COLUMN department_info.status IS '状态：active/inactive/deleted';
COMMENT ON COLUMN department_info.created_at IS '创建时间';
COMMENT ON COLUMN department_info.updated_at IS '更新时间';

-- 添加外键约束（部门经理）
ALTER TABLE department_info 
ADD CONSTRAINT fk_dept_manager 
FOREIGN KEY (manager_employee_id) 
REFERENCES employee_info(employee_id) 
ON DELETE SET NULL;

-- ============================================
-- 3. 岗位表（position_info）
-- 说明：存储岗位职位信息
-- ============================================
CREATE TABLE position_info (
    position_id SERIAL PRIMARY KEY,
    position_name VARCHAR(100) NOT NULL,
    position_level VARCHAR(50),
    base_salary DECIMAL(10,2) CHECK (base_salary >= 0),
    description TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deleted')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE position_info IS '岗位信息表';
COMMENT ON COLUMN position_info.position_id IS '岗位ID（主键）';
COMMENT ON COLUMN position_info.position_name IS '岗位名称';
COMMENT ON COLUMN position_info.position_level IS '岗位级别：初级/中级/高级/专家';
COMMENT ON COLUMN position_info.base_salary IS '基本工资';
COMMENT ON COLUMN position_info.description IS '岗位描述';
COMMENT ON COLUMN position_info.status IS '状态：active/inactive/deleted';
COMMENT ON COLUMN position_info.created_at IS '创建时间';
COMMENT ON COLUMN position_info.updated_at IS '更新时间';

-- ============================================
-- 4. 用户表（user_info）
-- 说明：存储系统登录用户信息
-- ============================================
CREATE TABLE user_info (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    employee_id INTEGER REFERENCES employee_info(employee_id) ON DELETE SET NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive', 'deleted')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE user_info IS '用户信息表（登录认证）';
COMMENT ON COLUMN user_info.user_id IS '用户ID（主键）';
COMMENT ON COLUMN user_info.username IS '用户名（登录账号）';
COMMENT ON COLUMN user_info.password IS '密码（BCrypt加密）';
COMMENT ON COLUMN user_info.employee_id IS '关联员工ID';
COMMENT ON COLUMN user_info.status IS '状态：active/inactive/deleted';
COMMENT ON COLUMN user_info.created_at IS '创建时间';
COMMENT ON COLUMN user_info.updated_at IS '更新时间';

-- ============================================
-- 5. 员工分配表（assignment_info）
-- 说明：关联员工、部门、岗位，记录调动历史
-- ============================================
CREATE TABLE assignment_info (
    assignment_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee_info(employee_id) ON DELETE CASCADE,
    dept_id INTEGER NOT NULL REFERENCES department_info(dept_id) ON DELETE CASCADE,
    position_id INTEGER NOT NULL REFERENCES position_info(position_id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    is_primary BOOLEAN NOT NULL DEFAULT true,
    change_reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'active' CHECK (status IN ('active', 'inactive')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE assignment_info IS '员工分配表（员工-部门-岗位关联）';
COMMENT ON COLUMN assignment_info.assignment_id IS '分配ID（主键）';
COMMENT ON COLUMN assignment_info.employee_id IS '员工ID';
COMMENT ON COLUMN assignment_info.dept_id IS '部门ID';
COMMENT ON COLUMN assignment_info.position_id IS '岗位ID';
COMMENT ON COLUMN assignment_info.start_date IS '生效日期';
COMMENT ON COLUMN assignment_info.is_primary IS '是否主部门';
COMMENT ON COLUMN assignment_info.change_reason IS '调动原因';
COMMENT ON COLUMN assignment_info.status IS '状态：active/inactive';
COMMENT ON COLUMN assignment_info.created_at IS '创建时间';

-- ============================================
-- 6. 考勤表（attendance_info）
-- 说明：记录员工每日考勤情况
-- ============================================
CREATE TABLE attendance_info (
    attendance_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee_info(employee_id) ON DELETE CASCADE,
    attendance_date DATE NOT NULL,
    check_in_time TIME,
    check_out_time TIME,
    attendance_status VARCHAR(20) NOT NULL DEFAULT 'normal' 
        CHECK (attendance_status IN ('normal', 'late', 'early_leave', 'absent', 'leave')),
    late_min INTEGER NOT NULL DEFAULT 0 CHECK (late_min >= 0),
    early_leave_min INTEGER NOT NULL DEFAULT 0 CHECK (early_leave_min >= 0),
    overtime_hours DECIMAL(4,2) NOT NULL DEFAULT 0 CHECK (overtime_hours >= 0 AND overtime_hours <= 24),
    remark TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (employee_id, attendance_date)
);

COMMENT ON TABLE attendance_info IS '考勤信息表';
COMMENT ON COLUMN attendance_info.attendance_id IS '考勤ID（主键）';
COMMENT ON COLUMN attendance_info.employee_id IS '员工ID';
COMMENT ON COLUMN attendance_info.attendance_date IS '考勤日期';
COMMENT ON COLUMN attendance_info.check_in_time IS '签到时间';
COMMENT ON COLUMN attendance_info.check_out_time IS '签退时间';
COMMENT ON COLUMN attendance_info.attendance_status IS '考勤状态：normal/late/early_leave/absent/leave';
COMMENT ON COLUMN attendance_info.late_min IS '迟到分钟数';
COMMENT ON COLUMN attendance_info.early_leave_min IS '早退分钟数';
COMMENT ON COLUMN attendance_info.overtime_hours IS '加班小时数';
COMMENT ON COLUMN attendance_info.remark IS '备注';
COMMENT ON COLUMN attendance_info.created_at IS '创建时间';

-- ============================================
-- 7. 薪资记录表（payroll_info）
-- 说明：记录员工每月工资发放情况
-- ============================================
CREATE TABLE payroll_info (
    payroll_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee_info(employee_id) ON DELETE CASCADE,
    pay_month VARCHAR(7) NOT NULL,
    base_pay DECIMAL(10,2) NOT NULL CHECK (base_pay >= 0),
    allowance DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (allowance >= 0),
    bonus DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (bonus >= 0),
    deduction DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (deduction >= 0),
    social_insurance DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (social_insurance >= 0),
    tax DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (tax >= 0),
    net_pay DECIMAL(10,2) NOT NULL CHECK (net_pay >= 0),
    pay_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (employee_id, pay_month)
);

COMMENT ON TABLE payroll_info IS '薪资记录表';
COMMENT ON COLUMN payroll_info.payroll_id IS '薪资ID（主键）';
COMMENT ON COLUMN payroll_info.employee_id IS '员工ID';
COMMENT ON COLUMN payroll_info.pay_month IS '发薪月份（格式：YYYY-MM）';
COMMENT ON COLUMN payroll_info.base_pay IS '基本工资';
COMMENT ON COLUMN payroll_info.allowance IS '津贴';
COMMENT ON COLUMN payroll_info.bonus IS '奖金';
COMMENT ON COLUMN payroll_info.deduction IS '扣款';
COMMENT ON COLUMN payroll_info.social_insurance IS '社保';
COMMENT ON COLUMN payroll_info.tax IS '税';
COMMENT ON COLUMN payroll_info.net_pay IS '实发工资';
COMMENT ON COLUMN payroll_info.pay_date IS '发薪日期';
COMMENT ON COLUMN payroll_info.created_at IS '创建时间';

-- ============================================
-- 8. 绩效考核表（performance_info）
-- 说明：记录员工绩效考核结果
-- ============================================
CREATE TABLE performance_info (
    performance_id SERIAL PRIMARY KEY,
    employee_id INTEGER NOT NULL REFERENCES employee_info(employee_id) ON DELETE CASCADE,
    eval_date DATE NOT NULL,
    score DECIMAL(5,2) NOT NULL CHECK (score >= 0 AND score <= 100),
    grade VARCHAR(10) NOT NULL CHECK (grade IN ('A', 'B', 'C', 'D')),
    comment TEXT,
    evaluate_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE performance_info IS '绩效考核表';
COMMENT ON COLUMN performance_info.performance_id IS '绩效ID（主键）';
COMMENT ON COLUMN performance_info.employee_id IS '员工ID';
COMMENT ON COLUMN performance_info.eval_date IS '考核周期（月份）';
COMMENT ON COLUMN performance_info.score IS '绩效分数（0-100）';
COMMENT ON COLUMN performance_info.grade IS '绩效等级：A/B/C/D';
COMMENT ON COLUMN performance_info.comment IS '评价意见';
COMMENT ON COLUMN performance_info.evaluate_date IS '评估日期';
COMMENT ON COLUMN performance_info.created_at IS '创建时间';

-- ============================================
-- 表创建完成提示
-- ============================================
SELECT '所有表创建成功！' as message;

-- 查看所有表
SELECT tablename, schemaname 
FROM pg_tables 
WHERE schemaname = 'public' 
ORDER BY tablename;

-- ============================================
-- 执行说明：
-- 1. 确保已连接到employee_management_system数据库
-- 2. 执行命令：\i /path/to/02-create-tables.sql
-- 3. 确认所有表创建成功
-- 4. 继续执行03-insert-test-data.sql插入测试数据
-- ============================================
