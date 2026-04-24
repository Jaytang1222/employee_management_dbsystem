<template>
  <router-view />
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { getToken, clearAuth } from '@/utils/auth'

const router = useRouter()

let tokenCheckInterval = null

// 检查Token是否过期
const checkTokenExpiration = () => {
  const token = getToken()
  // 如果getToken返回null，说明Token已过期
  if (!token && router.currentRoute.value.path !== '/login') {
    clearAuth()
    router.replace('/login')
  }
}

onMounted(() => {
  // 每分钟检查一次Token是否过期
  tokenCheckInterval = setInterval(checkTokenExpiration, 60 * 1000)
})

onUnmounted(() => {
  if (tokenCheckInterval) {
    clearInterval(tokenCheckInterval)
  }
})
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  width: 100%;
  height: 100%;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  width: 100%;
  height: 100%;
  overflow-x: hidden;
}
</style>
