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
  background-color: #000000;
  transition: width 0.3s;
  overflow-x: hidden;
  border-right: 1px solid #5e5e5e;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #76b900;
  font-size: 24px;
  font-weight: 700;
  background-color: #000000;
  border-bottom: 2px solid #76b900;
  letter-spacing: 2px;
}

.layout-menu {
  border-right: none;
  background-color: #000000;
}

.layout-menu :deep(.el-menu-item) {
  color: #ffffff;
  font-weight: 700;
  font-size: 14px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-left: 2px solid transparent;
  transition: all 0.3s;
}

.layout-menu :deep(.el-menu-item:hover) {
  background-color: #1a1a1a !important;
  color: #76b900;
  border-left-color: #76b900;
}

.layout-menu :deep(.el-menu-item.is-active) {
  background-color: #1a1a1a !important;
  color: #76b900;
  border-left-color: #76b900;
}

.layout-menu :deep(.el-menu-item .el-icon) {
  color: inherit;
}

.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #000000;
  border-bottom: 2px solid #76b900;
  padding: 0 20px;
  height: 60px;
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
  color: #ffffff;
}

.collapse-icon:hover {
  color: #76b900;
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
  padding: 8px 13px;
  border-radius: 2px;
  transition: all 0.3s;
  color: #ffffff;
  font-weight: 700;
  font-size: 14px;
  text-transform: uppercase;
  border: 2px solid transparent;
}

.language-switch:hover,
.user-info:hover {
  background-color: #1eaedb;
  border-color: #76b900;
}

.layout-main {
  background-color: #ffffff;
  padding: 24px;
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
    padding: 16px;
  }
  
  .language-switch,
  .user-info {
    font-size: 12px;
    padding: 6px 10px;
  }
}
</style>
