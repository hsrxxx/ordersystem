<template>
    <el-main>
        <el-row :gutter="10">
            <el-col :span="1.5">
                <el-button type="primary" plain size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            </el-col>
        </el-row>
        
        <el-dialog title="新增菜单" :visible.sync="dialogFormVisible" width="40%" :showClose="false" :close-on-click-modal="false">
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
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="地址" prop="address">
                            <el-input v-model="form.address" autocomplete="off" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="12">
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
            id="menu"
            :data="menuList"
            :header-cell-style="{ background:'#f8f8f9' }"
            style="margin-top:20px"
            row-key="id"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
            <el-table-column prop="name" label="菜单名称" width="160" :show-overflow-tooltip="true" />
            <el-table-column prop="icon" label="图标" header-align="center" align="center">
                <template slot-scope="scope">
                    <i :class="scope.row.icon" />
                </template>
            </el-table-column>
            <el-table-column prop="orderNum" label="排序" header-align="center" align="center" />
            <el-table-column prop="perms" label="权限标识" header-align="center" align="center" :show-overflow-tooltip="true" />
            <el-table-column prop="component" label="组件路径" header-align="center" align="center" :show-overflow-tooltip="true" />
            <el-table-column prop="status" label="状态" header-align="center" align="center" />
            <el-table-column prop="createTime" label="创建时间" header-align="center" align="center" />
            <el-table-column
                fixed="right"
                label="操作"
                header-align="center"
                align="center"
                class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleRemove(scope.row)" type="text" size="small" icon="el-icon-delete">删除</el-button>
                    <el-button @click="handleAdd(scope.row)" type="text" size="small" icon="el-icon-plus">新增</el-button>
                    <el-button @click="handleEdit(scope.row)" type="text" size="small"  icon="el-icon-edit">修改</el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-main>
</template>

<script>

    import { listMenu } from '@/api/system/menu'

    export default {
        name: 'OrderSystemUser',
        data() {
            return {
                // 菜单表格树数据
                menuList: [],
                editDisabled: true,
                removeDisabled: true,
                dialogFormVisible: false,
                addLoading: false,
                statusOptions: [],
                // 表单参数
                form: {},
                // 表单校验
                rules: {
                    name: [
                        { required: true, message: "菜单名称不能为空", trigger: "blur" }
                    ],
                    orderNum: [
                        { required: true, message: "菜单顺序不能为空", trigger: "blur" }
                    ],
                    path: [
                        { required: true, message: "路由地址不能为空", trigger: "blur" }
                    ]
                },
                roleOptions: []
            }
        },
        created() {
            this.getList()
        },
        methods: {

            getList(){
                listMenu().then(res => {
                    this.menuList = this.handleTree(res.data, "id");
                });
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
                        this.$Message.success('菜单' + data.username + '添加成功')
                        this.userList(this.index, this.limit)
                    })
                }else{
                    userEdit(data).then(res => {
                        this.$Message.success('菜单' + data.username + '修改成功')
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