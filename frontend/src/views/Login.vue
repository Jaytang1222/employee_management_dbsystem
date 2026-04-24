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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
}

.language-switch {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

@media screen and (max-width: 768px) {
  .login-box {
    width: 90%;
    padding: 30px 20px;
  }
}
</style>
