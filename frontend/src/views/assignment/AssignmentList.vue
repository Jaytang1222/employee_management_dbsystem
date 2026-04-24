<template>
  <div class="assignment-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          {{ t('common.add') }}
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="assignmentId" label="ID" width="80" />
        <el-table-column prop="employeeName" :label="t('assignment.employee')" width="120" />
        <el-table-column prop="deptName" :label="t('assignment.department')" width="150" />
        <el-table-column prop="positionName" :label="t('assignment.position')" width="150" />
        <el-table-column prop="startDate" :label="t('assignment.startDate')" width="120" />
        <el-table-column prop="isPrimary" :label="t('assignment.isPrimary')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.isPrimary ? 'success' : 'info'">
              {{ row.isPrimary ? t('assignment.yes') : t('assignment.no') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="changeReason" :label="t('assignment.changeReason')" show-overflow-tooltip />
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
        <el-form-item :label="t('assignment.employee')" prop="employeeId">
          <el-select 
            v-model="form.employeeId" 
            filterable
            remote
            reserve-keyword
            :remote-method="searchEmployees"
            :loading="employeeSearchLoading"
            :placeholder="t('common.pleaseSelect')" 
            style="width: 100%"
          >
            <el-option
              v-for="emp in employeeOptions"
              :key="emp.employeeId"
              :label="`${emp.name} (ID: ${emp.employeeId})`"
              :value="emp.employeeId"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('assignment.department')" prop="deptId">
          <el-select v-model="form.deptId" :placeholder="t('common.pleaseSelect')" style="width: 100%">
            <el-option
              v-for="dept in departmentOptions"
              :key="dept.deptId"
              :label="dept.deptName"
              :value="dept.deptId"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('assignment.position')" prop="positionId">
          <el-select v-model="form.positionId" :placeholder="t('common.pleaseSelect')" style="width: 100%">
            <el-option
              v-for="pos in positionOptions"
              :key="pos.positionId"
              :label="pos.positionName"
              :value="pos.positionId"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('assignment.startDate')" prop="startDate">
          <el-date-picker
            v-model="form.startDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('assignment.isPrimary')" prop="isPrimary">
          <el-switch v-model="form.isPrimary" />
        </el-form-item>
        
        <el-form-item :label="t('assignment.changeReason')" prop="changeReason">
          <el-input v-model="form.changeReason" type="textarea" :rows="3" />
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
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import {
  getAssignmentList,
  addAssignment,
  updateAssignment,
  deleteAssignment
} from '@/api/assignment'
import { getEmployeeList } from '@/api/employee'
import { getDepartmentList } from '@/api/department'
import { getPositionList, getAllPositions } from '@/api/position'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const isEdit = ref(false)
const employeeSearchLoading = ref(false)

const queryForm = reactive({ pageNum: 1, pageSize: 10 })
const form = reactive({
  assignmentId: null,
  employeeId: null,
  deptId: null,
  positionId: null,
  startDate: '',
  isPrimary: true,
  changeReason: ''
})

const tableData = ref([])
const total = ref(0)
const employeeOptions = ref([])
const departmentOptions = ref([])
const positionOptions = ref([])

const dialogTitle = computed(() => {
  return isEdit.value ? t('assignment.editAssignment') : t('assignment.addAssignment')
})

const formRules = {
  employeeId: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  deptId: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  positionId: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  startDate: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getAssignmentList(queryForm)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('Load data error:', error)
  } finally {
    loading.value = false
  }
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
    
    await deleteAssignment(row.assignmentId)
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
          await updateAssignment(form.assignmentId, form)
        } else {
          await addAssignment(form)
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
    form[key] = key === 'assignmentId' || key === 'employeeId' || key === 'deptId' || key === 'positionId' ? null : (key === 'isPrimary' ? true : '')
  })
}

const loadEmployeeOptions = async () => {
  try {
    const res = await getEmployeeList({ pageNum: 1, pageSize: 50, status: 'active' })
    employeeOptions.value = res.data.records || []
  } catch (error) {
    console.error('Load employee options error:', error)
  }
}

const searchEmployees = async (query) => {
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

const loadDepartmentOptions = async () => {
  try {
    const res = await getDepartmentList()
    departmentOptions.value = res.data || []
  } catch (error) {
    console.error('Load department options error:', error)
  }
}

const loadPositionOptions = async () => {
  try {
    const res = await getAllPositions()
    positionOptions.value = res.data || []
  } catch (error) {
    console.error('Load position options error:', error)
  }
}

onMounted(() => {
  loadData()
  loadEmployeeOptions()
  loadDepartmentOptions()
  loadPositionOptions()
})
</script>

<style scoped>
.assignment-container {
  padding: 20px;
  width: 100%;
  max-width: 100%;
}

.toolbar {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
