<template>
    <div>
        <el-container style="height: 100vh; border: 1px solid #eee">
            <Sidebar />
            <el-container>
                <el-header>
                    <Navbar />
                </el-header>
                <Main />
            </el-container>
        </el-container>
    </div>
</template>


<script>

    import { Main, Navbar, Sidebar } from './components'

    export default {
        data() {
            return {}
        },
        components: {
            Main,
            Navbar,
            Sidebar,
        },
    }
    
</script>

<style>

</style>