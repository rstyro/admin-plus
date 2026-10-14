<script setup lang="ts">
/**
 * @file 个人中心
 * @description 占位页，可后续实现修改密码/头像等
 */
import { computed } from 'vue'
import { useUserStore } from '@/store/user'

defineOptions({ name: 'Profile' })

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)
const roles = computed(() => userStore.roles)
</script>

<template>
  <el-card shadow="never">
    <template #header>个人中心</template>
    <el-descriptions :column="1" border>
      <el-descriptions-item label="用户 ID">{{ userInfo?.id }}</el-descriptions-item>
      <el-descriptions-item label="用户名">{{ userInfo?.username }}</el-descriptions-item>
      <el-descriptions-item label="昵称">{{ userInfo?.nickName }}</el-descriptions-item>
      <el-descriptions-item label="头像">
        <el-avatar :size="60" :src="userInfo?.avatar" />
      </el-descriptions-item>
      <el-descriptions-item label="角色">
        <el-tag v-for="role in roles" :key="role" class="role-tag">{{ role }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="用户状态">{{ userInfo?.status }}</el-descriptions-item>
      <el-descriptions-item label="最后登录IP">{{ userInfo?.loginIp }}</el-descriptions-item>
      <el-descriptions-item label="最后登录时间">{{ userInfo?.loginDate }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ userInfo?.createTime }}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<style scoped>
.role-tag {
  margin-right: 8px;
}
</style>
