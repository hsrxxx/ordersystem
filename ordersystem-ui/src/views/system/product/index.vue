<template>
    <el-main>
        <!--  产品查询参数  -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="产品名称" prop="name">
                <el-input
                        v-model="queryParams.name"
                        placeholder="请输入产品名称"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="产品分类" prop="type">
                <el-select
                        v-model="queryParams.type"
                        placeholder="用户状态"
                        clearable
                        size="small"
                        style="width: 240px"
                >
                    <el-option
                            v-for="type in typeOptions"
                            :key="type.id"
                            :label="type.name"
                            :value="type.id"
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

        <!--  产品数据  -->
        <el-table 
            v-loading="loading"
            ref="multipleTable"
            :data="productList"
            :header-cell-style="{background:'#f8f8f9'}"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="编号" align="center" width="80" />
            <el-table-column prop="name" label="菜品" align="center" />
            <el-table-column prop="price" label="单价" align="center" />
            <el-table-column prop="flavor" label="口味" align="center" />
            <el-table-column prop="type" label="分类" align="center" />
            <el-table-column prop="remark" label="备注" align="center" />
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

        <!-- 添加或修改产品对话框 -->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="30%" :showClose="false" :close-on-click-modal="false">
            <el-form id="form" :model="form" :rules="rules" ref="form" label-width="60px">
                <el-form-item label="菜品" prop="name">
                    <el-input v-model="form.name" autocomplete="off" />
                </el-form-item>
                <el-form-item label="单价" prop="price">
                    <el-input v-model="form.price" type="number" autocomplete="off" />
                </el-form-item>
                <el-form-item label="口味" prop="flavor">
                    <el-input v-model="form.flavor" autocomplete="off" />
                </el-form-item>
                <el-form-item label="分类" prop="tid">
                    <el-select v-model="form.tid" placeholder="请选择分类">
                        <el-option
                                v-for="item in typeOptions"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id" />
                    </el-select>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id === undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>
    </el-main>
</template>

<script>

    import { listProduct, queryProduct, addProduct, editProduct, removeProduct, getTypes } from '@/api/system/product'

    export default {
        name: 'Product',
        data() {
            return {
                // 遮罩层
                loading: true,
                // 产品列表
                productList: [],
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
                // 状态数据字典
                statusOptions: [],
                // 产品类型数据字典
                typeOptions: [],
                // 表单标题
                title: '',
                // 添加/修改表单
                form: {},
                // 表单验证
                rules: {
                    name: [
                        { required: true, message: "产品名称不能为空", trigger: "blur" }
                    ],
                    price: [
                        {
                            pattern: /(^[1-9][0-9]{0,7}$)|(^((0\.0[1-9]$)|(^0\.[1-9]\d?)$)|(^[1-9][0-9]{0,7}\.\d{1,2})$)/,
                            message: "请输入正确的价格",
                            trigger: "blur"
                        }
                    ],
                    flavor: [
                        { required: true, message: "口味不能为空", trigger: "blur" }
                    ],
                    tid: [
                        { required: true, message: "分类不能为空", trigger: "blur" }
                    ]
                },
                // 分页信息
                queryParams:{
                    pageNum: 1,
                    pageSize: 10,
                    name: undefined,
                    type: undefined
                },
            }
        },
        created() {
            this.getList()
            getTypes().then(res => { this.typeOptions = res.types })
        },
        methods: {
            // 获取产品列表
            getList(){
                this.loading = true;
                listProduct(this.addDateRange(this.queryParams, this.dateRange))
                    .then( res => {
                        this.productList = res.rows
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
                    id: undefined,
                    name: undefined,
                    price: undefined,
                    flavor: undefined,
                    tid: undefined,
                    remark: undefined,
                };
                this.resetForm("form");
            },
            // 按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.addLoading = true
                        if(this.form.id !== undefined){
                            editProduct(this.form).then(res => {
                                this.Message.success('产品' + this.form.name + '修改成功')
                                this.getList()
                            })
                        }else{
                            addProduct(this.form).then(res => {
                                this.Message.success('产品' + this.form.name + '添加成功')
                                this.getList()
                            })
                        }
                        this.dialogFormVisible = false
                        this.addLoading = false
                    }
                })
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
                        ids.push(row[index].id)
                    }
                } else {
                    ids.push(row.id)
                }
                let params = {
                    productIds: ids
                }
                removeProduct(params).then(res => {
                    this.Message.success('删除成功')
                    this.getList()
                })
            },
            // 修改按钮交互
            handleEdit(row) {
                this.reset()
                this.title = '修改产品'
                this.dialogFormVisible = true
                queryProduct(row.id).then(res => {
                    // this.form = res
                    this.form = res.data
                    this.typeOptions = res.types
                })
            },
            // 添加按钮交互
            handleAdd(){
                this.reset()
                this.title = '添加产品'
                this.dialogFormVisible = true
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
</style>