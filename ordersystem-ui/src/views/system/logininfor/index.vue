<template>
    <el-main>
        <!--  登录日志查询参数  -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="登陆地址" prop="ipaddr">
                <el-input
                        v-model="queryParams.ipaddr"
                        placeholder="请输入登陆地址"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="用户名称" prop="userName">
                <el-input
                        v-model="queryParams.userName"
                        placeholder="请输入用户名称"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
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
            <el-form-item label="登录时间">
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
<!--            <el-col :span="1.5">-->
<!--                <el-button type="warning" plain size="mini" icon="el-icon-download" @click="handleExport">导出</el-button>-->
<!--            </el-col>-->
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--  登录日志数据  -->
        <el-table 
            v-loading="loading"
            ref="multipleTable"
            :data="logininforList"
            :header-cell-style="{background:'#f8f8f9'}"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column label="访问编号" align="center" prop="infoId" />
            <el-table-column label="用户名称" align="center" prop="userName" />
            <el-table-column label="登陆地址" align="center" prop="ipaddr" width="130" :show-overflow-tooltip="true" />
<!--            <el-table-column label="登陆地点" align="center" prop="loginLocation" />-->
            <el-table-column label="浏览器" align="center" prop="browser" />
            <el-table-column label="操作系统" align="center" prop="os" />
            <el-table-column label="登录状态" align="center" prop="status" :formatter="statusFormat" />
            <el-table-column label="操作信息" align="center" prop="msg" />
            <el-table-column label="操作日期" align="center" prop="loginTime" width="180" />
        </el-table>

        <!--  分页器  -->
        <pagination
                v-show="total > 0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList" />
    </el-main>
</template>

<script>

    import { listLogininfor, removeLogininfor, cleanLogininfor } from '@/api/system/logininfor'

    export default {
        name: 'Logininfor',
        data() {
            return {
                // 遮罩层
                loading: true,
                // 产品列表
                logininforList: [],
                // 总数
                total: 0,
                // 显示搜索条件
                showSearch: true,
                // 选中列表
                multipleSelection: [],
                // 删除按钮控制
                removeDisabled: true,
                // 日期范围
                dateRange: [],
                // 状态数据字典
                statusOptions: [],
                // 分页信息
                queryParams:{
                    pageNum: 1,
                    pageSize: 10,
                    ipaddr: undefined,
                    userName: undefined,
                    status: undefined
                },
            }
        },
        created() {
            this.getList()
            this.getDicts("sys_common_status").then(response => {
                this.statusOptions = response.data;
            });
        },
        methods: {
            // 获取产品列表
            getList(){
                this.loading = true;
                listLogininfor(this.addDateRange(this.queryParams, this.dateRange))
                    .then( res => {
                        this.logininforList = res.rows
                        this.total = res.total
                        this.loading = false;
                    })
            },
            // 操作日志状态字典翻译
            statusFormat(row) {
                return this.selectDictLabel(this.statusOptions, row.status);
            },
            // 搜索按钮操作
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            // 重置按钮操作
            resetQuery() {
                this.dateRange = [];
                this.resetForm("queryForm");
                this.handleQuery();
            },
            // 删除按钮交互
            handleRemove(row){
                let ids = []
                if (Array.isArray(row)){
                    for (let index = 0; index < row.length; index++) {
                        ids.push(row[index].infoId)
                    }
                } else {
                    ids.push(row.infoId)
                }
                let params = {
                    infoIds: ids
                }
                removeLogininfor(params).then(res => {
                    this.Message.success('删除成功')
                    this.getList()
                })
            },
            /** 清空按钮操作 */
            handleClean() {
                this.$confirm('是否确认清空所有登录日志数据项?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    return cleanLogininfor();
                }).then(() => {
                    this.getList();
                    this.Message.success("清空成功");
                })
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