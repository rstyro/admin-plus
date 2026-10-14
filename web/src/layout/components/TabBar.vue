<script setup lang="ts">
/**
 * @file 顶部多 Tab 工具栏
 * @description 替代旧版 index.html 的 content-tabs（app_iframe.js）
 *   行为对齐：点击切换、双击刷新、右键菜单（刷新/关闭）、下拉批量操作、左右滚动、全屏
 */
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useTabsStore, type TabItem } from '@/store/tabs'
import { Close, Refresh, ArrowLeft, ArrowRight, FullScreen } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const tabsStore = useTabsStore()

/** Tab 容器，用于滚动 */
const scrollContainer = ref<HTMLElement>()

/** 当前激活的 Tab 路径 */
const activePath = computed(() => tabsStore.activePath)

/** 右键菜单状态 */
const ctxMenu = ref<{ visible: boolean; x: number; y: number; path: string }>({
  visible: false,
  x: 0,
  y: 0,
  path: '',
})

/** 右键菜单指向的页签 */
const ctxTab = computed(() => tabsStore.tabs.find((t) => t.path === ctxMenu.value.path))

/**
 * 点击 Tab 切换路由
 */
function handleClick(path: string) {
  if (path !== activePath.value) router.push(path)
}

/**
 * 关闭某个 Tab
 */
function handleClose(path: string, ev?: Event) {
  ev?.stopPropagation()
  hideCtxMenu()
  const next = tabsStore.closeTab(path)
  if (next) router.push(next)
}

/** 关闭当前 */
function closeCurrent() {
  if (activePath.value) handleClose(activePath.value)
}

/** 关闭其他 */
function closeOther() {
  tabsStore.closeOtherTabs(activePath.value)
  hideCtxMenu()
}

/** 全部关闭（保留 affix） */
function closeAll() {
  const next = tabsStore.closeAllTabs()
  hideCtxMenu()
  if (route.path !== next) router.push(next)
}

/**
 * 刷新指定 Tab
 * @description 先移出 keep-alive 缓存名单再走 /redirect 重进，组件才会真正重建；
 *   重进后放回名单，恢复该页签的缓存能力
 */
async function refreshTab(tab?: TabItem) {
  hideCtxMenu()
  if (!tab) return
  if (tab.path !== activePath.value) await router.push(tab.path)
  tabsStore.removeCached(tab.name)
  await nextTick()
  await router.replace('/redirect' + tab.path)
  await nextTick()
  tabsStore.addCached(tab.name)
}

/** 刷新当前 */
function refreshCurrent() {
  const tab = tabsStore.tabs.find((t) => t.path === activePath.value)
  if (tab) refreshTab(tab)
}

/** 打开右键菜单 */
function openCtxMenu(ev: MouseEvent, tab: TabItem) {
  ev.preventDefault()
  ctxMenu.value = { visible: true, x: ev.clientX, y: ev.clientY, path: tab.path }
}

function hideCtxMenu() {
  ctxMenu.value.visible = false
}

/** 全屏切换（对应旧版 App.handleFullScreen） */
function toggleFullscreen() {
  if (document.fullscreenElement) {
    document.exitFullscreen()
  } else {
    document.documentElement.requestFullscreen()
  }
}

/**
 * 横向滚动 Tab 列表
 */
function scroll(direction: 'left' | 'right') {
  const container = scrollContainer.value
  if (!container) return
  const step = 200
  container.scrollBy({ left: direction === 'left' ? -step : step, behavior: 'smooth' })
}

// 激活页签变化时保证其可见（对应旧版 scrollToTab）
watch(activePath, async () => {
  await nextTick()
  scrollContainer.value
    ?.querySelector('.tab-item.active')
    ?.scrollIntoView({ block: 'nearest', inline: 'nearest' })
})

onMounted(() => document.addEventListener('click', hideCtxMenu))
onBeforeUnmount(() => document.removeEventListener('click', hideCtxMenu))
</script>

<template>
  <div class="tab-bar">
    <el-button class="tab-scroll-btn" link :icon="ArrowLeft" @click="scroll('left')" />
    <div ref="scrollContainer" class="tab-list">
      <div
        v-for="tab in tabsStore.tabs"
        :key="tab.path"
        class="tab-item"
        :class="{ active: tab.path === activePath }"
        @click="handleClick(tab.path)"
        @dblclick="refreshTab(tab)"
        @contextmenu="openCtxMenu($event, tab)"
      >
        <span>{{ tab.title }}</span>
        <el-icon v-if="!tab.affix" class="tab-close" @click="handleClose(tab.path, $event)">
          <Close />
        </el-icon>
      </div>
    </div>
    <el-button class="tab-scroll-btn" link :icon="ArrowRight" @click="scroll('right')" />
    <el-button class="tab-fullscreen-btn" link :icon="FullScreen" @click="toggleFullscreen" />
    <el-dropdown class="tab-actions" trigger="click">
      <el-button link :icon="Refresh" />
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="refreshCurrent">刷新当前</el-dropdown-item>
          <el-dropdown-item @click="closeCurrent">关闭当前</el-dropdown-item>
          <el-dropdown-item @click="closeOther">关闭其他</el-dropdown-item>
          <el-dropdown-item @click="closeAll">全部关闭</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>

    <!-- 右键菜单：对齐旧版（刷新 / 关闭） -->
    <ul
      v-show="ctxMenu.visible"
      class="ctx-menu"
      :style="{ left: ctxMenu.x + 'px', top: ctxMenu.y + 'px' }"
      @click.stop
    >
      <li @click="refreshTab(ctxTab)">刷新（双击标签）</li>
      <li v-if="ctxTab && !ctxTab.affix" @click="handleClose(ctxTab.path)">关闭</li>
    </ul>
  </div>
</template>

<style scoped>
.tab-bar {
  display: flex;
  align-items: center;
  height: 36px;
  background: #fff;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 8px;
}

.tab-scroll-btn {
  padding: 4px;
}

.tab-fullscreen-btn {
  padding: 4px;
  margin-left: 8px;
}

.tab-list {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 4px;
  overflow-x: auto;
  scrollbar-width: none;
}
.tab-list::-webkit-scrollbar {
  display: none;
}

.tab-item {
  display: flex;
  align-items: center;
  gap: 4px;
  height: 28px;
  padding: 0 10px;
  font-size: 13px;
  color: #606266;
  background: #f5f7fa;
  border-radius: 4px;
  cursor: pointer;
  white-space: nowrap;
  border: 1px solid transparent;
}
.tab-item.active {
  background: var(--el-color-primary-light-9);
  color: var(--accent, #409eff);
  border-color: var(--el-color-primary-light-7);
}
.tab-close {
  font-size: 12px;
  border-radius: 50%;
}
.tab-close:hover {
  background: #c0c4cc;
  color: #fff;
}

.tab-actions {
  margin-left: 8px;
}

.ctx-menu {
  position: fixed;
  z-index: 3000;
  margin: 4px 0 0;
  padding: 4px 0;
  list-style: none;
  background: #fff;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.12);
}
.ctx-menu li {
  padding: 6px 16px;
  font-size: 13px;
  color: #333;
  cursor: pointer;
}
.ctx-menu li:hover {
  background: #ecf5ff;
  color: #409eff;
}
</style>
