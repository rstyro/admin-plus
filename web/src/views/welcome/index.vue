<script setup lang="ts">
/**
 * @file 欢迎页
 * @description 由旧版 templates/welcome.html 重构，展示欢迎语与基础统计
 */
import { computed } from 'vue'
import { useUserStore } from '@/store/user'

defineOptions({ name: 'Welcome' })

const userStore = useUserStore()
const nickName = computed(() => userStore.userInfo?.nickName || '大佬')
</script>

<template>
  <div class="welcome">
    <el-card shadow="never">
      <div class="welcome-content">
        <h1>欢迎，{{ nickName }}！</h1>
        <p>这是一个前后端分离的管理系统模板，基于 Vue 3 + Element Plus + Vite。</p>
        <el-divider />
        <p class="tip">通过左侧菜单开始使用。</p>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.welcome {
  height: 100%;
}

.welcome-content {
  text-align: center;
  padding: 60px 20px;
}

.welcome-content h1 {
  color: #409eff;
  margin-bottom: 16px;
}

.tip {
  color: #909399;
  font-size: 14px;
}
</style>
