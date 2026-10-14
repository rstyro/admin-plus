<script setup lang="ts">
/**
 * @file 操作日志页
 * @description 由旧版 templates/page/system/sysOperLog_list.html 重构而来
 *   仅查询 + 查看详情 + 删除，无新增/编辑（与旧版一致）
 */
import { ref } from 'vue'
import { Delete, Search, View } from '@element-plus/icons-vue'
import { useCrud } from '@/composables/useCrud'
import { getOperLogPage, delOperLog, batchDelOperLog, type SysOperLog } from '@/api/system/log'

defineOptions({ name: 'OperLog' })

/** 业务类型映射（与旧版一致的取值） */
const businessTypeMap: Record<number, { label: string; type: 'success' | 'warning' | 'danger' | 'info' | 'primary' }> = {
  0: { label: '其它', type: 'info' },
  1: { label: '新增', type: 'success' },
  2: { label: '修改', type: 'primary' },
  3: { label: '删除', type: 'danger' },
}

/** 查看详情弹窗：数据直接取自行记录，旧版同样不调详情接口 */
const viewVisible = ref(false)
const viewData = ref<SysOperLog>({})

/** 打开详情 */
function view(row: SysOperLog) {
  viewData.value = row
  viewVisible.value = true
}

/** JSON 字符串美化，非法 JSON 时原样展示 */
function formatJSON(text?: string) {
  if (!text) return ''
  try {
    return JSON.stringify(JSON.parse(text), null, 2)
  } catch {
    return text
  }
}

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
} = useCrud<SysOperLog, { keyword: string }>({
  list: getOperLogPage,
  del: delOperLog,
  batchDel: batchDelOperLog,
  defaultQuery: { keyword: '' },
  defaultItem: () => ({}) as SysOperLog,
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
          v-permission="'system:sysOperLog:list:del'"
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
        <el-table-column prop="title" label="模块标题" width="140" />
        <el-table-column prop="businessType" label="业务类型" width="100">
          <template #default="scope">
            <el-tag :type="businessTypeMap[scope.row.businessType]?.type || 'info'">
              {{ businessTypeMap[scope.row.businessType]?.label || '未知' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="requestMethod" label="请求方式" width="100" />
        <el-table-column prop="status" label="操作状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
              {{ scope.row.status === 0 ? '正常' : '异常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operName" label="操作人员" width="120" />
        <el-table-column prop="operIp" label="主机地址" width="140" />
        <el-table-column prop="operLocation" label="操作地点" width="120" />
        <el-table-column prop="operTime" label="操作时间" width="180" />
        <el-table-column prop="costTime" label="消耗时间" width="100">
          <template #default="scope">{{ scope.row.costTime }} ms</template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="120">
          <template #default="scope">
            <el-tooltip content="查看详情" placement="top">
              <el-button
                v-permission="'system:sysOperLog:list:view'"
                type="primary"
                :icon="View"
                circle
                @click="view(scope.row)"
              />
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button
                v-permission="'system:sysOperLog:list:del'"
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

    <!-- 查看详情弹窗（对应旧版 el-descriptions 详情） -->
    <el-dialog v-model="viewVisible" title="查看" width="700px">
      <el-descriptions :column="1" border>
        <el-descriptions-item label="请求地址">{{ viewData.operUrl }}</el-descriptions-item>
        <el-descriptions-item label="请求方法">{{ viewData.method }}</el-descriptions-item>
        <el-descriptions-item label="请求参数">
          <pre class="json-box">{{ formatJSON(viewData.operParam) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="返回参数">
          <pre class="json-box">{{ formatJSON(viewData.jsonResult) }}</pre>
        </el-descriptions-item>
        <el-descriptions-item v-if="viewData.errorMsg" label="错误消息">
          <pre class="json-box error">{{ viewData.errorMsg }}</pre>
        </el-descriptions-item>
      </el-descriptions>
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
