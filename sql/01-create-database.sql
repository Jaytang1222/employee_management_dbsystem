-- ============================================
-- 企业员工管理系统 - 数据库创建脚本
-- ============================================
-- 数据库名称：employee_management_system
-- 数据库类型：OpenGauss
-- 创建日期：2024-01-19
-- 说明：创建数据库和基础配置
-- ============================================

-- 1. 创建数据库
CREATE DATABASE employee_management_system
    WITH 
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.UTF-8'
    LC_CTYPE = 'en_US.UTF-8'
    TEMPLATE = template0;

-- 2. 连接到新创建的数据库
\c employee_management_system

-- 3. 创建schema（可选，使用默认public schema）
-- CREATE SCHEMA IF NOT EXISTS ems;

-- 4. 设置搜索路径
-- SET search_path TO ems, public;

-- 5. 显示当前数据库信息
SELECT current_database() as database_name, 
       current_user as current_user,
       version() as database_version;

-- ============================================
-- 执行说明：
-- 1. 使用gsql连接到OpenGauss数据库
-- 2. 执行命令：\i /path/to/01-create-database.sql
-- 3. 确认数据库创建成功
-- ============================================
