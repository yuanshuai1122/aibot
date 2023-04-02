<template>
  <div class="app-content">
    <div class="app-header">
      <div style="color: #f8f8f8 ">您好，</div>
      <div style="color: #f8f8f8 ">欢迎注册云网AI</div>
    </div>
    <div class="login-content">
      <van-cell-group class="content" :ref="rules">
        <van-field type="number" label-width="7vh" class="field" v-model="logForm.account" label="手机号" placeholder="请输入11位手机号"
                   oninput="if(value.length>11)value=value.slice(0,11)" />
        <van-field label-width="7vh" class="field" v-model="logForm.verifyCode" label="验证码" placeholder="请输入验证码">
          <template #button>
            <span
              v-show="show"
              class="btn"
              @click="sendCode"
            >
              获取验证码
            </span>
            <span v-show="!show" class="count">{{count}}s后重新获取</span>
          </template>
        </van-field>
        <van-field label-width="7vh" class="field" v-model="passwordOne" label="密码" placeholder="请输入登录密码"/>
        <van-field label-width="7vh" class="field" v-model="passwordTwo" label="密码" placeholder="请再次输入登录密码"
                   @blur="checkPass()"/>
        <span v-show="toast" class="toast-text">两次输入不一致，请重新输入</span>
        <van-field label-width="7vh" class="field" v-model="logForm.shareCode" label="邀请码" placeholder="请输入邀请码（选填）"/>
      </van-cell-group>
      <div style="display: flex;justify-content: center;align-items: center;margin-top: 2vh">
        <van-radio-group v-model="radio" @change="changeRadio">
          <van-radio name='1' icon-size="2vh" checked-color="#3398FF" @click="agreeBtn('1')"></van-radio>
        </van-radio-group>
        <!--        <van-checkbox v-model="radio" checked-color="#3398FF" :label-disabled="true" shape="square" icon-size="15px"/>-->
        <span style="font-size: 1vh;color: #CACACA">我已阅读并同意《用户协议》《xxxx》和《隐私政策》</span>
      </div>

      <van-button class="submit-btn" round type="success" @click="registerBtn">注册</van-button>
      <div style="font-size: 1vh;margin-top: 2vh">
        <span style="color: #CACACA">已有账号？</span>
        <span style="color: #1989fa" @click="toLogin">直接登录</span>
      </div>
    </div>
  </div>

</template>

<script>
import { register } from '@/api/user.js'
import { Toast } from 'vant'

export default {
  name: 'register',
  data() {
    return {
      show: true,
      count: '',
      timer: null,
      logForm: {
        account: '',
        password: '',
        shareCode: '',
        verifyCode:''
      },
      //密码不一致提示
      toast:false,
      passwordOne:'',
      passwordTwo:'',
      radio: '',
      radioChange: false,
      rules: {
        account: [{ required: true, message: '账号必须填写！', trigger: ['blur', 'change'] }],
        password: [{ required: true, message: '密码必须填写！', trigger: ['blur', 'change'] }],
      }
    }
  },
  methods: {
    changeRadio() {
      this.radioChange = true
    },
    agreeBtn(e) {
      console.log(this.radio)
      if ( !this.radioChange) {
        this.radio = ''
      }
      this.radioChange = false
    },
    //校验两次密码是否相同
    checkPass(){
      if(this.passwordOne===this.passwordTwo){
        this.logForm.password=this.passwordOne
      }else if(this.passwordOne!==''&&this.passwordOne!==this.passwordTwo){
        this.toast=true
      }
    },
    //  提交表单
    registerBtn() {
      console.log(this.logForm)
      register(this.logForm).then(res => {
        console.log(res)
        if (res.code == 200) {
          Toast.success('注册成功')
          this.$router.push({
            path: '/login'
          })
        }

      })
    },
    toLogin() {
      this.$router.push({
        path: '/login'
      })
    },
    //发送验证码
    sendCode() {
      const TIME_COUNT = 60;
      if (!this.timer) {
        this.count = TIME_COUNT;
        this.show = false;
        this.timer = setInterval(() => {
          if (this.count > 0 && this.count <= TIME_COUNT) {
            this.count--;
          } else {
            this.show = true;
            clearInterval(this.timer);
            this.timer = null;
          }
        }, 1000)
      }

    },

  },
}
</script>

<style scoped>
.login-content{
  width: 100%;
  height: 80%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 2vh;
  background-size: cover;
  background: url(@/assets/bg.png) no-repeat fixed #333333;
}
.app-content{
  width: 100%;
  height: 100vh;
  background-size: cover;
  background: url(@/assets/bg.png) no-repeat fixed #333333;
}
.app-header{
  padding-top: 10vh;
  padding-left: 8vh;
}

.content {
  background: none;
}
.toast-text{
  font-size: 1.5vh;
  color: red;
  margin-top: 1vh;
  margin-left: 0;
}

.van-cell {
  display: flex;
}

.van-cell::after {
  border-bottom: 0;
}

.van-hairline--top-bottom::after {
  border-width: 0;
}

.van-cell-group {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.field {
  width: 80%;
  /*background: none;*/
  /*background-color: white ;*/
  margin-top: 3vh;
  border: 0.1vh solid black;
  border-radius: 3vh;
  vertical-align: center;
  display: flex;
  align-items: center;
}
.van-button--default{
  border: 0.2vh solid black;
  border-radius: 2vh;
}

.submit-btn {
  margin-top: 2vh;
  width: 70%;
  background-color: steelblue;
}

.btn {
  height: 3.5vh;
  width: 10vh;
  color: #1989fa;
}

.van-radio__icon--checked .van-icon {
  color: red;
  background-color: #1989fa;
  border-color: #1989fa;
}

</style>

