<template>
    <el-main>
        <!--  字典数据查询参数  -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="字典名称" prop="dictName">
                <el-input
                        v-model="queryParams.dictName"
                        placeholder="请输入字典名称"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="字典类型" prop="dictType">
                <el-input
                        v-model="queryParams.dictType"
                        placeholder="请输入字典类型"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select
                        v-model="queryParams.status"
                        placeholder="字典状态"
                        clearable
                        size="small"
                        style="width: 240px"
                >
                    <el-option
                            v-for="dict in statusOptions"
                            :key="dict.dictValue"
                            :label="dict.dictLabel"
                            :value="dict.dictValue"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="创建时间">
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
                <el-button type="primary" plain size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain size="small" icon="el-icon-edit" :disabled="editDisabled" @click="handleEdit(multipleSelection[0])">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain size="small" icon="el-icon-delete" :disabled="removeDisabled" @click="handleRemove(multipleSelection)">删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--  字典类型数据  -->
        <el-table
            v-loading="loading"
            ref="multipleTable"
            :data="typeList"
            :header-cell-style="{ background:'#f8f8f9' }"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="dictId" label="字典编号" align="center"/>
            <el-table-column prop="dictName" label="字典名称" align="center" />
            <el-table-column label="字典类型" align="center" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <router-link :to="'/dict/type/data/' + scope.row.dictId" class="link-type">
                        <span>{{ scope.row.dictType }}</span>
                    </router-link>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="status" :formatter="statusFormat" />
            <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
            <el-table-column label="创建时间" align="center" prop="createTime" width="180" />
            <el-table-column
                fixed="right"
                label="操作"
                header-align="center"
                align="center"
                class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleEdit(scope.row)" type="text" size="small"  icon="el-icon-edit">修改</el-button>
                    <el-button @click="handleRemove(scope.row)" type="text" size="small" icon="el-icon-delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!--  分页器  -->
        <el-pagination
            style="margin-top:20px"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            background
            :current-page="1"
            :page-sizes="[10, 20, 30, 40, 50]"
            :page-size="limit"
            layout="->, total, sizes, prev, pager, next, jumper"
            :total="count" />

        <!-- 添加或修改字典类型对话框 -->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="40%" :showClose="false" :close-on-click-modal="false">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="字典名称" prop="dictName">
                    <el-input v-model="form.dictName" placeholder="请输入字典名称" />
                </el-form-item>
                <el-form-item label="字典类型" prop="dictType">
                    <el-input v-model="form.dictType" placeholder="请输入字典类型" />
                </el-form-item>
                <el-form-item label="状态" prop="status">
                    <el-radio-group v-model="form.status">
                        <el-radio
                                v-for="dict in statusOptions"
                                :key="dict.dictValue"
                                :label="dict.dictValue"
                        >{{dict.dictLabel}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.dictId == undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>
    </el-main>
</template>

<script>

    import { listType, queryType, addType, editType, removeType } from '@/api/system/dict/type'

    export default {
        name: 'Type',
        data() {
            return {
                // 遮罩层
                loading: true,
                // 字典类型列表
                typeList: [],
                // 总数
                count: 0,
                // 第一页
                index: 1,
                // 每页个数
                limit: 10,
                // 显示搜索条件
                showSearch: true,
                // 选中列表
                multipleSelection: [],
                // 修改按钮控制
                editDisabled: true,
                // 删除按钮控制
                removeDisabled: true,
                // 添加按钮控制
                addLoading: false,
                // 添加/修改弹窗表单
                dialogFormVisible: false,
                // 日期范围
                dateRange: [],
                // 表单标题
                title: '',
                // 添加/修改表单
                form: {},
                // 状态数据字典
                statusOptions: [],
                // 表单验证
                rules: {
                    dictName: [
                        { required: true, message: "字典名称不能为空", trigger: "blur" }
                    ],
                    dictType: [
                        { required: true, message: "字典类型不能为空", trigger: "blur" }
                    ]
                },
                // 分页信息
                queryParams:{
                    pageNum: 1,
                    pageSize: 10,
                    dictName: undefined,
                    dictType: undefined,
                    status: undefined
                },
            }
        },
        created() {
            this.getList()
            this.getDicts("sys_normal_disable").then(response => {
                this.statusOptions = response.data;
            });
        },
        methods: {
            // 查询字典类型列表
            getList(){
                this.loading = true;
                listType(this.addDateRange(this.queryParams, this.dateRange))
                    .then( res => {
                        this.typeList = res.rows
                        this.count = res.total
                        this.loading = false;
                    })
            },
            // 取消按钮
            cancel() {
                this.dialogFormVisible = false
                this.reset();
            },
            // 表单重置
            reset() {
                this.form = {
                    dictId: undefined,
                    dictName: undefined,
                    dictType: undefined,
                    status: "0",
                    remark: undefined
                };
                this.resetForm("form");
            },
            // 对话框添加/修改按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.addLoading = true
                        if(this.form.dictId !== undefined){
                            editType(this.form).then(res => {
                                this.Message.success('用户' + this.form.dictName + '修改成功')
                                this.getList()
                            })
                        }else{
                            addType(this.form).then(res => {
                                this.Message.success('用户' + this.form.dictName + '添加成功')
                                this.getList()
                            })
                        }
                        this.dialogFormVisible = false
                        this.addLoading = false
                    }
                })
            },
            // 删除按钮交互
            handleRemove(row){
                let ids = []
                if (Array.isArray(row)){
                    for (let index = 0; index < row.length; index++) {
                        ids.push(row[index].dictId)
                    }
                } else {
                    ids.push(row.dictId)
                }
                let params = {
                    dictIds: ids
                }
                removeType(params).then(res => {
                    this.Message.success("删除成功!")
                    this.getList()
                })
            },
            // 修改按钮交互
            handleEdit(row) {
                this.reset()
                this.title = '修改字典类型'
                this.dialogFormVisible = true
                queryType(row.dictId).then(res => {
                    this.form = res.data
                })
            },
            // 添加按钮交互
            handleAdd(){
                this.reset()
                this.title = '添加字典类型'
                this.dialogFormVisible = true
            },
            // 字典状态字典翻译
            statusFormat(row, column) {
                return this.selectDictLabel(this.statusOptions, row.status);
            },
            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.dateRange = []
                this.resetForm("queryForm");
                this.handleQuery();
            },
            // 修改每页数量
            handleSizeChange(val) {
                if(this.count < val){
                    this.index = Math.ceil(this.count / val)
                }
                this.limit = val
                this.queryParams.pageNum = this.index
                this.queryParams.pageSize = this.limit
                this.getList()
            },
            // 修改第几页
            handleCurrentChange(val) {
                this.index = val
                this.queryParams.pageNum = this.index
                this.getList()
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

    .link-type {
        color: #337ab7;
        text-decoration: none;
    }
    .link-type:hover {
         color: rgb(32, 160, 255);
    }
</style>