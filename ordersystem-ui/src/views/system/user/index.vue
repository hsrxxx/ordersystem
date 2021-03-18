<template>
    <el-main>
        <div>
            <el-button type="primary" plain size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            <el-button type="success" plain size="small" icon="el-icon-edit" :disabled="updateDisabled" @click="handleUpdate(multipleSelection[0])">修改</el-button>
            <el-button type="danger" plain size="small" icon="el-icon-delete" :disabled="deleteDisabled" @click="handleDeletes(multipleSelection)">删除</el-button>
        </div>
        
        <el-dialog title="新增用户" :visible.sync="dialogFormVisible" width="40%" :showClose="false" :close-on-click-modal="false">
            <el-form id="form" :model="form" :rules="rules" ref="form" label-width="60px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="名称" prop="username">
                            <el-input v-model="form.username" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="昵称" prop="nickname">
                            <el-input v-model="form.nickname" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="邮箱" prop="email">
                            <el-input type="email" v-model="form.email" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="手机" prop="telephone">
                            <el-input type="number" v-model="form.telephone" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="性别" prop="sex">
                            <el-select v-model="form.sex" placeholder="请选择">
                                <el-option
                                        v-for="item in sex"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态" prop="status">
                            <el-select v-model="form.status" placeholder="请选择">
                                <el-option
                                        v-for="item in status"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="住址" prop="address">
                    <el-input v-model="form.address" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input type="textarea" v-model="form.remark"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id == '' ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>

        <el-table
            id="user"
            ref="multipleTable"
            :data="tableData"
            :header-cell-style="{ background:'#f8f8f9' }"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
             <el-table-column
                type="selection"
                width="55">
            </el-table-column>
            <el-table-column prop="id" label="用户编号" header-align="center" align="center" width="80">
            </el-table-column>
            <el-table-column prop="username" label="用户名称" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="nickname" label="用户昵称" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="telephone" label="用户手机" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="sex" label="性别" header-align="center" align="center">
            </el-table-column>
            <el-table-column prop="status" label="状态" header-align="center" align="center">
            </el-table-column>
            <el-table-column
                fixed="right"
                label="操作"
                width="100" 
                header-align="center"
                align="center">
                <template slot-scope="scope">
                    <el-button @click="handleDelete(scope.row)" type="text" size="small">删除</el-button>
                    <el-button @click="handleUpdate(scope.row)" type="text" size="small">修改</el-button>
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

    import { userList, userQuery, userAdd, userEdit, userRemove, userGetInfo, resetPwd, changeStatus } from '@/request/api'

    export default {
        name: 'OrderSystemUser',
        data() {

            // 自定义校验
            var validateName = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('菜品不能为空'));
                }
                callback();
            };
            var validatePrice = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('单价不能为空'));
                }
                setTimeout(() => {
                    if (value instanceof Number || typeof value == 'number') {
                        callback(new Error('请输入数字'));
                    } else {
                        if(value.toString().split(".").length != 2 && value.toString().split(".").length != 1){
                            callback(new Error('请输入数字'));
                        }
                        callback();
                    }
                }, 1000);
            };
            var validateFlavor = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('口味不能为空'));
                }
                callback();
            };
            var validateType = (rule, value, callback) => {
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
                updateDisabled: true,
                deleteDisabled: true,
                dialogFormVisible: false,
                addLoading: false,
                sex: [
                    {
                        value: '0',
                        label: '男'
                    },
                    {
                        value: '1',
                        label: '女'
                    },
                    {
                        value: '2',
                        label: '未知'
                    }
                ],
                status: [
                    {
                        value: '0',
                        label: '正常'
                    },
                    {
                        value: '1',
                        label: '停用'
                    }
                ],
                form: {
                    id: '',
                    username: '',
                    nickname: '',
                    email: '',
                    telephone: '',
                    sex: '',
                    address: '',
                    remark: '',
                    status: '',
                },
                rules: {
                    username: [
                        { validator: validateName, required: true, trigger: 'blur' }
                    ],
                    nickname: [
                        { validator: validatePrice, required: true, trigger: 'blur' }
                    ],
                },
                options: [],
            }
        },
        created() {
            this.findAllUser(this.index,this.limit)
            countUser().then(res => { this.count = res })
        },
        methods: {
            findAllUser(index, limit){
                findAllUser(index,limit)
                    .then( res => {
                        this.tableData = res
                    })
            },
            saveOrUpdate(){
                let data = {
                    id: this.form.id,
                    username: this.form.username,
                    nickname: this.form.nickname,
                    email: this.form.email,
                    telephone: this.form.telephone,
                    sex: this.form.sex,
                    address: this.form.address,
                    status: this.form.status,
                    remark: this.form.remark,
                }
                this.addLoading = true

                if(data.id === ''){
                    saveUser(data).then(res => {
                        this.$Message.success("新增成功!")
                        this.findAllUser(this.index, this.limit)
                    })
                }else{
                    updateByIdUser(data).then(res => {
                        this.$Message.success("修改成功!")
                        this.findAllUser(this.index, this.limit)
                    })
                }

                this.dialogFormVisible = false
                this.addLoading = false
            },


            // 按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.saveOrUpdate()
                    } else {
                        return false;
                    }
                })
            },
            handleDelete(row) {
                deleteByIdUser(row.id).then(res => {
                    this.$Message("删除成功!")
                    this.findAllUser(this.index, this.limit)
                })
            },
            handleDeletes(row){
                let ids = []
                for (let index = 0; index < row.length; index++) {
                    ids.push(row[index].id)
                }
                let params = {
                    ids: ids
                }
                deleteBatchIdsUser(params).then(res => {
                    this.$Message("删除成功!")
                    this.findAllUser(this.index, this.limit)
                })
            },
            handleUpdate(row) {
                this.dialogFormVisible = true
                findByIdUser(row.id).then(res => {
                    // this.form = res
                    this.form = {
                        id: res.id,
                        username: res.username,
                        nickname: res.nickname,
                        email: res.email,
                        telephone: res.telephone,
                        sex: res.sex,
                        address: res.address,
                        status: res.status,
                        remark: res.remark,
                    }
                })
            },
            handleAdd(){
                this.dialogFormVisible = true
                this.form = {
                    id: '',
                    username: '',
                    nickname: '',
                    email: '',
                    telephone: '',
                    sex: '男',
                    address: '',
                    remark: '',
                    status: '正常',
                }
            },

            // 分页
            // 修改每页数量
            handleSizeChange(val) {
                if(this.count < val){
                    this.index = Math.ceil(this.count / val)
                }
                this.limit = val
                this.findAllUser(this.index, this.limit)
            },
            // 修改第几页
            handleCurrentChange(val) {
                this.index = val
                this.findAllUser(this.index, this.limit)
            },

            // 多选
            handleSelectionChange(val) {
                this.multipleSelection = val;

                if (this.multipleSelection.length === 1) {
                    this.updateDisabled = false
                }else{
                    this.updateDisabled = true
                }

                if (this.multipleSelection.length === 0) {
                    this.deleteDisabled = true
                }else{
                    this.deleteDisabled = false
                }
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