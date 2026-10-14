<script setup lang="ts">
/**
 * @file 菜单 ICON 参考页
 * @description 旧版 templates/icon.html 是 2300 行硬编码的 Font Awesome 清单；
 *   新前端统一使用 Element Plus 图标，这里直接取其导出清单渲染图标墙，
 *   菜单表 sys_menu.icon 存的就是图标名（如 Setting），点击即可复制。
 */
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { epIconNames, epIconRegistry } from '@/utils/epIcons'

defineOptions({ name: 'IconList' })

/** 搜索关键词 */
const keyword = ref('')

/** 过滤后的图标 */
const filteredIcons = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return epIconNames
  return epIconNames.filter((n) => n.toLowerCase().includes(kw))
})

/**
 * 复制图标名（菜单表里要填的值）
 */
async function copyName(name: string) {
  try {
    await navigator.clipboard.writeText(name)
    ElMessage.success(`已复制：${name}`)
  } catch {
    ElMessage.warning('复制失败，请手动复制')
  }
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="header">
        <span>Element Plus 图标（共 {{ epIconNames.length }} 个，当前 {{ filteredIcons.length }} 个）</span>
        <el-input
          v-model="keyword"
          placeholder="搜索图标名"
          clearable
          style="width: 240px"
        />
      </div>
    </template>

    <div class="icon-grid">
      <div
        v-for="name in filteredIcons"
        :key="name"
        class="icon-item"
        @click="copyName(name)"
      >
        <el-icon class="icon"><component :is="epIconRegistry[name]" /></el-icon>
        <div class="icon-name">{{ name }}</div>
      </div>
    </div>

    <el-empty v-if="!filteredIcons.length" description="未匹配到图标" />
  </el-card>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.icon-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
}

.icon-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px 4px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s;
}
.icon-item:hover {
  border-color: #409eff;
  color: #409eff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
}

.icon {
  font-size: 24px;
  margin-bottom: 6px;
}

.icon-name {
  font-size: 12px;
  color: #909399;
  text-align: center;
  word-break: break-all;
}
</style>
