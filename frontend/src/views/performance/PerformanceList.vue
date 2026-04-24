<template>
  <div class="performance-container">
    <el-card>
      <el-form :model="queryForm" :inline="true" class="search-form">
        <el-form-item :label="t('performance.grade')">
          <el-select v-model="queryForm.grade" :placeholder="t('common.pleaseSelect')" clearable>
            <el-option label="A" value="A" />
            <el-option label="B" value="B" />
            <el-option label="C" value="C" />
            <el-option label="D" value="D" />
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
        <el-button type="info" :icon="TrendCharts" @click="showRanking">
          {{ t('performance.ranking') }}
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="performanceId" label="ID" width="80" />
        <el-table-column prop="employeeName" :label="t('performance.employee')" width="120" />
        <el-table-column prop="evalDate" :label="t('performance.evalDate')" width="120" />
        <el-table-column prop="score" :label="t('performance.score')" width="100" />
        <el-table-column prop="grade" :label="t('performance.grade')" width="100">
          <template #default="{ row }">
            <el-tag :type="getGradeType(row.grade)">
              {{ getGradeText(row.grade) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="comment" :label="t('performance.comment')" show-overflow-tooltip />
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
        <el-form-item :label="t('performance.employee')" prop="employeeId">
          <el-select v-model="form.employeeId" :placeholder="t('common.pleaseSelect')" style="width: 100%">
            <el-option
              v-for="emp in employeeOptions"
              :key="emp.employeeId"
              :label="emp.name"
              :value="emp.employeeId"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('performance.evalDate')" prop="evalDate">
          <el-date-picker
            v-model="form.evalDate"
            type="date"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-form-item :label="t('performance.score')" prop="score">
          <el-input-number v-model="form.score" :min="0" :max="100" style="width: 100%" @change="autoSetGrade" />
        </el-form-item>
        
        <el-form-item :label="t('performance.grade')" prop="grade">
          <el-select v-model="form.grade" style="width: 100%">
            <el-option :label="t('performance.gradeA')" value="A" />
            <el-option :label="t('performance.gradeB')" value="B" />
            <el-option :label="t('performance.gradeC')" value="C" />
            <el-option :label="t('performance.gradeD')" value="D" />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('performance.comment')" prop="comment">
          <el-input v-model="form.comment" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          {{ t('common.save') }}
        </el-button>
      </template>
    </el-dialog>
    
    <el-dialog
      v-model="rankingVisible"
      :title="t('performance.ranking')"
      width="600px"
    >
      <div style="margin-bottom: 10px;">
        <el-button type="success" :icon="Download" @click="handleExportRanking">
          {{ t('common.export') }}
        </el-button>
      </div>
      
      <el-table :data="rankingData" v-loading="rankingLoading">
        <el-table-column prop="ranking" :label="t('performance.rank')" width="80" align="center" />
        <el-table-column prop="employeeName" :label="t('performance.employee')" width="150" />
        <el-table-column prop="score" :label="t('performance.score')" width="100" align="center" />
        <el-table-column prop="grade" :label="t('performance.grade')" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getGradeType(row.grade)">
              {{ getGradeText(row.grade) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, TrendCharts, Edit, Delete, Download } from '@element-plus/icons-vue'
import {
  getPerformanceList,
  addPerformance,
  updatePerformance,
  deletePerformance,
  getPerformanceRanking
} from '@/api/performance'
import { getEmployeeList } from '@/api/employee'
import { exportPerformanceRanking } from '@/api/export'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const rankingVisible = ref(false)
const rankingLoading = ref(false)
const formRef = ref(null)
const isEdit = ref(false)

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10,
  grade: ''
})

const form = reactive({
  performanceId: null,
  employeeId: null,
  evalDate: '',
  score: 0,
  grade: 'C',
  comment: ''
})

const tableData = ref([])
const rankingData = ref([])
const total = ref(0)
const employeeOptions = ref([])

const dialogTitle = computed(() => {
  return isEdit.value ? t('performance.editPerformance') : t('performance.addPerformance')
})

const formRules = {
  employeeId: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  evalDate: [{ required: true, message: () => t('common.pleaseSelect'), trigger: 'change' }],
  score: [{ required: true, message: () => t('common.pleaseInput'), trigger: 'blur' }]
}

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

const autoSetGrade = () => {
  if (form.score >= 90) form.grade = 'A'
  else if (form.score >= 80) form.grade = 'B'
  else if (form.score >= 60) form.grade = 'C'
  else form.grade = 'D'
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPerformanceList(queryForm)
    tableData.value = res.data.records || []
    total.value = res.data.total || 0
  } catch (error) {
    console.error('Load data error:', error)
  } finally {
    loading.value = false
  }
}

const showRanking = async () => {
  rankingVisible.value = true
  rankingLoading.value = true
  try {
    const res = await getPerformanceRanking({})
    rankingData.value = res.data || []
  } catch (error) {
    console.error('Load ranking error:', error)
  } finally {
    rankingLoading.value = false
  }
}

const handleSearch = () => {
  queryForm.pageNum = 1
  loadData()
}

const handleReset = () => {
  queryForm.grade = ''
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
    
    await deletePerformance(row.performanceId)
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
          await updatePerformance(form.performanceId, form)
        } else {
          await addPerformance(form)
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
    form[key] = key === 'performanceId' || key === 'employeeId' ? null : (key === 'score' ? 0 : (key === 'grade' ? 'C' : ''))
  })
}

const handleExportRanking = async () => {
  try {
    await exportPerformanceRanking({})
    ElMessage.success(t('common.exportSuccess'))
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
.performance-container {
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
