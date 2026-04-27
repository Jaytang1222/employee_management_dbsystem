<template>
  <div class="statistics-container">
    <el-card>
      <el-tabs v-model="activeTab">
        <el-tab-pane :label="t('statistics.employeeStats')" name="employee">
          <el-table :data="employeeData" v-loading="employeeLoading" stripe>
            <el-table-column prop="deptName" :label="t('statistics.departmentName')" />
            <el-table-column prop="employeeCount" :label="t('statistics.employeeCount')" width="150" />
          </el-table>
        </el-tab-pane>
        
        <el-tab-pane :label="t('statistics.attendanceStats')" name="attendance">
          <el-form :inline="true" class="search-form">
            <el-form-item :label="t('attendance.employee')">
              <el-select 
                v-model="attendanceEmployeeId" 
                filterable
                remote
                reserve-keyword
                :remote-method="searchEmployeesForAttendance"
                :loading="employeeSearchLoading"
                :placeholder="t('common.pleaseSelect')"
                style="width: 200px"
              >
                <el-option
                  v-for="emp in employeeOptions"
                  :key="emp.employeeId"
                  :label="`${emp.name} (ID: ${emp.employeeId})`"
                  :value="emp.employeeId"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="t('attendance.selectMonth')">
              <el-date-picker
                v-model="attendanceMonth"
                type="month"
                value-format="YYYY-MM"
                :placeholder="t('common.pleaseSelect')"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadAttendanceReport">
                {{ t('common.search') }}
              </el-button>
            </el-form-item>
          </el-form>
          
          <el-table :data="attendanceData" v-loading="attendanceLoading" stripe>
            <el-table-column prop="employeeName" :label="t('attendance.employee')" width="120" />
            <el-table-column prop="attendanceDays" :label="t('attendance.attendanceDays')" width="100" />
            <el-table-column prop="lateTimes" :label="t('attendance.lateTimes')" width="100" />
            <el-table-column prop="earlyLeaveTimes" :label="t('attendance.earlyLeaveTimes')" width="100" />
            <el-table-column prop="totalOvertimeHours" :label="t('attendance.totalOvertimeHours')" width="120" />
            <el-table-column prop="absentDays" :label="t('attendance.absentDays')" width="100" />
          </el-table>
        </el-tab-pane>
        
        <el-tab-pane :label="t('statistics.performanceStats')" name="performance">
          <el-form :inline="true" class="search-form">
            <el-form-item :label="t('performance.evalDate')">
              <el-date-picker
                v-model="performanceDate"
                type="date"
                value-format="YYYY-MM-DD"
                :placeholder="t('common.pleaseSelect')"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="loadPerformanceRanking">
                {{ t('common.search') }}
              </el-button>
              <el-button type="success" @click="handleExportPerformanceRanking">
                {{ t('common.export') }}
              </el-button>
            </el-form-item>
          </el-form>
          
          <el-table :data="performanceData" v-loading="performanceLoading" stripe>
            <el-table-column prop="ranking" :label="t('performance.rank')" width="80" align="center" />
            <el-table-column prop="employeeName" :label="t('performance.employee')" width="150" />
            <el-table-column prop="score" :label="t('performance.score')" width="100" align="center" />
            <el-table-column prop="grade" :label="t('performance.grade')" width="120" align="center">
              <template #default="{ row }">
                <el-tag :type="getGradeType(row.grade)">
                  {{ getGradeText(row.grade) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import {
  getEmployeeByDept,
  getAttendanceReport,
  getPerformanceRanking
} from '@/api/statistics'
import { getEmployeeList } from '@/api/employee'
import { exportPerformanceRanking } from '@/api/export'

const { t } = useI18n()

const activeTab = ref('employee')
const employeeLoading = ref(false)
const attendanceLoading = ref(false)
const performanceLoading = ref(false)
const employeeSearchLoading = ref(false)

const employeeData = ref([])
const attendanceData = ref([])
const performanceData = ref([])
const employeeOptions = ref([])

const attendanceEmployeeId = ref(null)
const attendanceMonth = ref('')
const performanceDate = ref('')

const getGradeType = (grade) => {
  const typeMap = { A: 'success', B: '', C: 'warning', D: 'danger' }
  return typeMap[grade] || ''
}

const getGradeText = (grade) => {
  const textMap = {
    A: t('performance.gradeA'),
    B: t('performance.gradeB'),
    C: t('performance.gradeC'),
    D: t('performance.gradeD')
  }
  return textMap[grade] || grade
}

const loadEmployeeByDept = async () => {
  employeeLoading.value = true
  try {
    const res = await getEmployeeByDept()
    employeeData.value = res.data || []
  } catch (error) {
    console.error('Load employee data error:', error)
  } finally {
    employeeLoading.value = false
  }
}

const loadAttendanceReport = async () => {
  if (!attendanceEmployeeId.value || !attendanceMonth.value) {
    return
  }
  
  attendanceLoading.value = true
  try {
    const res = await getAttendanceReport({ 
      employeeId: attendanceEmployeeId.value,
      month: attendanceMonth.value 
    })
    // 将单个报表包装成数组显示
    attendanceData.value = res.data ? [res.data] : []
  } catch (error) {
    console.error('Load attendance data error:', error)
  } finally {
    attendanceLoading.value = false
  }
}

const loadPerformanceRanking = async () => {
  if (!performanceDate.value) return
  
  performanceLoading.value = true
  try {
    const res = await getPerformanceRanking({ evalDate: performanceDate.value })
    performanceData.value = res.data || []
  } catch (error) {
    console.error('Load performance data error:', error)
  } finally {
    performanceLoading.value = false
  }
}

const loadEmployeeOptions = async () => {
  try {
    const res = await getEmployeeList({ pageNum: 1, pageSize: 50, status: 'active' })
    employeeOptions.value = res.data.records || []
  } catch (error) {
    console.error('Load employee options error:', error)
  }
}

const searchEmployeesForAttendance = async (query) => {
  if (query) {
    employeeSearchLoading.value = true
    try {
      const res = await getEmployeeList({ 
        pageNum: 1, 
        pageSize: 50, 
        name: query,
        status: 'active' 
      })
      employeeOptions.value = res.data.records || []
    } catch (error) {
      console.error('Search employees error:', error)
    } finally {
      employeeSearchLoading.value = false
    }
  } else {
    loadEmployeeOptions()
  }
}

const handleExportPerformanceRanking = async () => {
  if (!performanceDate.value) {
    ElMessage.warning(t('common.pleaseSelect'))
    return
  }
  
  try {
    await exportPerformanceRanking({ evalDate: performanceDate.value })
    ElMessage.success(t('common.exportSuccess'))
  } catch (error) {
    console.error('Export error:', error)
  }
}

onMounted(() => {
  loadEmployeeByDept()
  loadEmployeeOptions()
})
</script>

<style scoped>
.statistics-container {
  padding: 24px;
  width: 100%;
  max-width: 100%;
}

.search-form {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 2px solid #76b900;
}

@media screen and (max-width: 768px) {
  .statistics-container {
    padding: 16px;
  }
  
  .search-form :deep(.el-form-item) {
    width: 100%;
  }
}
</style>
