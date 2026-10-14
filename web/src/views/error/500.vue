<script setup lang="ts">
/**
 * @file 500 错误页
 * @description 替代旧版 error/500.html
 */
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="error-page">
    <div class="error-content">
      <h1>500</h1>
      <p>抱歉，服务器出现异常</p>
      <el-button type="primary" @click="router.push('/')">返回首页</el-button>
    </div>
  </div>
</template>

<style scoped>
.error-page {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: #f0f2f5;
}

.error-content {
  text-align: center;
}

.error-content h1 {
  font-size: 100px;
  color: #f56c6c;
  margin-bottom: 20px;
}

.error-content p {
  color: #606266;
  margin-bottom: 20px;
}
</style>
