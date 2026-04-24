<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="layout-aside">
      <div class="logo">
        <span v-if="!isCollapse">EMS</span>
        <span v-else>E</span>
      </div>
      
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :unique-opened="true"
        router
        class="layout-menu"
      >
        <el-menu-item index="/home">
          <el-icon><HomeFilled /></el-icon>
          <template #title>{{ t('menu.home') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/employee">
          <el-icon><User /></el-icon>
          <template #title>{{ t('menu.employee') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/department">
          <el-icon><OfficeBuilding /></el-icon>
          <template #title>{{ t('menu.department') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/position">
          <el-icon><Briefcase /></el-icon>
          <template #title>{{ t('menu.position') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/assignment">
          <el-icon><Connection /></el-icon>
          <template #title>{{ t('menu.assignment') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/attendance">
          <el-icon><Calendar /></el-icon>
          <template #title>{{ t('menu.attendance') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/payroll">
          <el-icon><Wallet /></el-icon>
          <template #title>{{ t('menu.payroll') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/performance">
          <el-icon><TrendCharts /></el-icon>
          <template #title>{{ t('menu.performance') }}</template>
        </el-menu-item>
        
        <el-menu-item index="/statistics">
          <el-icon><DataAnalysis /></el-icon>
          <template #title>{{ t('menu.statistics') }}</template>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主内容区 -->
    <el-container>
      <!-- 顶部导航栏 -->
      <el-header class="layout-header">
        <div class="header-left">
          <el-icon class="collapse-icon" @click="toggleCollapse">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
          <Breadcrumb />
        </div>
        
        <div class="header-right">
          <!-- 语言切换 -->
          <el-dropdown @command="handleLanguageChange">
            <span class="language-switch">
              <el-icon><Operation /></el-icon>
              {{ currentLanguage === 'zh' ? '中文' : 'EN' }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="zh">中文</el-dropdown-item>
                <el-dropdown-item command="en">English</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          
          <!-- 用户信息 -->
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <el-icon><Avatar /></el-icon>
              {{ userInfo?.username || 'User' }}
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  {{ t('common.logout') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  HomeFilled,
  User,
  OfficeBuilding,
  Briefcase,
  Connection,
  Calendar,
  Wallet,
  TrendCharts,
  DataAnalysis,
  Expand,
  Fold,
  Operation,
  Avatar,
  SwitchButton
} from '@element-plus/icons-vue'
import Breadcrumb from './Breadcrumb.vue'
import { getUserInfo, clearAuth } from '@/utils/auth'
import { logout } from '@/api/user'

const router = useRouter()
const route = useRoute()
const { t, locale } = useI18n()

// 侧边栏折叠状态
const isCollapse = ref(false)

// 当前激活的菜单
const activeMenu = computed(() => {
  return route.path
})

// 当前语言
const currentLanguage = computed(() => locale.value)

// 用户信息
const userInfo = ref(null)

// 切换侧边栏折叠
const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

// 语言切换
const handleLanguageChange = (lang) => {
  locale.value = lang
  localStorage.setItem('language', lang)
  ElMessage.success(t('common.success'))
}

// 下拉菜单命令处理
const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm(
        t('common.confirmDelete'),
        t('common.tips'),
        {
          confirmButtonText: t('common.confirm'),
          cancelButtonText: t('common.cancel'),
          type: 'warning'
        }
      )
      
      // 调用登出接口
      await logout()
      
      // 清除本地认证信息
      clearAuth()
      
      // 跳转到登录页（使用replace防止返回）
      router.replace('/login')
      
      ElMessage.success(t('common.success'))
    } catch (error) {
      if (error !== 'cancel') {
        console.error('Logout error:', error)
      }
    }
  }
}

// 初始化
onMounted(() => {
  userInfo.value = getUserInfo()
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
  width: 100vw;
}

.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow-x: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  background-color: #2b3a4a;
}

.layout-menu {
  border-right: none;
  background-color: #304156;
}

.layout-menu :deep(.el-menu-item) {
  color: #bfcbd9;
}

.layout-menu :deep(.el-menu-item:hover) {
  background-color: #263445 !important;
  color: #fff;
}

.layout-menu :deep(.el-menu-item.is-active) {
  background-color: #409eff !important;
  color: #fff;
}

.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.collapse-icon {
  font-size: 20px;
  cursor: pointer;
  transition: color 0.3s;
}

.collapse-icon:hover {
  color: #409eff;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.language-switch,
.user-info {
  display: flex;
  align-items: center;
  gap: 5px;
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.language-switch:hover,
.user-info:hover {
  background-color: #f5f5f5;
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
  width: 100%;
  max-width: 100%;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .layout-aside {
    position: fixed;
    left: 0;
    top: 0;
    bottom: 0;
    z-index: 1000;
  }
  
  .layout-aside.is-collapse {
    width: 0 !important;
  }
  
  .header-left {
    gap: 10px;
  }
  
  .header-right {
    gap: 10px;
  }
  
  .layout-main {
    padding: 10px;
  }
}
</style>
