<template>
    <div class="login-content">
      <van-cell-group class="content" :ref="rules">
        <van-field class="field" v-model="logForm.account" label="账号" placeholder="请输入账号" />
        <van-field class="field" v-model="logForm.password" label="密码" placeholder="请输入密码" />
        <van-field class="field" v-model="logForm.shareCode" label="邀请码" placeholder="请输入邀请码" />
      </van-cell-group>
      <van-button class="submit-btn" round type="success" @click="registerBtn">注册</van-button>
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
    .field{
      background: none;
    }
    .submit-btn{
      margin-top: 2vh;
      width: 20vh;
      background-color: steelblue;
    }

  </style>

