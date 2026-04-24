import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: 'login.title' }
  },
  {
    path: '/',
    component: () => import('@/components/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        meta: { title: 'menu.home' }
      },
      {
        path: 'employee',
        name: 'Employee',
        component: () => import('@/views/employee/EmployeeList.vue'),
        meta: { title: 'menu.employee' }
      },
      {
        path: 'department',
        name: 'Department',
        component: () => import('@/views/department/DepartmentList.vue'),
        meta: { title: 'menu.department' }
      },
      {
        path: 'position',
        name: 'Position',
        component: () => import('@/views/position/PositionList.vue'),
        meta: { title: 'menu.position' }
      },
      {
        path: 'assignment',
        name: 'Assignment',
        component: () => import('@/views/assignment/AssignmentList.vue'),
        meta: { title: 'menu.assignment' }
      },
      {
        path: 'attendance',
        name: 'Attendance',
        component: () => import('@/views/attendance/AttendanceList.vue'),
        meta: { title: 'menu.attendance' }
      },
      {
        path: 'payroll',
        name: 'Payroll',
        component: () => import('@/views/payroll/PayrollList.vue'),
        meta: { title: 'menu.payroll' }
      },
      {
        path: 'performance',
        name: 'Performance',
        component: () => import('@/views/performance/PerformanceList.vue'),
        meta: { title: 'menu.performance' }
      },
      {
        path: 'statistics',
        name: 'Statistics',
        component: () => import('@/views/statistics/StatisticsReport.vue'),
        meta: { title: 'menu.statistics' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from) => {
  const token = getToken()
  
  // 如果访问登录页，允许访问（不再自动跳转首页）
  if (to.path === '/login') {
    return true
  }
  
  // 访问其他页面
  // 未登录则跳转登录页
  if (!token) {
    return '/login'
  }
  
  // 已登录则允许访问
  return true
})

export default router
