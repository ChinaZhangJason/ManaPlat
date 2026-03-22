<template>
  <div class="password-reset-page">
    <el-card class="reset-card">
      <template #header>
        <span>密码重置</span>
      </template>
      
      <el-steps :active="step" finish-status="success" align-center>
        <el-step title="验证身份" />
        <el-step title="重置密码" />
      </el-steps>

      <div class="step-content">
        <div v-if="step === 0" class="verify-step">
          <el-form :model="verifyForm" :rules="verifyRules" ref="verifyFormRef" label-width="100px">
            <el-form-item label="验证方式">
              <el-radio-group v-model="verifyForm.type">
                <el-radio label="phone">手机号</el-radio>
                <el-radio label="email">邮箱</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item :label="verifyForm.type === 'phone' ? '手机号' : '邮箱'" prop="target">
              <el-input v-model="verifyForm.target" :placeholder="verifyForm.type === 'phone' ? '请输入手机号' : '请输入邮箱'" />
            </el-form-item>
            <el-form-item label="验证码" prop="code">
              <el-input v-model="verifyForm.code" placeholder="请输入验证码" style="width: 150px" />
              <el-button @click="sendCode" :disabled="countdown > 0" style="margin-left: 10px">
                {{ countdown > 0 ? `${countdown}秒后重发` : '发送验证码' }}
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleVerify">验证</el-button>
            </el-form-item>
          </el-form>
        </div>

        <div v-else class="reset-step">
          <el-form :model="resetForm" :rules="resetRules" ref="resetFormRef" label-width="100px">
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="resetForm.newPassword" type="password" placeholder="请输入新密码" show-password />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="resetForm.confirmPassword" type="password" placeholder="请确认新密码" show-password />
            </el-form-item>
            <el-form-item>
              <el-button @click="step = 0">上一步</el-button>
              <el-button type="primary" @click="handleReset">重置密码</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { authApi } from '@/api/auth'

const step = ref(0)
const countdown = ref(0)
const verifyFormRef = ref(null)
const resetFormRef = ref(null)

const verifyForm = reactive({
  type: 'email',
  target: '',
  code: ''
})

const resetForm = reactive({
  newPassword: '',
  confirmPassword: ''
})

const validateConfirm = (rule, value, callback) => {
  if (value !== resetForm.newPassword) callback(new Error('两次输入密码不一致'))
  else callback()
}

const verifyRules = {
  target: [{ required: true, message: '请输入', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
}

const resetRules = {
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码6-20位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' }
  ]
}

let timer = null

const sendCode = async () => {
  if (!verifyForm.target) {
    ElMessage.warning('请输入手机号或邮箱')
    return
  }
  try {
    await authApi.sendCode({ type: verifyForm.type.toUpperCase(), target: verifyForm.target, scene: 'RESET_PASSWORD' })
    ElMessage.success('验证码已发送')
    countdown.value = 60
    timer = setInterval(() => {
      countdown.value--
      if (countdown.value <= 0) clearInterval(timer)
    }, 1000)
  } catch (e) {
    ElMessage.error('发送失败')
  }
}

const handleVerify = async () => {
  await verifyFormRef.value.validate((valid) => {
    if (valid) {
      step.value = 1
    }
  })
}

const handleReset = async () => {
  await resetFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await authApi.resetPassword({
          type: verifyForm.type,
          target: verifyForm.target,
          code: verifyForm.code,
          newPassword: resetForm.newPassword
        })
        ElMessage.success('密码重置成功，请登录')
      } catch (e) {
        ElMessage.error('重置失败')
      }
    }
  })
}
</script>

<style scoped>
.password-reset-page { padding: 40px; display: flex; justify-content: center; }
.reset-card { width: 500px; }
.step-content { margin-top: 40px; }
</style>
