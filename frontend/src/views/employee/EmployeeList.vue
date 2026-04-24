<template>
  <div class="employee-container">
    <el-card>
      <!-- 搜索表单 -->
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item :label="t('employee.name')">
          <el-input
            v-model="queryForm.name"
            :placeholder="t('common.pleaseInput')"
            clearable
          />
        </el-form-item>
        
        <el-form-item :label="t('employee.status')">
          <el-select v-model="queryForm.status" :placeholder="t('common.pleaseSelect')" clearable>
            <el-option :label="t('employee.active')" value="active" />
            <el-option :label="t('employee.inactive')" value="inactive" />
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
      
      <!-- 操作按钮 -->
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          {{ t('common.add') }}
        </el-button>
        <el-button type="success" :icon="Download" @click="handleExport">
          {{ t('common.export') }}
        </el-button>
      </div>
      
      <!-- 表格 -->
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="employeeId" label="ID" width="80" />
        <el-table-column prop="name" :label="t('employee.name')" width="120" />
        <el-table-column prop="idCard" :label="t('employee.idCard')" width="180" />
        <el-table-column prop="gender" :label="t('employee.gender')" width="80">
          <template #default="{ row }">
            {{ row.gender === '男' ? t('employee.male') : t('employee.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" :label="t('employee.phone')" width="130" />
        <el-table-column prop="email" :label="t('employee.email')" width="200" />
        <el-table-column prop="hireDate" :label="t('employee.hireDate')" width="120" />
        <el-table-column prop="status" :label="t('common.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? t('employee.active') : t('employee.inactive') }}
            </el-tag>
          </template>
        </el-table-column>
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
      
      <!-- 分页 -->
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
    
    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item :label="t('employee.name')" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        
        <el-form-item :label="t('employee.idCard')" prop="idCard">
          <el-input v-model="form.idCard" />
        </el-form-item>
        
        <el-form-item :label="t('employee.gender')" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio label="男">{{ t('employee.male') }}</el-radio>
            <el-radio label="女">{{ t('employee.female') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item :label="t('employee.birthDate')" prop="birthDate">
          <el-date-picker
            v-model="form.birthDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('employee.phone')" prop="phone">
          <el-input v-model="form.phone" />
        </el-form-item>
        
        <el-form-item :label="t('employee.email')" prop="email">
          <el-input v-model="form.email" />
        </el-form-item>
        
        <el-form-item :label="t('employee.address')" prop="address">
          <el-input v-model="form.address" type="textarea" />
        </el-form-item>
        
        <el-form-item :label="t('employee.hireDate')" prop="hireDate">
          <el-date-picker
            v-model="form.hireDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('employee.educationLevel')" prop="educationLevel">
          <el-select v-model="form.educationLevel" style="width: 100%">
            <el-option :label="t('employee.highSchool')" value="高中" />
            <el-option :label="t('employee.college')" value="大专" />
            <el-option :label="t('employee.bachelor')" value="本科" />
            <el-option :label="t('employee.master')" value="硕士" />
            <el-option :label="t('employee.doctor')" value="博士" />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('employee.emergencyContact')" prop="emergencyContact">
          <el-input v-model="form.emergencyContact" />
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
  getEmployeeList,
  addEmployee,
  updateEmployee,
  deleteEmployee
} from '@/api/employee'
import { exportEmployees } from '@/api/export'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const isEdit = ref(false)

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  status: ''
})

const form = reactive({
  employeeId: null,
  name: '',
  idCard: '',
  gender: '男',
  birthDate: '',
  phone: '',
  email: '',
  address: '',
  hireDate: '',
  educationLevel: '',
  emergencyContact: ''
})

const tableData = ref([])
const total = ref(0)

const dialogTitle = computed(() => {
  return isEdit.value ? t('employee.editEmployee') : t('employee.addEmployee')
})

const formRules = {
  name: [{ required: true, message: () => t('common.pleaseInput'), trigger: 'blur' }],
  idCard: [
    { required: true, message: () => t('common.pleaseInput'), trigger: 'blur' },
    { pattern: /^\d{17}[\dXx]$/, message: () => t('employee.idCardFormat'), trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: () => t('employee.phoneFormat'), trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: () => t('employee.emailFormat'), trigger: 'blur' }
  ],
  hireDate: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getEmployeeList(queryForm)
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
  queryForm.name = ''
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
    await ElMessageBox.confirm(
      t('common.confirmDelete'),
      t('common.tips'),
      {
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel'),
        type: 'warning'
      }
    )
    
    await deleteEmployee(row.employeeId)
    ElMessage.success(t('common.deleteSuccess'))
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Delete error:', error)
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitLoading.value = true
      try {
        if (isEdit.value) {
          await updateEmployee(form.employeeId, form)
        } else {
          await addEmployee(form)
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
    form[key] = key === 'gender' ? '男' : (key === 'employeeId' ? null : '')
  })
}

const handleExport = async () => {
  try {
    await exportEmployees(queryForm)
    ElMessage.success(t('common.success'))
  } catch (error) {
    console.error('Export error:', error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.employee-container {
  padding: 20px;
  width: 100%;
  max-width: 100%;
}

.search-form {
  margin-bottom: 20px;
}

.toolbar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}

@media screen and (max-width: 768px) {
  .employee-container {
    padding: 10px;
  }
  
  .search-form :deep(.el-form-item) {
    width: 100%;
  }
}
</style>
