<template>
    <el-main>
        <!--  字典数据查询参数  -->
        <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
            <el-form-item label="字典名称" prop="dictType">
                <el-select v-model="queryParams.dictType" size="small">
                    <el-option
                            v-for="item in typeOptions"
                            :key="item.dictId"
                            :label="item.dictName"
                            :value="item.dictType"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="字典标签" prop="dictLabel">
                <el-input
                        v-model="queryParams.dictLabel"
                        placeholder="请输入字典标签"
                        clearable
                        size="small"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="数据状态" clearable size="small">
                    <el-option
                            v-for="dict in statusOptions"
                            :key="dict.dictValue"
                            :label="dict.dictLabel"
                            :value="dict.dictValue"
                    />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <!--  权限操作按钮  -->
        <el-row :gutter="10">
            <el-col :span="1.5">
                <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain size="mini" icon="el-icon-edit" :disabled="editDisabled" @click="handleEdit(multipleSelection[0])">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain size="mini" icon="el-icon-delete" :disabled="removeDisabled" @click="handleRemove(multipleSelection)">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain size="mini" icon="el-icon-download" @click="handleExport">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--  字典数据  -->
        <el-table
                v-loading="loading"
                ref="multipleTable"
                :data="dataList"
                :header-cell-style="{ background:'#f8f8f9' }"
                style="margin-top:20px"
                @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column label="字典编码" align="center" prop="dictCode" />
            <el-table-column label="字典标签" align="center" prop="dictLabel" />
            <el-table-column label="字典键值" align="center" prop="dictValue" />
            <el-table-column label="字典排序" align="center" prop="dictSort" />
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
                    <el-button @click="handleEdit(scope.row)" type="text" size="mini"  icon="el-icon-edit">修改</el-button>
                    <el-button @click="handleRemove(scope.row)" type="text" size="mini" icon="el-icon-delete">删除</el-button>
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

        <!-- 添加或修改字典数据对话框 -->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="40%" :showClose="false" :close-on-click-modal="false">
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="字典类型">
                    <el-input v-model="form.dictType" :disabled="true" />
                </el-form-item>
                <el-form-item label="数据标签" prop="dictLabel">
                    <el-input v-model="form.dictLabel" placeholder="请输入数据标签" />
                </el-form-item>
                <el-form-item label="数据键值" prop="dictValue">
                    <el-input v-model="form.dictValue" placeholder="请输入数据键值" />
                </el-form-item>
                <el-form-item label="显示排序" prop="dictSort">
                    <el-input-number v-model="form.dictSort" controls-position="right" :min="0" />
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
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.dictCode == undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>
    </el-main>
</template>

<script>

    import { listData, queryData, addData, editData, removeData } from '@/api/system/dict/data'
    import { listType, queryType } from "@/api/system/dict/type";

    export default {
        name: 'Data',
        data() {
            return {
                // 遮罩层
                loading: true,
                // 字典类型列表
                dataList: [],
                // 总数
                total: 0,
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
                // 状态数据字典
                statusOptions: [],
                // 字典类型数据字典
                typeOptions: [],
                // 默认字典类型
                defaultDictType: "",
                // 表单标题
                title: '',
                // 添加/修改表单
                form: {},
                // 表单验证
                rules: {
                    dictLabel: [
                        { required: true, message: "数据标签不能为空", trigger: "blur" }
                    ],
                    dictValue: [
                        { required: true, message: "数据键值不能为空", trigger: "blur" }
                    ],
                    dictSort: [
                        { required: true, message: "数据顺序不能为空", trigger: "blur" }
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
            const dictId = this.$route.params && this.$route.params.dictId;
            this.getType(dictId)
            this.getTypeList()
            this.getDicts("sys_normal_disable").then(response => {
                this.statusOptions = response.data;
            });
        },
        methods: {
            // 查询字典类型详细
            getType(dictId) {
                queryType(dictId).then(response => {
                    this.queryParams.dictType = response.data.dictType;
                    this.defaultDictType = response.data.dictType;
                    this.getList();
                });
            },
            // 查询字典类型列表
            getTypeList() {
                listType().then(res => {
                    this.typeOptions = res.rows;
                });
            },
            // 查询字典类型列表
            getList(){
                this.loading = true;
                listData(this.queryParams)
                    .then( res => {
                        this.dataList = res.rows
                        this.total = res.total
                        this.loading = false;
                    })
            },
            // 表单重置
            reset() {
                this.form = {
                    dictCode: undefined,
                    dictLabel: undefined,
                    dictValue: undefined,
                    dictSort: 0,
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
                        if(this.form.dictCode !== undefined){
                            editData(this.form).then(res => {
                                this.Message.success('字典类型"' + this.defaultDictType + '"修改键值成功')
                                this.getList()
                            })
                        }else{
                            console.log(this.form)
                            addData(this.form).then(res => {
                                this.Message.success('字典类型"' + this.defaultDictType + '"添加键值成功')
                                this.getList()
                            })
                        }
                        this.addLoading = false
                        this.dialogFormVisible = false
                    }
                })
            },
            // 删除按钮交互
            handleRemove(row){
                let ids = []
                if (Array.isArray(row)){
                    for (let index = 0; index < row.length; index++) {
                        ids.push(row[index].dictCode)
                    }
                } else {
                    ids.push(row.dictCode)
                }
                let params = {
                    dictCodes: ids
                }
                removeData(params).then(res => {
                    this.Message.success("删除成功!")
                    this.getList()
                })
            },
            // 修改按钮交互
            handleEdit(row) {
                this.reset()
                this.title = '修改字典数据'
                this.dialogFormVisible = true
                queryData(row.dictCode).then(res => {
                    this.form = res.data
                })
            },
            // 添加按钮交互
            handleAdd(){
                this.reset()
                this.title = '添加字典数据'
                this.dialogFormVisible = true
                this.form.dictType = this.defaultDictType
            },
            // 搜索按钮交互
            handleQuery() {
                this.queryParams.pageNum = 1;
                this.getList();
            },
            // 重置按钮交互
            resetQuery() {
                this.resetForm("queryForm");
                this.queryParams.dictType = this.defaultDictType;
                this.handleQuery();
            },
            // 导出按钮交互
            handleExport(){
                this.download('system/dict/data/export', {
                    ...this.queryParams
                }, `data_${new Date().getTime()}.xlsx`)
            },
            // 字典状态字典翻译
            statusFormat(row, column) {
                return this.selectDictLabel(this.statusOptions, row.status);
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
    /* 清除 input[data=number] 样式 */
    input[data=number] {
        -moz-appearance:textfield;
    }
    input[data=number]::-webkit-inner-spin-button,
    input[data=number]::-webkit-outer-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }
</style>