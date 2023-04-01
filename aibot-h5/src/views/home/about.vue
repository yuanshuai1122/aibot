<template>
  <div class="about-container">
    <div class="container-header">
    <van-image
      style="vertical-align: center"
      round
      width="7vh"
      height="7vh"
      :src="userForm.avatar"
    />
      <span style="vertical-align:center;color: white">{{userForm.account}}</span>
    </div>
    <div class="center-about">
    <div class="container-center">
      <van-grid clickable :column-num="3">
        <van-grid-item icon="cart-o" text="我的创作" to="/" style="color: white"/>
        <van-grid-item text="|" to="/" style="color: white"/>
        <van-grid-item icon="balance-pay" text="我的钱包" url="/vant/mobile.html" style="color: white" />
      </van-grid>
    </div>
    </div>
    <div class="container-footer">
      <van-cell title="我的订单" is-link to="index"/>
      <van-cell title="我的认证" is-link to="realName" value="已认证" @click="toReal"/>
      <van-cell title="账号与安全" is-link to="index"/>
      <van-cell title="隐私设置" is-link to="index"/>
      <van-cell title="关于云网AI" is-link to="index"/>
      <van-cell title="我的客服" is-link to="index"/>
    </div>

  </div>
</template>

<script>
// 请求接口
import { getUserInfo } from '@/api/user.js'
export default {
  data() {
    return {
      userForm:{
        avatar:'',
        account:'',
        nickname:''
      },
      isSuccess: false,

    }
  },
  created() {
    this.getUser()
  },
  computed: {
  },
  mounted() {

  },
  methods: {
    getUser(){
      getUserInfo().then(res=>{
        this.userForm=res.data
        if(this.userForm.isRealName==1){
          this.isSuccess=true
        }
      })
    },
    toReal(){
      this.$router.push({
        path:'/realName',
        query:{
          isSuccess:this.isSuccess
        }
      })
    }

  }
}
</script>

<style lang="scss">
.about-container {
  /* 你的命名空间 */
  //background: #fff;
  height: 100vh;
  box-sizing: border-box;
  background-color:rgb(10,9,26);

}
.container-header{
  display: flex;
  //justify-content: center;
  align-items: center;
  vertical-align: center;
  padding: 3vh 0 0 3vh;

}
.container-center{
  //padding: 2vh 0;
  width: 90%;
  background-color: #202058;
  display: flex;
  justify-content: center;
  margin: 2vh auto;
  border-radius: 2vh;
}
.van-grid{
  width: 80%;
  height: 80%;
  background: none;
}
[class*=van-hairline]::after{
  border:0 solid #202058
}
.van-grid-item__content{
  background:none;
}
.van-grid-item__icon+.van-grid-item__text {
  margin-top: 2.133333vw;
  color: aliceblue;
}
.container-footer{
  //padding: 2vh 0;
  width: 90%;
  background-color: #202058;
  margin: 2vh auto;
  border-radius: 2vh;
}
.van-cell {
  position: relative;
  display: -webkit-box;
  display: -webkit-flex;
  display: flex;
  box-sizing: border-box;
  width: 100%;
  padding: 2.666667vw 4.266667vw;
  overflow: hidden;
  color: white;
  font-size: 3.733333vw;
  line-height: 6.4vw;
  background: none;
}
.van-cell::after{
  border: 0;
}
</style>
