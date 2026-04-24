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
  padding: 20px;
  width: 100%;
  max-width: 100%;
}

.home-container h2 {
  margin: 0 0 20px 0;
  color: #333;
}

.stats-row {
  margin-bottom: 20px;
}

.stats-card {
  margin-bottom: 20px;
}

.stats-content {
  display: flex;
  align-items: center;
  gap: 20px;
}

.stats-icon {
  font-size: 48px;
}

.stats-info {
  flex: 1;
}

.stats-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  line-height: 1;
  margin-bottom: 8px;
}

.stats-label {
  font-size: 14px;
  color: #999;
}

.content-row {
  margin-top: 20px;
}

.welcome-text {
  padding: 20px;
  text-align: center;
}

.welcome-text p {
  margin: 10px 0;
  font-size: 16px;
  color: #666;
}

@media screen and (max-width: 768px) {
  .home-container {
    padding: 10px;
  }
  
  .stats-value {
    font-size: 24px;
  }
  
  .stats-icon {
    font-size: 36px;
  }
}
</style>
