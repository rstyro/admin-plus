<script setup lang="ts">
/**
 * @file 按钮管理页
 * @description 由旧版 templates/system/btn.html 重构而来
 *   - 使用 Element Plus 替代 Element UI 2
 *   - 使用 useCrud composable 复用通用 CRUD 逻辑
 *   - v-permission 替代 sa:hasPermissionOr
 */
import { Plus, Delete, Edit, Search } from '@element-plus/icons-vue'
import { useCrud } from '@/composables/useCrud'
import { getBtnPage, addBtn, editBtn, delBtn, batchDelBtn, type SysBtn } from '@/api/system/btn'

defineOptions({ name: 'SystemBtn' })

/** 初始化 CRUD：注入 btn 的 API 函数 */
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
} = useCrud<SysBtn, { keyword: string }>({
  list: getBtnPage,
  add: addBtn,
  edit: editBtn,
  del: delBtn,
  batchDel: batchDelBtn,
  defaultQuery: { keyword: '' },
  defaultItem: () => ({ btnName: '', btnKey: '', remark: '' }),
})
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
        <el-button v-permission="'system:btn:list:add'" type="success" :icon="Plus" @click="openAdd">
          新增
        </el-button>
        <el-button v-permission="'system:btn:list:del'" type="danger" :icon="Delete" @click="batchDel">
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
        <el-table-column prop="btnName" label="按钮名称" />
        <el-table-column prop="btnKey" label="按钮Key" />
        <el-table-column prop="remark" label="按钮说明" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column fixed="right" label="操作" width="160">
          <template #default="scope">
            <el-tooltip content="编辑" placement="top">
              <el-button
                v-permission="'system:btn:list:edit'"
                type="primary"
                :icon="Edit"
                circle
                @click="openEdit(scope.row)"
              />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button
                v-permission="'system:btn:list:del'"
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="itemData" label-width="80px">
        <el-form-item label="按钮名称">
          <el-input v-model="itemData.btnName" />
        </el-form-item>
        <el-form-item label="按钮Key">
          <el-input v-model="itemData.btnKey" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="itemData.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveOrUpdate">确定</el-button>
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
</style>
