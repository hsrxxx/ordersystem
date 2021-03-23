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
            <right-toolbar @queryTable="getList(1,10)"></right-toolbar>
        </el-row>
        
        <el-dialog title="新增角色" :visible.sync="dialogFormVisible" width="30%" :showClose="false" :close-on-click-modal="false">
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
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id == undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>

        <el-table
            id="role"
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
                <template slot-scope="scope">
                    <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
            </el-table-column>
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

    import { listRole, queryRole, addRole, editRole, removeRole, changeStatus } from '@/api/system/role'
    import { treeselect, roleMenuTreeselect } from '@/api/system/menu'

    export default {
        name: 'Role',
        data() {
            return {
                // 角色列表
                roleList: [],
                // 选中数组
                ids: [],
                // 总数
                count: 0,
                // 第一页
                index: 1,
                // 每页个数
                limit: 10,
                // 选中列表
                multipleSelection: [],
                // 展开/折叠
                menuExpand: false,
                // 全选/全不选
                menuNodeAll: false,
                // 修改按钮控制
                editDisabled: true,
                // 删除按钮控制
                removeDisabled: true,
                // 添加按钮控制
                addLoading: false,
                // 添加/修改弹窗表单
                dialogFormVisible: false,
                // 添加/修改表单
                form: {},
                // 状态数据字典
                statusOptions: [],
                // 菜单列表
                menuOptions: [],
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
                pageDomain:{
                    pageNum: this.index,
                    pageSize: this.limit,
                    orderByColumn: undefined,
                    isAsc: undefined
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
            getList(){
                listRole(this.pageDomain)
                    .then( res => {
                        this.roleList = res.rows
                        this.count = res.total
                    })
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
            /** 查询菜单树结构 */
            getMenuTreeselect() {
                menuTreeselect().then(response => {
                    this.menuOptions = response.data;
                });
            },
            /** 根据角色ID查询菜单树结构 */
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
            handleEdit(row) {
                this.reset()
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
            handleAdd(){
                this.reset()
                this.dialogFormVisible = true
                this.getMenuTreeselect();
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
            // 分页
            // 修改每页数量
            handleSizeChange(val) {
                if(this.count < val){
                    this.index = Math.ceil(this.count / val)
                }
                this.limit = val
                this.getList()
            },
            // 修改第几页
            handleCurrentChange(val) {
                this.index = val
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

    .tree-border {
        margin-top: 5px;
        border: 1px solid #e5e6e7;
        background: #fff none;
        border-radius: 4px;
    }
</style>