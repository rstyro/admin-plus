<script setup lang="ts">
/**
 * @file 用户管理页
 * @description 由旧版 templates/system/user.html 重构而来
 *   - 使用 Element Plus 替代 Element UI 2
 *   - 使用 useCrud composable 复用通用 CRUD 逻辑
 *   - v-permission 替代 sa:hasPermissionOr
 *   - 头像上传走 /upload/image（vite proxy 转发到后端）
 *   - 授权角色弹窗独立于新增/编辑弹窗
 */
import { nextTick, ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules, UploadProps } from 'element-plus'
import { Plus, Delete, Edit, Search, Avatar } from '@element-plus/icons-vue'
import { useCrud } from '@/composables/useCrud'
import {
  getUserPage,
  addUser,
  editUser,
  delUser,
  batchDelUser,
  type SysUser,
} from '@/api/system/user'
import { getUserRole, editUserRole, type UserRoleVo } from '@/api/system/role'

defineOptions({ name: 'SystemUser' })

/** CRUD 主体逻辑 */
const {
  tableData,
  formDto,
  showSearch,
  pageDto,
  dialogVisible,
  dialogTitle,
  itemData,
  loading,
  handleQuery,
  handleSizeChange,
  handleCurrentChange,
  handleSelectionChange,
  showSearchBox,
  openAdd,
  openEdit,
  saveOrUpdate,
  del,
  batchDel,
} = useCrud<SysUser, { keyword: string }>({
  list: getUserPage,
  add: addUser,
  edit: editUser,
  del: delUser,
  batchDel: batchDelUser,
  defaultQuery: { keyword: '' },
  defaultItem: () => ({ username: '', nickName: '', password: '', avatar: '' }),
})

/* ---------------- 新增/编辑表单校验 ---------------- */

/** 表单实例 */
const formRef = ref<FormInstance>()
/** 校验规则与旧版 user.html 一致：昵称与用户名必填 */
const rules: FormRules = {
  nickName: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
}

/** 提交前先走表单校验 */
async function submit() {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }
  await saveOrUpdate()
}

/** 打开弹窗后清掉上一次的校验残留 */
async function clearFormValidate() {
  await nextTick()
  formRef.value?.clearValidate()
}

function openAddItem() {
  openAdd()
  clearFormValidate()
}

function openEditItem(row: SysUser) {
  openEdit(row)
  clearFormValidate()
}

/* ---------------- 授权角色弹窗 ---------------- */

/** 授权弹窗显隐 */
const roleDialogVisible = ref(false)
/** 当前要授权的用户 ID */
const currentUserId = ref<number>(0)
/** 可选角色列表（来自 getUserRole） */
const roleData = ref<UserRoleVo[]>([])
/** 已勾选的角色 id 列表 */
const checkedRoleIds = ref<number[]>([])
/** 授权弹窗加载中 */
const roleLoading = ref(false)

/**
 * 打开授权弹窗
 * @description 拉取该用户已有角色，初始勾选 hasRole=true 的项
 */
async function showRoleDialog(row: SysUser) {
  if (!row.id) return
  currentUserId.value = row.id
  roleDialogVisible.value = true
  roleLoading.value = true
  try {
    const res = await getUserRole(row.id)
    roleData.value = res.data
    checkedRoleIds.value = res.data.filter((r) => r.hasRole).map((r) => r.id)
  } finally {
    roleLoading.value = false
  }
}

/**
 * 提交授权
 */
async function grantPermission() {
  await editUserRole({
    userId: currentUserId.value,
    roleIdList: checkedRoleIds.value,
  })
  ElMessage.success('授权成功')
  roleDialogVisible.value = false
}

/* ---------------- 头像上传 ---------------- */

/**
 * 上传前校验：仅 jpg/png，且 <10MB
 */
const beforeAvatarUpload: UploadProps['beforeUpload'] = (file) => {
  const isImage = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isImage) ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
  if (!isLt10M) ElMessage.error('上传头像图片大小不能超过 10MB!')
  return isImage && isLt10M
}

/**
 * 上传成功回调
 * @description 后端 R 结构 {code, msg, data}，data 是图片路径
 */
const onUploadSuccess: UploadProps['onSuccess'] = (response: { code: number; data: string }) => {
  if (response.code === 200) {
    itemData.avatar = response.data
  } else {
    ElMessage.error('上传失败，请稍后重试')
  }
}
</script>

<template>
  <div class="container">
    <!-- 搜索栏 -->
    <el-card v-if="showSearch" class="search-box" shadow="never">
      <el-form :inline="true" :model="formDto">
        <el-form-item label="关键词检索">
          <el-input v-model="formDto.keyword" placeholder="请输入关键词" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <!-- 操作栏 -->
      <div class="content-header">
        <el-button v-permission="'system:user:list:add'" type="success" :icon="Plus" @click="openAddItem">
          新增
        </el-button>
        <el-button v-permission="'system:user:list:del'" type="danger" :icon="Delete" @click="batchDel">
          删除
        </el-button>
        <div class="content-header-right">
          <el-button type="primary" :icon="Search" circle @click="showSearchBox" />
        </div>
      </div>

      <!-- 表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="nickName" label="用户昵称" width="120" />
        <el-table-column prop="username" label="用户名" width="120" sortable />
        <el-table-column prop="avatar" label="用户头像" width="100">
          <template #default="scope">
            <el-avatar :size="35" :src="scope.row.avatar" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="用户状态" width="100" />
        <el-table-column prop="loginIp" label="最后登录IP" width="140" />
        <el-table-column prop="loginDate" label="最后登录时间" width="180" />
        <el-table-column prop="updateTime" label="更新时间" width="180" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column fixed="right" label="操作" width="200">
          <template #default="scope">
            <el-tooltip content="编辑用户" placement="top">
              <el-button
                v-permission="'system:user:list:edit'"
                type="primary"
                :icon="Edit"
                circle
                @click="openEditItem(scope.row)"
              />
            </el-tooltip>
            <el-tooltip content="给用户赋予角色" placement="top">
              <el-button
                v-permission="'system:user:list:grant'"
                type="success"
                :icon="Avatar"
                circle
                @click="showRoleDialog(scope.row)"
              />
            </el-tooltip>
            <el-tooltip v-if="scope.row.username !== 'admin'" content="删除用户" placement="top">
              <el-button
                v-permission="'system:user:list:del'"
                type="danger"
                :icon="Delete"
                circle
                @click="del(scope.row.id as number)"
              />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-if="pageDto.pageCount > 1"
        class="page-box"
        :current-page="pageDto.currentPage"
        :page-size="pageDto.pageSize"
        :total="pageDto.total"
        :page-sizes="[10, 20, 30, 40, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="560px">
      <el-form ref="formRef" :model="itemData" :rules="rules" label-width="100px">
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="itemData.nickName" />
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="itemData.username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="itemData.password" show-password placeholder="不修改则留空" />
        </el-form-item>
        <el-form-item label="头像">
          <el-input v-model="itemData.avatar" placeholder="可上传或填写 URL" />
          <el-upload
            class="avatar-uploader"
            drag
            :limit="1"
            :action="'/upload/image'"
            :before-upload="beforeAvatarUpload"
            :on-success="onUploadSuccess"
            list-type="picture"
          >
            <el-icon class="el-icon--upload"><Plus /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <template #tip>
              <div class="el-upload__tip">只能上传 jpg/png 文件，且不超过 10MB</div>
            </template>
          </el-upload>
          <el-avatar v-if="itemData.avatar" :size="60" :src="itemData.avatar" class="avatar-preview" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 授权弹窗 -->
    <el-dialog v-model="roleDialogVisible" title="授权" width="480px">
      <el-form v-loading="roleLoading" label-width="100px">
        <el-form-item label="可选角色:">
          <el-checkbox-group v-model="checkedRoleIds">
            <ul class="role-list">
              <li v-for="role in roleData" :key="role.id">
                <el-tooltip :content="role.remark || role.roleName" placement="top">
                  <el-checkbox :value="role.id">{{ role.roleName }}</el-checkbox>
                </el-tooltip>
              </li>
            </ul>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="grantPermission">授权</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.container {
  padding: 8px;
}
.search-box {
  margin-bottom: 12px;
}
.content-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.content-header-right {
  margin-left: auto;
}
.page-box {
  margin-top: 12px;
  display: flex;
  justify-content: flex-end;
}
.avatar-uploader {
  margin-top: 8px;
}
.avatar-preview {
  margin-top: 8px;
}
.role-list {
  list-style: none;
  padding: 0;
}
.role-list li {
  margin-bottom: 8px;
}
</style>
