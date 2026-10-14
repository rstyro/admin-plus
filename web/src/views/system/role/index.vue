<script setup lang="ts">
/**
 * @file 角色管理页
 * @description 由旧版 templates/system/role.html 迁移而来
 *   - 使用 Element Plus 替代 Element UI 2
 *   - 使用 useCrud composable 复用通用 CRUD 逻辑
 *   - v-permission 替代 sa:hasPermissionOr
 *   - 弹窗内 el-tree 实现菜单权限分配，编辑时根据后端返回的 hasRoleFlag 回显勾选
 */
import { nextTick, ref } from 'vue'
import { Plus, Delete, Edit, Search } from '@element-plus/icons-vue'
import { ElTree } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { useCrud } from '@/composables/useCrud'
import {
  getRolePage,
  addRole,
  editRole,
  delRole,
  batchDelRole,
  type SysRole,
  type SysRoleDto,
} from '@/api/system/role'
import { getRoleMenuList, type RoleMenuVo } from '@/api/system/menu'

defineOptions({ name: 'SystemRole' })

/** 菜单权限树 ref 实例（用于回显与收集勾选） */
const treeRef = ref<InstanceType<typeof ElTree>>()
/** 表单 ref 实例（用于提交前校验） */
const formRef = ref<FormInstance>()
/** 菜单权限树数据 */
const roleMenuList = ref<RoleMenuVo[]>([])
/** 菜单权限树加载中 */
const treeLoading = ref(false)
/** 是否关闭父子联动（回显期间为 true，避免父级勾选导致子级全部被勾选） */
const checkStrictly = ref(false)
/** el-tree 的 props 配置：children 字段为 child，label 字段为 menuName */
const treeProps = { children: 'child', label: 'menuName' }

/** 表单校验规则（与旧版一致：角色名称必填） */
const rules: FormRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
}

/** 初始化 CRUD：注入 role 的 API 函数 */
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
} = useCrud<SysRole, SysRoleDto>({
  list: getRolePage,
  add: addRole,
  edit: editRole,
  del: delRole,
  batchDel: batchDelRole,
  defaultQuery: { keyword: '' },
  defaultItem: () => ({ roleName: '', remark: '', menuIdList: [] }),
})

/**
 * 递归收集树中已勾选（hasRoleFlag 为 true）的节点 id 列表
 * @description 编辑回显时用于设置树的默认勾选项
 */
function getTreeDefaultCheck(data: RoleMenuVo[]): number[] {
  const checkedList: number[] = []
  for (const node of data) {
    if (node.hasRoleFlag) {
      checkedList.push(node.id)
    }
    if (node.child && node.child.length > 0) {
      checkedList.push(...getTreeDefaultCheck(node.child))
    }
  }
  return checkedList
}

/**
 * 请求角色对应的菜单树并回显已选项
 * @param roleId 角色 ID，0 表示新增（查整棵菜单树，无勾选）
 */
async function requestRoleMenuList(roleId: number) {
  treeLoading.value = true
  // 回显期间关闭父子联动，防止父级勾选导致子级全部被勾选
  checkStrictly.value = true
  try {
    const res = await getRoleMenuList(roleId)
    roleMenuList.value = res.data
    // 等树渲染完成后再设置勾选，避免 ref 未就绪
    await nextTick()
    // 根据后端返回的 hasRoleFlag 收集默认勾选项（新增场景为空）
    const checkedIds = getTreeDefaultCheck(res.data)
    treeRef.value?.setCheckedKeys(checkedIds, false)
  } finally {
    treeLoading.value = false
    // 回显完成后延时恢复联动，子级选中时父级呈半选状态
    setTimeout(() => {
      checkStrictly.value = false
    }, 300)
  }
}

/** 打开新增弹窗：调用 useCrud 的 openAdd，再加载整棵菜单树 */
function handleAdd() {
  openAdd()
  requestRoleMenuList(0)
}

/** 打开编辑弹窗：调用 useCrud 的 openEdit，再加载该角色的菜单树回显 */
function handleEdit(row: SysRole) {
  openEdit(row)
  requestRoleMenuList(row.id as number)
}

/** 提交新增/编辑：表单校验通过后收集树勾选项，赋给 menuIdList 再保存 */
async function handleSubmit() {
  // 表单校验（ref 存在则校验，不通过则中止提交）
  try {
    await formRef.value?.validate()
  } catch {
    return
  }
  // 保存时恢复联动，保证 getCheckedNodes 能取到半选父级
  checkStrictly.value = false
  // 获取选中的菜单节点（含半选），收集 id 列表
  const checkedNodes = treeRef.value?.getCheckedNodes(false, true) ?? []
  itemData.menuIdList = (checkedNodes as unknown as RoleMenuVo[]).map((n) => n.id)
  await saveOrUpdate()
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
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <!-- 操作栏 -->
      <div class="content-header">
        <el-button v-permission="'system:role:list:add'" type="success" :icon="Plus" @click="handleAdd">
          新增
        </el-button>
        <el-button v-permission="'system:role:list:del'" type="danger" :icon="Delete" @click="batchDel">
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
        <el-table-column prop="roleName" label="角色名称" />
        <el-table-column prop="remark" label="备注" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column fixed="right" label="操作" width="160">
          <template #default="scope">
            <el-tooltip content="编辑角色" placement="top">
              <el-button
                v-permission="'system:role:list:edit'"
                type="primary"
                :icon="Edit"
                circle
                @click="handleEdit(scope.row)"
              />
            </el-tooltip>
            <el-tooltip content="删除角色" placement="top">
              <el-button
                v-permission="'system:role:list:del'"
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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="itemData.roleName" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="itemData.remark" type="textarea" :rows="2" />
        </el-form-item>
        <el-form-item label="权限控制">
          <!-- 菜单权限树：show-checkbox 多选，node-key 用 id，check-strictly 回显期间关闭联动 -->
          <div v-loading="treeLoading" class="menu-tree">
            <el-tree
              ref="treeRef"
              :data="roleMenuList"
              show-checkbox
              node-key="id"
              :check-on-click-node="true"
              :check-strictly="checkStrictly"
              :props="treeProps"
            />
          </div>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="itemData.createTime"
            type="datetime"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择日期"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
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
/* 菜单权限树容器：限制高度并支持内部滚动，避免弹窗过高 */
.menu-tree {
  max-height: 280px;
  overflow: auto;
  width: 100%;
}
</style>
