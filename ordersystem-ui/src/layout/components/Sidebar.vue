<template>
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu router>
            <el-submenu v-for="(item,index) in $router.options.routes" :key="index" :index="index + ''" v-if="item.show">
                <template slot="title">
                    <i :class="item.icon"></i>
                    <span>{{ item.name }}</span>
                </template>
                <el-menu-item v-for="(item2,index2) in item.children"
                              :key="index2"
                              :index="item2.path"
                              :class="$route.path == item2.path ? 'is-active' : ''">
                    {{item2.name}}
                </el-menu-item>
            </el-submenu>
        </el-menu>
    </el-aside>
</template>

<script>
    export default {
        name: 'Sidebar'
    }
</script>

<style>

    .el-aside {
        color: #333;
    }
</style>