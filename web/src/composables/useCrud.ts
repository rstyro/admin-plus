/**
 * @file 通用 CRUD 组合式函数
 * @description 封装列表页通用逻辑：分页、查询、新增/编辑/删除/批量删除
 *   各业务页面（user/role/menu/btn/log）只需传入 api 函数即可复用
 */
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import type { PageResult } from '@/api/system/user'

/** 分页参数 */
export interface PageDto {
  currentPage: number
  pageSize: number
  total: number
  pageCount: number
}

/** 初始分页参数 */
function defaultPageDto(): PageDto {
  return {
    currentPage: 1,
    pageSize: 10,
    total: 0,
    pageCount: 1,
  }
}

/**
 * CRUD 配置：由调用方提供各 API 函数
 */
export interface CrudConfig<T, Q> {
  /** 列表查询：返回 PageResult */
  list: (pageNum: number, pageSize: number, query: Q) => Promise<{ data: PageResult<T> }>
  /** 新增 */
  add?: (item: T) => Promise<unknown>
  /** 编辑 */
  edit?: (item: T) => Promise<unknown>
  /** 删除单个 */
  del?: (id: number) => Promise<unknown>
  /** 批量删除 */
  batchDel?: (ids: number[]) => Promise<unknown>
  /** 查询表单默认值 */
  defaultQuery: Q
  /** 新增项的默认值（工厂函数） */
  defaultItem: () => T
}

/**
 * 通用 CRUD composable
 */
export function useCrud<T extends { id?: number | string }, Q extends object>(
  config: CrudConfig<T, Q>,
) {
  /** 列表数据 */
  const tableData = ref<T[]>([]) as { value: T[] }
  /** 当前选中的行 */
  const multipleSelection = ref<T[]>([])
  /** 查询表单 */
  const formDto = reactive<Q>({ ...config.defaultQuery })
  /** 是否显示搜索栏 */
  const showSearch = ref(false)
  /** 分页参数 */
  const pageDto = reactive<PageDto>(defaultPageDto())
  /** 弹窗显隐 */
  const dialogVisible = ref(false)
  /** 弹窗标题（新增/编辑） */
  const dialogTitle = ref('新增')
  /** 当前编辑项 */
  const itemData = reactive<T>(config.defaultItem())
  /** 列表加载中 */
  const loading = ref(false)

  /**
   * 查询列表
   * @description 失败提示已由 axios 响应拦截器统一弹出，这里只吞掉 rejection，
   *   避免 onMounted 触发的查询产生未捕获的 Promise 异常
   */
  async function searchList() {
    loading.value = true
    try {
      const res = await config.list(pageDto.currentPage, pageDto.pageSize, { ...formDto } as unknown as Q)
      const data = res.data
      pageDto.total = data.total
      pageDto.pageCount = data.pages
      tableData.value = data.records
    } catch {
      // 异常已提示
    } finally {
      loading.value = false
    }
  }

  /** 点击查询：条件变化后回到第一页，对应旧版 searchList */
  function handleQuery() {
    pageDto.currentPage = 1
    searchList()
  }

  /** 每页条数变化 */
  function handleSizeChange(val: number) {
    pageDto.pageSize = val
    pageDto.currentPage = 1
    searchList()
  }
  /** 当前页变化 */
  function handleCurrentChange(val: number) {
    pageDto.currentPage = val
    searchList()
  }
  /** 选中项变化 */
  function handleSelectionChange(val: T[]) {
    multipleSelection.value = val
  }
  /** 切换搜索栏显隐 */
  function showSearchBox() {
    showSearch.value = !showSearch.value
  }

  /**
   * 打开新增弹窗
   */
  function openAdd() {
    Object.assign(itemData, config.defaultItem())
    dialogTitle.value = '新增'
    dialogVisible.value = true
  }

  /**
   * 打开编辑弹窗
   * @description 直接把行数据赋给 itemData（保留引用关系，编辑后表格同步更新）
   */
  function openEdit(row: T) {
    Object.assign(itemData, config.defaultItem(), row)
    dialogTitle.value = '编辑'
    dialogVisible.value = true
  }

  /**
   * 提交新增/编辑
   */
  async function saveOrUpdate() {
    const isEdit = !!itemData.id
    try {
      if (isEdit && config.edit) {
        await config.edit({ ...itemData } as unknown as T)
      } else if (!isEdit && config.add) {
        await config.add({ ...itemData } as unknown as T)
      }
    } catch {
      // 失败提示由响应拦截器统一处理，保持弹窗打开便于修改
      return
    }
    ElMessage.success(isEdit ? '编辑成功' : '新增成功')
    dialogVisible.value = false
    searchList()
  }

  /**
   * 删除单条
   * @description 用户点「取消」时 ElMessageBox 会 reject，需要捕获后直接返回
   */
  async function del(id: number) {
    try {
      await ElMessageBox.confirm('确定删除该数据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
    } catch {
      return
    }
    try {
      if (config.del) await config.del(id)
    } catch {
      return
    }
    ElMessage.success('删除成功')
    searchList()
  }

  /**
   * 批量删除
   */
  async function batchDel() {
    const ids = multipleSelection.value.map((i) => i.id as number)
    if (!ids.length) {
      ElMessage.warning('您未选中任何内容')
      return
    }
    try {
      await ElMessageBox.confirm('确定删除选中的列表数据？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      })
    } catch {
      return
    }
    try {
      if (config.batchDel) await config.batchDel(ids)
    } catch {
      return
    }
    ElMessage.success('删除成功')
    searchList()
  }

  onMounted(() => {
    searchList()
  })

  return {
    tableData,
    multipleSelection,
    formDto,
    showSearch,
    pageDto,
    dialogVisible,
    dialogTitle,
    itemData,
    loading,
    searchList,
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
  }
}
