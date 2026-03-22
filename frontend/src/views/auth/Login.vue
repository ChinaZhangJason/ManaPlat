<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-header">
        <h1>管理平台</h1>
        <p>Management Platform</p>
      </div>
      
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginRules"
        class="login-form"
        @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            size="large"
            show-password
          />
        </el-form-item>
        
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
        
        <div class="login-footer">
          <router-link to="/reset-password">忘记密码？</router-link>
        </div>
      </el-form>
      
      <div class="third-party-login">
        <div class="divider">
          <span>其他登录方式</span>
        </div>
        <div class="third-party-icons">
          <el-button circle @click="handleThirdPartyLogin('wechat')">
            <span style="font-size: 18px;">W</span>
          </el-button>
          <el-button circle @click="handleThirdPartyLogin('dingtalk')">
            <span style="font-size: 18px;">D</span>
          </el-button>
          <el-button circle @click="handleThirdPartyLogin('feishu')">
            <span style="font-size: 18px;">F</span>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      await userStore.login(loginForm.username, loginForm.password)
      ElMessage.success('登录成功')
      router.push('/dashboard')
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
    } finally {
      loading.value = false
    }
  })
}

const handleThirdPartyLogin = (type) => {
  ElMessage.info(`${type} 登录开发中`)
}
</script>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h1 {
  font-size: 28px;
  color: #333;
  margin-bottom: 8px;
}

.login-header p {
  font-size: 14px;
  color: #999;
}

.login-form {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
}

.third-party-login {
  text-align: center;
}

.divider {
  position: relative;
  margin: 20px 0;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #eee;
}

.divider span {
  position: relative;
  background: #fff;
  padding: 0 12px;
  color: #999;
  font-size: 12px;
}

.third-party-icons {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.login-footer {
  text-align: right;
  margin-top: -10px;
}

.login-footer a {
  color: #409eff;
  font-size: 14px;
  text-decoration: none;
}

.login-footer a:hover {
  text-decoration: underline;
}
</style>
