# API接口文档

## 基础信息

- 基础URL: `http://localhost:8080`
- 认证方式: JWT Token
- 请求头: `Authorization: Bearer {token}`
- 响应格式: JSON

## 通用响应格式

### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

### 错误响应
```json
{
  "code": 400,
  "message": "错误信息",
  "data": null
}
```

## 认证接口

### 用户登录
```
POST /api/user/login
```

请求体：
```json
{
  "username": "admin",
  "password": "password123"
}
```

响应：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userId": 1,
    "username": "admin"
  }
}
```

### 获取用户信息
```
GET /api/user/info
```

响应：
```json
{
  "code": 200,
  "data": {
    "userId": 1,
    "username": "admin",
    "employeeId": 1,
    "employeeName": "张伟"
  }
}
```

## 员工管理

### 查询员工列表
```
GET /api/employee/list?page=1&size=10&name=张&deptId=1
```

参数：
- page: 页码（默认1）
- size: 每页数量（默认10）
- name: 姓名（模糊查询）
- deptId: 部门ID
- status: 状态（active/inactive/deleted）

### 获取员工详情
```
GET /api/employee/{id}
```

### 新增员工
```
POST /api/employee
```

请求体：
```json
{
  "idCard": "110101199001011234",
  "name": "张三",
  "gender": "男",
  "birthDate": "1990-01-01",
  "phone": "13800138000",
  "email": "zhangsan@example.com",
  "address": "北京市朝阳区",
  "hireDate": "2024-01-01",
  "educationLevel": "本科"
}
```

### 更新员工
```
PUT /api/employee/{id}
```

### 删除员工
```
DELETE /api/employee/{id}
```

## 部门管理

### 查询部门列表
```
GET /api/department/list
```

### 部门树形结构
```
GET /api/department/tree
```

响应：
```json
{
  "code": 200,
  "data": [
    {
      "deptId": 1,
      "deptName": "总公司",
      "parentDeptId": null,
      "children": [
        {
          "deptId": 2,
          "deptName": "技术部",
          "parentDeptId": 1,
          "children": []
        }
      ]
    }
  ]
}
```

### 新增部门
```
POST /api/department
```

请求体：
```json
{
  "deptName": "研发部",
  "parentDeptId": 1,
  "managerEmployeeId": 2
}
```

### 更新部门
```
PUT /api/department/{id}
```

### 删除部门
```
DELETE /api/department/{id}
```

## 岗位管理

### 查询岗位列表
```
GET /api/position/list?page=1&size=10
```

### 新增岗位
```
POST /api/position
```

请求体：
```json
{
  "positionName": "Java开发工程师",
  "positionLevel": "中级",
  "baseSalary": 15000.00,
  "description": "负责后端开发"
}
```

### 更新岗位
```
PUT /api/position/{id}
```

### 删除岗位
```
DELETE /api/position/{id}
```

## 员工分配

### 查询员工分配
```
GET /api/assignment/list?employeeId=1
```

### 新增分配
```
POST /api/assignment
```

请求体：
```json
{
  "employeeId": 1,
  "deptId": 2,
  "positionId": 3,
  "startDate": "2024-01-01",
  "isPrimary": true,
  "changeReason": "部门调整"
}
```

## 考勤管理

### 查询考勤列表
```
GET /api/attendance/list?page=1&size=10&employeeId=1&startDate=2024-01-01&endDate=2024-01-31
```

### 新增考勤
```
POST /api/attendance
```

请求体：
```json
{
  "employeeId": 1,
  "attendanceDate": "2024-01-15",
  "checkInTime": "09:00:00",
  "checkOutTime": "18:00:00",
  "attendanceStatus": "normal",
  "lateMin": 0,
  "earlyLeaveMin": 0,
  "overtimeHours": 0
}
```

### 考勤月报
```
GET /api/attendance/monthly-report?employeeId=1&month=2024-01
```

## 薪资管理

### 查询薪资列表
```
GET /api/payroll/list?page=1&size=10&employeeId=1&payMonth=2024-01
```

### 新增薪资
```
POST /api/payroll
```

请求体：
```json
{
  "employeeId": 1,
  "payMonth": "2024-01",
  "basePay": 10000.00,
  "allowance": 1000.00,
  "bonus": 2000.00,
  "deduction": 0.00,
  "socialInsurance": 1000.00,
  "tax": 500.00,
  "netPay": 11500.00,
  "payDate": "2024-02-05"
}
```

## 绩效管理

### 查询绩效列表
```
GET /api/performance/list?page=1&size=10&employeeId=1
```

### 新增绩效
```
POST /api/performance
```

请求体：
```json
{
  "employeeId": 1,
  "evalDate": "2024-01-31",
  "score": 85.5,
  "grade": "B",
  "comment": "表现良好",
  "evaluateDate": "2024-02-01"
}
```

### 绩效排名
```
GET /api/performance/ranking?evalDate=2024-01-31
```

## 统计报表

### 按部门统计员工
```
GET /api/statistics/employee-by-department
```

响应：
```json
{
  "code": 200,
  "data": [
    {
      "deptName": "技术部",
      "employeeCount": 15
    }
  ]
}
```

### 部门考勤统计
```
GET /api/statistics/attendance-by-department?month=2024-01
```

### 绩效分布统计
```
GET /api/statistics/performance-distribution?evalDate=2024-01-31
```

## 数据导出

### 导出员工数据
```
GET /api/export/employee?deptId=1&status=active
```

返回Excel文件

### 导出考勤数据
```
GET /api/export/attendance?employeeId=1&startDate=2024-01-01&endDate=2024-01-31
```

### 导出薪资数据
```
GET /api/export/payroll?payMonth=2024-01
```

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权（Token无效或过期） |
| 403 | 无权限 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |
