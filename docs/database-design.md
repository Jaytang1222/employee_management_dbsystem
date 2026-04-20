# 企业员工管理系统 - 数据库设计文档

## 📊 文档信息

- **项目名称**：企业员工管理系统（Employee Management System）
- **数据库名称**：employee_management_system（简写：ems_db）
- **数据库类型**：OpenGauss
- **设计版本**：v1.0
- **设计日期**：2024-01-19

---

## 🎯 设计概述

### **设计目标**
基于ER图设计，实现企业员工全生命周期管理，包括员工基本信息、组织架构、考勤、薪资、绩效等核心业务数据的存储和管理。

### **设计原则**
1. **规范化**：遵循第三范式，消除数据冗余
2. **可扩展性**：支持未来功能扩展
3. **性能优化**：合理使用索引提升查询效率
4. **数据完整性**：使用主键、外键、约束保证数据一致性
5. **审计追踪**：所有表包含创建和更新时间戳
6. **软删除**：通过status字段实现逻辑删除

---

## 📐 ER图说明

### **核心实体（5个）**
1. **员工（Employee）** - 系统核心实体，存储员工基本信息
2. **部门（Department）** - 组织架构实体，支持树形结构
3. **岗位（Position）** - 职位信息实体
4. **考勤（Attendance）** - 员工考勤记录
5. **薪资记录（Payroll）** - 工资发放记录
6. **绩效考核（Performance）** - 员工绩效评估

### **辅助实体（2个）**
7. **用户（User）** - 登录认证实体
8. **员工分配（Assignment）** - 员工-部门-岗位关联实体

### **实体关系**
- **Employee ↔ Department**：多对多关系，通过Assignment表关联
- **Employee ↔ Position**：多对多关系，通过Assignment表关联
- **Employee → Attendance**：一对多关系
- **Employee → Payroll**：一对多关系
- **Employee → Performance**：一对多关系
- **Department → Department**：自关联，实现树形结构
- **User → Employee**：一对一关系

---

## 🗄️ 表结构设计

### **表清单**
| 序号 | 表名 | 中文名 | 说明 |
|------|------|--------|------|
| 1 | user_info | 用户表 | 登录认证 |
| 2 | employee_info | 员工表 | 员工基本信息 |
| 3 | department_info | 部门表 | 组织架构 |
| 4 | position_info | 岗位表 | 职位信息 |
| 5 | assignment_info | 员工分配表 | 员工-部门-岗位关联 |
| 6 | attendance_info | 考勤表 | 考勤记录 |
| 7 | payroll_info | 薪资记录表 | 工资发放 |
| 8 | performance_info | 绩效考核表 | 绩效评估 |

---

## 📋 详细表设计

### **1. user_info（用户表）**

**用途**：存储系统登录用户信息，用于身份认证

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| user_id | SERIAL | PRIMARY KEY | 自增 | 用户ID |
| username | VARCHAR(50) | NOT NULL, UNIQUE | - | 用户名（登录账号） |
| password | VARCHAR(255) | NOT NULL | - | 密码（BCrypt加密） |
| employee_id | INTEGER | REFERENCES employee_info(employee_id) | NULL | 关联员工ID |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态：active/inactive/deleted |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- PRIMARY KEY: user_id
- UNIQUE INDEX: username
- INDEX: employee_id

**业务规则**：
- 用户名唯一，不可重复
- 密码必须使用BCrypt加密存储
- 一个员工只能对应一个用户账号

---

### **2. employee_info（员工表）**

**用途**：存储员工基本信息，系统核心实体

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| employee_id | SERIAL | PRIMARY KEY | 自增 | 员工ID |
| id_card | VARCHAR(18) | NOT NULL, UNIQUE | - | 身份证号 |
| name | VARCHAR(50) | NOT NULL | - | 姓名 |
| gender | VARCHAR(10) | CHECK | NULL | 性别：男/女 |
| birth_date | DATE | - | NULL | 出生日期 |
| phone | VARCHAR(20) | - | NULL | 手机号 |
| email | VARCHAR(100) | - | NULL | 邮箱 |
| address | VARCHAR(200) | - | NULL | 地址 |
| hire_date | DATE | NOT NULL | - | 入职日期 |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态：active/inactive/deleted |
| education_level | VARCHAR(50) | - | NULL | 学历：高中/大专/本科/硕士/博士 |
| emergency_contact | VARCHAR(100) | - | NULL | 紧急联系人 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- PRIMARY KEY: employee_id
- UNIQUE INDEX: id_card
- INDEX: name
- INDEX: status

**约束**：
- CHECK (gender IN ('男', '女'))
- CHECK (status IN ('active', 'inactive', 'deleted'))

**业务规则**：
- 身份证号唯一，不可重复
- 姓名、身份证号、入职日期为必填项
- 出生日期应在合理范围内（18-65岁）
- 入职日期不能晚于当前日期

---

### **3. department_info（部门表）**

**用途**：存储部门信息，支持树形层级结构

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| dept_id | SERIAL | PRIMARY KEY | 自增 | 部门ID |
| dept_name | VARCHAR(100) | NOT NULL | - | 部门名称 |
| parent_dept_id | INTEGER | REFERENCES department_info(dept_id) | NULL | 上级部门ID |
| manager_employee_id | INTEGER | REFERENCES employee_info(employee_id) | NULL | 部门经理员工ID |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态：active/inactive/deleted |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- PRIMARY KEY: dept_id
- INDEX: parent_dept_id
- INDEX: manager_employee_id

**业务规则**：
- 根部门的parent_dept_id为NULL
- 支持3-5级部门层级
- 删除部门前需检查是否有子部门和员工
- 部门经理必须是该部门的员工

---

### **4. position_info（岗位表）**

**用途**：存储岗位职位信息

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| position_id | SERIAL | PRIMARY KEY | 自增 | 岗位ID |
| position_name | VARCHAR(100) | NOT NULL | - | 岗位名称 |
| position_level | VARCHAR(50) | - | NULL | 岗位级别：初级/中级/高级/专家 |
| base_salary | DECIMAL(10,2) | - | NULL | 基本工资 |
| description | TEXT | - | NULL | 岗位描述 |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态：active/inactive/deleted |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引**：
- PRIMARY KEY: position_id
- INDEX: position_name

**业务规则**：
- 岗位名称不能为空
- 基本工资必须大于0
- 删除岗位前需检查是否有员工在该岗位

---

### **5. assignment_info（员工分配表）**

**用途**：关联员工、部门、岗位的多对多关系，记录员工调动历史

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| assignment_id | SERIAL | PRIMARY KEY | 自增 | 分配ID |
| employee_id | INTEGER | NOT NULL, REFERENCES employee_info(employee_id) | - | 员工ID |
| dept_id | INTEGER | NOT NULL, REFERENCES department_info(dept_id) | - | 部门ID |
| position_id | INTEGER | NOT NULL, REFERENCES position_info(position_id) | - | 岗位ID |
| start_date | DATE | NOT NULL | - | 生效日期 |
| is_primary | BOOLEAN | NOT NULL | true | 是否主部门 |
| change_reason | TEXT | - | NULL | 调动原因 |
| status | VARCHAR(20) | NOT NULL | 'active' | 状态：active/inactive |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引**：
- PRIMARY KEY: assignment_id
- INDEX: employee_id
- INDEX: dept_id
- INDEX: position_id
- COMPOSITE INDEX: (employee_id, status)

**业务规则**：
- 一个员工可以有多个部门（主部门+兼职部门）
- is_primary=true标识主部门，每个员工只能有一个主部门
- 员工调动时新增记录，旧记录status改为inactive
- 保留历史分配记录，便于追溯

---

### **6. attendance_info（考勤表）**

**用途**：记录员工每日考勤情况

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| attendance_id | SERIAL | PRIMARY KEY | 自增 | 考勤ID |
| employee_id | INTEGER | NOT NULL, REFERENCES employee_info(employee_id) | - | 员工ID |
| attendance_date | DATE | NOT NULL | - | 考勤日期 |
| check_in_time | TIME | - | NULL | 签到时间 |
| check_out_time | TIME | - | NULL | 签退时间 |
| attendance_status | VARCHAR(20) | NOT NULL | 'normal' | 考勤状态 |
| late_min | INTEGER | NOT NULL | 0 | 迟到分钟数 |
| early_leave_min | INTEGER | NOT NULL | 0 | 早退分钟数 |
| overtime_hours | DECIMAL(4,2) | NOT NULL | 0 | 加班小时数 |
| remark | TEXT | - | NULL | 备注 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引**：
- PRIMARY KEY: attendance_id
- COMPOSITE INDEX: (employee_id, attendance_date)
- INDEX: attendance_date

**约束**：
- CHECK (attendance_status IN ('normal', 'late', 'early_leave', 'absent', 'leave'))
- CHECK (late_min >= 0)
- CHECK (early_leave_min >= 0)
- CHECK (overtime_hours >= 0 AND overtime_hours <= 24)

**业务规则**：
- 考勤状态：normal(正常)、late(迟到)、early_leave(早退)、absent(缺勤)、leave(请假)
- 签退时间必须晚于签到时间
- 考勤日期不能是未来日期
- 同一员工同一天只能有一条考勤记录

---

### **7. payroll_info（薪资记录表）**

**用途**：记录员工每月工资发放情况

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| payroll_id | SERIAL | PRIMARY KEY | 自增 | 薪资ID |
| employee_id | INTEGER | NOT NULL, REFERENCES employee_info(employee_id) | - | 员工ID |
| pay_month | VARCHAR(7) | NOT NULL | - | 发薪月份（格式：YYYY-MM） |
| base_pay | DECIMAL(10,2) | NOT NULL | - | 基本工资 |
| allowance | DECIMAL(10,2) | NOT NULL | 0 | 津贴 |
| bonus | DECIMAL(10,2) | NOT NULL | 0 | 奖金 |
| deduction | DECIMAL(10,2) | NOT NULL | 0 | 扣款 |
| social_insurance | DECIMAL(10,2) | NOT NULL | 0 | 社保 |
| tax | DECIMAL(10,2) | NOT NULL | 0 | 税 |
| net_pay | DECIMAL(10,2) | NOT NULL | - | 实发工资 |
| pay_date | DATE | - | NULL | 发薪日期 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引**：
- PRIMARY KEY: payroll_id
- COMPOSITE INDEX: (employee_id, pay_month)
- INDEX: pay_month

**约束**：
- CHECK (base_pay >= 0)
- CHECK (allowance >= 0)
- CHECK (bonus >= 0)
- CHECK (deduction >= 0)
- CHECK (social_insurance >= 0)
- CHECK (tax >= 0)
- CHECK (net_pay >= 0)

**业务规则**：
- 实发工资计算公式：net_pay = base_pay + allowance + bonus - deduction - social_insurance - tax
- pay_month格式必须为YYYY-MM（如：2024-01）
- 同一员工同一月份只能有一条薪资记录
- 所有金额字段必须大于等于0

---

### **8. performance_info（绩效考核表）**

**用途**：记录员工绩效考核结果

| 字段名 | 数据类型 | 约束 | 默认值 | 说明 |
|--------|----------|------|--------|------|
| performance_id | SERIAL | PRIMARY KEY | 自增 | 绩效ID |
| employee_id | INTEGER | NOT NULL, REFERENCES employee_info(employee_id) | - | 员工ID |
| eval_date | DATE | NOT NULL | - | 考核周期（月份） |
| score | DECIMAL(5,2) | NOT NULL | - | 绩效分数 |
| grade | VARCHAR(10) | NOT NULL | - | 绩效等级 |
| comment | TEXT | - | NULL | 评价意见 |
| evaluate_date | DATE | - | NULL | 评估日期 |
| created_at | TIMESTAMP | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引**：
- PRIMARY KEY: performance_id
- INDEX: employee_id
- INDEX: eval_date
- COMPOSITE INDEX: (employee_id, eval_date)

**约束**：
- CHECK (score >= 0 AND score <= 100)
- CHECK (grade IN ('A', 'B', 'C', 'D'))

**业务规则**：
- 绩效分数范围：0-100
- 绩效等级：A(优秀，90-100)、B(良好，80-89)、C(合格，60-79)、D(待改进，<60)
- 评估日期不能是未来日期
- 同一员工同一考核周期可以有多次考核记录

---

## 🔗 主外键关系图

```
user_info
  └─ employee_id → employee_info.employee_id

employee_info (核心表)
  ├─ ← assignment_info.employee_id
  ├─ ← attendance_info.employee_id
  ├─ ← payroll_info.employee_id
  ├─ ← performance_info.employee_id
  └─ ← department_info.manager_employee_id

department_info
  ├─ parent_dept_id → department_info.dept_id (自关联)
  ├─ manager_employee_id → employee_info.employee_id
  └─ ← assignment_info.dept_id

position_info
  └─ ← assignment_info.position_id

assignment_info (关联表)
  ├─ employee_id → employee_info.employee_id
  ├─ dept_id → department_info.dept_id
  └─ position_id → position_info.position_id
```

---

## 📊 索引设计说明

### **索引策略**
1. **主键索引**：所有表的主键自动创建索引
2. **外键索引**：所有外键字段创建索引，提升关联查询效率
3. **唯一索引**：唯一约束字段（如username、id_card）
4. **单列索引**：频繁查询的字段（如name、status）
5. **组合索引**：多字段联合查询（如employee_id + attendance_date）

### **索引清单**
| 表名 | 索引名 | 索引类型 | 字段 | 说明 |
|------|--------|----------|------|------|
| user_info | idx_user_username | UNIQUE | username | 用户名唯一索引 |
| user_info | idx_user_employee | INDEX | employee_id | 关联员工索引 |
| employee_info | idx_employee_idcard | UNIQUE | id_card | 身份证唯一索引 |
| employee_info | idx_employee_name | INDEX | name | 姓名查询索引 |
| employee_info | idx_employee_status | INDEX | status | 状态查询索引 |
| department_info | idx_dept_parent | INDEX | parent_dept_id | 上级部门索引 |
| department_info | idx_dept_manager | INDEX | manager_employee_id | 部门经理索引 |
| position_info | idx_position_name | INDEX | position_name | 岗位名称索引 |
| assignment_info | idx_assignment_employee | INDEX | employee_id | 员工分配索引 |
| assignment_info | idx_assignment_dept | INDEX | dept_id | 部门分配索引 |
| assignment_info | idx_assignment_position | INDEX | position_id | 岗位分配索引 |
| assignment_info | idx_assignment_emp_status | INDEX | (employee_id, status) | 员工状态组合索引 |
| attendance_info | idx_attendance_emp_date | INDEX | (employee_id, attendance_date) | 考勤查询组合索引 |
| attendance_info | idx_attendance_date | INDEX | attendance_date | 日期查询索引 |
| payroll_info | idx_payroll_emp_month | INDEX | (employee_id, pay_month) | 薪资查询组合索引 |
| payroll_info | idx_payroll_month | INDEX | pay_month | 月份查询索引 |
| performance_info | idx_performance_employee | INDEX | employee_id | 员工绩效索引 |
| performance_info | idx_performance_date | INDEX | eval_date | 考核日期索引 |
| performance_info | idx_performance_emp_date | INDEX | (employee_id, eval_date) | 员工考核组合索引 |

---

## ✅ 数据验证规则

### **1. 员工信息验证**
| 字段 | 验证规则 | 正则表达式/范围 |
|------|----------|------------------|
| id_card | 18位身份证号 | `^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$` |
| phone | 11位手机号 | `^1[3-9]\d{9}$` |
| email | 邮箱格式 | `^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$` |
| name | 2-50字符 | 长度：2-50 |
| birth_date | 年龄18-65岁 | 当前日期-65年 ≤ birth_date ≤ 当前日期-18年 |
| hire_date | 不能晚于当前日期 | hire_date ≤ CURRENT_DATE |
| gender | 男/女 | IN ('男', '女') |

### **2. 考勤信息验证**
| 字段 | 验证规则 |
|------|----------|
| check_out_time | 必须晚于check_in_time |
| attendance_date | 不能是未来日期 |
| late_min | ≥0 |
| early_leave_min | ≥0 |
| overtime_hours | 0-24 |
| attendance_status | IN ('normal', 'late', 'early_leave', 'absent', 'leave') |

### **3. 薪资信息验证**
| 字段 | 验证规则 |
|------|----------|
| 所有金额字段 | ≥0 |
| pay_month | 格式：YYYY-MM |
| net_pay | = base_pay + allowance + bonus - deduction - social_insurance - tax |

### **4. 绩效信息验证**
| 字段 | 验证规则 |
|------|----------|
| score | 0-100 |
| grade | IN ('A', 'B', 'C', 'D') |
| evaluate_date | 不能是未来日期 |
| grade与score对应 | A:90-100, B:80-89, C:60-79, D:0-59 |

---

## 🔄 数据字典

### **状态枚举值**
| 字段 | 可选值 | 说明 |
|------|--------|------|
| status（通用） | active | 激活/在职 |
| | inactive | 停用/离职 |
| | deleted | 已删除 |
| attendance_status | normal | 正常 |
| | late | 迟到 |
| | early_leave | 早退 |
| | absent | 缺勤 |
| | leave | 请假 |
| grade | A | 优秀（90-100分） |
| | B | 良好（80-89分） |
| | C | 合格（60-79分） |
| | D | 待改进（<60分） |
| gender | 男 | 男性 |
| | 女 | 女性 |
| education_level | 高中 | 高中学历 |
| | 大专 | 大专学历 |
| | 本科 | 本科学历 |
| | 硕士 | 硕士学历 |
| | 博士 | 博士学历 |
| position_level | 初级 | 初级岗位 |
| | 中级 | 中级岗位 |
| | 高级 | 高级岗位 |
| | 专家 | 专家岗位 |

---

## 📈 数据量估算

### **预期数据量（5年）**
| 表名 | 初始数据量 | 年增长量 | 5年后数据量 |
|------|-----------|----------|-------------|
| user_info | 30 | 10 | 80 |
| employee_info | 30 | 10 | 80 |
| department_info | 10 | 2 | 20 |
| position_info | 15 | 3 | 30 |
| assignment_info | 30 | 20 | 130 |
| attendance_info | 600 | 7,200 | 36,600 |
| payroll_info | 90 | 360 | 1,890 |
| performance_info | 60 | 360 | 1,860 |

---

## 🔒 安全设计

### **1. 密码安全**
- 使用BCrypt算法加密存储
- 密码字段长度：VARCHAR(255)
- 不在日志中输出密码
- 不在API响应中返回密码

### **2. 数据访问控制**
- 通过JWT Token验证用户身份
- 软删除保留数据，避免误删
- 重要操作记录审计日志

### **3. SQL注入防护**
- 使用MyBatis参数绑定（#{}）
- 禁止使用字符串拼接SQL

---

## 📝 维护说明

### **1. 数据备份策略**
- 每日全量备份
- 每小时增量备份
- 保留30天备份数据

### **2. 数据清理策略**
- 软删除数据保留1年
- 考勤数据保留3年
- 薪资数据永久保留
- 绩效数据永久保留

### **3. 性能优化建议**
- 定期分析慢查询
- 定期更新统计信息
- 考勤表按年分区（可选）
- 定期清理无效索引

---

## 📚 参考资料

- [OpenGauss官方文档](https://docs.opengauss.org/)
- [数据库设计规范](https://www.postgresql.org/docs/)
- [SQL标准](https://www.iso.org/standard/63555.html)

---

## 📋 变更记录

| 版本 | 日期 | 变更内容 | 变更人 |
|------|------|----------|--------|
| v1.0 | 2024-01-19 | 初始版本，完成8张表设计 | 开发团队 |

---

**文档状态**：✅ 已完成  
**审核状态**：待审核  
**最后更新**：2024-01-19
