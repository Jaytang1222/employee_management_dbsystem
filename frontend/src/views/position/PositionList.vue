<template>
  <div class="position-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          {{ t('common.add') }}
        </el-button>
      </div>
      
      <el-table :data="tableData" v-loading="loading" stripe>
        <el-table-column prop="positionId" label="ID" width="80" />
        <el-table-column prop="positionName" :label="t('position.name')" width="150" />
        <el-table-column prop="positionLevel" :label="t('position.level')" width="120" />
        <el-table-column prop="baseSalary" :label="t('position.baseSalary')" width="120" />
        <el-table-column prop="description" :label="t('position.description')" show-overflow-tooltip />
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
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="120px"
      >
        <el-form-item :label="t('position.name')" prop="positionName">
          <el-input v-model="form.positionName" />
        </el-form-item>
        
        <el-form-item :label="t('position.level')" prop="positionLevel">
          <el-select v-model="form.positionLevel" style="width: 100%">
            <el-option :label="t('position.junior')" value="初级" />
            <el-option :label="t('position.intermediate')" value="中级" />
            <el-option :label="t('position.senior')" value="高级" />
            <el-option :label="t('position.expert')" value="专家" />
          </el-select>
        </el-form-item>
        
        <el-form-item :label="t('position.baseSalary')" prop="baseSalary">
          <el-input-number v-model="form.baseSalary" :min="0" style="width: 100%" />
        </el-form-item>
        
        <el-form-item :label="t('position.description')" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
  getPositionList,
  addPosition,
  updatePosition,
  deletePosition
} from '@/api/position'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const isEdit = ref(false)

const queryForm = reactive({
  pageNum: 1,
  pageSize: 10
})

const form = reactive({
  positionId: null,
  positionName: '',
  positionLevel: '',
  baseSalary: 0,
  description: ''
})

const tableData = ref([])
const total = ref(0)

const dialogTitle = computed(() => {
  return isEdit.value ? t('position.editPosition') : t('position.addPosition')
})

const formRules = {
  positionName: [{ required: true, message: () => t('common.pleaseInput'), trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getPositionList(queryForm)
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
    await ElMessageBox.confirm(
      t('common.confirmDelete'),
      t('common.tips'),
      {
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel'),
        type: 'warning'
      }
    )
    
    await deletePosition(row.positionId)
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
          await updatePosition(form.positionId, form)
        } else {
          await addPosition(form)
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
    form[key] = key === 'positionId' ? null : (key === 'baseSalary' ? 0 : '')
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.position-container {
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

@media screen and (max-width: 768px) {
  .position-container {
    padding: 10px;
  }
}
</style>
