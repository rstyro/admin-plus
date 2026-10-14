<script setup lang="ts">
/**
 * @file 菜单管理页
 * @description 由旧版 templates/system/menu.html 重构而来
 *   - 使用 Element Plus 替代 Element UI 2
 *   - 使用 useCrud composable 复用通用 CRUD 逻辑
 *   - 树形表格（tree-props.children = 'child'）展示菜单层级：
 *     菜单名称列置于首个普通列（内置展开箭头固定渲染在第一个普通列），
 *     配 default-expand-all + 展开/折叠全部切换按钮
 *   - el-tree-select 替代旧版 el-select + el-tree 组合实现父级菜单选择
 *   - v-permission 替代 sa:hasPermissionOr
 */
import { computed, onMounted, ref } from 'vue'
import type { TableInstance } from 'element-plus'
import {
  Plus,
  Delete,
  Edit,
  Search,
  Expand,
  Fold,
  FolderOpened,
  Document,
  Mouse,
} from '@element-plus/icons-vue'
import MenuIcon from '@/components/MenuIcon.vue'
import IconPicker from '@/components/IconPicker.vue'
import { isEpIconName } from '@/utils/epIcons'
import { useCrud } from '@/composables/useCrud'
import {
  getMenuPage,
  addMenu,
  editMenu,
  delMenu,
  batchDelMenu,
  type SysMenu,
  type MenuType,
} from '@/api/system/menu'

defineOptions({ name: 'SystemMenu' })

/** 新增菜单的默认项工厂 */
function defaultMenu(): SysMenu {
  return {
    menuName: '',
    menuType: 0,
    parentId: 0,
    menuUrl: '',
    permit: '',
    sortNum: 0,
    icon: '',
    addBtn: false,
  }
}

/** 初始化 CRUD：注入 menu 的 API 函数 */
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
} = useCrud<SysMenu, { keyword: string }>({
  list: getMenuPage,
  add: addMenu,
  edit: editMenu,
  del: delMenu,
  batchDel: batchDelMenu,
  defaultQuery: { keyword: '' },
  defaultItem: defaultMenu,
})

/**
 * 父级菜单候选：全量菜单（旧版同样用 pageSize=1000 取全表）
 * @description 不能用表格当前页数据，否则分页时父级下拉会缺项
 */
const allMenus = ref<SysMenu[]>([])

/** 拉取全量菜单用于父级选择树 */
async function loadAllMenus() {
  try {
    const res = await getMenuPage(1, 1000, {})
    allMenus.value = res.data.records
  } catch {
    // 失败提示由响应拦截器处理
  }
}

onMounted(loadAllMenus)

/**
 * 父级菜单选择树数据
 * @description 递归过滤掉 menuType===2（按钮）的节点，
 *   按钮不能作为父级；同时挂一个虚拟根节点（id=0 表示顶级菜单），
 *   便于在 el-tree-select 中显式选择"顶级菜单"
 */
const parentMenuOptions = computed<SysMenu[]>(() => {
  // 递归过滤：剔除按钮类型节点，并清理空 child
  const filterTree = (nodes: SysMenu[]): SysMenu[] =>
    nodes
      .filter((n) => n.menuType !== 2)
      .map((n) => ({
        ...n,
        child: n.child ? filterTree(n.child) : undefined,
      }))
  return [
    {
      id: 0,
      menuName: '顶级菜单',
      menuType: 0,
      child: filterTree(allMenus.value),
    },
  ]
})

/**
 * 打开"添加子菜单"弹窗
 * @description 预填 parentId，复用 useCrud 返回的 itemData 与弹窗状态
 */
function openAddChild(row: SysMenu) {
  Object.assign(itemData, defaultMenu(), { parentId: row.id })
  dialogTitle.value = '新增子菜单'
  dialogVisible.value = true
}

/** 提交：后端按 child 字段递归处理，保存前清空避免把表格树数据带进去（与旧版一致） */
async function submit() {
  itemData.child = []
  await saveOrUpdate()
  loadAllMenus()
}

/** 菜单类型映射：用于 el-tag 显示文案与颜色 */
const menuTypeMap: Record<MenuType, { label: string; type: 'info' | 'success' | 'warning' }> = {
  0: { label: '目录', type: 'info' },
  1: { label: '菜单', type: 'success' },
  2: { label: '按钮', type: 'warning' },
}

/**
 * 树形表格展开控制
 * @description el-table 没有内置"全部展开/折叠"，需拿表格实例逐行 toggleRowExpansion；
 *   表格 default-expand-all 初始全展开，所以 allExpanded 初值为 true
 */
const tableRef = ref<TableInstance>()
/** 是否存在任意可展开行（有子节点的目录/菜单），决定按钮是否出现 */
const hasTree = computed(() => collectExpandable(tableData.value).length > 0)

/** 递归收集所有带子节点的行 */
function collectExpandable(nodes: SysMenu[]): SysMenu[] {
  const out: SysMenu[] = []
  const walk = (list: SysMenu[]) => {
    list.forEach((n) => {
      if (n.child && n.child.length) {
        out.push(n)
        walk(n.child)
      }
    })
  }
  walk(nodes)
  return out
}

const allExpanded = ref(true)
function toggleExpandAll() {
  const table = tableRef.value
  if (!table) return
  allExpanded.value = !allExpanded.value
  collectExpandable(tableData.value).forEach((row) =>
    table.toggleRowExpansion(row, allExpanded.value)
  )
}
</script>

<template>
  <div class="container">
    <!-- 搜索栏 -->
    <el-card v-if="showSearch" class="search-box" shadow="never">
      <el-form :inline="true" :model="formDto">
        <el-form-item label="关键词检索">
          <el-input v-model="formDto.keyword" placeholder="请输入菜单名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never">
      <!-- 操作栏 -->
      <div class="content-header">
        <el-button v-permission="'system:menu:list:add'" type="success" :icon="Plus" @click="openAdd">
          新增
        </el-button>
        <el-button v-permission="'system:menu:list:del'" type="danger" :icon="Delete" @click="batchDel">
          删除
        </el-button>
        <div class="content-header-right">
          <el-button
            v-if="hasTree"
            :icon="allExpanded ? Fold : Expand"
            @click="toggleExpandAll"
          >
            {{ allExpanded ? '折叠全部' : '展开全部' }}
          </el-button>
          <el-button type="primary" :icon="Search" circle @click="showSearchBox" />
        </div>
      </div>

      <!-- 树形表格：子菜单在 child 字段 -->
      <el-table
        ref="tableRef"
        v-loading="loading"
        :data="tableData"
        border
        row-key="id"
        :indent="24"
        :tree-props="{ children: 'child' }"
        default-expand-all
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <!-- 菜单名称放最前：el-table 的树形展开箭头固定渲染在第一个普通列，
             放 ID 列会导致箭头与文字脱节；名称列自带缩进层级与类型图标 -->
        <el-table-column label="菜单名称" min-width="240">
          <template #default="scope">
            <span class="menu-name-cell">
              <el-icon class="type-icon" :class="'type-' + scope.row.menuType">
                <FolderOpened v-if="scope.row.menuType === 0" />
                <Mouse v-else-if="scope.row.menuType === 2" />
                <Document v-else />
              </el-icon>
              <span>{{ scope.row.menuName }}</span>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="menuUrl" label="菜单地址" />
        <!-- 菜单类型：用 el-tag 显示目录/菜单/按钮 -->
        <el-table-column label="菜单类型" width="100">
          <template #default="scope">
            <el-tag :type="menuTypeMap[(scope.row.menuType ?? 0) as MenuType].type">
              {{ menuTypeMap[(scope.row.menuType ?? 0) as MenuType].label }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permit" label="权限Key" />
        <el-table-column prop="sortNum" label="排序" width="80" />
        <!-- icon 列：存 Element Plus 图标名，命中则渲染预览，脏数据原样显示 -->
        <el-table-column label="icon" width="150">
          <template #default="scope">
            <span v-if="isEpIconName(scope.row.icon)" class="icon-preview">
              <MenuIcon :icon="scope.row.icon" />
              <span class="icon-name">{{ scope.row.icon }}</span>
            </span>
            <span v-else-if="scope.row.icon" class="icon-raw">{{ scope.row.icon }}</span>
            <span v-else class="icon-empty">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column fixed="right" label="操作" width="220">
          <template #default="scope">
            <!-- 添加子菜单：仅目录/菜单可加子，按钮不可 -->
            <el-tooltip
              v-if="scope.row.menuType !== 2"
              content="添加子菜单"
              placement="top"
            >
              <el-button
                v-permission="'system:menu:list:add'"
                type="warning"
                :icon="Plus"
                circle
                @click="openAddChild(scope.row)"
              />
            </el-tooltip>
            <el-tooltip content="编辑" placement="top">
              <el-button
                v-permission="'system:menu:list:edit'"
                type="primary"
                :icon="Edit"
                circle
                @click="openEdit(scope.row)"
              />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button
                v-permission="'system:menu:list:del'"
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="itemData" label-width="100px">
        <!-- 父级菜单：el-tree-select 替代旧版 el-select+el-tree 组合 -->
        <el-form-item label="父级菜单">
          <el-tree-select
            v-model="itemData.parentId"
            :data="parentMenuOptions"
            :props="{ children: 'child', label: 'menuName' }"
            node-key="id"
            check-strictly
            clearable
            placeholder="请选择父级菜单"
            style="width: 100%"
          />
        </el-form-item>
        <!-- 菜单名称 -->
        <el-form-item label="菜单名称">
          <el-input v-model="itemData.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <!-- 菜单类型：0=目录 1=菜单 2=按钮 -->
        <el-form-item label="菜单类型">
          <el-radio-group v-model="itemData.menuType">
            <el-radio :value="0">目录</el-radio>
            <el-radio :value="1">菜单</el-radio>
            <el-radio :value="2">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 菜单地址：仅菜单类型显示 -->
        <el-form-item v-if="itemData.menuType === 1" label="菜单地址">
          <el-input v-model="itemData.menuUrl" placeholder="请输入菜单地址" />
        </el-form-item>
        <!-- 权限Key：非目录（菜单/按钮）显示 -->
        <el-form-item v-if="itemData.menuType !== 0" label="权限Key">
          <el-input v-model="itemData.permit" placeholder="请输入权限Key" />
        </el-form-item>
        <!-- 排序：仅菜单类型显示 -->
        <el-form-item v-if="itemData.menuType === 1" label="排序">
          <el-input-number v-model="itemData.sortNum" :min="0" />
        </el-form-item>
        <!-- 菜单icon：非按钮（目录/菜单）显示，存 Element Plus 图标名 -->
        <el-form-item v-if="itemData.menuType !== 2" label="菜单icon">
          <IconPicker v-model="itemData.icon" />
        </el-form-item>
        <!-- 添加按钮权限：仅菜单类型显示 -->
        <el-form-item v-if="itemData.menuType === 1" label="添加按钮权限">
          <el-radio-group v-model="itemData.addBtn">
            <el-radio :value="false">否</el-radio>
            <el-radio :value="true">是</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
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

/* 树形表格：菜单名称单元格 */
.menu-name-cell {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  vertical-align: middle;
}
.type-icon {
  font-size: 15px;
}
.type-icon.type-0 {
  color: #e6a23c;
}
.type-icon.type-1 {
  color: var(--el-color-primary);
}
.type-icon.type-2 {
  color: #a8abb2;
}

/* 内置展开箭头美化为圆形按钮（名称列即树形首个普通列） */
:deep(.el-table__expand-icon) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  margin-right: 2px;
  border-radius: 50%;
  color: #909399;
  font-size: 11px;
  transition: all 0.2s;
}
:deep(.el-table__expand-icon:hover) {
  background: var(--el-color-primary-light-9);
  color: var(--el-color-primary);
}
:deep(.el-table__expand-icon .el-icon) {
  transition: transform 0.2s ease-in-out;
}
:deep(.el-table__placeholder) {
  width: 20px;
  margin-right: 2px;
}

/* icon 列预览 */
.icon-preview {
  display: inline-flex;
  align-items: center;
  gap: 2px;
  color: #606266;
  font-size: 14px;
}
.icon-preview .icon-name {
  font-size: 12px;
  color: var(--el-text-color-regular);
}
.icon-raw {
  color: var(--el-text-color-secondary);
  font-size: 12px;
}
.icon-empty {
  color: var(--el-text-color-placeholder);
}
</style>
