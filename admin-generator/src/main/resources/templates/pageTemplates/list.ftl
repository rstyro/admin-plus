<#macro gemTH  name>
	<@compress single_line=true>
		${r"${item."}${name} }
	</@compress>
</#macro>

<#macro notTran  name>
	<@compress single_line=true>
		${r"${"}${name} }
	</@compress>
</#macro>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org" xmlns:sa="http://www.thymeleaf.org/extras/sa-token">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>菜单按钮</title>
    <!-- element-ui 引入样式 -->
    <link rel="stylesheet" href="https://unpkg.com/element-ui/lib/theme-chalk/index.css" th:href="@{/static/element-ui/index.css}">
    <style>
        .container{
            padding: 5px;
        }

        .container .search-box{
            margin-bottom: 20px;
        }

        .container .content-header{
            margin-bottom: 10px;
        }

        .container .content-header .content-header-right {
            text-align: right;
        }
    </style>
</head>
<body>

<div id="app" class="container">
    <el-card class="box-card search-box" v-show="showSearch">
        <el-form :inline="true" :model="formDto" class="demo-form-inline">
            <el-form-item label="关键词检索">
                <el-input v-model="formDto.keyword" placeholder="请输入关键词"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="searchList">查询</el-button>
            </el-form-item>
        </el-form>
    </el-card>

    <el-card class="box-card">
        <section class="content-header">
            <el-row :gutter="2">
                <el-col :span="11">
                    <el-button sa:hasPermissionOr="${package.ModuleName}:${table.entityPath}:list:add,admin" @click="add" type="success" size="mini" icon="el-icon-plus">新增</el-button>
                    <el-button sa:hasPermissionOr="${package.ModuleName}:${table.entityPath}:list:del,admin" @click="batchDel" type="danger" size="mini" icon="el-icon-delete">删除</el-button>
                </el-col>
                <div class="content-header-right">
                    <el-button type="primary" @click="showSearchBox" size="mini" icon="el-icon-search" circle></el-button>
                </div>
            </el-row>

        </section>

        <!-- Main content -->
        <section class="content">
            <el-table
                    :data="tableData"
                    style="width: 100%;margin-bottom: 20px;"
                    row-key="id"
                    border
                    @selection-change="handleSelectionChange"
            >
                <el-table-column type="selection" width="55"></el-table-column>
                <#list table.fields as column>
                    <el-table-column prop="${column.propertyName}" label="<#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if>" width="<#if column.propertyName?contains('Time')>160<#else>100</#if>" ></el-table-column>
                </#list>
                <el-table-column
                        fixed="right"
                        label="操作"
                        width="140">
                    <template slot-scope="scope">
                        <el-tooltip sa:hasPermissionOr="${package.ModuleName}:${table.entityPath}:list:edit,admin" class="item" effect="dark" content="编辑" placement="top-start">
                            <el-button @click="edit(scope.row)" type="primary" icon="el-icon-edit" size="mini"
                                       circle></el-button>
                        </el-tooltip>
                        <el-tooltip sa:hasPermissionOr="${package.ModuleName}:${table.entityPath}:list:del,admin" class="item" effect="dark" content="删除" placement="top-start">
                            <el-button v-if="scope.row.username != 'admin'" @click="del(scope.row.id)" type="danger" icon="el-icon-delete" size="mini"
                                       circle></el-button>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </el-table>

            <div class="page-box" v-show="pageDto.pageCount>1">
                <el-pagination
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"
                        @prev-click="prePage"
                        @next-click="nextPage"
                        :current-page="pageDto.currentPage"
                        :page-size="pageDto.pagerCount"
                        :page-count="pageDto.pageCount"
                        :pager-count="pageDto.pagerCount"
                        layout="sizes,prev, pager, next"
                        :page-sizes="[10, 20, 30, 40, 50, 100]"
                        :total="pageDto.total">
                </el-pagination>
            </div>
        </section>
    </el-card>


    <el-dialog :title="dialogTitle" :visible.sync="addDialogVisible">
        <el-form :model="itemData" :rules="rules" ref="itemData">

            <#list table.fields as column>
                <#if column.keyFlag >
                    <el-form-item label="${column.propertyName}" prop="${column.propertyName}" :label-width="formLabelWidth" style="display: none">
                        <el-input v-model="itemData.${column.propertyName}" autocomplete="off"></el-input>
                    </el-form-item>
                </#if>
            </#list>
            <#list table.fields as column>
                <#if column.propertyName != 'isDeleted' && !column.keyFlag>

                    <#if column.propertyName?contains('Time')>
                        <el-form-item label="<#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if>" prop="${column.propertyName}" :label-width="formLabelWidth">
                            <el-date-picker
                                    v-model="itemData.${column.propertyName}"
                                    align="right"
                                    type="datetime"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    placeholder="选择日期"
                                    :picker-options="pickerOptions">
                            </el-date-picker>
                        </el-form-item>
                    <#else>
                        <el-form-item label="<#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if>" prop="${column.propertyName}" :label-width="formLabelWidth">
                            <el-input v-model="itemData.${column.propertyName}" autocomplete="off" placeholder="请输入<#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if>" ></el-input>
                        </el-form-item>
                    </#if>
                </#if>
            </#list>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveOrUpdate">{{dialogTitle}}</el-button>
        </div>
    </el-dialog>

</div>
<!-- element-ui -->
<script src="https://unpkg.com/vue@2/dist/vue.js" th:src="@{/static/vue/vue.js}"></script>
<script src="https://cdn.jsdelivr.net/npm/axios@1.1.2/dist/axios.min.js" th:src="@{/static/vue/axios.min.js}"></script>
<script src="https://unpkg.com/element-ui/lib/index.js" th:src="@{/static/element-ui/index.js}"></script>
<script th:src="@{/static/vue/api.js}"></script>
<script type="text/javascript" th:inline="javascript">
    let ctx = [[@{/}]];
        new Vue({
            el: '#app',
            data: function () {
                return {
                    listUrl: ctx + '${package.ModuleName}/${table.entityPath}/list',
                    delUrl: ctx + '${package.ModuleName}/${table.entityPath}/del?id=',
                    batchDelUrl: ctx + '${package.ModuleName}/${table.entityPath}/batchDel',
                    addUrl: ctx + '${package.ModuleName}/${table.entityPath}/add',
                    editUrl: ctx + '${package.ModuleName}/${table.entityPath}/edit',
                    dialogUrl:'',
                    tableData: [],
                    multipleSelection: [],
                    formDto: {
                        keyword: '',
                        region: ''
                    },
                    showSearch: false,
                    pageDto: {
                        currentPage: 1,
                        pageSize: 10,
                        total: 110,
                        pageCount: 1, //总页数
                        pagerCount: 9, //页码按钮的数量
                    },
                    addDialogVisible: false,
                    itemData: {
                        menuIdList:[],
                    },
                    dialogTitle: '新增',
                    formLabelWidth: '100px',
                    formTimeLabelWidth: '120px',
                    pickerOptions: {
                        disabledDate(time) {
                            return time.getTime() > Date.now();
                        },
                        shortcuts: [{
                            text: '今天',
                            onClick(picker) {
                                picker.$emit('pick', new Date());
                            }
                        }, {
                            text: '昨天',
                            onClick(picker) {
                                const date = new Date();
                                date.setTime(date.getTime() - 3600 * 1000 * 24);
                                picker.$emit('pick', date);
                            }
                        }, {
                            text: '一周前',
                            onClick(picker) {
                                const date = new Date();
                                date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                                picker.$emit('pick', date);
                            }
                        }]
                    },
                    rules: {
                        <#list table.fields as column>
                            <#if column.propertyName != 'isDeleted' && !column.keyFlag>
                                <#if column.propertyName?contains('Time')>

                                <#else>
                                    ${column.propertyName}: [
                                        { required: true, message: '请输入<#if column.comment == ''>${column.propertyName}<#else>${column.comment}</#if>', trigger: 'blur' },
                                    ],
                                </#if>
                            </#if>
                        </#list>
                    }

                }
            },
            methods: {
                searchList: function () {
                    // 列表检索
                    let url = this.listUrl + '?pageNum=' + this.pageDto.currentPage + '&pageSize=' + this.pageDto.pageSize;
                    axios.post(url, this.formDto).then(r => {
                        if (r.data.code === 200) {
                            let data = r.data.data;
                            this.pageDto.total = Number(data.total);
                            this.pageDto.pageCount = Number(data.pages);
                            this.tableData = data.records;
                        }
                    }).catch(error => {
                        console.error("请求失败：", error);
                        ELEMENT.Message({
                            showClose: true,
                            message: "请求失败，请稍后重试",
                            type: 'error'
                        });
                    })
                },
                prePage() {
                    this.pageDto.currentPage -= 1;
                    this.searchList();
                },
                nextPage() {
                    this.pageDto.currentPage += 1;
                    this.searchList();
                },
                handleSizeChange(val) {
                    this.pageDto.pageSize=val;
                    this.searchList();
                },
                handleCurrentChange(val) {
                    this.pageDto.currentPage=val;
                    this.searchList();
                },
                handleSelectionChange(val) {
                    //当选择项发生变化时会触发该事件
                    this.multipleSelection = val;
                },
                showSearchBox() {
                    this.showSearch = !this.showSearch;
                },
                add() {
                    this.addDialogVisible = true;
                    this.dialogTitle = '新增';
                    this.dialogUrl=this.addUrl;
                    this.itemData = {};
                },
                edit(row) {
                    this.addDialogVisible = true;
                    this.dialogTitle = '编辑';
                    this.itemData = row;
                    this.dialogUrl=this.editUrl;
                },
                saveOrUpdate(){
                    this.$refs['itemData'].validate((valid) => {
                        if (valid) {
                            requestPost(this.dialogUrl, this.itemData);
                        } else {
                            return false;
                        }
                    });
                },
                batchDel() {
                    let ids = this.multipleSelection.map(i => i.id);
                    if (!ids.length) {
                        // 数组为空
                        ELEMENT.Message({
                            showClose: true,
                            message: "您未选中任何内容",
                            type: 'warning'
                        });
                        return false;
                    }
                    ELEMENT.MessageBox.confirm('确定删除选中的列表数据?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        requestPost(this.batchDelUrl, ids);
                    }).catch(() => {
                        ELEMENT.Message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
                },
                del(id) {
                    ELEMENT.MessageBox.confirm('确定删除该数据吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        requestGet(this.delUrl + id);
                    }).catch(() => {
                        ELEMENT.Message({
                            type: 'info',
                            message: '已取消删除'
                        });
                    });
                }
            }, created() {
                this.searchList();
            }
        });
</script>

</body>
</html>
