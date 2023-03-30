<template>
  <div >
    <div style="color: #f8f8f8 ">您好，</div>
    <div style="color: #f8f8f8 ">欢迎来到云网AI</div>
    <div class="login-content">
    <van-cell-group class="content">
      <van-field class="field" v-model="logForm.account" label="账号" placeholder="请输入账号" />
      <van-field class="field" v-model="logForm.password" label="密码" placeholder="请输入密码" />
    </van-cell-group>
    <div style="display: flex;justify-content: center;align-items: center;margin-top: 2vh">
      <van-radio-group v-model="radio" @change="changeRadio">
        <van-radio name='1' icon-size="2vh" checked-color="#3398FF" @click="agreeBtn('1')"></van-radio>
      </van-radio-group>
      <!--        <van-checkbox v-model="radio" checked-color="#3398FF" :label-disabled="true" shape="square" icon-size="15px"/>-->
      <span style="font-size: 1vh;color: #CACACA">我已阅读并同意《用户协议》《xxxx》和《隐私政策》</span>
    </div>
    <van-button class="submit-btn" round type="success" @click="loginBtn">登录</van-button>
    <div style="display: flex;justify-content: space-around;width: 100%;margin-top: 2vh">
      <div style="font-size: 1vh;float: left;color: #CACACA">验证码登录</div>
      <div style="font-size: 1vh;float: right;color: #CACACA" @click="toRegister">新用户注册</div>
    </div>
    </div>
  </div>

</template>

<script>
import {login} from "@/api/user.js"
import { setToken } from '@/utils/auth'
import { Toast } from 'vant'
export default {
  name: 'Login',
  data(){
    return{
      logForm:{
      account: "",
      password: ""
      },
      radio:"",
      radioChange:false
    }
  },
  methods:{
    changeRadio(){
      this.radioChange=true
    },
    agreeBtn(e){
      console.log(this.radio)
      if(!this.radioChange){
        this.radio=''
      }
      this.radioChange=false
    },
  //  提交表单
    loginBtn(){
      console.log(this.logForm)
      login(this.logForm).then(res=>{
        console.log(res)
        if(res.code==200){
          setToken(res.data)
          Toast.success('登陆成功');
          this.$router.push({
            path:'/index'
          })
        }
      })
    },
  //  去注册
    toRegister(){
      this.$router.push({
        path:'/register'
      })
    }
  },
}
</script>

<style scoped>
.login-content{
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-size: cover;
  background: url(@/assets/bg.png) no-repeat fixed #333333;
}
.content{
  width: 80%;
  background: none;
  /*background-color: red;*/
}
.field{
  width: 100%;
  background-color: #212230 ;
  margin-top: 3vh;
  border: 0.1vh solid black;
  border-radius: 3vh;
  display: flex;
  justify-content: center;
  align-items: center;
}
.van-field__control{
  color: white;
}
.van-cell-group{
  background: none;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.van-cell::after{
  border-bottom: 0;
}
.van-hairline--top-bottom::after{
  border-width: 0;
}
.submit-btn{
  margin-top: 2vh;
  width: 80%;
  background-color: steelblue;
}

</style>
