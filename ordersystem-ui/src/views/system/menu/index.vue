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
        
        <el-dialog title="新增用户" :visible.sync="dialogFormVisible" width="40%" :showClose="false" :close-on-click-modal="false">
            <el-form id="form" :model="form" :rules="rules" ref="form" label-width="60px">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="名称" prop="username">
                            <el-input v-model="form.username" autocomplete="off" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="昵称" prop="nickname">
                            <el-input v-model="form.nickname" autocomplete="off" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="邮箱" prop="email">
                            <el-input type="email" v-model="form.email" autocomplete="off" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="手机" prop="telephone">
                            <el-input type="number" v-model="form.telephone" autocomplete="off" />
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
                                        :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="地址" prop="address">
                            <el-input v-model="form.address" autocomplete="off" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
                        <el-form-item label="状态" prop="status">
                            <el-select v-model="form.status" placeholder="请选择">
                                <el-option
                                        v-for="item in status"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="form.id == undefined" label="密码" prop="password">
                            <el-input v-model="form.password" autocomplete="off" type="password" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="角色">
                            <el-select v-model="form.roleIds" multiple placeholder="请选择">
                                <el-option
                                        v-for="item in roleOptions"
                                        :key="item.id"
                                        :label="item.roleName"
                                        :value="item.id"
                                        :disabled="item.status == 1" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="备注">
                    <el-input type="textarea" v-model="form.remark" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id == undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>

        <el-table
            id="user"
            ref="multipleTable"
            :data="tableData"
            :header-cell-style="{ background:'#f8f8f9' }"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="用户编号" header-align="center" align="center" width="80" />
            <el-table-column prop="username" label="用户名称" header-align="center" align="center" />
            <el-table-column prop="nickname" label="用户昵称" header-align="center" align="center" />
            <el-table-column prop="telephone" label="用户手机" header-align="center" align="center" />
            <el-table-column prop="sex" label="性别" header-align="center" align="center" />
            <el-table-column prop="status" label="状态" header-align="center" align="center" />
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
    </el-main>
</template>

<script>

    import { userList, userQuery, userAdd, userEdit, userRemove, getInfo, userGetInfo, resetPwd, changeStatus } from '@/request/api'

    export default {
        name: 'OrderSystemUser',
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
                sex: [
                    {
                        value: '0',
                        label: '男'
                    },
                    {
                        value: '1',
                        label: '女'
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
                form: {},
                rules: {
                    username: [
                        { required: true, message: "用户名称不能为空", trigger: "blur" }
                    ],
                    nickname: [
                        { required: true, message: "用户昵称不能为空", trigger: "blur" }
                    ],
                    password: [
                        { required: true, message: "用户密码不能为空", trigger: "blur" }
                    ],
                    address: [
                        { required: true, message: "地址不能为空", trigger: "blur" }
                    ],
                    email: [
                        {
                            type: "email",
                            message: "'请输入正确的邮箱地址",
                            trigger: ["blur", "change"]
                        }
                    ],
                    telephone: [
                        {
                            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
                            message: "请输入正确的手机号码",
                            trigger: "blur"
                        }
                    ]
                },
                options: [],
                pageDomain:{
                    pageNum: undefined,
                    pageSize: undefined,
                    orderByColumn: undefined,
                    isAsc: undefined
                },
                roleOptions: [],
            }
        },
        created() {
            this.userList(1, 10)
        },
        methods: {
            userList(index, limit, orderByColumn, isAsc){
                this.pageDomain.pageNum = index
                this.pageDomain.pageSize = limit
                userList(this.pageDomain)
                    .then( res => {
                        this.tableData = res.rows
                        this.count = res.total
                    })
            },
            addOrEditUser(){
                let data = {
                    id: this.form.id,
                    username: this.form.username,
                    nickname: this.form.nickname,
                    password: this.form.password,
                    email: this.form.email,
                    telephone: this.form.telephone,
                    sex: this.form.sex,
                    address: this.form.address,
                    status: this.form.status,
                    remark: this.form.remark,
                    roleIds: this.form.roleIds,
                }
                this.addLoading = true

                if(data.id === undefined){
                    userAdd(data).then(res => {
                        this.$Message.success('用户' + data.username + '添加成功')
                        this.userList(this.index, this.limit)
                    })
                }else{
                    userEdit(data).then(res => {
                        this.$Message.success('用户' + data.username + '修改成功')
                        this.userList(this.index, this.limit)
                    })
                }

                this.dialogFormVisible = false
                this.addLoading = false
            },
            // 表单重置
            reset() {
                this.form = {
                    id: undefined,
                    username: undefined,
                    nickname: undefined,
                    password: undefined,
                    email: undefined,
                    telephone: undefined,
                    sex: '男',
                    address: undefined,
                    remark: undefined,
                    status: '正常',
                    roleIds: []
                };
                this.resetForm("form");
            },
            // 按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.addOrEditUser()
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
                    userIds: ids
                }
                userRemove(params).then(res => {
                    this.$Message.success("删除成功!")
                    this.userList(this.index, this.limit)
                })
            },
            handleEdit(row) {
                this.reset()
                this.dialogFormVisible = true
                userQuery(row.id).then(res => {
                    // this.form = res
                    this.form = {
                        id: res.data.id,
                        username: res.data.username,
                        nickname: res.data.nickname,
                        password: res.data.password,
                        email: res.data.email,
                        telephone: res.data.telephone,
                        sex: res.data.sex,
                        address: res.data.address,
                        status: res.data.status,
                        remark: res.data.remark,
                        roleIds: res.roleIds,
                    }
                    this.roleOptions = res.roles
                })
            },
            handleAdd(){
                this.reset()
                this.dialogFormVisible = true
                getInfo().then(res => {
                    this.roleOptions = res.roles
                })
            },

            // 分页
            // 修改每页数量
            handleSizeChange(val) {
                if(this.count < val){
                    this.index = Math.ceil(this.count / val)
                }
                this.limit = val
                this.userList(this.index, this.limit)
            },
            // 修改第几页
            handleCurrentChange(val) {
                this.index = val
                this.userList(this.index, this.limit)
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