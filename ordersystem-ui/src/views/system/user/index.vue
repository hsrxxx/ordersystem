<template>
    <el-main>
        <!--  用户查询参数  -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="用户名称" prop="username">
                <el-input
                        v-model="queryParams.username"
                        placeholder="请输入用户名称"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="手机号码" prop="telephone">
                <el-input
                        v-model="queryParams.telephone"
                        placeholder="请输入手机号码"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select
                        v-model="queryParams.status"
                        placeholder="用户状态"
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
                <el-button type="primary" plain size="mini" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain size="mini" icon="el-icon-edit" :disabled="editDisabled" @click="handleEdit(multipleSelection[0])">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain size="mini" icon="el-icon-delete" :disabled="removeDisabled" @click="handleRemove(multipleSelection)">删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--  用户数据  -->
        <el-table
            v-loading="loading"
            ref="multipleTable"
            :data="userList"
            :header-cell-style="{ background:'#f8f8f9' }"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="用户编号" align="center" width="80" />
            <el-table-column prop="username" label="用户名称" align="center" />
            <el-table-column prop="nickname" label="用户昵称" align="center" />
            <el-table-column prop="telephone" label="用户手机" align="center" />
            <el-table-column prop="sex" label="性别" :formatter="sexFormat" align="center" />
            <el-table-column prop="status" label="状态" align="center">
                <template slot-scope="scope">
                    <el-switch
                            v-model="scope.row.status"
                            active-value="0"
                            inactive-value="1"
                            @change="handleStatusChange(scope.row)"
                    ></el-switch>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" align="center" />
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

        <!-- 添加或修改用户对话框 -->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="40%" :showClose="false" :close-on-click-modal="false">
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
                                        v-for="item in sexOptions"
                                        :key="item.dictValue"
                                        :label="item.dictLabel"
                                        :value="item.dictValue" />
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
                                        v-for="item in statusOptions"
                                        :key="item.dictValue"
                                        :label="item.dictLabel"
                                        :value="item.dictValue" />
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
                                        :value="item.id"/>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="备注">
                    <el-input type="textarea" v-model="form.remark" />
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id == undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>
    </el-main>
</template>

<script>

    import { listUser, queryUser, addUser, editUser, removeUser, getRoles, changeStatus } from '@/api/system/user'

    export default {
        name: 'User',
        data() {
            return {
                // 遮罩层
                loading: true,
                // 用户列表
                userList: [],
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
                // 日期范围
                dateRange: [],
                // 用户性别数据字典
                sexOptions: [],
                // 用户状态数据字典
                statusOptions: [],
                // 角色数据字典
                roleOptions: [],
                // 表单标题
                title: '',
                // 添加/修改表单
                form: {},
                // 表单验证
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
                // 查询参数
                queryParams:{
                    pageNum: 1,
                    pageSize: 10,
                    username: undefined,
                    telephone: undefined,
                    status: undefined
                },
            }
        },
        created() {
            this.getList()
            this.getDicts("sys_user_sex").then(response => {
                this.sexOptions = response.data;
            });
            this.getDicts("sys_normal_disable").then(response => {
                this.statusOptions = response.data;
            });
        },
        methods: {
            // 查询用户列表
            getList(){
                this.loading = true;
                listUser(this.addDateRange(this.queryParams, this.dateRange))
                    .then( res => {
                        this.userList = res.rows
                        this.total = res.total
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
                    username: undefined,
                    nickname: undefined,
                    password: undefined,
                    email: undefined,
                    telephone: undefined,
                    sex: '0',
                    address: undefined,
                    remark: undefined,
                    status: '0',
                    roleIds: []
                };
                this.resetForm("form");
            },
            // 用户性别字典翻译
            sexFormat(row, column) {
                return this.selectDictLabel(this.sexOptions, row.sex);
            },
            // 角色状态修改
            handleStatusChange(row) {
                let text = row.status === "0" ? "启用" : "停用";
                this.$confirm('确认要"' + text + '""' + row.username + '"用户吗?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(function() {
                    let data = {
                        id: row.id,
                        status: row.status
                    }
                    return changeStatus(data);
                }).then(() => {
                    this.Message.success(text + "成功");
                }).catch(function() {
                    row.status = row.status === "0" ? "1" : "0";
                });
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
            // 按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.addLoading = true
                        if(this.form.id === undefined){
                            addUser(this.form).then(res => {
                                this.Message.success('用户' + this.form.username + '添加成功')
                                this.getList()
                            })
                        }else{
                            editUser(this.form).then(res => {
                                this.Message.success('用户' + this.form.username + '修改成功')
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
                        ids.push(row[index].id)
                    }
                } else {
                    ids.push(row.id)
                }
                let params = {
                    userIds: ids
                }
                removeUser(params).then(res => {
                    this.Message.success("删除成功!")
                    this.getList()
                })
            },
            // 修改按钮交互
            handleEdit(row) {
                this.reset()
                this.title = '修改用户'
                this.dialogFormVisible = true
                queryUser(row.id).then(res => {
                    // this.form = res
                    this.form = res.data
                    this.form.roleIds = res.roleIds
                    this.roleOptions = res.roles
                })
            },
            // 添加按钮交互
            handleAdd(){
                this.reset()
                this.title = '添加用户'
                this.dialogFormVisible = true
                getRoles().then(res => {
                    this.roleOptions = res.roles
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