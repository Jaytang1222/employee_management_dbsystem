# 企业员工管理系统（Employee Management System）

## 📋 项目简介

企业员工管理系统是一个基于OpenGauss数据库的全功能员工管理平台，实现了员工、部门、岗位、考勤、薪资、绩效的全生命周期管理。

**项目简称**：EMS  
**开发周期**：3-4周  
**当前状态**：✅ 数据库设计与实现阶段已完成

---

## 🛠️ 技术栈

### **后端技术**
- Java 8
- Spring Boot 2.7.18
- MyBatis 2.3.1
- OpenGauss JDBC 3.1.0
- JWT (jjwt) 0.9.1
- Apache POI 5.2.3
- Maven 3.6+

### **前端技术**
- Vue.js 3.x
- Element Plus
- Vue Router 4.x
- Axios
- Vite

### **数据库**
- OpenGauss

---

## ✨ 功能特性

### **核心功能**
- ✅ 用户登录认证（JWT Token）
- ✅ 员工信息管理（CRUD）
- ✅ 部门管理（CRUD + 树形展示）
- ✅ 岗位管理（CRUD）
- ✅ 员工分配管理（部门+岗位）
- ✅ 考勤管理（CRUD + 月报）
- ✅ 薪资管理（CRUD + 统计）
- ✅ 绩效管理（CRUD + 排名）

### **特色功能**
- 🌳 **部门树形展示**：递归展示部门层级结构
- 📊 **统计报表**：
  - 按部门统计员工数量
  - 员工考勤月报
  - 绩效排名
- 📥 **数据导出**：Excel格式导出员工、考勤、薪资数据

---

## 📂 项目结构

```
employee-management-system/
├── README.md                          # 项目说明
├── list.md                            # 开发规范文档
├── docs/                              # 文档目录
│   ├── database-design.md            # 数据库设计文档 ✅
│   └── system-module-diagram.md      # 系统功能模块图 ✅
├── sql/                               # SQL脚本 ✅
│   ├── 01-create-database.sql        # 创建数据库
│   ├── 02-create-tables.sql          # 创建表结构（8张表）
│   ├── 03-insert-test-data.sql       # 插入测试数据（30个员工）
│   └── 04-create-indexes.sql         # 创建索引
├── backend/                           # 后端项目（待开发）
└── frontend/                          # 前端项目（待开发）
```

---

## 🗄️ 数据库设计

### **数据库信息**
- **数据库名**：employee_management_system（简写：ems_db）
- **表数量**：8张核心业务表
- **测试数据**：30个员工 + 相关数据

### **表清单**
| 序号 | 表名 | 中文名 | 说明 |
|------|------|--------|------|
| 1 | user_info | 用户表 | 登录认证 |
| 2 | employee_info | 员工表 | 员工基本信息 |
| 3 | department_info | 部门表 | 组织架构（树形结构） |
| 4 | position_info | 岗位表 | 职位信息 |
| 5 | assignment_info | 员工分配表 | 员工-部门-岗位关联 |
| 6 | attendance_info | 考勤表 | 考勤记录 |
| 7 | payroll_info | 薪资记录表 | 工资发放 |
| 8 | performance_info | 绩效考核表 | 绩效评估 |

### **ER关系**
- Employee ↔ Department：多对多（通过Assignment）
- Employee ↔ Position：多对多（通过Assignment）
- Employee → Attendance：一对多
- Employee → Payroll：一对多
- Employee → Performance：一对多
- Department → Department：自关联（树形结构）

---

## 🚀 快速开始

### **环境要求**
- OpenGauss 数据库（已配置）
- Java 8+
- Maven 3.6+
- Node.js 14+（前端开发）

### **数据库安装步骤**

#### **1. 连接到OpenGauss**
```bash
gsql -d postgres -h localhost -U gaussdb -W -p 5432
```

#### **2. 创建数据库**
```bash
\i /path/to/sql/01-create-database.sql
```

#### **3. 创建表结构**
```bash
\i /path/to/sql/02-create-tables.sql
```

#### **4. 插入测试数据**
```bash
\i /path/to/sql/03-insert-test-data.sql
```

#### **5. 创建索引**
```bash
\i /path/to/sql/04-create-indexes.sql
```

#### **6. 验证安装**
```sql
-- 查看所有表
SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;

-- 统计数据量
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
SELECT 'performance_info', COUNT(*) FROM performance_info;
```

**预期结果**：
- employee_info: 30条
- department_info: 10条
- position_info: 15条
- user_info: 6条
- assignment_info: 30条
- attendance_info: 300条
- payroll_info: 90条
- performance_info: 60条

---

## 🔑 测试账号

系统已预置以下测试账号（密码统一为：`password123`）：

| 用户名 | 密码 | 关联员工 | 说明 |
|--------|------|----------|------|
| admin | password123 | 张伟 | 管理员账号 |
| zhangwei | password123 | 张伟 | 总经理 |
| lina | password123 | 李娜 | 技术总监 |
| wangfang | password123 | 王芳 | 市场总监 |
| liuqiang | password123 | 刘强 | 人力资源经理 |
| zhaomin | password123 | 赵敏 | 财务经理 |

---

## 📊 数据库设计亮点

### **1. 规范化设计**
- 遵循第三范式，消除数据冗余
- 合理使用主键、外键约束
- 统一的命名规范（下划线命名法）

### **2. 审计追踪**
- 所有表包含 `created_at` 和 `updated_at` 字段
- 记录数据创建和更新时间

### **3. 软删除**
- 通过 `status` 字段实现逻辑删除
- 保留历史数据，避免误删

### **4. 性能优化**
- 为外键字段创建索引
- 为频繁查询字段创建索引
- 为组合查询创建复合索引
- 共创建30+个索引

### **5. 数据完整性**
- CHECK约束：性别、状态、等级等枚举值
- UNIQUE约束：身份证号、用户名等唯一字段
- NOT NULL约束：必填字段
- 外键约束：保证关联数据一致性

### **6. 灵活扩展**
- 部门支持树形结构（3-5级）
- 员工支持多部门（主部门+兼职）
- 保留员工调动历史记录

---

## 📝 开发进度

### **✅ 阶段一：数据库设计与实现（已完成）**
- [x] ER图绘制
- [x] 数据库设计文档编写
- [x] 系统功能模块图设计
- [x] SQL建表脚本编写
- [x] 测试数据脚本编写
- [x] 索引创建脚本编写
- [x] 数据库部署验证

### **⏳ 阶段二：后端开发（进行中）**
- [ ] 项目初始化
- [ ] 基础架构搭建
- [ ] 登录功能实现
- [ ] 基础CRUD开发（7个模块）
- [ ] 统计报表功能
- [ ] 数据导出功能

### **⏳ 阶段三：前端开发（待开始）**
- [ ] 项目初始化
- [ ] 基础架构搭建
- [ ] 登录页面
- [ ] 各模块页面开发
- [ ] 统计报表页面
- [ ] 导出功能集成

### **⏳ 阶段四：联调测试（待开始）**
- [ ] 功能测试
- [ ] 边界测试
- [ ] 用户体验优化
- [ ] Bug修复

### **⏳ 阶段五：文档整理（待开始）**
- [ ] API接口文档
- [ ] 用户操作手册
- [ ] 部署文档

---

## 📚 文档说明

### **已完成文档**
1. **list.md** - 开发规范文档
   - 技术栈规范
   - 命名规范
   - 数据库设计规范
   - API接口规范
   - 项目结构规范
   - 测试数据规范
   - Git提交规范

2. **docs/database-design.md** - 数据库设计文档
   - ER图说明
   - 8张表的详细设计
   - 主外键关系图
   - 索引设计说明
   - 数据验证规则
   - 数据字典

3. **docs/system-module-diagram.md** - 系统功能模块图
   - 功能模块结构
   - 模块详细说明
   - 模块间关系
   - 功能优先级
   - 用户界面设计

---

## 🔒 安全设计

### **1. 密码安全**
- 使用BCrypt算法加密存储
- 密码字段长度：VARCHAR(255)
- 不在日志中输出密码

### **2. SQL注入防护**
- 使用MyBatis参数绑定（#{}）
- 禁止使用字符串拼接SQL

### **3. 数据访问控制**
- JWT Token身份验证
- 软删除保留数据
- 重要操作记录审计日志

---

## 📞 技术支持

### **问题反馈**
如遇到问题，请按以下步骤处理：
1. 查阅项目文档
2. 检查数据库连接配置
3. 查看错误日志
4. 联系开发团队

### **常见问题**

**Q1: 数据库连接失败？**
A: 检查OpenGauss服务是否启动，确认连接参数（host、port、username、password）是否正确。

**Q2: 表创建失败？**
A: 确保按顺序执行SQL脚本，先创建数据库，再创建表，最后插入数据。

**Q3: 测试数据插入失败？**
A: 检查外键约束，确保关联表的数据已存在。

**Q4: 索引创建失败？**
A: 确保表和字段已存在，检查索引名称是否重复。

---

## 📖 参考资料

### **官方文档**
- [OpenGauss官方文档](https://docs.opengauss.org/)
- [Spring Boot文档](https://docs.spring.io/spring-boot/)
- [MyBatis文档](https://mybatis.org/mybatis-3/zh/index.html)
- [Vue 3文档](https://cn.vuejs.org/)
- [Element Plus文档](https://element-plus.org/zh-CN/)

### **技术社区**
- [Stack Overflow](https://stackoverflow.com/)
- [GitHub](https://github.com/)
- [CSDN](https://www.csdn.net/)
- [掘金](https://juejin.cn/)

---

## 📄 许可证

本项目仅用于学习和研究目的。

---

## 👥 开发团队

**项目负责人**：开发团队  
**开发周期**：2024-01-19 至今  
**当前版本**：v1.0（数据库设计阶段）

---

## 🎯 下一步计划

1. **后端开发**（预计7-10天）
   - 搭建Spring Boot项目
   - 实现登录认证
   - 完成7个模块的CRUD
   - 实现统计报表和数据导出

2. **前端开发**（预计5-7天）
   - 搭建Vue3项目
   - 实现登录页面
   - 完成各模块页面
   - 实现统计报表和导出功能

3. **联调测试**（预计2-3天）
   - 功能测试
   - 性能测试
   - 用户体验优化

---

**项目状态**：🚧 开发中  
**最后更新**：2024-01-19  
**文档版本**：v1.0

---

## ⭐ 项目亮点

1. ✅ **完整的数据库设计**：8张表，30+索引，完善的约束
2. ✅ **规范的开发文档**：详细的设计文档和开发规范
3. ✅ **丰富的测试数据**：30个员工，300+条考勤记录
4. ✅ **树形结构支持**：部门层级管理
5. ✅ **软删除机制**：保留历史数据
6. ✅ **审计追踪**：记录创建和更新时间
7. ✅ **性能优化**：合理的索引设计
8. ✅ **数据完整性**：完善的约束和验证

---

**感谢使用企业员工管理系统！** 🎉
