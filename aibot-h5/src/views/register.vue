<template>
    <div class="login-content">
      <van-cell-group class="content" :ref="rules">
        <van-field class="field" v-model="logForm.account" label="手机号" placeholder="请输入11位手机号" />
        <van-field class="field" v-model="logForm.password" label="密码" placeholder="请输入验证码">
          <template #button>
            <van-button size="small" type="text" @click="sendCode">发送验证码</van-button>
          </template>
        </van-field>
        <van-field class="field" v-model="logForm.shareCode" label="邀请码" placeholder="请输入登录密码" />
        <van-field class="field" v-model="logForm.account" label="账号" placeholder="请再次输入登录密码" />
        <van-field class="field" v-model="logForm.password" label="密码" placeholder="请输入邀请码（选填）" />
      </van-cell-group>
      <div style="display: flex;justify-content: center;align-items: center;margin-top: 2vh">
        <van-radio-group v-model="radio">
        <van-radio name="1" icon-size="2vh" checked-color="#3398FF"></van-radio>
        </van-radio-group>
<!--        <van-checkbox v-model="radio" checked-color="#3398FF" :label-disabled="true" shape="square" icon-size="15px"/>-->
        <span style="font-size: 1vh">我已阅读并同意《用户协议》《xxxx》和《隐私政策》</span>
      </div>

      <van-button class="submit-btn" round type="success" @click="registerBtn">注册</van-button>
      <div style="font-size: 1vh;margin-top: 2vh">
      <span>已有账号？</span>
      <span style="color: #1989fa" @click="toLogin">直接登录</span>
      </div>
    </div>

  </template>

  <script>
    import {register} from "@/api/user.js"
    import { Toast } from 'vant'
    export default {
      name: 'register',
      data(){
        return{
          radio:'1',
          logForm:{
            account: '',
            password: '',
            shareCode:''
          },
          rules:{
            account: [{ required: true, message: '账号必须填写！', trigger: ['blur', 'change'] }],
            password: [{ required: true, message: '密码必须填写！', trigger: ['blur', 'change'] }],
          }
        }
      },
      methods:{
        //  提交表单
        registerBtn(){
          console.log(this.logForm)
          register(this.logForm).then(res=>{
            console.log(res)
            if(res.code==200){
              Toast.success('注册成功');
              this.$router.push({
                path:'/login'
              })
            }

          })
        },
        toLogin(){
          this.$router.push({
            path:'/login'
          })
        },
        //发送验证码
        sendCode(){

        },


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
      background: url(@/assets/login.png) no-repeat fixed;
    }
    .content{
      background: none;
    }
    .van-cell{
      background: none;
      display: flex;
      /*flex-direction: column;*/
      /*justify-content: center;*/
      /*align-items: center;*/
    }
    .van-cell::after{
      border-bottom: 0;
    }
    .van-hairline--top-bottom::after{
      border-width: 0;
    }
    .van-cell-group{
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
    }
    .field{

      width: 80%;
      background: none;
      margin-top: 3vh;
      border: 0.1vh solid black;
      border-radius: 3vh;
    }

    .submit-btn{
      margin-top: 2vh;
      width: 20vh;
      background-color: steelblue;
    }
    /*@field-label-width{*/
    /*  width: 6vh;*/
    /*}*/
    .van-radio__icon--checked .van-icon {
      color: red;
      background-color: #1989fa;
      border-color: #1989fa;
    }

  </style>

