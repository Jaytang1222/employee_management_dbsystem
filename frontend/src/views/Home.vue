<template>
  <div class="home-container">
    <h2>{{ t('menu.home') }}</h2>
    
    <el-row :gutter="20" class="stats-row">
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <el-icon class="stats-icon" color="#409eff"><User /></el-icon>
            <div class="stats-info">
              <div class="stats-value">{{ stats.totalEmployees }}</div>
              <div class="stats-label">{{ t('statistics.totalEmployees') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <el-icon class="stats-icon" color="#67c23a"><OfficeBuilding /></el-icon>
            <div class="stats-info">
              <div class="stats-value">{{ stats.totalDepartments }}</div>
              <div class="stats-label">{{ t('statistics.totalDepartments') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <el-icon class="stats-icon" color="#e6a23c"><Calendar /></el-icon>
            <div class="stats-info">
              <div class="stats-value">{{ stats.todayAttendance }}</div>
              <div class="stats-label">{{ t('statistics.todayAttendance') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :sm="12" :md="6">
        <el-card class="stats-card">
          <div class="stats-content">
            <el-icon class="stats-icon" color="#f56c6c"><TrendCharts /></el-icon>
            <div class="stats-info">
              <div class="stats-value">{{ stats.avgPerformance }}</div>
              <div class="stats-label">{{ t('performance.score') }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20" class="content-row">
      <el-col :xs="24" :md="12">
        <el-card>
          <template #header>
            <span>{{ t('statistics.employeeByDept') }}</span>
          </template>
          <el-table :data="employeeByDept" style="width: 100%">
            <el-table-column
              prop="deptName"
              :label="t('statistics.departmentName')"
            />
            <el-table-column
              prop="employeeCount"
              :label="t('statistics.employeeCount')"
              width="120"
            />
          </el-table>
        </el-card>
      </el-col>
      
      <el-col :xs="24" :md="12">
        <el-card>
          <template #header>
            <span>{{ t('common.tips') }}</span>
          </template>
          <div class="welcome-text">
            <p>{{ t('login.title') }}</p>
            <p>{{ t('common.success') }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { User, OfficeBuilding, Calendar, TrendCharts } from '@element-plus/icons-vue'
import { getEmployeeByDept } from '@/api/statistics'

const { t } = useI18n()

const stats = ref({
  totalEmployees: 0,
  totalDepartments: 0,
  todayAttendance: 0,
  avgPerformance: 0
})

const employeeByDept = ref([])

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
})
</script>

<style scoped>
.home-container {
  padding: 24px;
  width: 100%;
  max-width: 100%;
}

.home-container h2 {
  margin: 0 0 32px 0;
  color: #000000;
  font-size: 36px;
  font-weight: 700;
  line-height: 1.25;
  text-transform: uppercase;
  letter-spacing: 1px;
  border-bottom: 2px solid #76b900;
  padding-bottom: 16px;
}

.stats-row {
  margin-bottom: 32px;
}

.stats-card {
  margin-bottom: 20px;
  border-radius: 2px;
  border: 1px solid #5e5e5e;
  box-shadow: rgba(0, 0, 0, 0.3) 0px 0px 5px 0px;
  transition: all 0.3s;
}

.stats-card:hover {
  border-color: #76b900;
  box-shadow: rgba(0, 0, 0, 0.5) 0px 0px 10px 0px;
}

.stats-card :deep(.el-card__body) {
  padding: 24px;
}

.stats-content {
  display: flex;
  align-items: center;
  gap: 24px;
}

.stats-icon {
  font-size: 56px;
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 36px;
  font-weight: 700;
  color: #000000;
  line-height: 1.25;
  margin-bottom: 8px;
}

.stats-label {
  font-size: 15px;
  font-weight: 700;
  color: #757575;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.content-row {
  margin-top: 32px;
}

.content-row :deep(.el-card) {
  border-radius: 2px;
  border: 1px solid #5e5e5e;
  box-shadow: rgba(0, 0, 0, 0.3) 0px 0px 5px 0px;
  transition: all 0.3s;
}

.content-row :deep(.el-card:hover) {
  border-color: #76b900;
}

.content-row :deep(.el-card__header) {
  background-color: #000000;
  color: #ffffff;
  font-weight: 700;
  font-size: 18px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  border-bottom: 2px solid #76b900;
  padding: 16px 24px;
}

.content-row :deep(.el-table) {
  font-family: Arial, Helvetica, sans-serif;
}

.content-row :deep(.el-table th) {
  background-color: #1a1a1a;
  color: #ffffff;
  font-weight: 700;
  font-size: 14px;
  text-transform: uppercase;
}

.content-row :deep(.el-table td) {
  font-size: 15px;
  color: #000000;
}

.content-row :deep(.el-table tr:hover > td) {
  background-color: rgba(118, 185, 0, 0.1);
}

.welcome-text {
  padding: 32px;
  text-align: center;
}

.welcome-text p {
  margin: 16px 0;
  font-size: 18px;
  font-weight: 700;
  color: #000000;
  line-height: 1.67;
}

@media screen and (max-width: 768px) {
  .home-container {
    padding: 16px;
  }
  
  .home-container h2 {
    font-size: 24px;
    margin-bottom: 24px;
  }
  
  .stats-value {
    font-size: 24px;
  }
  
  .stats-icon {
    font-size: 40px;
  }
  
  .stats-content {
    gap: 16px;
  }
}
</style>
