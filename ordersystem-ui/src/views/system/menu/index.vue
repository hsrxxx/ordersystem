<template>
    <el-main>
        <el-row :gutter="10">
            <el-col :span="1.5">
                <el-button type="primary" plain size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
            </el-col>
        </el-row>

        <el-table
            id="menu"
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

        <el-dialog title="新增菜单" :visible.sync="dialogFormVisible" width="30%" :showClose="false" :close-on-click-modal="false">
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
                <el-button @click="dialogFormVisible = false">取 消</el-button>
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
                // 菜单表格树数据
                menuList: [],
                editDisabled: true,
                removeDisabled: true,
                // 是否显示弹出层
                dialogFormVisible: false,
                addLoading: false,
                // 菜单树选项
                menuOptions: [],
                // 显示状态数据字典
                visibleOptions: [],
                // 菜单状态数据字典
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

            getList(){
                listMenu().then(res => {
                    this.menuList = this.handleTree(res.data, "id");
                });
            },
            getTreeselect() {
                listMenu().then(response => {
                    this.menuOptions = [];
                    const menu = { id: 0, name: '主类目', children: [] };
                    menu.children = this.handleTree(response.data, "id");
                    this.menuOptions.push(menu);
                });
            },
            /** 转换菜单数据结构 */
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
            /** 搜索按钮操作 */
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

            // 按钮交互
            submitForm(form) {
                this.$refs[form].validate((valid) => {
                    if (valid) {
                        this.addLoading = true
                        if(this.form.id === undefined){
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
                        }else{
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
                        }
                        this.dialogFormVisible = false
                        this.addLoading = false
                    }
                })
            },

            handleRemove(row){
                removeMenu(row.id).then(res => {
                    this.Message.success("删除成功!")
                    this.getList()
                })
            },

            handleEdit(row) {
                this.reset()
                this.getTreeselect()
                queryMenu(row.id).then(res => {
                    this.form = res.data
                    console.log(this.form)
                    this.dialogFormVisible = true
                })
            },

            handleAdd(row){
                this.reset()
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

    .el-select-dropdown__item  {
        font-size: 20px!important;
    }
</style>