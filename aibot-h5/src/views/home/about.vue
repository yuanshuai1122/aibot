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
      <span style="vertical-align:center">{{userForm.account}}</span>
    </div>
    <div class="container-center">
      <van-grid clickable :column-num="2">
        <van-grid-item icon="cart-o" text="我的藏品" to="/" />
        <van-grid-item icon="balance-pay" text="我的钱包" url="/vant/mobile.html" />
      </van-grid>
    </div>
    <div class="container-footer">
      <van-cell title="我的订单" is-link to="index"/>
      <van-cell title="我的客服" is-link to="index"/>
      <van-cell title="单元格" is-link to="index"/>
      <van-cell title="单元格" is-link to="index"/>
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
      }

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
  background-color:whitesmoke;
}
.container-header{
  display: flex;
  //justify-content: center;
  align-items: center;
  vertical-align: center;
  padding: 3vh 0 0 3vh;

}
.container-center{
  padding: 2vh;
  width: 42vh;
  height: 10vh;
  background-color: #202058;
}
.container-footer{
  padding: 2vh;
}
</style>
