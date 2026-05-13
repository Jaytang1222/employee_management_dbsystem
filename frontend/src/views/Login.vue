<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <img src="/src/views/resources/logo.png" alt="Logo" class="login-logo" />
        <h2>{{ t('login.title') }}</h2>
        <p class="login-subtitle">Employee Management System</p>
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
  background: linear-gradient(135deg, #1a1a1a 0%, #000000 100%);
  position: relative;
  overflow: hidden;
}

.login-container::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -50%;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle, rgba(118, 185, 0, 0.15) 0%, transparent 70%);
  z-index: 0;
  animation: pulse 8s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.5; }
  50% { transform: scale(1.1); opacity: 0.8; }
}

.login-box {
  width: 480px;
  padding: 48px;
  background: rgba(26, 26, 26, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 8px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.5), 0 0 0 1px rgba(118, 185, 0, 0.3);
  border: 2px solid #76b900;
  position: relative;
  z-index: 1;
  transition: all 0.3s ease;
}

.login-box:hover {
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.7), 0 0 0 2px rgba(118, 185, 0, 0.5);
  transform: translateY(-2px);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-logo {
  width: 120px;
  height: auto;
  margin-bottom: 24px;
  filter: drop-shadow(0 4px 8px rgba(118, 185, 0, 0.3));
  transition: transform 0.3s ease;
}

.login-logo:hover {
  transform: scale(1.05);
}

.login-header h2 {
  margin: 0 0 8px 0;
  color: #ffffff;
  font-size: 32px;
  font-weight: 700;
  line-height: 1.25;
  letter-spacing: 1px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.login-subtitle {
  margin: 0;
  color: #76b900;
  font-size: 14px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 2px;
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
  background-color: rgba(0, 0, 0, 0.6);
  border: 2px solid #3a3a3a;
  border-radius: 4px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.3);
  transition: all 0.3s;
}

.login-form :deep(.el-input__wrapper:hover) {
  border-color: #76b900;
  background-color: rgba(0, 0, 0, 0.8);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  border-color: #76b900;
  box-shadow: 0 0 0 3px rgba(118, 185, 0, 0.2), inset 0 2px 4px rgba(0, 0, 0, 0.3);
  background-color: rgba(0, 0, 0, 0.8);
}

.login-form :deep(.el-input__inner) {
  color: #ffffff;
  font-weight: 400;
  font-size: 15px;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: #888888;
}

.login-form :deep(.el-input__prefix) {
  color: #76b900;
  font-size: 18px;
}

.login-button {
  width: 100%;
  height: 52px;
  background: linear-gradient(135deg, #76b900 0%, #5a8f00 100%);
  border: 2px solid #76b900;
  border-radius: 4px;
  color: #a7a7a7 !important;
  font-size: 16px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 1px;
  transition: all 0.3s;
  box-shadow: 0 4px 12px rgba(118, 185, 0, 0.3);
  position: relative;
  overflow: hidden;
}

.login-button :deep(span) {
  color: #a7a7a7 !important;
}

.login-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s;
}

.login-button:hover {
  background: linear-gradient(135deg, #8acc00 0%, #76b900 100%);
  border-color: #8acc00;
  color: #a7a7a7 !important;
  box-shadow: 0 6px 20px rgba(118, 185, 0, 0.5);
  transform: translateY(-2px);
}

.login-button:hover :deep(span) {
  color: #a7a7a7 !important;
}

.login-button:hover::before {
  left: 100%;
}

.login-button:active {
  transform: translateY(0);
  box-shadow: 0 2px 8px rgba(118, 185, 0, 0.4);
}

.language-switch {
  display: flex;
  justify-content: center;
  gap: 16px;
  margin-top: 32px;
}

.language-switch :deep(.el-button) {
  background: rgba(0, 0, 0, 0.4);
  border: 1px solid #3a3a3a;
  border-radius: 4px;
  color: #a7a7a7 !important;
  font-weight: 600;
  text-transform: uppercase;
  transition: all 0.3s;
  min-width: 80px;
}

.language-switch :deep(.el-button:hover) {
  border-color: #76b900;
  color: #76b900 !important;
  background: rgba(118, 185, 0, 0.1);
}

.language-switch :deep(.el-button--primary) {
  background: rgba(118, 185, 0, 0.2);
  border: 2px solid #76b900;
  color: #a7a7a7 !important;
}

.language-switch :deep(.el-button--primary:hover) {
  background: rgba(118, 185, 0, 0.3);
  border-color: #8acc00;
  color: #a7a7a7 !important;
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
