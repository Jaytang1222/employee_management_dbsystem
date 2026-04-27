<template>
  <div class="attendance-container">
    <el-card>
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item :label="t('attendance.date')">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            value-format="YYYY-MM-DD"
            :start-placeholder="t('common.pleaseSelect')"
            :end-placeholder="t('common.pleaseSelect')"
          />
        </el-form-item>
        
        <el-form-item :label="t('attendance.status')">
          <el-select v-model="queryForm.status" :placeholder="t('common.pleaseSelect')" clearable>
            <el-option :label="t('attendance.normal')" value="normal" />
            <el-option :label="t('attendance.late')" value="late" />
            <el-option :label="t('attendance.earlyLeave')" value="early_leave" />
            <el-option :label="t('attendance.absent')" value="absent" />
            <el-option :label="t('attendance.leave')" value="leave" />
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">
            {{ t('common.search') }}
          </el-button>
          <el-button :icon="Refresh" @click="handleReset">
            {{ t('common.reset') }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          {{ t('common.add') }}
        </el-button>
        <el-button type="success" :icon="Download" @click="handleExport">
          {{ t('common.export') }}
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="attendanceId" label="ID" width="80" />
        <el-table-column prop="employeeName" :label="t('attendance.employee')" width="120" />
        <el-table-column prop="attendanceDate" :label="t('attendance.date')" width="120" />
        <el-table-column prop="checkInTime" :label="t('attendance.checkInTime')" width="100" />
        <el-table-column prop="checkOutTime" :label="t('attendance.checkOutTime')" width="100" />
        <el-table-column prop="attendanceStatus" :label="t('attendance.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.attendanceStatus)">
              {{ getStatusText(row.attendanceStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lateMin" :label="t('attendance.lateMinutes')" width="100" />
        <el-table-column prop="overtimeHours" :label="t('attendance.overtimeHours')" width="100" />
        <el-table-column :label="t('common.operation')" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(row)">
              {{ t('common.edit') }}
            </el-button>
            <el-button type="danger" size="small" :icon="Delete" @click="handleDelete(row)">
              {{ t('common.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-model:current-page="queryForm.pageNum"
        v-model:page-size="queryForm.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="loadData"
        @current-change="loadData"
        class="pagination"
      />
    </el-card>
    
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="120px">
        <el-form-item :label="t('attendance.employee')" prop="employeeId">
          <el-select v-model="form.employeeId" :placeholder="t('common.pleaseSelect')" style="width: 100%">
            <el-option
              v-for="emp in employeeOptions"
              :key="emp.employeeId"
              :label="emp.name"
              :value="emp.employeeId"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('attendance.date')" prop="attendanceDate">
          <el-date-picker
            v-model="form.attendanceDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('attendance.checkInTime')" prop="checkInTime">
          <el-time-picker
            v-model="form.checkInTime"
            value-format="HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('attendance.checkOutTime')" prop="checkOutTime">
          <el-time-picker
            v-model="form.checkOutTime"
            value-format="HH:mm:ss"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('attendance.status')" prop="attendanceStatus">
          <el-select v-model="form.attendanceStatus" style="width: 100%">
            <el-option :label="t('attendance.normal')" value="normal" />
            <el-option :label="t('attendance.late')" value="late" />
            <el-option :label="t('attendance.earlyLeave')" value="early_leave" />
            <el-option :label="t('attendance.absent')" value="absent" />
            <el-option :label="t('attendance.leave')" value="leave" />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('attendance.remark')" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          {{ t('common.save') }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Download, Edit, Delete } from '@element-plus/icons-vue'
import {
  getAttendanceList,
  addAttendance,
  updateAttendance,
  deleteAttendance
} from '@/api/attendance'
import { getEmployeeList } from '@/api/employee'
import { exportAttendances } from '@/api/export'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const isEdit = ref(false)
const dateRange = ref([])

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  status: ''
})

const form = reactive({
  attendanceId: null,
  employeeId: null,
  attendanceDate: '',
  checkInTime: '',
  checkOutTime: '',
  attendanceStatus: 'normal',
  remark: ''
})

const tableData = ref([])
const total = ref(0)
const employeeOptions = ref([])

const dialogTitle = computed(() => {
  return isEdit.value ? t('attendance.editAttendance') : t('attendance.addAttendance')
})

const formRules = {
  employeeId: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  attendanceDate: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }]
}

const getStatusType = (status) => {
  const typeMap = {
    normal: 'success',
    late: 'warning',
    early_leave: 'warning',
    absent: 'danger',
    leave: 'info'
  }
  return typeMap[status] || 'info'
}

const getStatusText = (status) => {
  const textMap = {
    normal: t('attendance.normal'),
    late: t('attendance.late'),
    early_leave: t('attendance.earlyLeave'),
    absent: t('attendance.absent'),
    leave: t('attendance.leave')
  }
  return textMap[status] || status
}

const loadData = async () => {
  loading.value = true
  try {
    const params = { ...queryForm }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    
    const res = await getAttendanceList(params)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('Load data error:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryForm.pageNum = 1
  loadData()
}

const handleReset = () => {
  dateRange.value = []
  queryForm.status = ''
  queryForm.pageNum = 1
  loadData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(t('common.confirmDelete'), t('common.tips'), {
      confirmButtonText: t('common.confirm'),
      cancelButtonText: t('common.cancel'),
      type: 'warning'
    })
    
    await deleteAttendance(row.attendanceId)
    ElMessage.success(t('common.deleteSuccess'))
    loadData()
  } catch (error) {
    if (error !== 'cancel') console.error('Delete error:', error)
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateAttendance(form.attendanceId, form)
        } else {
          await addAttendance(form)
        }
        
        ElMessage.success(t('common.saveSuccess'))
        dialogVisible.value = false
        loadData()
      } catch (error) {
        console.error('Submit error:', error)
      } finally {
        submitLoading.value = false
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.keys(form).forEach(key => {
    form[key] = key === 'attendanceId' || key === 'employeeId' ? null : (key === 'attendanceStatus' ? 'normal' : '')
  })
}

const handleExport = async () => {
  try {
    const params = {}
    if (dateRange.value && dateRange.value.length === 2) {
      params.month = dateRange.value[0].substring(0, 7)
    }
    await exportAttendances(params)
    ElMessage.success(t('common.success'))
  } catch (error) {
    console.error('Export error:', error)
  }
}

const loadEmployeeOptions = async () => {
  try {
    const res = await getEmployeeList({ pageNum: 1, pageSize: 1000, status: 'active' })
    employeeOptions.value = res.data.records || []
  } catch (error) {
    console.error('Load employee options error:', error)
  }
}

onMounted(() => {
  loadData()
  loadEmployeeOptions()
})
</script>

<style scoped>
.attendance-container {
  padding: 24px;
  width: 100%;
  max-width: 100%;
}

.search-form {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 2px solid #76b900;
}

.toolbar {
  margin-bottom: 24px;
}

.pagination {
  margin-top: 24px;
  justify-content: flex-end;
}

@media screen and (max-width: 768px) {
  .attendance-container {
    padding: 16px;
  }
  
  .search-form :deep(.el-form-item) {
    width: 100%;
  }
  
  .toolbar {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }
  
  .toolbar :deep(.el-button) {
    width: 100%;
  }
}
</style>
