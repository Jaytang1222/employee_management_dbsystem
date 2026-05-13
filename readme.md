# 企业员工管理系统

## 项目简介

企业员工管理系统是一个基于OpenGauss数据库的全功能员工管理平台，实现了员工、部门、岗位、考勤、薪资、绩效的全生命周期管理。

项目简称：EMS

## 技术栈

### 后端
- Java 8
- Spring Boot 2.7.18
- MyBatis 2.3.2
- OpenGauss JDBC 6.0.3
- JWT (jjwt) 0.11.5
- Apache POI 5.2.5

### 前端
- Vue 3
- Element Plus
- Vue Router 4
- Axios
- Vite

## 功能特性

### 核心功能
- 用户登录认证（JWT Token）
- 员工信息管理（CRUD）
- 部门管理（CRUD + 树形展示）
- 岗位管理（CRUD）
- 员工分配管理（部门+岗位）
- 考勤管理（CRUD + 月报）
- 薪资管理（CRUD + 统计）
- 绩效管理（CRUD + 排名）

### 特色功能
- 部门树形展示：递归展示部门层级结构
- 统计报表：按部门统计员工数量、员工考勤月报、绩效排名
- 数据导出：Excel格式导出员工、考勤、薪资数据

## 项目结构

```
employee-management-system/
├── docs/                              # 文档目录
│   ├── database-design.md            # 数据库设计文档
│   ├── system-module-diagram.md      # 系统功能模块图
│   ├── deployment-guide.md           # 部署指南
│   ├── development-guide.md          # 开发指南
│   └── api-reference.md              # API接口文档
├── sql/                               # SQL脚本
│   ├── 01-create-database.sql        # 创建数据库
│   ├── 02-create-tables.sql          # 创建表结构
│   ├── 03-insert-test-data.sql       # 插入测试数据
│   └── 04-create-indexes.sql         # 创建索引
├── src/                               # 后端源码
└── frontend/                          # 前端源码
```

## 数据库设计

### 数据库信息
- 数据库名：employee_management_system
- 表数量：8张核心业务表
- 测试数据：30个员工 + 相关数据

### 表清单
| 表名 | 说明 |
|------|------|
| user_info | 用户表（登录认证） |
| employee_info | 员工表（员工基本信息） |
| department_info | 部门表（组织架构） |
| position_info | 岗位表（职位信息） |
| assignment_info | 员工分配表（员工-部门-岗位关联） |
| attendance_info | 考勤表（考勤记录） |
| payroll_info | 薪资记录表（工资发放） |
| performance_info | 绩效考核表（绩效评估） |

详细设计请参考 [数据库设计文档](docs/database-design.md)

## 快速部署

### 环境要求
- OpenGauss 数据库
- Java 8+
- Maven 3.6+
- Node.js 14+

### 数据库部署

1. 连接到OpenGauss
```bash
gsql -d postgres -h localhost -U gaussdb -W -p 5432
```

2. 按顺序执行SQL脚本
```bash
\i sql/01-create-database.sql
\i sql/02-create-tables.sql
\i sql/03-insert-test-data.sql
\i sql/04-create-indexes.sql
```

3. 验证安装
```sql
-- 查看所有表
SELECT tablename FROM pg_tables WHERE schemaname='public' ORDER BY tablename;

-- 统计数据量
SELECT 'employee_info' as table_name, COUNT(*) FROM employee_info
UNION ALL SELECT 'department_info', COUNT(*) FROM department_info
UNION ALL SELECT 'position_info', COUNT(*) FROM position_info
UNION ALL SELECT 'user_info', COUNT(*) FROM user_info
UNION ALL SELECT 'assignment_info', COUNT(*) FROM assignment_info
UNION ALL SELECT 'attendance_info', COUNT(*) FROM attendance_info
UNION ALL SELECT 'payroll_info', COUNT(*) FROM payroll_info
UNION ALL SELECT 'performance_info', COUNT(*) FROM performance_info;
```

预期结果：
- employee_info: 30条
- department_info: 10条
- position_info: 15条
- user_info: 6条
- assignment_info: 30条
- attendance_info: 300条
- payroll_info: 90条
- performance_info: 60条

### 后端部署

1. 配置数据库连接

编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:opengauss://localhost:5432/employee_management_system
    username: gaussdb
    password: your_password
    driver-class-name: org.opengauss.Driver
```

2. 编译打包
```bash
mvn clean package
```

3. 运行应用
```bash
java -jar target/ems-1.0.0.jar
```

后端服务运行在 http://localhost:8080

### 前端部署

1. 安装依赖
```bash
cd frontend
npm install
```

2. 配置后端地址

编辑 `frontend/.env.development`：
```
VITE_API_BASE_URL=http://localhost:8080
```

3. 开发模式运行
```bash
npm run dev
```

前端服务运行在 http://localhost:5173

4. 生产构建
```bash
npm run build
```

构建产物在 `frontend/dist` 目录

### 生产环境部署

使用Nginx部署前端：
```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    root /path/to/frontend/dist;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

详细部署说明请参考 [部署指南](docs/deployment-guide.md)

## 测试账号

系统预置测试账号（密码统一为用户名相同·）：

| 用户名 | 关联员工 | 角色 |
|--------|----------|------|
| admin | 张伟 | 管理员 |
| zhangwei | 张伟 | 总经理 |
| lina | 李娜 | 技术总监 |
| wangfang | 王芳 | 市场总监 |
| liuqiang | 刘强 | 人力资源经理 |
| zhaomin | 赵敏 | 财务经理 |

## 文档

- [数据库设计文档](docs/database-design.md) - 数据库表结构、索引、约束设计
- [系统功能模块图](docs/system-module-diagram.md) - 系统功能模块和业务流程
- [部署指南](docs/deployment-guide.md) - 详细的部署步骤和配置说明
- [开发指南](docs/development-guide.md) - 开发规范、项目结构、技术实现
- [API接口文档](docs/api-reference.md) - RESTful API接口说明

## 常见问题

### 数据库连接失败
检查OpenGauss服务是否启动，确认连接参数（host、port、username、password）是否正确。

### 后端启动失败
检查Java版本是否为8+，数据库是否已创建并初始化，application.yml配置是否正确。

### 前端无法访问后端
检查后端服务是否正常运行，CORS配置是否正确，.env文件中的API地址是否正确。

