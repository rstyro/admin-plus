<script setup lang="ts">
/**
 * @file 菜单 ICON 参考页
 * @description 由旧版 templates/icon.html 重构而来。旧版是 2300 行硬编码的 Font Awesome 4 图标清单，
 *   这里直接从同一份 font-awesome.css 解析出图标名，既保证与侧栏实际可用的图标一致，也不会随版本脱节。
 *   菜单表 sys_menu.icon 存的就是 `fa fa-xxx` 形式的 class，点击图标即可复制。
 */
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import faCss from '@/assets/fontawesome/css/font-awesome.min.css?raw'

defineOptions({ name: 'IconList' })

/** 从 CSS 源码里提取 .fa-xxx:before 选择器得到图标名 */
const iconNames = computed<string[]>(() => {
  const names = new Set<string>()
  for (const m of faCss.matchAll(/\.fa-([a-z0-9-]+):before/g)) names.add(m[1])
  return Array.from(names).sort()
})

/** 搜索关键词 */
const keyword = ref('')

/** 过滤后的图标 */
const filteredIcons = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return iconNames.value
  return iconNames.value.filter((n) => n.includes(kw))
})

/**
 * 复制完整 class（菜单表里要填的形式）
 */
async function copyClass(name: string) {
  const text = `fa fa-${name}`
  try {
    await navigator.clipboard.writeText(text)
    ElMessage.success(`已复制：${text}`)
  } catch {
    ElMessage.warning('复制失败，请手动复制')
  }
}
</script>

<template>
  <el-card shadow="never">
    <template #header>
      <div class="header">
        <span>Font Awesome 图标（共 {{ iconNames.length }} 个）</span>
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
        @click="copyClass(name)"
      >
        <i :class="`fa fa-${name} icon`" />
        <div class="icon-name">fa-{{ name }}</div>
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
