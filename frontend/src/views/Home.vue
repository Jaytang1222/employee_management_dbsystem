<template>
  <div class="home-container">
    <!-- 页面标题区 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">{{ t('home.dashboard') }}</h1>
        <p class="page-subtitle">{{ t('home.overview') }}</p>
      </div>
      <div class="header-right">
        <span class="last-update">{{ t('home.lastUpdated') }}: {{ currentTime }}</span>
      </div>
    </div>

    <!-- KPI 卡片区域 -->
    <el-row :gutter="20" class="kpi-row">
      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card" @click="navigateTo('/employee')">
          <div class="kpi-icon-wrapper blue">
            <el-icon class="kpi-icon"><User /></el-icon>
          </div>
          <div class="kpi-content">
            <div class="kpi-label">{{ t('statistics.totalEmployees') }}</div>
            <div class="kpi-value">{{ stats.totalEmployees }}</div>
            <div class="kpi-trend positive">
              <el-icon><TrendCharts /></el-icon>
              <span>+12% {{ t('home.vsLastMonth') }}</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card" @click="navigateTo('/department')">
          <div class="kpi-icon-wrapper green">
            <el-icon class="kpi-icon"><OfficeBuilding /></el-icon>
          </div>
          <div class="kpi-content">
            <div class="kpi-label">{{ t('statistics.totalDepartments') }}</div>
            <div class="kpi-value">{{ stats.totalDepartments }}</div>
            <div class="kpi-trend neutral">
              <el-icon><Minus /></el-icon>
              <span>{{ t('home.noChange') }}</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card" @click="navigateTo('/attendance')">
          <div class="kpi-icon-wrapper orange">
            <el-icon class="kpi-icon"><Calendar /></el-icon>
          </div>
          <div class="kpi-content">
            <div class="kpi-label">{{ t('statistics.todayAttendance') }}</div>
            <div class="kpi-value">{{ stats.todayAttendance }}</div>
            <div class="kpi-trend positive">
              <el-icon><Top /></el-icon>
              <span>95% {{ t('home.attendanceRate') }}</span>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="6">
        <div class="kpi-card" @click="navigateTo('/performance')">
          <div class="kpi-icon-wrapper purple">
            <el-icon class="kpi-icon"><TrendCharts /></el-icon>
          </div>
          <div class="kpi-content">
            <div class="kpi-label">{{ t('home.avgScore') }}</div>
            <div class="kpi-value">{{ stats.avgPerformance }}</div>
            <div class="kpi-trend positive">
              <el-icon><Top /></el-icon>
              <span>+3.5 {{ t('home.vsLastQuarter') }}</span>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 数据可视化与信息区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 部门员工分布 -->
      <el-col :xs="24" :lg="12">
        <div class="data-card">
          <div class="card-header">
            <div class="card-title">
              <el-icon><DataAnalysis /></el-icon>
              <span>{{ t('statistics.employeeByDept') }}</span>
            </div>
            <el-button text @click="navigateTo('/statistics')">
              {{ t('home.viewAll') }} <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
          <div class="card-body">
            <div class="table-wrapper">
              <table class="modern-table">
                <thead>
                  <tr>
                    <th>{{ t('statistics.departmentName') }}</th>
                    <th class="text-right">{{ t('statistics.employeeCount') }}</th>
                    <th class="text-right">{{ t('home.percentage') }}</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="dept in employeeByDept" :key="dept.deptName">
                    <td>
                      <div class="dept-name">
                        <span class="dept-dot"></span>
                        {{ dept.deptName }}
                      </div>
                    </td>
                    <td class="text-right">{{ dept.employeeCount }}</td>
                    <td class="text-right">
                      <span class="percentage">{{ calculatePercentage(dept.employeeCount) }}%</span>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </el-col>

      <!-- 最近活动 -->
      <el-col :xs="24" :lg="12">
        <div class="data-card">
          <div class="card-header">
            <div class="card-title">
              <el-icon><Bell /></el-icon>
              <span>{{ t('home.recentActivities') }}</span>
            </div>
            <el-button text @click="navigateTo('/employee')">
              {{ t('home.viewAll') }} <el-icon><ArrowRight /></el-icon>
            </el-button>
          </div>
          <div class="card-body">
            <div class="activity-list">
              <div class="activity-item">
                <div class="activity-icon success">
                  <el-icon><UserFilled /></el-icon>
                </div>
                <div class="activity-content">
                  <div class="activity-title">{{ t('home.newEmployeeOnboarded') }}</div>
                  <div class="activity-desc">Zhang Wei {{ t('home.joinedDepartment') }} Engineering Department</div>
                  <div class="activity-time">2 {{ t('home.hoursAgo') }}</div>
                </div>
              </div>
              <div class="activity-item">
                <div class="activity-icon warning">
                  <el-icon><Clock /></el-icon>
                </div>
                <div class="activity-content">
                  <div class="activity-title">{{ t('home.attendanceAlert') }}</div>
                  <div class="activity-desc">5 {{ t('home.employeesMarkedLate') }}</div>
                  <div class="activity-time">4 {{ t('home.hoursAgo') }}</div>
                </div>
              </div>
              <div class="activity-item">
                <div class="activity-icon info">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="activity-content">
                  <div class="activity-title">{{ t('home.performanceReviewCompleted') }}</div>
                  <div class="activity-desc">Q4 2025 {{ t('home.reviewsFinalized') }} 45 employees</div>
                  <div class="activity-time">1 {{ t('home.daysAgo') }}</div>
                </div>
              </div>
              <div class="activity-item">
                <div class="activity-icon primary">
                  <el-icon><Promotion /></el-icon>
                </div>
                <div class="activity-content">
                  <div class="activity-title">{{ t('home.departmentRestructure') }}</div>
                  <div class="activity-desc">Marketing {{ t('home.teamExpanded') }} 12 {{ t('home.members') }}</div>
                  <div class="activity-time">2 {{ t('home.daysAgo') }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 快速操作区域 -->
    <div class="quick-actions">
      <div class="section-title">{{ t('home.quickActions') }}</div>
      <el-row :gutter="16" class="action-row">
        <el-col :xs="12" :sm="6" :lg="3">
          <div class="action-card" @click="navigateTo('/employee')">
            <el-icon class="action-icon"><Plus /></el-icon>
            <span class="action-label">{{ t('home.addEmployee') }}</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :lg="3">
          <div class="action-card" @click="navigateTo('/attendance')">
            <el-icon class="action-icon"><Calendar /></el-icon>
            <span class="action-label">{{ t('home.markAttendance') }}</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :lg="3">
          <div class="action-card" @click="navigateTo('/payroll')">
            <el-icon class="action-icon"><Wallet /></el-icon>
            <span class="action-label">{{ t('home.processPayroll') }}</span>
          </div>
        </el-col>
        <el-col :xs="12" :sm="6" :lg="3">
          <div class="action-card" @click="navigateTo('/statistics')">
            <el-icon class="action-icon"><DataLine /></el-icon>
            <span class="action-label">{{ t('home.viewReports') }}</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { 
  User, 
  OfficeBuilding, 
  Calendar, 
  TrendCharts,
  DataAnalysis,
  Bell,
  ArrowRight,
  UserFilled,
  Clock,
  Document,
  Promotion,
  Plus,
  Wallet,
  DataLine,
  Top,
  Minus
} from '@element-plus/icons-vue'
import { getEmployeeByDept } from '@/api/statistics'

const { t } = useI18n()
const router = useRouter()

const stats = ref({
  totalEmployees: 0,
  totalDepartments: 0,
  todayAttendance: 0,
  avgPerformance: 0
})

const employeeByDept = ref([])
const currentTime = ref('')

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleString('en-US', { 
    month: 'short', 
    day: 'numeric', 
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const calculatePercentage = (count) => {
  if (stats.value.totalEmployees === 0) return 0
  return ((count / stats.value.totalEmployees) * 100).toFixed(1)
}

const navigateTo = (path) => {
  router.push(path)
}

const loadStatistics = async () => {
  try {
    const res = await getEmployeeByDept()
    employeeByDept.value = res.data || []
    
    // 计算统计数据
    stats.value.totalDepartments = employeeByDept.value.length
    stats.value.totalEmployees = employeeByDept.value.reduce(
      (sum, item) => sum + (item.employeeCount || 0),
      0
    )
    stats.value.todayAttendance = Math.floor(stats.value.totalEmployees * 0.95)
    stats.value.avgPerformance = 85
  } catch (error) {
    console.error('Load statistics error:', error)
  }
}

onMounted(() => {
  loadStatistics()
  updateTime()
  setInterval(updateTime, 60000) // 每分钟更新一次时间
})
</script>

<style scoped>
.home-container {
  padding: 24px;
  width: 100%;
  max-width: 100%;
  background: #f5f7fa;
  min-height: calc(100vh - 60px);
}

/* 页面标题区 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 32px;
}

.header-left {
  flex: 1;
}

.page-title {
  margin: 0;
  font-size: 32px;
  font-weight: 700;
  color: #1a1a1a;
  letter-spacing: -0.5px;
}

.page-subtitle {
  margin: 4px 0 0 0;
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
}

.last-update {
  font-size: 13px;
  color: #9ca3af;
  font-weight: 500;
}

/* KPI 卡片 */
.kpi-row {
  margin-bottom: 24px;
}

.kpi-card {
  background: #ffffff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 20px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #e5e7eb;
  margin-bottom: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.kpi-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 24px rgba(0, 0, 0, 0.1);
  border-color: #76b900;
}

.kpi-icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.kpi-icon-wrapper.blue {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.kpi-icon-wrapper.green {
  background: linear-gradient(135deg, #76b900 0%, #5a8f00 100%);
}

.kpi-icon-wrapper.orange {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.kpi-icon-wrapper.purple {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.kpi-icon {
  font-size: 28px;
  color: #ffffff;
}

.kpi-content {
  flex: 1;
}

.kpi-label {
  font-size: 13px;
  color: #6b7280;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 8px;
}

.kpi-value {
  font-size: 32px;
  font-weight: 700;
  color: #1a1a1a;
  line-height: 1;
  margin-bottom: 8px;
}

.kpi-trend {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  font-weight: 600;
}

.kpi-trend.positive {
  color: #10b981;
}

.kpi-trend.negative {
  color: #ef4444;
}

.kpi-trend.neutral {
  color: #6b7280;
}

.kpi-trend .el-icon {
  font-size: 14px;
}

/* 数据卡片 */
.content-row {
  margin-bottom: 24px;
}

.data-card {
  background: #ffffff;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s;
  margin-bottom: 20px;
}

.data-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f3f4f6;
}

.card-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 16px;
  font-weight: 700;
  color: #1a1a1a;
}

.card-title .el-icon {
  font-size: 20px;
  color: #76b900;
}

.card-header .el-button {
  font-size: 13px;
  font-weight: 600;
  color: #6b7280;
  padding: 0;
}

.card-header .el-button:hover {
  color: #76b900;
}

.card-body {
  padding: 24px;
}

/* 现代表格 */
.table-wrapper {
  overflow-x: auto;
}

.modern-table {
  width: 100%;
  border-collapse: collapse;
}

.modern-table thead th {
  text-align: left;
  padding: 12px 16px;
  font-size: 12px;
  font-weight: 700;
  color: #6b7280;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 2px solid #f3f4f6;
}

.modern-table tbody td {
  padding: 16px;
  font-size: 14px;
  color: #1a1a1a;
  border-bottom: 1px solid #f3f4f6;
}

.modern-table tbody tr:last-child td {
  border-bottom: none;
}

.modern-table tbody tr:hover {
  background: #f9fafb;
}

.text-right {
  text-align: right !important;
}

.dept-name {
  display: flex;
  align-items: center;
  gap: 10px;
  font-weight: 600;
}

.dept-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #76b900;
}

.percentage {
  display: inline-block;
  padding: 4px 10px;
  background: #f0fdf4;
  color: #16a34a;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 700;
}

/* 活动列表 */
.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  gap: 16px;
  padding: 16px;
  border-radius: 8px;
  transition: all 0.2s;
}

.activity-item:hover {
  background: #f9fafb;
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  font-size: 18px;
}

.activity-icon.success {
  background: #d1fae5;
  color: #10b981;
}

.activity-icon.warning {
  background: #fef3c7;
  color: #f59e0b;
}

.activity-icon.info {
  background: #dbeafe;
  color: #3b82f6;
}

.activity-icon.primary {
  background: #e0e7ff;
  color: #6366f1;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 4px;
}

.activity-desc {
  font-size: 13px;
  color: #6b7280;
  margin-bottom: 6px;
}

.activity-time {
  font-size: 12px;
  color: #9ca3af;
  font-weight: 500;
}

/* 快速操作 */
.quick-actions {
  margin-top: 32px;
}

.section-title {
  font-size: 18px;
  font-weight: 700;
  color: #1a1a1a;
  margin-bottom: 16px;
  letter-spacing: -0.3px;
}

.action-row {
  margin-bottom: 0;
}

.action-card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 16px;
}

.action-card:hover {
  border-color: #76b900;
  background: #f0fdf4;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(118, 185, 0, 0.15);
}

.action-icon {
  font-size: 28px;
  color: #76b900;
}

.action-label {
  font-size: 13px;
  font-weight: 600;
  color: #374151;
  text-align: center;
}

/* 响应式 */
@media screen and (max-width: 768px) {
  .home-container {
    padding: 16px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .page-title {
    font-size: 24px;
  }

  .kpi-card {
    padding: 20px;
  }

  .kpi-icon-wrapper {
    width: 48px;
    height: 48px;
  }

  .kpi-icon {
    font-size: 24px;
  }

  .kpi-value {
    font-size: 28px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
</style>
