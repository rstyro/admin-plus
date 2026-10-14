<script setup lang="ts">
/**
 * @file 登录页
 * @description 由旧版 templates/login.html 重构而来
 *   - 替换 Vue2 + Element UI 2 -> Vue3 + Element Plus
 *   - 验证码地址走 vite 代理的 /captcha
 *   - 登录成功后按 redirect 跳转，无则跳首页
 */
import { reactive, ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, type FormInstance, type FormRules } from 'element-plus'
import { User, Lock, Picture } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import loginBg from '@/assets/login-bg.jpg'
import type { LoginDto } from '@/api/auth'

/** 背景图（旧版 static/images/w.jpg），经模块导入保证 dev/build 路径都正确 */
const bgStyle = `url(${loginBg}) center/cover no-repeat`

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

/** 表单引用 */
const formRef = ref<FormInstance>()
/** 登录加载中 */
const loading = ref(false)
/** 验证码图片地址（带时间戳防缓存） */
const captchaSrc = ref('')

/** 登录表单数据 */
const form = reactive<LoginDto>({
  username: '',
  password: '',
  code: '',
  rememberMe: false,
})

/** 表单校验规则 */
const rules: FormRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

/**
 * 刷新验证码
 * @description 加时间戳避免浏览器缓存
 */
function refreshCaptcha() {
  captchaSrc.value = `/captcha?t=${Date.now()}`
}

/**
 * 提交登录
 */
async function onSubmit() {
  if (!formRef.value) return
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    await userStore.login(form)
    ElMessage.success('登录成功')
    const redirect = (route.query.redirect as string) || '/'
    router.push(redirect)
  } catch {
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<template>
  <div class="login-page">
    <div class="login-box">
      <!-- 左侧装饰图 -->
      <div class="login-left" />
      <!-- 右侧表单区 -->
      <div class="login-right">
        <h2>欢迎登录</h2>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          class="login-form"
          @keyup.enter="onSubmit"
        >
          <el-form-item prop="username">
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              show-password
              placeholder="请输入密码"
              :prefix-icon="Lock"
              size="large"
            />
          </el-form-item>
          <el-form-item prop="code">
            <div class="captcha-row">
              <el-input
                v-model="form.code"
                placeholder="请输入验证码"
                :prefix-icon="Picture"
                size="large"
              />
              <img
                :src="captchaSrc"
                alt="验证码"
                title="点击更换"
                class="captcha-img"
                @click="refreshCaptcha"
              />
            </div>
          </el-form-item>
          <el-form-item>
            <el-tooltip
              content="未选中时关闭浏览器需要重新登录"
              placement="right"
            >
              <el-checkbox v-model="form.rememberMe">记住我</el-checkbox>
            </el-tooltip>
          </el-form-item>
          <el-button
            type="primary"
            size="large"
            class="login-btn"
            :loading="loading"
            @click="onSubmit"
          >
            登录
          </el-button>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  height: 100vh;
  width: 100vw;
  display: flex;
  align-items: center;
  justify-content: center;
  /* 旧版同款背景图（static/images/w.jpg） */
  background: v-bind(bgStyle);
}

.login-box {
  display: flex;
  width: 900px;
  height: 520px;
  background: rgba(255, 255, 255, 0.7);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.login-left {
  width: 40%;
  background: v-bind(bgStyle);
}

.login-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.login-right h2 {
  color: #409eff;
  font-size: 28px;
  margin-bottom: 30px;
  letter-spacing: 6px;
}

.login-form {
  width: 80%;
}

.captcha-row {
  display: flex;
  gap: 10px;
  width: 100%;
}

.captcha-img {
  height: 40px;
  width: 120px;
  cursor: pointer;
  border-radius: 4px;
}

.login-btn {
  width: 100%;
}
</style>
