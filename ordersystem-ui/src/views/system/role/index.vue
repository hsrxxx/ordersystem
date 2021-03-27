<template>
    <el-main>
        <!--  角色查询参数  -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="角色名称" prop="dictName">
                <el-input
                        v-model="queryParams.roleName"
                        placeholder="请输入角色名称"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="权限字符" prop="dictType">
                <el-input
                        v-model="queryParams.roleKey"
                        placeholder="请输入权限字符"
                        clearable
                        size="small"
                        style="width: 240px"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select
                        v-model="queryParams.status"
                        placeholder="角色状态"
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
            <el-col :span="1.5">
                <el-button type="warning" plain size="mini" icon="el-icon-download" @click="handleExport">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--  角色数据  -->
        <el-table
            v-loading="loading"
            ref="multipleTable"
            :data="roleList"
            :header-cell-style="{ background:'#f8f8f9' }"
            style="margin-top:20px"
            @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="id" label="角色编号" header-align="center" align="center" width="80" />
            <el-table-column prop="roleName" label="角色名称" header-align="center" align="center" />
            <el-table-column prop="roleKey" label="权限字符" header-align="center" align="center" />
            <el-table-column prop="status" label="状态" header-align="center" align="center">
                <template slot-scope="scope">
                    <el-switch
                            v-model="scope.row.status"
                            active-value="0"
                            inactive-value="1"
                            @change="handleStatusChange(scope.row)"
                    ></el-switch>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间" header-align="center" align="center">
            </el-table-column>
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

        <!-- 添加或修改角色对话框 -->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="30%" :showClose="false" :close-on-click-modal="false">
            <el-form id="form" :model="form" :rules="rules" ref="form" label-width="80px">
                <el-form-item label="角色名称" prop="roleName">
                    <el-input v-model="form.roleName" placeholder="请输入角色名称" />
                </el-form-item>
                <el-form-item label="权限字符" prop="roleKey">
                    <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
                </el-form-item>
                <el-form-item label="状态">
                    <el-radio-group v-model="form.status">
                        <el-radio
                                v-for="dict in statusOptions"
                                :key="dict.dictValue"
                                :label="dict.dictValue"
                        >{{dict.dictLabel}}</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="菜单权限">
                    <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>
                    <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选</el-checkbox>
                    <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动</el-checkbox>
                    <el-tree
                            class="tree-border"
                            :data="menuOptions"
                            show-checkbox
                            ref="menu"
                            node-key="id"
                            :check-strictly="!form.menuCheckStrictly"
                            empty-text="加载中，请稍后"
                            :props="defaultProps"
                    ></el-tree>
                </el-form-item>
                <el-form-item label="备注">
                    <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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

    import { listRole, queryRole, addRole, editRole, removeRole, changeStatus } from '@/api/system/role'
    import { treeselect as menuTreeselect, roleMenuTreeselect } from '@/api/system/menu'

    export default {
        name: 'Role',
        data() {
            return {
                // 遮罩层
                loading: true,
                // 角色列表
                roleList: [],
                // 选中数组
                ids: [],
                // 总数
                total: 0,
                // 展开/折叠
                menuExpand: false,
                // 全选/全不选
                menuNodeAll: false,
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
                // 菜单列表
                menuOptions: [],
                // 默认数据
                defaultProps: {
                    children: "children",
                    label: "label"
                },
                // 表单验证
                rules: {
                    roleName: [
                        { required: true, message: "角色名称不能为空", trigger: "blur" }
                    ],
                    roleKey: [
                        { required: true, message: "权限字符不能为空", trigger: "blur" }
                    ],
                },
                // 查询参数
                queryParams: {
                    pageNum: 1,
                    pageSize: 10,
                    roleName: undefined,
                    roleKey: undefined,
                    status: undefined
                }
            }
        },
        created() {
            this.getList()
            this.getDicts("sys_normal_disable").then(response => {
                this.statusOptions = response.data;
            });
        },
        methods: {
            // 查询角色列表
            getList(){
                this.loading = true;
                listRole(this.addDateRange(this.queryParams, this.dateRange))
                    .then( res => {
                        this.roleList = res.rows
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
                    roleName: undefined,
                    roleKey: undefined,
                    status: "0",
                    menuIds: [],
                    menuCheckStrictly: true,
                    remark: undefined
                };
                this.resetForm("form");
            },
            // 角色状态修改
            handleStatusChange(row) {
                let text = row.status === "0" ? "启用" : "停用";
                this.$confirm('确认要"' + text + '""' + row.roleName + '"角色吗?', "警告", {
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
            // 查询菜单树结构
            getMenuTreeselect() {
                menuTreeselect().then(response => {
                    this.menuOptions = response.data;
                });
            },
            // 根据角色ID查询菜单树结构
            getRoleMenuTreeselect(roleId) {
                return roleMenuTreeselect(roleId).then(response => {
                    this.menuOptions = response.menus;
                    return response;
                });
            },
            // 所有菜单节点数据
            getMenuAllCheckedKeys() {
                // 目前被选中的菜单节点
                let checkedKeys = this.$refs.menu.getCheckedKeys();
                // 半选中的菜单节点
                let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
                checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
                return checkedKeys;
            },
            // 按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.form.menuIds = this.getMenuAllCheckedKeys();
                        this.addLoading = true
                        if(this.form.id !== undefined){
                            editRole(this.form).then(res => {
                                this.Message.success('角色"' + this.form.roleName + '"修改成功')
                                this.getList()
                            })
                        }else{
                            addRole(this.form).then(res => {
                                this.Message.success('角色"' + this.form.roleName + '"添加成功')
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
                this.dateRange = []
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
                    roleIds: ids
                }
                removeRole(params).then(res => {
                    this.Message.success("删除成功!")
                    this.getList()
                })
            },
            // 修改按钮交互
            handleEdit(row) {
                this.reset()
                this.title = '修改角色'
                const roleId = row.id || this.ids
                const roleMenu = this.getRoleMenuTreeselect(roleId);
                queryRole(row.id).then(res => {
                    // this.form = res
                    this.form = res.data
                    this.dialogFormVisible = true
                    this.$nextTick(() => {
                        roleMenu.then(res => {
                            let checkedKeys = res.checkedKeys
                            checkedKeys.forEach((v) => {
                                this.$nextTick(()=>{
                                    this.$refs.menu.setChecked(v, true ,false);
                                })
                            })
                        });
                    });
                })
            },
            // 添加按钮交互
            handleAdd(){
                this.reset()
                this.title = '添加产品'
                this.dialogFormVisible = true
                this.getMenuTreeselect()
            },
            // 导出按钮交互
            handleExport(){
                this.download('system/role/export', {
                    ...this.queryParams
                }, `role_${new Date().getTime()}.xlsx`)
            },
            // 树权限（全选/全不选）
            handleCheckedTreeNodeAll(value, type) {
                if (type == 'menu') {
                    this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
                } else if (type == 'dept') {
                    this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
                }
            },
            // 树权限（展开/折叠）
            handleCheckedTreeExpand(value, type) {
                if (type == 'menu') {
                    let treeList = this.menuOptions;
                    for (let i = 0; i < treeList.length; i++) {
                        this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
                    }
                } else if (type == 'dept') {
                    let treeList = this.deptOptions;
                    for (let i = 0; i < treeList.length; i++) {
                        this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
                    }
                }
            },
            // 树权限（父子联动）
            handleCheckedTreeConnect(value, type) {
                if (type == 'menu') {
                    this.form.menuCheckStrictly = value ? true: false;
                } else if (type == 'dept') {
                    this.form.deptCheckStrictly = value ? true: false;
                }
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

    .tree-border {
        margin-top: 5px;
        border: 1px solid #e5e6e7;
        background: #fff none;
        border-radius: 4px;
    }
</style>