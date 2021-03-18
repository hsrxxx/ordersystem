<template>
    <el-main>
        <div>
            <el-button type="primary" plain size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            <el-button type="success" plain size="small" icon="el-icon-edit" :disabled="editDisabled" @click="handleEdit(multipleSelection[0])">修改</el-button>
            <el-button type="danger" plain size="small" icon="el-icon-delete" :disabled="removeDisabled" @click="handleRemove(multipleSelection)">删除</el-button>
        </div>
        
        <el-dialog title="新增菜品" :visible.sync="dialogFormVisible" width="30%" :showClose="false" :close-on-click-modal="false">
            <el-form id="form" :model="form" :rules="rules" ref="form" label-width="60px">
                <el-form-item label="菜品" prop="name">
                    <el-input v-model="form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="单价" prop="price">
                    <el-input v-model="form.price" type="number" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="口味" prop="flavor">
                    <el-input v-model="form.flavor" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="分类" prop="tid">
                    <el-select v-model="form.tid" placeholder="请选择分类">
                        <el-option
                            v-for="item in options"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="form.remark" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id === '' ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>

        <el-table 
            id="product"
            ref="multipleTable"
            :data="tableData" 
            :header-cell-style="{background:'#f8f8f9'}" 
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
             <el-table-column type="selection" width="55">
            </el-table-column>
            <el-table-column prop="id" label="编号" header-align="center" align="center" width="80">
            </el-table-column>
            <el-table-column prop="name" label="菜品" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="price" label="单价" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="flavor" label="口味" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="type" label="分类" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="remark" label="备注" header-align="center" align="center">
            </el-table-column>
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
            :total="count">
        </el-pagination>
    </el-main>
</template>

<script>

    import { productList, productQuery, productAdd, productEdit, productRemove, typeList, userGetInfo } from '@/request/api'

    export default {
        name: 'OrderSystemProduct',
        data() {
            // 自定义校验
            let validateName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('菜品不能为空'));
                }
                callback();
            };
            let validatePrice = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('单价不能为空'));
                }
                setTimeout(() => {
                    if (value instanceof Number || typeof value == 'number') {
                        callback(new Error('请输入数字'));
                    } else {
                        if(value.toString().split(".").length !== 2 && value.toString().split(".").length !== 1){
                            callback(new Error('请输入数字'));
                        }
                        callback();
                    }
                }, 1000);
            };
            let validateFlavor = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('口味不能为空'));
                }
                callback();
            };
            let validateType = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('分类不能为空'));
                }
                callback();
            };
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
                form: {
                    id: '',
                    name: '',
                    price: '',
                    flavor: '',
                    tid: '',
                    remark: '',
                },
                rules: {
                    name: [
                        { validator: validateName, required: true, trigger: 'blur' }
                    ],
                    price: [
                        { validator: validatePrice, required: true, trigger: 'blur' }
                    ],
                    flavor: [
                        { validator: validateFlavor, required: true, trigger: 'blur' }
                    ],
                    tid: [
                        { validator: validateType, required: true, trigger: 'blur' }
                    ]
                },
                options: [],
                pageDomain:{
                    pageNum: '',
                    pageSize: '',
                    orderByColumn: '',
                    isAsc: ''
                },
            }
        },
        created() {
            this.productList(1, 10)
            typeList().then(res => { this.options = res.rows })
            userGetInfo()
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

                if(data.id === ''){
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
                this.dialogFormVisible = true
                productQuery(row.id).then(res => {
                    // this.form = res
                    this.form = {
                        id: res.data.id,
                        name: res.data.name,
                        price: (res.data.price).toString(),
                        flavor: res.data.flavor,
                        tid: res.data.tid,
                        remark: res.data.remark,
                    }
                })
            },
            handleAdd(){
                this.dialogFormVisible = true
                this.form = {
                    id: '',
                    name: '',
                    price: '',
                    flavor: '',
                    tid: '',
                    remark: '',
                }
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