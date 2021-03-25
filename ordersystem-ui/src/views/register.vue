<template>
    <div>
        <el-row class="login">
            <el-col class="demo-ruleForm">
                <h1 style="text-align: center;margin: 5px;font-size: x-large;">用户注册</h1>
                <div style="display: flex;justify-content: center;margin-bottom: 10px">
                    <span>已拥有账号?</span>
                    <router-link to="/login" style="font-size: 16px;margin-left: 10px;text-decoration: none">登陆</router-link>
                </div>
                <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm">
                    <el-form-item prop="user">
                        <el-input type="text" v-model="ruleForm.user" autocomplete="off" placeholder="用户名">
                            <i slot="prefix" class="el-icon-user-solid" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="email">
                        <el-input type="text" v-model="ruleForm.email" autocomplete="off" placeholder="电子邮箱">
                            <i slot="prefix" class="el-icon-message" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="pass">
                        <el-input type="password" v-model="ruleForm.pass" autocomplete="off" placeholder="密码">
                            <i slot="prefix" class="el-icon-lock" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="checkPass">
                        <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" placeholder="再次输入密码">
                            <i slot="prefix" class="el-icon-lock" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="phone">
                        <el-input type="text" v-model="ruleForm.phone" autocomplete="off" placeholder="手机号码">
                            <i slot="prefix" class="el-icon-phone-outline" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="invite">
                        <el-input type="text" v-model="ruleForm.invite_code" autocomplete="off" placeholder="邀请码">
                            <i slot="prefix" class="el-icon-link" />
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="code">
                        <el-input type="text" v-model="ruleForm.code" placeholder="验证码">
                            <i slot="prefix" class="el-icon-position" />
                        </el-input>
                        <el-button type="primary" @click="sendCode" v-if="show">获取验证码</el-button>
                        <el-button type="primary" disabled v-else>{{ countDownTime }} s</el-button>
                    </el-form-item>
                    <el-form-item prop="protocol">
                        <el-checkbox type="checkbox" v-model="ruleForm.protocol"></el-checkbox>
                        <span style="margin-left: 5px">已阅读并同意<el-button type="text" @click="openProtocol = true">《用户注册协议》</el-button></span>
                    </el-form-item>
                    <el-dialog
                            title="熊之囧途电商服务网注册用户协议"
                            :visible.sync="openProtocol"
                            width="50%"
                            :before-close="handleClose">
                        <div>
                            <p> 欢迎阅读雄途网络科技服务网（桂平市雄途网络科技有限公司，以下称“本公司”）条款协议（下称“本协议”），本协议适用于您使用本网站（www.xiongzhijiongtu.com）所提供的服务和工具。</p>

                            <h4>1.接受协议</h4>

                            <p>通过本网站首页进入（www.xiongzhijiongtu.com）即代表您同意即表示您同意自己已经与桂平市雄途网络科技有限公司订立本协议，且您将受本协议的条款和条件 (“条款”) 约束。桂平市雄途网络科技有限公司可随时自行全权决定更改“条款”。如“条款”有任何变更，桂平市雄途网络科技有限公司将在其网站上刊载公告，通知予您。如您不同意相关变更，必须停止使用“服务”。经修订的“条款”一经在雄途网络科技服务网的公布后，立即自动生效。您应在第一次登录后仔细阅读修订后的“条款”，并有权选择停止继续使用“服务”；一旦您继续使用“服务”，则表示您已接受经修订的“条款”，当您与雄途网络科技服务网发生争议时，应以最新的服务协议为准。除另行明确声明外，任何使“服务”范围扩大或功能增强的新内容均受本协议约束。除非桂平市雄途网络科技有限公司的授权高层管理人员签订书面协议，本协议不得另行作出修订。</p>

                            <h4>2.谁可使用（www.xiongzhijiongtu.com）？</h4>

                            <p>“服务”仅供能够根据相关法律订立具有法律约束力的合约的个人或公司使用。因此，您的年龄必须在十八周岁或以上，才可使用本公司服务。如不符合本项条件，请勿使用“服务”。桂平市雄途网络科技有限公司可随时自行全权决定拒绝向任何人士提供“服务”。“服务”不会提供给被暂时或永久中止资格的雄途网络科技服务网会员。</p>

                            <h4>3. 收费。</h4>

                            <p>本公司保留在根据第1条通知您后，收取“服务”费用的权利。您因进行交易、向本公司获取有偿服务或接触本公司服务器而发生的所有应纳税赋，以及相关硬件、软件、通讯、网络服务及其他方面的费用均由您自行承担。本公司保留在无须发出书面通知，仅在雄途网络科技服务网公示的情况下，暂时或永久地更改或停止部分或全部“服务”的权利。</p>

                            <h4>4.雄途网络科技服务网仅作为交易地点。</h4>

                            <p>本公司网站仅作为用户交易的工具，以及获取各类与贸易相关的服务的地点。本公司不能控制交易所涉及的物品的质量、安全或合法性，商贸信息的真实性或准确性，以及交易方履行其在贸易协议项下的各项义务的能力。本公司不能也不会控制交易各方能否履行协议义务。此外，您应注意到，与外国国民、未成年人或以欺诈手段行事的人进行交易的风险是客观存在的。</p>

                            <h4>5.您的资料和供买卖的物品。</h4>

                            <p>“您的资料”包括您在注册、交易或列举物品过程中、在任何公开信息场合或通过任何电子邮件形式，向本公司或其他用户提供的任何资料，包括数据、文本、软件、音乐、声响、照片、图画、影像、词句或其他材料。您应对“您的资料”负全部责任，而本公司仅作为您在网上发布和刊登“您的资料”的被动渠道。但是，倘若本公司认为“您的资料”可能使本公司承担任何法律或道义上的责任，或可能使本公司 (全部或部分地) 失去本公司的互联网服务供应商或其他供应商的服务，或您未在雄途网络科技服务网规定的期限内登录或再次登录网站，则本公司可自行全权决定对“您的资料”采取本公司认为必要或适当的任何行动，包括但不限于删除该类资料。您特此保证，您对提交给雄途网络科技服务网的“您的资料”拥有全部权利，包括全部版权。您确认，雄途网络科技服务网没有责任去认定或决定您提交给本公司的资料哪些是应当受到保护的，对享有“服务”的其他用户使用“您的资料”，本公司也不必负责。</p>

                            <h5>5.1 注册义务。</h5>

                            <p>如您在雄途网络科技服务网注册，您同意：(a) 根据雄途网络科技服务网所刊载的会员资料表格的要求，提供关于您或贵公司的真实、准确、完整和反映当前情况的资料；(b) 维持并及时更新会员资料，使其保持真实、准确、完整和反映当前情况。倘若您提供任何不真实、不准确、不完整或不能反映当前情况的资料，或雄途网络科技服务网有合理理由怀疑该等资料不真实、不准确、不完整或不能反映当前情况，雄途网络科技服务网有权暂停或终止您的注册身份及资料，并拒绝您在目前或将来对“服务”(或其任何部份) 以任何形式使用。如您代表一家公司或其他法律主体在本公司登记，则您声明和保证，您有权使该公司或其他法律主体受本协议“条款”约束。</p>

                            <h5>5.2 会员注册名、密码和保密。</h5>

                            <p>在登记过程中，您将选择会员注册名和密码。您须自行负责对您的会员注册名和密码保密，且须对您在会员注册名和密码下发生的所有活动承担责任。您同意：(a) 如发现任何人未经授权使用您的会员注册名或密码，或发生违反保密规定的任何其他情况，您会立即通知雄途网络科技服务网；及 (b) 确保您在每个上网时段结束时，以正确步骤离开网站。雄途网络科技服务网不能也不会对因您未能遵守本款规定而发生的任何损失或损毁负责。</p>

                            <h5>5.3 关于您的资料的规则。</h5>

                            <p>您同意，“您的资料”和您供在雄途网络科技服务网上交易的任何“物品”（泛指一切可供依法交易的、有形的或无形的、以各种形态存在的某种具体的物品，或某种权利或利益，或某种票据或证券，或某种服务或行为。本协议中“物品”一词均含此义） a. 不会有欺诈成份，与售卖伪造或盗窃无涉； b. 不会侵犯任何第三者对该物品享有的物权，或版权、专利、商标、商业秘密或其他知识产权，或隐私权、名誉权； c. 不会违反任何法律、法规、条例或规章 (包括但不限于关于规范出口管理、贸易配额、保护消费者、不正当竞争或虚假广告的法律、法规、条例或规章)； d. 不会含有诽谤（包括商业诽谤）、非法恐吓或非法骚扰的内容； e. 不会含有淫秽、或包含任何儿童色情内容； f. 不会含有蓄意毁坏、恶意干扰、秘密地截取或侵占任何系统、数据或个人资料的任何病毒、伪装破坏程序、电脑蠕虫、定时程序炸弹或其他电脑程序； g. 不会直接或间接与下述各项货物或服务连接，或包含对下述各项货物或服务的描述：(i) 本协议项下禁止的货物或服务；或 (ii) 您无权连接或包含的货物或服务。此外，您同意不会：(h) 在与任何连锁信件、大量胡乱邮寄的电子邮件、滥发电子邮件或任何复制或多余的信息有关的方面使用“服务”；(i) 未经其他人士同意，利用“服务”收集其他人士的电子邮件地址及其他资料；或 (j) 利用“服务”制作虚假的电子邮件地址，或以其他形式试图在发送人的身份或信息的来源方面误导其他人士。</p>

                            <h5>5.4 被禁止物品。</h5>

                            <p>您不得在本公司网站公布或通过本公司网站买卖：(a) 可能使本公司违反任何相关法律、法规、条例或规章的任何物品；或 (b) 雄途网络科技服务网认为应禁止或不适合通过本网站买卖的任何物品。</p>

                            <h4>6. 您授予本公司的许可使用权。</h4>

                            <p>您授予本公司独家的、全球通用的、永久的、免费的许可使用权利 (并有权在多个曾面对该权利进行再授权)，使本公司有权(全部或部份地) 使用、复制、修订、改写、发布、翻译、分发、执行和展示"您的资料"或制作其派生作品，和/或以现在已知或日后开发的任何形式、媒体或技术，将"您的资料"纳入其他作品内。</p>

                            <h4>7.隐私。</h4>

                            <p>尽管有第6条所规定的许可使用权，雄途网络科技服务网将仅根据本公司的隐私声明使用“您的资料”。本公司隐私声明的全部条款属于本协议的一部份，因此，您必须仔细阅读。请注意，您一旦自愿地在雄途网络科技服务网交易地点披露“您的资料”，该等资料即可能被其他人士获取和使用。</p>

                            <h4>8.交易程序。</h4>

                            <h5>8.1 添加产品描述条目。</h5>

                            <p>产品描述是由您提供的在雄途网络科技服务网上展示的文字描述、图画和/或照片，可以是(a) 对您拥有而您希望出售的产品的描述；或 (b) 对您正寻找的产品的描述。您可在雄途网络科技服务网发布任一类产品描述，或两种类型同时发布，条件是，您必须将该等产品描述归入正确的类目内。雄途网络科技服务网不对产品描述的准确性或内容负责。</p>

                            <h5>8.2 就交易进行协商。</h5>

                            <p>交易各方通过在雄途网络科技服务网上明确描述报盘和回盘，进行相互协商。所有各方接纳报盘或回盘将使所涉及的雄途网络科技服务网用户有义务完成交易。除非在特殊情况下，诸如用户在您提出报盘后实质性地更改对物品的描述或澄清任何文字输入错误，或您未能证实交易所涉及的用户的身份等，报盘和承诺均不得撤回。</p>

                            <h5>8.3 处理交易争议。</h5>

                            <p>本公司不会且不能牵涉进交易各方的交易当中。倘若您与一名或一名以上用户，或与您通过本公司网站获取其服务的第三者服务供应商发生争议，您免除雄途网络科技服务网 (及本公司代理人和雇员) 在因该等争议而引起的，或在任何方面与该等争议有关的不同种类和性质的任何 (实际和后果性的) 权利主张、要求和损害赔偿等方面的责任。</p>

                            <h5>8.4 运用常识。</h5>

                            <p>本公司不能亦不试图对其他用户通过“服务”提供的资料进行控制。就其本质而言，其他用户的资料，可能是令人反感的、有害的或不准确的，且在某些情况下可能带有错误的标识说明或以欺诈方式加上标识说明。本公司希望您在使用本公司网站时，小心谨慎并运用常识。</p>

                            <h4>9.交易系统。</h4>

                            <h5>9.1 不得操纵交易。</h5>

                            <p>您同意不利用帮助实现蒙蔽或欺骗意图的同伙(下属的客户或第三方)，操纵与另一交易方所进行的商业谈判的结果。</p>

                            <h5>9.2 系统完整性。</h5>

                            <p>您同意，您不得使用任何装置、软件或例行程序干预或试图干预雄途网络科技服务网网站的正常运作或正在本公司网站上进行的任何交易。您不得采取对任何将不合理或不合比例的庞大负载加诸本公司网络结构的行动。您不得向任何第三者披露您的密码，或与任何第三者共用您的密码，或为任何未经批准的目的使用您的密码。</p>

                            <h5>9.3 反馈。</h5>

                            <p>您不得采取任何可能损害信息反馈系统的完整性的行动，诸如：利用第二会员身份标识或第三者为您本身留下正面反馈；利用第二会员身份标识或第三者为其他用户留下负面反馈 (反馈数据轰炸)；或在用户未能履行交易范围以外的某些行动时，留下负面的反馈 (反馈恶意强加)。</p>

                            <h5>9.4 不作商业性利用。</h5>

                            <p>您同意，您不得对任何资料作商业性利用，包括但不限于在未经雄途网络科技服务网授权高层管理人员事先书面批准的情况下，复制在雄途网络科技服务网上展示的任何资料并用于商业用途。 </p>

                            <h5> 9.5 法律责任。</h5>

                            <p>您同意，在使用本网站图片下载工具所下载的图片、产品信息属于个人行为且不得传播，所下载图片、产品信息如造成侵权的由您个人负责，本网站不承担任何法律责任。</p>

                            <h4>10. 终止或访问限制。</h4>

                            <h5>您同意，在雄途网络科技服务网可自行全权决定以任何理由终止您的“服务”密码、帐户或您对“服务”的使用，并删除和丢弃您在使用“服务”中提交的的“您的资料”。</h5>
                        </div>
                        <span slot="footer" class="dialog-footer">
                            <el-button type="primary" @click="openProtocol = false">朕 已阅</el-button>
                        </span>
                    </el-dialog>
                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">注册</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
            <span style="position: fixed;bottom: 30px;font-size: 14px;color: #fff;text-align: center">Copyright © {{ new Date().getFullYear() }} 桂平市雄途网络科技有限公司 All Rights Reserved.<br><a href="http://beian.miit.gov.cn" style="color: white;text-decoration: none">桂ICP备2020007346号-1</a></span>
        </el-row>
    </div>
</template>

<script>
    // import md5 from 'js-md5';

    export default {
        data() {
            var validateProtocol = (rule, value, callback) => {
                if (value === false) {
                    callback(new Error('请先阅读并同意《用户注册协议》'));
                } else {
                    callback();
                }
            };
            var validatePass = (rule, value, callback) => {
                var lv = 0;
                if (/\d/.test(value)) lv++; //数字
                if (/[a-z]/.test(value)) lv++; //小写
                if (/[A-Z]/.test(value)) lv++; //大写
                if (/\W/.test(value)) lv++; //特殊字符

                if (value === '') {
                    callback(new Error('请输入密码'));
                } else if (value.indexOf(' ') !== -1){
                    callback(new Error('请输入不包含空格的密码'));
                } else if (lv == 1){
                    callback(new Error('为确保账号安全,密码必须由数字、字母、特殊字符组合！'));
                } else if (value.length < 6 || value.length > 16){
                    callback(new Error('密码长度请保持在6-16位！'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (value !== this.ruleForm.pass) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            var validateUser = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入用户名'));
                } else if (/[\u4E00-\u9FA5]/g.test(value)){
                    callback(new Error('禁止使用中文字符,请重新输入'));
                } else {
                    callback();
                }
            };
            var validatePhone = (rule, value, callback) => {
                const reg = /^\d{11}$/
                if (value === '') {
                    callback(new Error('请输入手机号码'));
                } else if (!reg.test(value)){
                    callback(new Error('请输入正确的11位手机号码'));
                } else {
                    callback();
                }
            };
            var validateCode = (rule, value, callback) => {
                const reg = /^\d{4}$/
                if (value === '') {
                    callback(new Error('请输入验证码'));
                } else if (!reg.test(value)){
                    callback(new Error('请输入正确的验证码'));
                } else {
                    callback();
                }
            };
            return {
                openProtocol: false,
                show: true,
                countDownTime: '',
                sms_token: '',
                ruleForm: {
                    user: '',
                    email: '',
                    pass: '',
                    checkPass: '',
                    phone: '',
                    code: '',
                    protocol: false,
                    invite_code: '',
                },
                rules: {
                    pass: [
                        { required: true, validator: validatePass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { required: true, validator: validatePass2, trigger: 'blur' }
                    ],
                    user: [
                        { required: true, validator: validateUser, trigger: 'blur' }
                    ],
                    phone: [
                        { required: true, validator: validatePhone, trigger: 'blur' }
                    ],
                    email: [
                        { required: true, type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change'] }
                    ],
                    code: [
                        { required: true, validator: validateCode, trigger: 'blur' }
                    ],
                    protocol: [
                        { required: true, validator: validateProtocol, trigger: 'blur' }
                    ]
                },
            };
        },
        created () {
            let myEndTime= localStorage.getItem('myEndTime')
            myEndTime && this.codeCountDown(myEndTime)
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.register()
                    } else {
                        // console.log('error submit!!');
                        // console.log(valid)
                        return false;
                    }
                });
            },
            // resetForm(formName) {
            //     this.$refs[formName].resetFields();
            // },
            register() {
                const nikeName = this.ruleForm.user
                const password = this.ruleForm.pass
                const phone = this.ruleForm.phone
                const email = this.ruleForm.email
                const code = this.ruleForm.code
                const invite_code = this.ruleForm.invite_code
                this.$http.post(
                    "/api/register/user",
                    {
                        'params': {
                            'nikeName': nikeName,
                            'password': password,
                            'phone': phone,
                            'email': email,
                            'code': code,
                            'invite_code': invite_code,
                        }
                    })
                    .then(res => {
                        if (res.data.result.code == 1000){
                            this.$message({
                                showClose: true,
                                message: '注册成功',
                                type: 'success'
                            });
                            this.$router.push("/login")
                        }else{
                            this.$error(res.data.result.code,res.data.result.message)
                        }
                    })
                    // err.request.status == 403
            },
            sendCode() {
                this.$refs.ruleForm.validateField('phone', phoneError => {
                    if (!phoneError) {
                        this.getVerificationCode()
                        this.getCode()
                    } else {
                        return false;
                    }
                });
            },
            getCode(){
                const time = new Date().getTime()
                const str = time.toString() + this.ruleForm.phone + this.$store.state.DEFAULT_NUM
                const sms_token = md5(str)
                this.$http.post(
                    "/api/message/send",
                    {
                            'params': {
                                'sms_token': sms_token,
                                'time': time,
                                'phone': this.ruleForm.phone,
                            }
                        })
                    .then(res => {
                        if (res.data.result.code == 1000){
                            this.$message({
                                showClose: true,
                                message: '获取成功,手机号' + this.ruleForm.phone.toString().substring(3, 0) + '***' + this.ruleForm.phone.toString().substring(7) + '接收验证码!',
                                type: 'success'
                            });
                        } else {
                            this.$error(res.data.result.code,res.data.result.message)
                        }
                    })
            },
            getVerificationCode () {
                let endMsRes = (new Date()).getTime() + 60000;
                localStorage.setItem('myEndTime', JSON.stringify(endMsRes));
		        this.codeCountDown(endMsRes)
            },
            codeCountDown (endMsRes) {
                this.show = false;
                this.countDownTime= Math.ceil((endMsRes - (new Date()).getTime()) / 1000)
                let time = setTimeout(() => {
                    this.countDownTime--
                    if (this.countDownTime < 1) {
                        this.show = true
                        this.countDownTime = 60
                        localStorage.removeItem('myEndTime')
                        clearTimeout(time)
                    } else {
                        this.codeCountDown(endMsRes)
                    }
                }, 1000)
            }
        }
    }
</script>

<style scoped>
    /deep/ .el-form-item__content {
        display: flex!important;
    }
</style>