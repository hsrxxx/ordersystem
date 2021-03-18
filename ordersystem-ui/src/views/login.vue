<template>
    <div>
        <el-row class="login">
            <el-col class="demo-ruleForm">
                <h1 style="text-align: center;margin: 5px;font-size: x-large;">用户登陆</h1>
                <div style="display: flex;justify-content: center;margin-bottom: 10px">
                    <span>没有账号?</span>
                    <router-link to="/register" style="font-size: 16px;margin-left: 10px;text-decoration: none">注册</router-link>
                </div>
                <el-form :model="form" status-icon :rules="rules" ref="form">
                    <el-form-item prop="username">
                        <el-input type="text" v-model="form.username" autocomplete="off" placeholder="用户名">
                            <i slot="prefix" class="el-icon-user-solid" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" v-model="form.password" autocomplete="off" placeholder="密码" @keyup.enter.native="submitForm('form')">
                            <i slot="prefix" class="el-icon-lock" />
                        </el-input>
                    </el-form-item>
<!--                    <router-link to="/changePass" style="text-decoration: none;">忘记密码?</router-link>-->
<!--                    <el-checkbox v-model="ruleForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>-->
                    <el-button type="primary"
                               @click="submitForm('form')"
                               style="width:100%;margin-top: 10px">登陆</el-button>
                </el-form>
            </el-col>
            <span style="position: fixed;bottom: 30px;font-size: 14px;color: #fff;text-align: center">Copyright © {{ new Date().getFullYear() }} 桂平市雄途网络科技有限公司 All Rights Reserved.<br><a href="http://beian.miit.gov.cn" style="color: white;text-decoration: none">桂ICP备2020007346号-1</a></span>
        </el-row>
    </div>
</template>

<script>
    // import Cookies from "js-cookie";
    // import { encrypt, decrypt } from '../utils/jsencrypt'
    import { login } from '@/request/api'

    export default {
        data() {
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    callback();
                }
            };
            var validateUser = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入用户名'));
                } else {
                    callback();
                }
            };
            return {
                form: {
                    grant_type: 'password',
                    client_id: 'hsrxxx',
                    username: '',
                    password: '',
                    client_secret:  'hsrxxx_9812'
                    // rememberMe: false,
                },
                rules: {
                    username: [
                        { validator: validateUser, trigger: 'blur' }
                    ],
                    password: [
                        { validator: validatePass, trigger: 'blur' }
                    ],
                }
            };
        },
        created() {
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.login(this.form.username, this.form.password)
                    } else {
                        // console.log('error submit!!');
                        return false;
                    }
                });
            },
            login(username, password){
                this.form.username = username
                this.form.password = password
                login(this.form).then(res => {
                        if (res.code === 200){
                            // this.$Message.success(res.msg)
                            localStorage.setItem('token',res.data.token)
                            localStorage.setItem('refresh_token',res.data.refresh_token)
                            localStorage.setItem('expires_in',res.data.expires_in)
                            this.$router.push("/")
                            // console.log(localStorage.getItem('expires_in'))
                            // this.$store.commit("chageToken",res.data.data.access_token)
                            // this.$http.defaults.headers = {
                            //     'access_token': res.data.data.access_token
                            // }
                            // this.$router.push("/home")
                        } else {
                            this.$Message.error(res.msg)
                        }
                    })
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