<template>
    <div>
        <el-row class="login">
            <el-col class="demo-ruleForm">
                <h1 style="text-align: center;margin: 5px;font-size: x-large;">用户登陆</h1>
                <div style="display: flex;justify-content: center;margin-bottom: 10px">
                    <span>没有账号?</span>
                    <router-link to="/register" style="font-size: 16px;margin-left: 10px;text-decoration: none">注册</router-link>
                </div>
                <el-form :model="loginForm" status-icon :rules="rules" ref="loginForm">
                    <el-form-item prop="username">
                        <el-input type="text" v-model="loginForm.username" autocomplete="off" placeholder="用户名">
                            <i slot="prefix" class="el-icon-user-solid" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" v-model="loginForm.password" autocomplete="off" placeholder="密码" @keyup.enter.native="submitForm('form')">
                            <i slot="prefix" class="el-icon-lock" />
                        </el-input>
                    </el-form-item>
                    <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
<!--                    <router-link to="/changePass" style="text-decoration: none;">忘记密码?</router-link>-->
<!--                    <el-checkbox v-model="ruleForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>-->
                    <el-button type="primary"
                               :loading="loading"
                               @click="handleLogin"
                               style="width:100%;margin-top: 10px">
                        <span v-if="!loading">登 录</span>
                        <span v-else>登 录 中...</span>
                    </el-button>
                </el-form>
            </el-col>
            <span style="position: fixed;bottom: 30px;font-size: 14px;color: #fff;text-align: center">Copyright © {{ new Date().getFullYear() }} 桂平市雄途网络科技有限公司 All Rights Reserved.<br><a href="http://beian.miit.gov.cn" style="color: white;text-decoration: none">桂ICP备2020007346号-1</a></span>
        </el-row>
    </div>
</template>


<script>
    import Cookies from "js-cookie";
    import { encrypt, decrypt } from '@/utils/jsencrypt'

    export default {
        data() {
            return {
                loginForm: {
                    grant_type: 'password',
                    client_id: 'hsrxxx',
                    username: '',
                    password: '',
                    client_secret:  'hsrxxx_9812',
                    rememberMe: false,
                },
                rules: {
                    username: [ { required: true, trigger: "blur", message: "用户名不能为空" } ],
                    password: [ { required: true, trigger: "blur", message: "密码不能为空" } ],
                },
                loading:false,
            };
        },
        created() {
            this.getCookie()
        },
        methods: {
            getCookie() {
                const grant_type = Cookies.get("grant_type");
                const client_id = Cookies.get("client_id");
                const username = Cookies.get("username");
                const password = Cookies.get("password");
                const client_secret = Cookies.get("client_secret");
                const rememberMe = Cookies.get('rememberMe')
                this.loginForm = {
                    grant_type: 'password',
                    client_id: 'hsrxxx',
                    username: username === undefined ? this.loginForm.username : username,
                    password: password === undefined ? this.loginForm.password : decrypt(password),
                    client_secret:  'hsrxxx_9812',
                    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
                };
            },
            handleLogin(){
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        this.loading = true;
                        if (this.loginForm.rememberMe) {
                            Cookies.set("grant_type", this.loginForm.grant_type, { expires: 30 });
                            Cookies.set("client_id", this.loginForm.client_id, { expires: 30 });
                            Cookies.set("username", this.loginForm.username, { expires: 30 });
                            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
                            Cookies.set('client_secret', this.loginForm.client_secret, { expires: 30 });
                            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
                        } else {
                            Cookies.remove("grant_type");
                            Cookies.remove("client_id");
                            Cookies.remove("username");
                            Cookies.remove("password");
                            Cookies.remove('client_secret');
                            Cookies.remove('rememberMe');
                        }
                        this.$store.dispatch("Login", this.loginForm).then(() => {
                            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
                        }).catch(() => {
                            this.loading = false;
                        });
                    }
                });
            }
        }
    }
</script>

<style>
    .login {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        background-image: url("https://vue.ruoyi.vip/static/img/login-background.f9f49138.jpg");
        background-size: cover;
    }

    .demo-ruleForm {
        width: 400px!important;
        border-radius: 6px;
        padding: 30px;
        background: #fff;
    }

    .login .el-input__prefix {
        left: 10px!important;
    }
</style>