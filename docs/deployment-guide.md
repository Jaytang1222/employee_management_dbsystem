# 部署指南

## 环境要求

### 必需软件
- OpenGauss 数据库
- Java 8+
- Maven 3.6+
- Node.js 14+

## 数据库部署

### 1. 连接数据库
```bash
gsql -d postgres -h localhost -U gaussdb -W -p 5432
```

### 2. 执行SQL脚本
按顺序执行以下脚本：
```bash
\i sql/01-create-database.sql
\i sql/02-create-tables.sql
\i sql/03-insert-test-data.sql
\i sql/04-create-indexes.sql
```

### 3. 验证安装
```sql
-- 查看表
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

## 后端部署

### 1. 配置数据库连接
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:opengauss://localhost:5432/employee_management_system
    username: gaussdb
    password: your_password
    driver-class-name: org.opengauss.Driver
```

### 2. 编译打包
```bash
mvn clean package
```

### 3. 运行应用
```bash
java -jar target/ems-1.0.0.jar
```

后端服务默认运行在 `http://localhost:8080`

### 4. 验证后端
访问健康检查接口：
```bash
curl http://localhost:8080/actuator/health
```

## 前端部署

### 1. 安装依赖
```bash
cd frontend
npm install
```

### 2. 配置后端地址
编辑 `frontend/.env.development`：
```
VITE_API_BASE_URL=http://localhost:8080
```

### 3. 开发模式运行
```bash
npm run dev
```

前端服务默认运行在 `http://localhost:5173`

### 4. 生产构建
```bash
npm run build
```

构建产物在 `frontend/dist` 目录

## 生产环境部署

### 后端生产部署
```bash
# 使用生产配置
java -jar target/ems-1.0.0.jar --spring.profiles.active=prod
```

### 前端生产部署
使用Nginx部署：
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

## 测试账号

系统预置测试账号（密码统一为 `password123`）：

| 用户名 | 关联员工 | 角色 |
|--------|----------|------|
| admin | 张伟 | 管理员 |
| zhangwei | 张伟 | 总经理 |
| lina | 李娜 | 技术总监 |
| wangfang | 王芳 | 市场总监 |
| liuqiang | 刘强 | 人力资源经理 |
| zhaomin | 赵敏 | 财务经理 |

## 常见问题

### 数据库连接失败
检查：
- OpenGauss服务是否启动
- 连接参数是否正确（host、port、username、password）
- 防火墙是否开放5432端口

### 后端启动失败
检查：
- Java版本是否为8+
- 数据库是否已创建并初始化
- application.yml配置是否正确

### 前端无法访问后端
检查：
- 后端服务是否正常运行
- CORS配置是否正确
- .env文件中的API地址是否正确
