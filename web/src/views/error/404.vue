<script setup lang="ts">
/**
 * @file 404 页面
 * @description 替代旧版 error/404.html
 */
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="error-page">
    <div class="error-content">
      <h1>404</h1>
      <p>抱歉，您访问的页面不存在</p>
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
  color: #409eff;
  margin-bottom: 20px;
}

.error-content p {
  color: #606266;
  margin-bottom: 20px;
}
</style>
