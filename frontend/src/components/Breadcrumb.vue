<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item
      v-for="(item, index) in breadcrumbs"
      :key="index"
      :to="item.path"
    >
      {{ item.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'

const route = useRoute()
const { t } = useI18n()

// 路由到标题的映射
const routeTitleMap = {
  '/home': 'menu.home',
  '/employee': 'menu.employee',
  '/department': 'menu.department',
  '/position': 'menu.position',
  '/assignment': 'menu.assignment',
  '/attendance': 'menu.attendance',
  '/payroll': 'menu.payroll',
  '/performance': 'menu.performance',
  '/statistics': 'menu.statistics'
}

// 生成面包屑
const breadcrumbs = computed(() => {
  const path = route.path
  const titleKey = routeTitleMap[path]
  
  if (!titleKey) {
    return []
  }
  
  return [
    {
      path: path,
      title: t(titleKey)
    }
  ]
})
</script>

<style scoped>
.el-breadcrumb {
  font-size: 14px;
}
</style>
