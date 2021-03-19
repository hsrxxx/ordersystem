<template>
    <el-main>
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
        </el-row>

        <el-table 
            id="product"
            ref="multipleTable"
            :data="tableData" 
            :header-cell-style="{background:'#f8f8f9'}" 
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="编号" header-align="center" align="center" width="80" />
            <el-table-column prop="name" label="菜品" header-align="center" align="center" />
            <el-table-column prop="price" label="单价" header-align="center" align="center" />
            <el-table-column prop="flavor" label="口味" header-align="center" align="center" />
            <el-table-column prop="type" label="分类" header-align="center" align="center" />
            <el-table-column prop="remark" label="备注" header-align="center" align="center" />
            <el-table-column
                fixed="right"
                label="操作"
                width="100" 
                header-align="center"
                align="center">
                <template slot-scope="scope">
                    <el-button @click="handleRemove(scope.row)" type="text" size="small">删除</el-button>
                    <el-button @click="handleEdit(scope.row)" type="text" size="small">修改</el-button>
                </template>
            </el-table-column>
        </el-table>

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

        <el-dialog title="新增菜品" :visible.sync="dialogFormVisible" width="30%" :showClose="false" :close-on-click-modal="false">
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
                    <el-input v-model="form.remark" autocomplete="off" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id === undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>
    </el-main>
</template>

<script>

    import { productList, productQuery, productAdd, productEdit, productRemove, typeList } from '@/request/api'

    export default {
        name: 'OrderSystemProduct',
        data() {
            return {
                tableData: [],
                count: 0,
                index: 1,
                limit: 10,
                multipleSelection: [],
                editDisabled: true,
                removeDisabled: true,
                dialogFormVisible: false,
                addLoading: false,
                form: {},
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
                typeOptions: [],
                pageDomain:{
                    pageNum: undefined,
                    pageSize: undefined,
                    orderByColumn: undefined,
                    isAsc: undefined
                },
            }
        },
        created() {
            this.productList(1, 10)
        },
        methods: {
            productList(index, limit, orderByColumn, isAsc){
                this.pageDomain.pageNum = index
                this.pageDomain.pageSize = limit
                productList(this.pageDomain)
                    .then( res => {
                        this.tableData = res.rows
                        this.count = res.total
                    })
            },
            addOrEditProduct(){
                let data = {
                    id: this.form.id,
                    name: this.form.name,
                    price: this.form.price,
                    flavor: this.form.flavor,
                    tid: this.form.tid,
                    remark: this.form.remark,
                }
                this.addLoading = true

                if(data.id === undefined){
                    productAdd(data).then(res => {
                        this.$Message.success('产品' + data.name + '添加成功')
                        this.productList(this.index, this.limit)
                    })
                }else{
                    productEdit(data).then(res => {
                        this.$Message.success('产品' + data.name + '修改成功')
                        this.productList(this.index, this.limit)
                    })
                }

                this.dialogFormVisible = false
                this.addLoading = false
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
                        this.addOrEditProduct()
                    } else {
                        return false;
                    }
                })
            },
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
                productRemove(params).then(res => {
                    this.$Message.success('删除成功')
                    this.productList(this.index, this.limit)
                })
            },
            handleEdit(row) {
                this.reset()
                typeList().then(res => { this.typeOptions = res.rows })
                this.dialogFormVisible = true
                productQuery(row.id).then(res => {
                    // this.form = res
                    this.form = {
                        id: res.data.id,
                        name: res.data.name,
                        price: res.data.price,
                        flavor: res.data.flavor,
                        tid: res.data.tid,
                        remark: res.data.remark,
                    }
                })
            },
            handleAdd(){
                this.reset()
                typeList().then(res => { this.typeOptions = res.rows })
                this.dialogFormVisible = true
            },

            // 分页
            // 修改每页数量
            handleSizeChange(val) {
                if(this.count < val){
                    this.index = Math.ceil(this.count / val)
                }
                this.limit = val
                this.productList(this.index, this.limit)
            },
            // 修改第几页
            handleCurrentChange(val) {
                this.index = val
                this.productList(this.index, this.limit)
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