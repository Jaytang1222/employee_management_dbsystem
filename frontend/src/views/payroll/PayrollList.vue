<template>
  <div class="payroll-container">
    <el-card>
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item :label="t('payroll.month')">
          <el-date-picker
            v-model="queryForm.payMonth"
            type="month"
            value-format="YYYY-MM"
            :placeholder="t('common.pleaseSelect')"
          />
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
        <el-table-column prop="payrollId" label="ID" width="80" />
        <el-table-column prop="employeeName" :label="t('payroll.employee')" width="120" />
        <el-table-column prop="payMonth" :label="t('payroll.month')" width="100" />
        <el-table-column prop="basePay" :label="t('payroll.basePay')" width="100" />
        <el-table-column prop="allowance" :label="t('payroll.allowance')" width="100" />
        <el-table-column prop="bonus" :label="t('payroll.bonus')" width="100" />
        <el-table-column prop="deduction" :label="t('payroll.deduction')" width="100" />
        <el-table-column prop="netPay" :label="t('payroll.netPay')" width="120">
          <template #default="{ row }">
            <span style="color: #67c23a; font-weight: bold">{{ row.netPay }}</span>
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
      
      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.pageSize"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="loadData"
        @current-change="loadData"
        class="pagination"
      />
    </el-card>
    
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="120px">
        <el-form-item :label="t('payroll.employee')" prop="employeeId">
          <el-select v-model="form.employeeId" :placeholder="t('common.pleaseSelect')" style="width: 100%">
            <el-option
              v-for="emp in employeeOptions"
              :key="emp.employeeId"
              :label="emp.name"
              :value="emp.employeeId"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('payroll.month')" prop="payMonth">
          <el-date-picker
            v-model="form.payMonth"
            type="month"
            value-format="YYYY-MM"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('payroll.basePay')" prop="basePay">
          <el-input-number v-model="form.basePay" :min="0" style="width: 100%" @change="calculateNetPay" />
        </el-form-item>
        
        <el-form-item :label="t('payroll.allowance')" prop="allowance">
          <el-input-number v-model="form.allowance" :min="0" style="width: 100%" @change="calculateNetPay" />
        </el-form-item>
        
        <el-form-item :label="t('payroll.bonus')" prop="bonus">
          <el-input-number v-model="form.bonus" :min="0" style="width: 100%" @change="calculateNetPay" />
        </el-form-item>
        
        <el-form-item :label="t('payroll.deduction')" prop="deduction">
          <el-input-number v-model="form.deduction" :min="0" style="width: 100%" @change="calculateNetPay" />
        </el-form-item>
        
        <el-form-item :label="t('payroll.socialInsurance')" prop="socialInsurance">
          <el-input-number v-model="form.socialInsurance" :min="0" style="width: 100%" @change="calculateNetPay" />
        </el-form-item>
        
        <el-form-item :label="t('payroll.tax')" prop="tax">
          <el-input-number v-model="form.tax" :min="0" style="width: 100%" @change="calculateNetPay" />
        </el-form-item>
        
        <el-form-item :label="t('payroll.netPay')" prop="netPay">
          <el-input-number v-model="form.netPay" :min="0" style="width: 100%" disabled />
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
  getPayrollList,
  addPayroll,
  updatePayroll,
  deletePayroll
} from '@/api/payroll'
import { getEmployeeList } from '@/api/employee'
import { exportPayrolls } from '@/api/export'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const isEdit = ref(false)

const queryForm = reactive({
  page: 1,
  pageSize: 10,
  payMonth: ''
})

const form = reactive({
  payrollId: null,
  employeeId: null,
  payMonth: '',
  basePay: 0,
  allowance: 0,
  bonus: 0,
  deduction: 0,
  socialInsurance: 0,
  tax: 0,
  netPay: 0
})

const tableData = ref([])
const total = ref(0)
const employeeOptions = ref([])

const dialogTitle = computed(() => {
  return isEdit.value ? t('payroll.editPayroll') : t('payroll.addPayroll')
})

const formRules = {
  employeeId: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  payMonth: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  basePay: [{ required: true, message: () => t('common.pleaseInput'), trigger: 'blur' }]
}

const calculateNetPay = () => {
  form.netPay = form.basePay + form.allowance + form.bonus - form.deduction - form.socialInsurance - form.tax
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPayrollList(queryForm)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('Load data error:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  queryForm.page = 1
  loadData()
}

const handleReset = () => {
  queryForm.payMonth = ''
  queryForm.page = 1
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
    
    await deletePayroll(row.payrollId)
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
          await updatePayroll(form.payrollId, form)
        } else {
          await addPayroll(form)
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
    form[key] = key === 'payrollId' || key === 'employeeId' ? null : (typeof form[key] === 'number' ? 0 : '')
  })
}

const handleExport = async () => {
  try {
    await exportPayrolls({ month: queryForm.payMonth })
    ElMessage.success(t('common.success'))
  } catch (error) {
    console.error('Export error:', error)
  }
}

const loadEmployeeOptions = async () => {
  try {
    const res = await getEmployeeList({ page: 1, pageSize: 1000, status: 'active' })
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
.payroll-container {
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
</style>
