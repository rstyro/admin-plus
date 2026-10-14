<script setup lang="ts">
/**
 * @file 顶部导航栏
 * @description 显示系统名、菜单图标入口、用户头像下拉（修改密码、登出）
 */
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Check } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import { useTabsStore } from '@/store/tabs'
import { SKIN_OPTIONS, useThemeStore } from '@/store/theme'
import { editUser, type SysUser } from '@/api/system/user'
import { resetRouter } from '@/router'

const router = useRouter()
const userStore = useUserStore()
const tabsStore = useTabsStore()
const themeStore = useThemeStore()

/** 用户信息（用于显示头像和昵称） */
const userInfo = computed(() => userStore.userInfo)

/** 系统名：后端 admin.common.systemName，未加载时兜底 */
const systemTitle = computed(() => userStore.systemName || '后台管理系统')

/** 修改密码弹窗 */
const pwdDialogVisible = ref(false)
const pwdFormRef = ref<FormInstance>()
const pwdForm = reactive({ password: '' })
const pwdRules: FormRules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
  ],
}

/**
 * 登出并回到登录页
 */
async function goLogin() {
  await userStore.logout()
  tabsStore.reset()
  resetRouter()
  router.push('/login')
}

/**
 * 用户登出
 */
async function handleLogout() {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
  } catch {
    return // 取消登出
  }
  await goLogin()
  ElMessage.success('已退出登录')
}

/**
 * 打开修改密码弹窗
 */
function openEditPwd() {
  pwdForm.password = ''
  pwdDialogVisible.value = true
}

/**
 * 保存新密码
 * @description 与旧版 index.html 一致，仍走 POST /system/user/edit（只传 id + password）
 */
async function savePassword() {
  if (!pwdFormRef.value) return
  try {
    await pwdFormRef.value.validate()
  } catch {
    return
  }
  await editUser({ id: userInfo.value?.id, password: pwdForm.password } as SysUser)
  ElMessage.success('密码修改成功，请重新登录')
  pwdDialogVisible.value = false
  await goLogin()
}

/**
 * 打开 ICON 参考页（对应旧版顶栏“菜单ICON”入口）
 */
function openIconPage() {
  router.push('/icon')
}
</script>

<template>
  <div class="navbar">
    <div class="navbar-left">
      <span class="system-title">{{ systemTitle }}</span>
      <el-button link class="icon-entry" @click="openIconPage">菜单ICON</el-button>
    </div>
    <div class="navbar-right">
      <!-- 皮肤切换面板（对应旧版 adminlte.skin.js 的 skins 侧栏面板） -->
      <el-popover placement="bottom-end" :width="332" trigger="click" popper-class="skin-popover">
        <template #reference>
          <el-button link class="skin-entry" title="切换主题皮肤">
            <el-icon :size="18"><Brush /></el-icon>
          </el-button>
        </template>
        <div class="skin-panel">
          <h4 class="skin-heading">皮肤</h4>
          <ul class="skin-list">
            <li v-for="s in SKIN_OPTIONS" :key="s.id" class="skin-item">
              <a
                href="javascript:void(0);"
                class="skin-preview"
                :class="{ active: themeStore.skin === s.id }"
                @click="themeStore.setSkin(s.id)"
              >
                <span class="strip-top">
                  <span class="w20" :style="{ background: s.logoBg }" />
                  <span class="w80" :style="{ background: s.navbarBg }" />
                </span>
                <span class="strip-bottom">
                  <span class="w20" :style="{ background: s.sidebarBg }" />
                  <span class="w80 content" />
                </span>
                <el-icon v-if="themeStore.skin === s.id" class="skin-check"><Check /></el-icon>
              </a>
              <p class="skin-label">{{ s.label }}</p>
            </li>
          </ul>
        </div>
      </el-popover>

      <el-dropdown @command="(cmd: string) => (cmd === 'logout' ? handleLogout() : openEditPwd())">
        <span class="user-info">
          <el-avatar :size="32" :src="userInfo?.avatar" />
          <span class="nick-name">{{ userInfo?.nickName || '管理员' }}</span>
          <el-icon><ArrowDown /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="editPwd">修改密码</el-dropdown-item>
            <el-dropdown-item command="logout" divided>登出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>

  <!-- 修改密码弹窗 -->
  <el-dialog v-model="pwdDialogVisible" title="修改密码" width="400px">
    <el-form ref="pwdFormRef" :model="pwdForm" :rules="pwdRules" label-width="80px">
      <el-form-item label="新密码" prop="password">
        <el-input v-model="pwdForm.password" type="password" show-password placeholder="请输入新密码" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="pwdDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="savePassword">修改</el-button>
    </template>
  </el-dialog>
</template>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 50px;
  padding: 0 20px;
  background: var(--navbar-bg, #fff);
  border-bottom: 1px solid var(--logo-border, #e6e6e6);
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.system-title {
  font-size: 18px;
  font-weight: bold;
  color: var(--navbar-text, #303133);
}

.icon-entry {
  color: var(--navbar-text, #909399);
  opacity: 0.85;
  font-size: 13px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.nick-name {
  font-size: 14px;
  color: var(--navbar-text, #606266);
}

.skin-entry {
  color: var(--navbar-text, #606266);
  margin-right: 16px;
}

/* ===== 皮肤切换面板：色块预览样式对照旧版 adminlte.skin.js ===== */
.skin-heading {
  margin: 0 0 10px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}
.skin-list {
  display: flex;
  flex-wrap: wrap;
  margin: 0;
  padding: 0;
  list-style: none;
}
.skin-item {
  width: 33.33%;
  padding: 4px;
  box-sizing: border-box;
}
.skin-preview {
  position: relative;
  display: block;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.4);
  text-decoration: none;
}
.skin-preview:hover {
  opacity: 0.85;
}
.skin-preview.active {
  outline: 2px solid var(--el-color-primary);
  outline-offset: -1px;
}
.strip-top,
.strip-bottom {
  display: flex;
  height: 14px;
}
.strip-top .w20 {
  width: 25%;
}
.strip-top .w80 {
  width: 75%;
}
.strip-bottom .w20 {
  width: 25%;
  height: 30px;
}
.strip-bottom .w80.content {
  width: 75%;
  height: 30px;
  background: #f4f5f7;
}
.skin-check {
  position: absolute;
  right: 3px;
  bottom: 3px;
  color: var(--el-color-primary);
  background: #fff;
  border-radius: 50%;
  font-size: 14px;
}
.skin-label {
  margin: 4px 0 0;
  text-align: center;
  font-size: 12px;
  color: #606266;
}
</style>
