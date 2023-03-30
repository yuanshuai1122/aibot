<template>
    <div class="login-content">
      <van-cell-group class="content" :ref="rules">
        <van-field label-width="8vh" class="field" v-model="logForm.account" label="手机号" placeholder="请输入11位手机号" />
        <van-field label-width="8vh" class="field" v-model="logForm.password" label="密码" placeholder="请输入验证码">
          <template #button>
            <van-button
              size="small"
              native-type="button"
              class="btn"
              @click="sendCode"
            >
              获取验证码
            </van-button>
<!--            <van-button size="small" type="text" @click="sendCode">发送验证码</van-button>-->
          </template>
        </van-field>
        <van-field label-width="8vh" class="field" v-model="logForm.shareCode" label="邀请码" placeholder="请输入登录密码" />
        <van-field label-width="8vh" class="field" v-model="logForm.account" label="账号" placeholder="请再次输入登录密码" />
        <van-field label-width="8vh" class="field" v-model="logForm.password" label="密码" placeholder="请输入邀请码（选填）" />
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

  </template>

  <script>
    import {register} from "@/api/user.js"
    import { Toast } from 'vant'
    export default {
      name: 'register',
      data(){
        return{
          logForm:{
            account: '',
            password: '',
            shareCode:''
          },
          radio:"",
          radioChange:false,
          rules:{
            account: [{ required: true, message: '账号必须填写！', trigger: ['blur', 'change'] }],
            password: [{ required: true, message: '密码必须填写！', trigger: ['blur', 'change'] }],
          }
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
      /*background-color: #333333;*/
      background: url(@/assets/bg.png) no-repeat fixed #333333;
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
      /*background: none;*/
      background-color: #212230 ;
      margin-top: 3vh;
      border: 0.1vh solid black;
      border-radius: 3vh;
      vertical-align: center;
      display: flex;
      align-items: center;
    }

    .submit-btn{
      margin-top: 2vh;
      width: 70%;
      background-color: steelblue;
    }
    .btn{
      height: 3.5vh;
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

