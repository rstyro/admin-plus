<script setup lang="ts">
/**
 * @file 中转刷新页
 * @description 用于 TabBar 的"刷新当前"功能：跳转到 /redirect/xxx，立即再跳回 xxx，
 *   由此重新触发组件挂载（因为 keep-alive 缓存的页面切换到 redirect 时不会命中缓存）
 */
import { onBeforeMount } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

onBeforeMount(() => {
  const { params, query } = route
  // params.path 是 /redirect/system/user -> ['system','user']
  const path = Array.isArray(params.path) ? params.path.join('/') : String(params.path || '')
  router.replace({ path: '/' + path, query })
})
</script>

<template>
  <div />
</template>
