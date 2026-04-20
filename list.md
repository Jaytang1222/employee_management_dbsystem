# 企业员工管理系统 - 开发规范文档

## 📋 项目概述

**项目名称**：企业员工管理系统（Employee Management System）  
**项目简称**：EMS  
**开发周期**：3-4周（约23个工作日）  
**项目目标**：基于OpenGauss数据库搭建完整的企业员工管理系统，实现员工、部门、岗位、考勤、薪资、绩效的全生命周期管理

---

## 🛠️ 技术栈规范

### **后端技术栈**
| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 8 | 编程语言 |
| Spring Boot | 2.7.18 | 应用框架 |
| MyBatis | 2.3.1 | 持久层框架 |
| OpenGauss JDBC | 3.1.0 | 数据库驱动 |
| Lombok | Latest | 简化实体类代码 |
| Validation | Spring Boot Starter | 数据校验 |
| JWT (jjwt) | 0.9.1 | 登录认证 |
| Apache POI | 5.2.3 | Excel导出 |
| Maven | 3.6+ | 构建工具 |

### **前端技术栈**
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue.js | 3.x | 前端框架 |
| Element Plus | Latest | UI组件库 |
| Vue Router | 4.x | 路由管理 |
| Axios | Latest | HTTP客户端 |
| Vite | Latest | 构建工具 |

### **数据库**
| 技术 | 版本 | 说明 |
|------|------|------|
| OpenGauss | 已配置 | 主数据库 |

### **开发工具**
- **IDE**：IntelliJ IDEA / VS Code
- **API测试**：Postman
- **版本控制**：Git + GitHub
- **数据库管理**：Data Studio / DBeaver

---

## 📐 命名规范

### **1. 项目命名**
- **后端包名**：`com.company.ems`
- **数据库名**：`employee_management_system`（简写：`ems_db`）
- **GitHub仓库名**：`employee-management-system`

### **2. 数据库命名规范**
- **表名**：下划线命名法，全小写，如：`employee_info`、`department_info`
- **字段名**：下划线命名法，全小写，如：`employee_id`、`dept_name`
- **主键**：`表名单数_id`，如：`employee_id`、`dept_id`
- **外键**：`关联表名_id`，如：`parent_dept_id`、`manager_employee_id`
- **索引**：`idx_表名_字段名`，如：`idx_employee_name`

### **3. Java代码命名规范**
- **类名**：大驼峰命名法（PascalCase），如：`EmployeeService`、`DepartmentController`
- **方法名**：小驼峰命名法（camelCase），如：`getEmployeeById`、`updateDepartment`
- **变量名**：小驼峰命名法，如：`employeeId`、`deptName`
- **常量名**：全大写+下划线，如：`MAX_PAGE_SIZE`、`DEFAULT_STATUS`

### **4. 前端命名规范**
- **组件名**：大驼峰命名法，如：`EmployeeList.vue`、`DepartmentTree.vue`
- **文件名**：小驼峰命名法，如：`request.js`、`auth.js`
- **变量/方法**：小驼峰命名法，如：`employeeList`、`fetchData()`

---

## 🗄️ 数据库设计规范

### **1. 表结构规范**

#### **必须包含的表（8张）**
1. `user_info` - 用户表（登录认证）
2. `employee_info` - 员工表
3. `department_info` - 部门表
4. `position_info` - 岗位表
5. `assignment_info` - 员工分配表（员工-部门-岗位关联）
6. `attendance_info` - 考勤表
7. `payroll_info` - 薪资记录表
8. `performance_info` - 绩效考核表

#### **字段设计规范**
- **主键**：使用 `SERIAL` 类型自增主键
- **外键**：必须添加 `REFERENCES` 约束
- **状态字段**：统一使用 `status VARCHAR(20) DEFAULT 'active'`
- **审计字段**：每张表必须包含
  - `created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
  - `updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
- **软删除**：通过 `status` 字段实现（'active', 'inactive', 'deleted'）

### **2. 数据类型规范**
| 数据类型 | 使用场景 | 示例 |
|----------|----------|------|
| SERIAL | 主键自增 | employee_id |
| VARCHAR(n) | 短文本 | name VARCHAR(50) |
| TEXT | 长文本 | description TEXT |
| DATE | 日期 | birth_date DATE |
| TIME | 时间 | check_in_time TIME |
| TIMESTAMP | 日期时间 | created_at TIMESTAMP |
| DECIMAL(m,n) | 金额/分数 | salary DECIMAL(10,2) |
| INTEGER | 整数 | late_min INTEGER |
| BOOLEAN | 布尔值 | is_primary BOOLEAN |

### **3. 约束规范**
- **NOT NULL**：必填字段必须添加
- **UNIQUE**：唯一字段（如身份证号）必须添加
- **CHECK**：枚举字段使用CHECK约束，如：`CHECK (gender IN ('男', '女'))`
- **DEFAULT**：有默认值的字段必须添加

### **4. 索引规范**
必须创建索引的场景：
- 外键字段
- 频繁查询的字段（如：name、status）
- 联合查询字段（如：employee_id + attendance_date）

---

## 🎯 功能需求规范

### **1. 核心功能（必须实现）**

#### **1.1 用户认证**
- ✅ 用户登录（用户名+密码）
- ✅ 登录状态保持（JWT Token）
- ✅ 登出功能
- ❌ 不需要角色权限管理
- ❌ 不需要用户注册功能

#### **1.2 基础CRUD（7个实体）**
每个实体必须实现：
- ✅ **Create（增）**：新增记录
- ✅ **Read（查）**：
  - 列表查询（支持分页）
  - 单条查询（按ID）
  - 条件查询（搜索功能）
- ✅ **Update（改）**：更新记录
- ✅ **Delete（删）**：软删除（修改status）

**实体清单**：
1. 员工管理
2. 部门管理
3. 岗位管理
4. 员工分配管理
5. 考勤管理
6. 薪资管理
7. 绩效管理

#### **1.3 特殊功能**
- ✅ **部门树形展示**：递归查询实现部门层级结构
- ✅ **统计报表**：3个复杂查询
  1. 按部门统计员工数量
  2. 员工考勤月报
  3. 绩效排名
- ✅ **数据导出**：Excel格式导出
  - 员工列表导出
  - 考勤记录导出
  - 薪资记录导出

### **2. 业务规则规范**

#### **2.1 员工-部门-岗位关系**
- 一个员工可以同时在多个部门（主部门 + 兼职部门）
- `is_primary = true` 标识主部门
- 员工调动时新增 `assignment_info` 记录（保留历史）

#### **2.2 部门树结构**
- 支持3-5级部门层级
- 使用 `parent_dept_id` 自关联实现
- 根部门的 `parent_dept_id` 为 `NULL`

#### **2.3 考勤状态枚举**
- `normal` - 正常
- `late` - 迟到
- `early_leave` - 早退
- `absent` - 缺勤
- `leave` - 请假

#### **2.4 绩效等级枚举**
- `A` - 优秀（90-100分）
- `B` - 良好（80-89分）
- `C` - 合格（60-79分）
- `D` - 待改进（<60分）

#### **2.5 薪资计算规则**
```
实发工资(net_pay) = 基本工资(base_pay) + 津贴(allowance) + 奖金(bonus) 
                    - 扣款(deduction) - 社保(social_insurance) - 税(tax)
```
- `net_pay` 作为存储字段（避免重复计算）
- 更新薪资组成部分时必须重新计算 `net_pay`

---

## ✅ 数据验证规范

### **1. 员工信息验证**
| 字段 | 验证规则 |
|------|----------|
| 身份证号 | 18位，格式校验（正则表达式） |
| 手机号 | 11位，格式校验 |
| 邮箱 | 邮箱格式校验 |
| 出生日期 | 年龄范围：18-65岁 |
| 入职日期 | 不能晚于当前日期 |
| 姓名 | 非空，2-50字符 |

### **2. 考勤信息验证**
| 字段 | 验证规则 |
|------|----------|
| 签退时间 | 必须晚于签到时间 |
| 考勤日期 | 不能是未来日期 |
| 迟到分钟数 | ≥0 |
| 加班小时数 | ≥0，≤24 |

### **3. 薪资信息验证**
| 字段 | 验证规则 |
|------|----------|
| 所有金额字段 | ≥0 |
| 发薪月份 | 格式：YYYY-MM |
| 实发工资 | 必须等于计算结果 |

### **4. 绩效信息验证**
| 字段 | 验证规则 |
|------|----------|
| 分数 | 0-100 |
| 等级 | 必须是A/B/C/D之一 |
| 评估日期 | 不能是未来日期 |

---

## 🔌 API接口规范

### **1. RESTful API设计规范**
- **URL命名**：小写+连字符，如：`/api/employees`、`/api/department-tree`
- **HTTP方法**：
  - `GET` - 查询
  - `POST` - 新增
  - `PUT` - 更新
  - `DELETE` - 删除

### **2. 统一返回格式**
```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

**状态码规范**：
- `200` - 成功
- `400` - 请求参数错误
- `401` - 未登录
- `403` - 无权限
- `404` - 资源不存在
- `500` - 服务器错误

### **3. 接口清单**

#### **认证接口**
```
POST   /api/login          - 登录
POST   /api/logout         - 登出
GET    /api/userinfo       - 获取当前用户信息
```

#### **员工管理**
```
GET    /api/employees              - 员工列表（分页+搜索）
GET    /api/employees/{id}         - 员工详情
POST   /api/employees              - 新增员工
PUT    /api/employees/{id}         - 更新员工
DELETE /api/employees/{id}         - 删除员工
```

#### **部门管理**
```
GET    /api/departments            - 部门列表
GET    /api/departments/tree       - 部门树形结构
GET    /api/departments/{id}       - 部门详情
POST   /api/departments            - 新增部门
PUT    /api/departments/{id}       - 更新部门
DELETE /api/departments/{id}       - 删除部门
```

#### **岗位管理**
```
GET    /api/positions              - 岗位列表
GET    /api/positions/{id}         - 岗位详情
POST   /api/positions              - 新增岗位
PUT    /api/positions/{id}         - 更新岗位
DELETE /api/positions/{id}         - 删除岗位
```

#### **考勤管理**
```
GET    /api/attendances            - 考勤列表
GET    /api/attendances/{id}       - 考勤详情
POST   /api/attendances            - 新增考勤
PUT    /api/attendances/{id}       - 更新考勤
DELETE /api/attendances/{id}       - 删除考勤
GET    /api/attendances/report     - 考勤月报（参数：employeeId, month）
```

#### **薪资管理**
```
GET    /api/payrolls               - 薪资列表
GET    /api/payrolls/{id}          - 薪资详情
POST   /api/payrolls               - 新增薪资
PUT    /api/payrolls/{id}          - 更新薪资
DELETE /api/payrolls/{id}          - 删除薪资
```

#### **绩效管理**
```
GET    /api/performances           - 绩效列表
GET    /api/performances/{id}      - 绩效详情
POST   /api/performances           - 新增绩效
PUT    /api/performances/{id}      - 更新绩效
DELETE /api/performances/{id}      - 删除绩效
GET    /api/performances/ranking   - 绩效排名（参数：evalDate）
```

#### **统计报表**
```
GET    /api/statistics/employee-by-dept     - 按部门统计员工数量
GET    /api/statistics/attendance-report    - 考勤月报
GET    /api/statistics/performance-ranking  - 绩效排名
```

#### **数据导出**
```
GET    /api/export/employees       - 导出员工列表
GET    /api/export/attendances     - 导出考勤记录（参数：month）
GET    /api/export/payrolls        - 导出薪资记录（参数：month）
```

---

## 📂 项目结构规范

### **后端项目结构**
```
backend/
├── src/main/java/com/company/ems/
│   ├── EmsApplication.java              # 启动类
│   ├── config/                          # 配置类
│   │   ├── DataSourceConfig.java
│   │   ├── MyBatisConfig.java
│   │   ├── CorsConfig.java
│   │   └── WebMvcConfig.java
│   ├── entity/                          # 实体类（8个）
│   ├── dto/                             # 数据传输对象
│   ├── vo/                              # 视图对象
│   │   └── Result.java                 # 统一返回结果
│   ├── mapper/                          # MyBatis Mapper接口（8个）
│   ├── service/                         # 业务逻辑层（8个+统计+导出）
│   ├── controller/                      # 控制器层（8个+统计+导出）
│   ├── exception/                       # 异常处理
│   │   ├── GlobalExceptionHandler.java
│   │   └── BusinessException.java
│   ├── interceptor/                     # 拦截器
│   │   └── LoginInterceptor.java
│   └── util/                            # 工具类
│       ├── JwtUtil.java
│       ├── ValidationUtil.java
│       └── ExcelUtil.java
└── src/main/resources/
    ├── application.yml                  # 应用配置
    └── mapper/                          # MyBatis XML映射文件（8个）
```

### **前端项目结构**
```
frontend/
├── src/
│   ├── main.js                         # 入口文件
│   ├── App.vue                         # 根组件
│   ├── router/                         # 路由配置
│   │   └── index.js
│   ├── api/                            # API接口封装（8个+统计+导出）
│   ├── utils/                          # 工具类
│   │   ├── request.js                 # Axios封装
│   │   └── auth.js                    # 登录状态管理
│   ├── views/                          # 页面组件
│   │   ├── Login.vue                  # 登录页
│   │   ├── Home.vue                   # 首页
│   │   ├── employee/                  # 员工管理页面
│   │   ├── department/                # 部门管理页面
│   │   ├── position/                  # 岗位管理页面
│   │   ├── attendance/                # 考勤管理页面
│   │   ├── payroll/                   # 薪资管理页面
│   │   ├── performance/               # 绩效管理页面
│   │   └── statistics/                # 统计报表页面
│   └── components/                     # 公共组件
│       ├── Layout.vue                 # 布局组件
│       └── Breadcrumb.vue             # 面包屑导航
└── package.json
```

---

## 📝 测试数据规范

### **测试数据量要求**
- **部门**：5-10个（含树形结构，至少3级）
- **岗位**：10-15个（覆盖不同级别）
- **员工**：30个（分布在不同部门和岗位）
- **考勤记录**：每个员工至少10条
- **薪资记录**：每个员工至少3条（不同月份）
- **绩效记录**：每个员工至少2条

### **测试数据覆盖场景**
- ✅ 正常数据
- ✅ 边界数据（最大值、最小值）
- ✅ 特殊数据（迟到、早退、缺勤）
- ✅ 关联数据（员工在多个部门）
- ✅ 历史数据（员工调动记录）

---

## 🔒 安全规范

### **1. 密码安全**
- 密码必须加密存储（使用BCrypt）
- 不得在日志中输出密码
- 不得在API响应中返回密码

### **2. SQL注入防护**
- 必须使用MyBatis的参数绑定（`#{}`）
- 禁止使用字符串拼接SQL（`${}`）

### **3. 跨域配置**
- 后端配置CORS允许前端域名访问
- 生产环境必须限制允许的域名

### **4. 登录认证**
- 使用JWT Token进行身份验证
- Token有效期：24小时
- 前端请求必须在Header中携带Token

---

## 📄 文档输出规范

### **必须输出的文档**

#### **1. 数据库设计文档**（`docs/database-design.md`）
内容包括：
- ER图及说明
- 8张表的详细设计（字段、类型、约束、说明）
- 主键、外键关系说明
- 索引设计说明
- 数据验证规则

#### **2. 系统功能模块图**（`docs/system-module-diagram.png`）
使用工具绘制系统功能模块结构图

#### **3. README.md**
内容包括：
- 项目简介
- 技术栈
- 功能特性
- 环境要求
- 安装步骤
- 运行说明
- 项目结构
- 开发团队

---

## ⏱️ 开发进度规范

### **开发阶段划分**
| 阶段 | 任务 | 预计时间 |
|------|------|----------|
| 阶段一 | 数据库设计与实现 | 2天 |
| 阶段二 | 后端开发 | 7-10天 |
| 阶段三 | 前端开发 | 5-7天 |
| 阶段四 | 联调测试 | 2-3天 |
| 阶段五 | 文档整理 | 1-2天 |
| **总计** | | **3-4周** |

### **里程碑节点**
- ✅ M1：数据库设计完成（Day 2）
- ✅ M2：后端基础CRUD完成（Day 9）
- ✅ M3：后端全部功能完成（Day 12）
- ✅ M4：前端全部页面完成（Day 18）
- ✅ M5：联调测试完成（Day 21）
- ✅ M6：文档整理完成（Day 23）

---

## 🔄 Git提交规范

### **分支管理**
```
main          - 主分支（稳定版本）
develop       - 开发分支
feature/xxx   - 功能分支
```

### **提交信息规范**
```
<type>: <subject>

type类型：
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 重构
- test: 测试相关
- chore: 构建/工具变动

示例：
feat: 添加员工管理CRUD功能
fix: 修复部门删除时的外键约束问题
docs: 更新数据库设计文档
```

### **提交频率**
- 每完成一个小功能点立即提交
- 每天至少提交1次
- 重要节点（如完成一个模块）必须提交

---

## ⚠️ 注意事项

### **开发约束**
1. ✅ 必须使用指定的技术栈，不得随意更换
2. ✅ 必须实现所有核心功能，不得删减
3. ✅ 必须遵循命名规范和代码规范
4. ✅ 必须进行数据验证，不得接受非法数据
5. ✅ 必须处理异常，不得让程序崩溃
6. ✅ 必须输出必需文档

### **禁止事项**
1. ❌ 禁止硬编码敏感信息（密码、密钥）
2. ❌ 禁止使用字符串拼接SQL
3. ❌ 禁止在生产环境使用测试数据
4. ❌ 禁止提交未测试的代码
5. ❌ 禁止删除或修改已有的表结构（除非经过讨论）

### **代码质量要求**
- ✅ 代码必须有适当的注释
- ✅ 方法长度不超过50行
- ✅ 类的职责单一
- ✅ 避免代码重复
- ✅ 异常必须有明确的错误信息

---

## 📞 问题处理流程

### **遇到技术问题时**
1. 先查阅官方文档
2. 搜索相关技术社区（Stack Overflow、CSDN）
3. 查看项目Issue记录
4. 与团队成员讨论
5. 记录问题和解决方案

### **需求变更时**
1. 评估变更影响范围
2. 更新相关文档
3. 通知团队成员
4. 调整开发计划

---

## ✅ 验收标准

### **功能验收**
- ✅ 所有核心功能正常运行
- ✅ 数据验证规则生效
- ✅ 异常处理正确
- ✅ 界面友好，操作流畅

### **代码验收**
- ✅ 代码规范符合要求
- ✅ 无明显的代码坏味道
- ✅ 关键代码有注释
- ✅ 无编译警告

### **文档验收**
- ✅ 数据库设计文档完整
- ✅ 系统功能模块图清晰
- ✅ README.md 内容完整

### **测试验收**
- ✅ 所有功能测试通过
- ✅ 边界测试通过
- ✅ 无严重bug

---

## 📚 参考资料

### **官方文档**
- [Spring Boot 2.7.x 文档](https://docs.spring.io/spring-boot/docs/2.7.x/reference/html/)
- [MyBatis 文档](https://mybatis.org/mybatis-3/zh/index.html)
- [Vue 3 文档](https://cn.vuejs.org/)
- [Element Plus 文档](https://element-plus.org/zh-CN/)
- [OpenGauss 文档](https://docs.opengauss.org/)

### **技术社区**
- Stack Overflow
- GitHub
- CSDN
- 掘金

---

**文档版本**：v1.0  
**最后更新**：2024-01-19  
**维护人员**：开发团队

---

## 📋 检查清单

在开发过程中，请定期检查以下事项：

### **数据库阶段**
- [ ] ER图已绘制并审核
- [ ] 8张表的DDL脚本已编写
- [ ] 测试数据脚本已编写
- [ ] 索引已创建
- [ ] 数据库设计文档已完成

### **后端开发阶段**
- [ ] 项目初始化完成
- [ ] 数据库连接测试通过
- [ ] 8个实体类已创建
- [ ] 8个Mapper接口和XML已创建
- [ ] 8个Service类已创建
- [ ] 8个Controller类已创建
- [ ] 登录功能已实现
- [ ] 统计报表功能已实现
- [ ] 数据导出功能已实现
- [ ] 全局异常处理已配置
- [ ] 跨域配置已完成

### **前端开发阶段**
- [ ] 项目初始化完成
- [ ] Axios封装完成
- [ ] 路由配置完成
- [ ] 登录页面已完成
- [ ] 布局组件已完成
- [ ] 8个模块的页面已完成
- [ ] 部门树形展示已实现
- [ ] 统计报表页面已完成
- [ ] 导出功能已集成

### **测试阶段**
- [ ] 登录功能测试通过
- [ ] 所有CRUD功能测试通过
- [ ] 部门树展示测试通过
- [ ] 统计报表测试通过
- [ ] 数据导出测试通过
- [ ] 数据验证测试通过
- [ ] 边界测试通过

### **文档阶段**
- [ ] 数据库设计文档已完成
- [ ] 系统功能模块图已绘制
- [ ] README.md已完成
- [ ] 代码注释已添加

---

**祝开发顺利！🚀**
