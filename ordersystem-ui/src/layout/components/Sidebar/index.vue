<template>
    <el-aside width="200px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-active="activeMenu"
                 :unique-opened="true">
            <sidebar-item
                    v-for="(route, index) in sidebarRouters"
                    :key="route.path  + index"
                    :item="route"
                    :base-path="route.path" />
<!--            <el-submenu v-for="(item,index) in sidebarRouters" :key="index" :index="index + ''" v-if="!item.hidden">-->
<!--                <template slot="title">-->
<!--                    <i :class="item.icon"></i>-->
<!--                    <span>{{ item.name }}</span>-->
<!--                </template>-->
<!--                <el-menu-item v-for="(item2,index2) in item.children"-->
<!--                              :key="index2"-->
<!--                              :index="item2.path"-->
<!--                              :class="$route.path == item2.path ? 'is-active' : ''">-->
<!--                    {{item2.name}}-->
<!--                </el-menu-item>-->
<!--            </el-submenu>-->
        </el-menu>
    </el-aside>
</template>

<script>
    import SidebarItem from "./SidebarItem";
    import { mapGetters } from "vuex";

    export default {
        name: 'Sidebar',
        components: { SidebarItem },
        computed: {
            ...mapGetters(["sidebarRouters"]),
            activeMenu() {
                const route = this.$route;
                const { meta, path } = route;
                // if set path, the sidebar will highlight the path you set
                if (meta.activeMenu) {
                    return meta.activeMenu;
                }
                return path;
            },
        },
    }
</script>

<style>

    .el-aside {
        color: #333;
    }
</style>