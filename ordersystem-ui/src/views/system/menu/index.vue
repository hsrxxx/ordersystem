<template>
    <el-main>
        <!--  菜单查询参数  -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch">
            <el-form-item label="菜单名称" prop="menuName">
                <el-input
                        v-model="queryParams.name"
                        placeholder="请输入菜单名称"
                        clearable
                        size="small"
                        @keyup.enter.native="handleQuery"
                />
            </el-form-item>
            <el-form-item label="状态" prop="status">
                <el-select v-model="queryParams.status" placeholder="菜单状态" clearable size="small">
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
                <el-button type="primary" plain size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!-- 菜单列表 -->
        <el-table
            v-loading="loading"
            :data="menuList"
            :header-cell-style="{ background:'#f8f8f9' }"
            style="margin-top:20px"
            row-key="id"
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
            <el-table-column prop="name" label="菜单名称" width="160" :show-overflow-tooltip="true" />
            <el-table-column prop="icon" label="图标" width="80" align="center">
                <template slot-scope="scope">
                    <i :class="scope.row.icon" />
                </template>
            </el-table-column>
            <el-table-column prop="orderNum" label="排序" width="80" align="center" />
            <el-table-column prop="perms" label="权限标识" :show-overflow-tooltip="true" />
            <el-table-column prop="component" label="组件路径" :show-overflow-tooltip="true" />
            <el-table-column prop="status" label="状态" :formatter="statusFormat" header-align="center" align="center" />
            <el-table-column prop="createTime" label="创建时间" header-align="center" align="center" />
            <el-table-column
                fixed="right"
                label="操作"
                header-align="center"
                align="center"
                class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleAdd(scope.row)" type="text" size="small" icon="el-icon-plus">新增</el-button>
                    <el-button @click="handleEdit(scope.row)" type="text" size="small"  icon="el-icon-edit">修改</el-button>
                    <el-button @click="handleRemove(scope.row)" type="text" size="small" icon="el-icon-delete">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 添加或修改菜单对话框 -->
        <el-dialog :title="title" :visible.sync="dialogFormVisible" width="30%" :showClose="false" :close-on-click-modal="false">
            <el-form id="form" :model="form" :rules="rules" ref="form" label-width="80px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="上级菜单">
                            <treeselect
                                    v-model="form.parentId"
                                    :options="menuOptions"
                                    :normalizer="normalizer"
                                    :show-count="true"
                                    placeholder="选择上级菜单" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="菜单类型">
                            <el-radio-group v-model="form.menuType">
                                <el-radio label="M">目录</el-radio>
                                <el-radio label="C">菜单</el-radio>
                                <el-radio label="F">按钮</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="菜单图标">
                            <e-icon-picker v-model="form.icon"/>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="菜单名称" prop="name">
                            <el-input v-model="form.name" autocomplete="off" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="显示排序" prop="orderNum">
                            <el-input v-model="form.orderNum" autocomplete="off" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="form.menuType != 'F'" label="是否外链">
                            <el-radio-group v-model="form.isFrame">
                                <el-radio label="0">是</el-radio>
                                <el-radio label="1">否</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="form.menuType != 'F'" label="路由地址" prop="path">
                            <el-input v-model="form.path" placeholder="请输入路由地址" />
                        </el-form-item>
                    </el-col>

                    <el-col :span="12" v-if="form.menuType == 'C'">
                        <el-form-item label="组件路径" prop="component">
                            <el-input v-model="form.component" placeholder="请输入组件路径" />
                        </el-form-item>
                    </el-col>

                    <el-col :span="12">
                        <el-form-item v-if="form.menuType != 'M'" label="权限标识">
                            <el-input v-model="form.perms" placeholder="请权限标识" maxlength="50" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="form.menuType != 'F'" label="显示状态">
                            <el-radio-group v-model="form.visible">
                                <el-radio
                                        v-for="dict in visibleOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictValue"
                                >{{dict.dictLabel}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="form.menuType != 'F'" label="菜单状态">
                            <el-radio-group v-model="form.status">
                                <el-radio
                                        v-for="dict in statusOptions"
                                        :key="dict.dictValue"
                                        :label="dict.dictValue"
                                >{{dict.dictLabel}}</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item v-if="form.menuType == 'C'" label="是否缓存">
                            <el-radio-group v-model="form.isCache">
                                <el-radio label="0">缓存</el-radio>
                                <el-radio label="1">不缓存</el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="submitForm('form')" :loading="addLoading">{{ form.id == undefined ? '新 增':'修 改' }}</el-button>
            </div>
        </el-dialog>
    </el-main>
</template>

<script>

    import { listMenu,queryMenu, addMenu, editMenu, removeMenu } from '@/api/system/menu';
    import Treeselect from "@riophae/vue-treeselect";
    import "@riophae/vue-treeselect/dist/vue-treeselect.css";

    export default {
        name: 'Menu',
        components: { Treeselect },
        data() {
            return {
                // 遮罩层
                loading: true,
                // 菜单表格树数据
                menuList: [],
                // 显示搜索条件
                showSearch: true,
                // 修改按钮控制
                editDisabled: true,
                // 删除按钮控制
                removeDisabled: true,
                // 添加按钮控制
                addLoading: false,
                // 添加/修改弹窗表单
                dialogFormVisible: false,
                // 菜单树选项
                menuOptions: [],
                // 显示状态数据字典
                visibleOptions: [],
                // 菜单状态数据字典
                statusOptions: [],
                // 查询参数
                queryParams: {
                    menuName: undefined,
                    visible: undefined
                },
                // 表单标题
                title: "",
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
            }
        },
        created() {
            this.getList()
            this.getDicts("sys_normal_disable").then(response => {
                this.statusOptions = response.data;
            });
            this.getDicts("sys_show_hide").then(response => {
                this.visibleOptions = response.data;
            });
        },
        methods: {
            // 获取菜单列表
            getList(){
                this.loading = true;
                listMenu(this.queryParams).then(res => {
                    this.menuList = this.handleTree(res.data, "id");
                    this.loading = false;
                });
            },
            // 获取菜单列表
            getTreeselect() {
                listMenu().then(response => {
                    this.menuOptions = [];
                    const menu = { id: 0, name: '主类目', children: [] };
                    menu.children = this.handleTree(response.data, "id");
                    this.menuOptions.push(menu);
                });
            },
            // 转换菜单数据结
            normalizer(node) {
                if (node.children && !node.children.length) {
                    delete node.children;
                }
                return {
                    id: node.id,
                    label: node.name,
                    children: node.children
                };
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
                    parentId: 0,
                    name: undefined,
                    icon: undefined,
                    menuType: "M",
                    orderNum: undefined,
                    isFrame: "1",
                    isCache: "0",
                    visible: "0",
                    status: "0"
                };
                this.resetForm("form");
            },
            // 搜索按钮操
            handleQuery() {
                this.getList();
            },
            // 菜单状态字典翻译
            statusFormat(row, column) {
                if (row.menuType == "F") {
                    return "";
                }
                return this.selectDictLabel(this.statusOptions, row.status);
            },
            // 搜索按钮操作
            handleQuery() {
                this.getList();
            },
            // 重置按钮操作
            resetQuery() {
                this.resetForm("queryForm");
                this.handleQuery();
            },
            // 按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.addLoading = true
                        if(this.form.id !== undefined){
                            editMenu(this.form).then(res => {
                                if (this.form.menuType === 'M'){
                                    this.Message.success('目录' + this.form.name + '添加成功')
                                } else if (this.form.menuType === 'C'){
                                    this.Message.success('菜单' + this.form.name + '添加成功')
                                } else if (this.form.menuType === 'F') {
                                    this.Message.success('按钮' + this.form.name + '添加成功')
                                }
                                this.getList()
                            })
                        }else{
                            addMenu(this.form).then(res => {
                                if (this.form.menuType === 'M'){
                                    this.Message.success('目录' + this.form.name + '添加成功')
                                } else if (this.form.menuType === 'C'){
                                    this.Message.success('菜单' + this.form.name + '添加成功')
                                } else if (this.form.menuType === 'F') {
                                    this.Message.success('按钮' + this.form.name + '添加成功')
                                }
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
                removeMenu(row.id).then(res => {
                    this.Message.success("删除成功!")
                    this.getList()
                })
            },
            // 修改按钮交互
            handleEdit(row) {
                this.reset()
                this.title = '修改菜单'
                this.getTreeselect()
                queryMenu(row.id).then(res => {
                    this.form = res.data
                    console.log(this.form)
                    this.dialogFormVisible = true
                })
            },
            // 添加按钮交互
            handleAdd(row){
                this.reset()
                this.title = '添加菜单'
                this.getTreeselect()
                if (row != null && row.id) {
                    this.form.parentId = row.id;
                } else {
                    this.form.parentId = 0;
                }
                this.dialogFormVisible = true
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