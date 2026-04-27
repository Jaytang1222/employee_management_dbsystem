<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h2>{{ t('login.title') }}</h2>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            :placeholder="t('login.usernamePlaceholder')"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            :placeholder="t('login.passwordPlaceholder')"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            {{ t('login.loginBtn') }}
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="language-switch">
        <el-button
          :type="currentLanguage === 'zh' ? 'primary' : ''"
          size="small"
          @click="switchLanguage('zh')"
        >
          中文
        </el-button>
        <el-button
          :type="currentLanguage === 'en' ? 'primary' : ''"
          size="small"
          @click="switchLanguage('en')"
        >
          English
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { login, getUserInfo as fetchUserInfo } from '@/api/user'
import { setToken, setUserInfo } from '@/utils/auth'

const router = useRouter()
const { t, locale } = useI18n()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: () => t('login.usernameRequired'), trigger: 'blur' }
  ],
  password: [
    { required: true, message: () => t('login.passwordRequired'), trigger: 'blur' }
  ]
}

const currentLanguage = computed(() => locale.value)

const switchLanguage = (lang) => {
  locale.value = lang
  localStorage.setItem('language', lang)
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // 调用登录接口
        const res = await login(loginForm)
        
        // 保存 Token
        setToken(res.data.token)
        
        // 获取用户信息
        const userRes = await fetchUserInfo()
        setUserInfo(userRes.data)
        
        ElMessage.success(t('login.loginSuccess'))
        
        // 跳转到首页
        router.push('/')
      } catch (error) {
        console.error('Login error:', error)
        ElMessage.error(t('login.loginFailed'))
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #000000;
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(118, 185, 0, 0.1) 0%, rgba(0, 0, 0, 0.9) 100%);
  z-index: 0;
}

.login-box {
  width: 450px;
  padding: 48px;
  background: #1a1a1a;
  border-radius: 2px;
  box-shadow: rgba(0, 0, 0, 0.3) 0px 0px 5px 0px;
  border: 2px solid #76b900;
  position: relative;
  z-index: 1;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-header h2 {
  margin: 0;
  color: #ffffff;
  font-size: 36px;
  font-weight: 700;
  line-height: 1.25;
  letter-spacing: 1px;
}

.login-form {
  margin-top: 32px;
}

.login-form :deep(.el-form-item__label) {
  color: #a7a7a7;
  font-weight: 700;
  font-size: 14px;
}

.login-form :deep(.el-input__wrapper) {
  background-color: #000000;
  border: 1px solid #5e5e5e;
  border-radius: 2px;
  box-shadow: none;
  transition: all 0.3s;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: #76b900;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #76b900;
  box-shadow: 0 0 0 1px #76b900;
}

.login-form :deep(.el-input__inner) {
  color: #ffffff;
  font-weight: 400;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: #757575;
}

.login-form :deep(.el-input__prefix) {
  color: #76b900;
}

.login-button {
  width: 100%;
  height: 48px;
  background: transparent;
  border: 2px solid #76b900;
  border-radius: 2px;
  color: #ffffff;
  font-size: 16px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  transition: all 0.3s;
}

.login-button:hover {
  background: #1eaedb;
  border-color: #1eaedb;
  color: #ffffff;
}

.login-button:active {
  background: #007fff;
  border-color: #007fff;
  transform: scale(1);
}

.language-switch {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 32px;
}

.language-switch :deep(.el-button) {
  background: transparent;
  border: 1px solid #5e5e5e;
  border-radius: 2px;
  color: #a7a7a7;
  font-weight: 700;
  text-transform: uppercase;
  transition: all 0.3s;
}

.language-switch :deep(.el-button:hover) {
  border-color: #76b900;
  color: #76b900;
  background: transparent;
}

.language-switch :deep(.el-button--primary) {
  background: transparent;
  border: 2px solid #76b900;
  color: #76b900;
}

.language-switch :deep(.el-button--primary:hover) {
  background: #1eaedb;
  border-color: #1eaedb;
  color: #ffffff;
}

@media screen and (max-width: 768px) {
  .login-box {
    width: 90%;
    padding: 32px 24px;
  }
  
  .login-header h2 {
    font-size: 24px;
  }
}
</style>
