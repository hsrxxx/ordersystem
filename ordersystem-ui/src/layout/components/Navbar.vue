<template>
    <div style="text-align: right; font-size: 12px">
        <el-dropdown @command="handleCommand">
            <i class="el-icon-setting" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="userInfo">个人信息</el-dropdown-item>
                <el-dropdown-item command="logOut">退出登录</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        <span>王小虎</span>
    </div>
</template>

<script>
    import store from "../../store";
    import { getToken, removeToken } from '@/utils/auth'

    export default {
        name: 'Navbar',
        methods: {
            handleCommand(command) {
                removeToken()
                this.$router.push('login')
            },
        }
    }
</script>

<style>

    .el-header {
        background-color: #B3C0D1;
        color: #333;
        line-height: 60px;
    }

</style>