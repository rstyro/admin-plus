<script setup lang="ts">
/**
 * @file 登录日志页
 * @description 由旧版 templates/page/system/sysLoginInfo_list.html 重构而来
 *   日志只读 + 删除，没有新增/编辑
 */
import { Delete, Search } from '@element-plus/icons-vue'
import { useCrud } from '@/composables/useCrud'

defineOptions({ name: 'LoginInfo' })
import {
  getLoginInfoPage,
  delLoginInfo,
  batchDelLoginInfo,
  type SysLoginInfo,
} from '@/api/system/log'

const {
  tableData,
  formDto,
  showSearch,
  pageDto,
  loading,
  handleQuery,
  handleSizeChange,
  handleCurrentChange,
  handleSelectionChange,
  showSearchBox,
  del,
  batchDel,
} = useCrud<SysLoginInfo, { keyword: string }>({
  list: getLoginInfoPage,
  del: delLoginInfo,
  batchDel: batchDelLoginInfo,
  defaultQuery: { keyword: '' },
  defaultItem: () => ({}) as SysLoginInfo,
})
</script>

<template>
  <div class="container">
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
      <div class="content-header">
        <el-button
          v-permission="'system:sysLoginInfo:list:del'"
          type="danger"
          :icon="Delete"
          @click="batchDel"
        >
          删除
        </el-button>
        <div class="content-header-right">
          <el-button type="primary" :icon="Search" circle @click="showSearchBox" />
        </div>
      </div>

      <el-table
        v-loading="loading"
        :data="tableData"
        border
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="访问ID" width="80" />
        <el-table-column prop="loginName" label="登录账号" width="120" />
        <el-table-column prop="ipaddr" label="登录IP地址" width="140" />
        <el-table-column prop="location" label="登录地点" width="120" />
        <el-table-column prop="browser" label="浏览器类型" width="120" />
        <el-table-column prop="os" label="操作系统" width="140" />
        <el-table-column prop="status" label="登录状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="msg" label="提示消息" />
        <el-table-column prop="createTime" label="访问时间" width="180" />
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-tooltip content="删除" placement="top">
              <el-button
                v-permission="'system:sysLoginInfo:list:del'"
                type="danger"
                :icon="Delete"
                circle
                @click="del(scope.row.id as number)"
              />
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

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
