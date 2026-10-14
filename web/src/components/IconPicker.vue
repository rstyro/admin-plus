<script setup lang="ts">
/**
 * @file 图标选择器
 * @description 输入框可手填 Element Plus 图标名，点右侧按钮弹出图标墙选择；
 *   左侧实时预览当前图标，名称未命中时按占位图标显示
 */
import { computed, ref } from 'vue'
import { Grid, Search } from '@element-plus/icons-vue'
import { epIconNames, epIconRegistry } from '@/utils/epIcons'
import MenuIcon from './MenuIcon.vue'

const props = withDefaults(
  defineProps<{
    modelValue?: string
    placeholder?: string
  }>(),
  { modelValue: '', placeholder: '选择或输入图标名' },
)

const emit = defineEmits<{ 'update:modelValue': [value: string] }>()

const visible = ref(false)
const keyword = ref('')

const current = computed({
  get: () => props.modelValue,
  set: (v) => emit('update:modelValue', v),
})

const filtered = computed(() => {
  const kw = keyword.value.trim().toLowerCase()
  if (!kw) return epIconNames
  return epIconNames.filter((n) => n.toLowerCase().includes(kw))
})

function pick(name: string) {
  current.value = name
  visible.value = false
}
</script>

<template>
  <div class="icon-picker">
    <el-input v-model="current" :placeholder="placeholder" clearable class="picker-input">
      <template #prefix>
        <MenuIcon :icon="current" fallback="Picture" />
      </template>
    </el-input>
    <el-popover
      v-model:visible="visible"
      trigger="click"
      placement="bottom-end"
      :width="338"
      popper-class="icon-picker-popper"
    >
      <template #reference>
        <el-button :icon="Grid" class="picker-btn" />
      </template>
      <div class="picker-body">
        <el-input v-model="keyword" size="small" placeholder="搜索图标名" clearable :prefix-icon="Search" />
        <div class="picker-grid">
          <el-icon
            v-for="name in filtered"
            :key="name"
            class="picker-cell"
            :class="{ 'is-active': name === current }"
            :title="name"
            @click="pick(name)"
          >
            <component :is="epIconRegistry[name]" />
          </el-icon>
        </div>
        <div v-if="!filtered.length" class="picker-empty">未匹配到图标</div>
      </div>
    </el-popover>
  </div>
</template>

<style scoped>
.icon-picker {
  display: flex;
  gap: 8px;
  width: 100%;
}
.picker-input {
  flex: 1;
}

.picker-body {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.picker-grid {
  display: grid;
  grid-template-columns: repeat(11, 1fr);
  gap: 4px;
  max-height: 240px;
  overflow-y: auto;
}

.picker-cell {
  justify-content: center;
  height: 26px;
  border-radius: 4px;
  font-size: 15px;
  cursor: pointer;
}
.picker-cell:hover {
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}
.picker-cell.is-active {
  background: var(--el-color-primary);
  color: #fff;
}

.picker-empty {
  padding: 12px 0;
  text-align: center;
  font-size: 12px;
  color: var(--el-text-color-secondary);
}
</style>

<style>
/* popover 渲染在 body 下，scoped 样式作用不到，这里收紧内边距 */
.icon-picker-popper .el-popover__title {
  display: none;
}
.icon-picker-popper {
  padding: 10px;
}
</style>
