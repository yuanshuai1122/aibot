<template>
  <div class="login-content">
    <van-cell-group class="content">
      <van-field class="field" v-model="logForm.account" label="账号" placeholder="请输入账号" />
      <van-field class="field" v-model="logForm.password" label="密码" placeholder="请输入密码" />
    </van-cell-group>
    <van-button class="submit-btn" round type="success" @click="loginBtn">登录</van-button>
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
    }
  },
  methods:{
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
  /*background-color: red;*/
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
