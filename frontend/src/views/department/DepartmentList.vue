<template>
  <div class="department-container">
    <el-card>
      <div class="toolbar">
        <el-button type="primary" :icon="Plus" @click="handleAdd">
          {{ t('common.add') }}
        </el-button>
        <el-button :icon="Refresh" @click="loadData">
          {{ t('common.refresh') }}
        </el-button>
      </div>
      
      <el-table
        :data="tableData"
        v-loading="loading"
        row-key="deptId"
        :tree-props="{ children: 'children' }"
        stripe
      >
        <el-table-column prop="deptName" :label="t('department.name')" width="200" />
        <el-table-column prop="managerName" :label="t('department.manager')" width="150" />
        <el-table-column prop="status" :label="t('common.status')" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'active' ? 'success' : 'info'">
              {{ row.status === 'active' ? t('employee.active') : t('employee.inactive') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" :label="t('common.createTime')" width="180" />
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
        <el-form-item :label="t('department.name')" prop="deptName">
          <el-input v-model="form.deptName" />
        </el-form-item>
        
        <el-form-item :label="t('department.parentDept')" prop="parentDeptId">
          <el-tree-select
            v-model="form.parentDeptId"
            :data="treeData"
            :props="{ label: 'deptName', value: 'deptId' }"
            check-strictly
            :placeholder="t('department.noDepartment')"
            clearable
            style="width: 100%"
          />
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
import { Plus, Refresh, Edit, Delete } from '@element-plus/icons-vue'
import {
  getDepartmentTree,
  addDepartment,
  updateDepartment,
  deleteDepartment
} from '@/api/department'

const { t } = useI18n()

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const formRef = ref(null)
const isEdit = ref(false)

const form = reactive({
  deptId: null,
  deptName: '',
  parentDeptId: null
})

const tableData = ref([])
const treeData = ref([])

const dialogTitle = computed(() => {
  return isEdit.value ? t('department.editDepartment') : t('department.addDepartment')
})

const formRules = {
  deptName: [{ required: true, message: () => t('common.pleaseInput'), trigger: 'blur' }]
}

const loadData = async () => {
  loading.value = true
  try {
    const res = await getDepartmentTree()
    tableData.value = res.data || []
    treeData.value = res.data || []
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
    
    await deleteDepartment(row.deptId)
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
          await updateDepartment(form.deptId, form)
        } else {
          await addDepartment(form)
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
    form[key] = key === 'deptId' || key === 'parentDeptId' ? null : ''
  })
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.department-container {
  padding: 24px;
  width: 100%;
  max-width: 100%;
}

.toolbar {
  margin-bottom: 24px;
}

@media screen and (max-width: 768px) {
  .department-container {
    padding: 16px;
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
