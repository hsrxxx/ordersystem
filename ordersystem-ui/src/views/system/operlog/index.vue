<template>
    <el-main>
        <!--  操作日志查询参数  -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="系统模块" prop="title">
                <el-input
                        v-model="queryParams.title"
                        placeholder="请输入系统模块"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="操作人员" prop="operName">
                <el-input
                        v-model="queryParams.operName"
                        placeholder="请输入操作人员"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="类型" prop="businessType">
                <el-select
                        v-model="queryParams.businessType"
                        placeholder="操作类型"
                        clearable
                        size="small"
                        style="width: 240px"
                >
                    <el-option
                            v-for="item in typeOptions"
                            :key="item.dictValue"
                            :label="item.dictLabel"
                            :value="item.dictValue"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select
                        v-model="queryParams.status"
                        placeholder="操作状态"
                        clearable
                        size="small"
                        style="width: 240px"
                >
                    <el-option
                            v-for="item in statusOptions"
                            :key="item.dictValue"
                            :label="item.dictLabel"
                            :value="item.dictValue"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="操作时间">
                <el-date-picker
                        v-model="dateRange"
                        size="small"
                        style="width: 240px"
                        value-format="yyyy-MM-dd"
                        type="daterange"
                        range-separator="-"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                ></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <!--  权限操作按钮  -->
        <el-row :gutter="10">
            <el-col :span="1.5">
                <el-button type="danger" plain size="mini" icon="el-icon-delete" :disabled="removeDisabled" @click="handleRemove(multipleSelection)">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain size="mini" icon="el-icon-delete" @click="handleClean">清空</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain size="mini" icon="el-icon-download" @click="handleExport">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--  操作日志数据  -->
        <el-table 
            v-loading="loading"
            ref="multipleTable"
            :data="operlogList"
            :header-cell-style="{background:'#f8f8f9'}"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column label="日志编号" align="center" prop="operId" />
            <el-table-column label="系统模块" align="center" prop="title" />
            <el-table-column label="操作类型" align="center" prop="businessType" :formatter="typeFormat" />
            <el-table-column label="请求方式" align="center" prop="requestMethod" />
            <el-table-column label="操作人员" align="center" prop="operName" />
            <el-table-column label="主机" align="center" prop="operIp" width="130" :show-overflow-tooltip="true" />
            <el-table-column label="操作状态" align="center" prop="status" :formatter="statusFormat" />
            <el-table-column label="操作日期" align="center" prop="operTime" width="180" />
            <el-table-column
                fixed="right"
                label="操作"
                align="center"
                class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleView(scope.row)" type="text" size="mini"  icon="el-icon-view">详情</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--  分页器  -->
        <pagination
                v-show="total > 0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList" />

        <!-- 操作日志详情弹框 -->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="50%" :showClose="false" :close-on-click-modal="false">
            <el-form ref="form" :model="form" label-width="100px" size="mini">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="操作模块：">{{ form.title }} / {{ typeFormat(form) }}</el-form-item>
                        <el-form-item
                                label="登录信息："
                        >{{ form.operName }} / {{ form.operIp }}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="请求地址：">{{ form.operUrl }}</el-form-item>
                        <el-form-item label="请求方式：">{{ form.requestMethod }}</el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="操作方法：">{{ form.method }}</el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="请求参数：">{{ form.operParam }}</el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="返回参数：">{{ form.jsonResult }}</el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="操作状态：">
                            <div v-if="form.status === 0">正常</div>
                            <div v-else-if="form.status === 1">失败</div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="操作时间：">{{ parseTime(form.operTime) }}</el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="异常信息：" v-if="form.status === 1">{{ form.errorMsg }}</el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">关 闭</el-button>
            </div>
        </el-dialog>
    </el-main>
</template>

<script>

    import { listOperlog, removeOperlog, cleanOperlog } from '@/api/system/operlog'

    export default {
        name: 'Operlog',
        data() {
            return {
                // 遮罩层
                loading: true,
                // 产品列表
                operlogList: [],
                // 总数
                total: 0,
                // 显示搜索条件
                showSearch: true,
                // 选中列表
                multipleSelection: [],
                // 删除按钮控制
                removeDisabled: true,
                // 详情信息弹窗
                dialogFormVisible: false,
                // 日期范围
                dateRange: [],
                // 状态数据字典
                statusOptions: [],
                // 操作类型数据字典
                typeOptions: [],
                // 表单标题
                title: '操作详情',
                // 详情表单
                form: {},
                // 分页信息
                queryParams:{
                    pageNum: 1,
                    pageSize: 10,
                    title: undefined,
                    operName: undefined,
                    businessType: undefined,
                    status: undefined
                },
            }
        },
        created() {
            this.getList()
            this.getDicts("sys_oper_type").then(response => {
                this.typeOptions = response.data;
            });
            this.getDicts("sys_common_status").then(response => {
                this.statusOptions = response.data;
            });
        },
        methods: {
            // 获取操作日志列表
            getList(){
                this.loading = true;
                listOperlog(this.addDateRange(this.queryParams, this.dateRange))
                    .then( res => {
                        this.operlogList = res.rows
                        this.total = res.total
                        this.loading = false;
                    })
            },
            // 操作日志状态字典翻译
            statusFormat(row, column) {
                return this.selectDictLabel(this.statusOptions, row.status);
            },
            // 操作日志类型字典翻译
            typeFormat(row, column) {
                return this.selectDictLabel(this.typeOptions, row.businessType);
            },
            // 搜索按钮交互
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            // 重置按钮交互
            resetQuery() {
                this.dateRange = [];
                this.resetForm("queryForm");
                this.handleQuery();
            },
            // 详细按钮交互
            handleView(row) {
                this.dialogFormVisible = true;
                this.form = row;
            },
            // 删除按钮交互
            handleRemove(row){
                let ids = []
                if (Array.isArray(row)){
                    for (let index = 0; index < row.length; index++) {
                        ids.push(row[index].operId)
                    }
                } else {
                    ids.push(row.operId)
                }
                let params = {
                    operIds: ids
                }
                removeOperlog(params).then(res => {
                    this.Message.success('删除成功')
                    this.getList()
                })
            },
            // 清空按钮交互
            handleClean() {
                this.$confirm('是否确认清空所有操作日志数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return cleanOperlog();
                }).then(() => {
                    this.getList();
                    this.Message.success("清空成功");
                })
            },
            // 导出按钮交互
            handleExport(){
                this.download('system/operlog/export', {
                    ...this.queryParams
                }, `operlog_${new Date().getTime()}.xlsx`)
            },
            // 多选
            handleSelectionChange(val) {
                this.multipleSelection = val;
                this.editDisabled = this.multipleSelection.length !== 1
                this.removeDisabled = this.multipleSelection.length === 0;
            }
        }
    }
</script>

<style>
    /* 清除 input[type=number] 样式 */
    input[type=number] {
        -moz-appearance:textfield;
    }
    input[type=number]::-webkit-inner-spin-button,
    input[type=number]::-webkit-outer-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }
</style>