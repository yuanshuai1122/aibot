<template>
<div class="realName-container">
  <div  v-if="isSuccess" class="realName-container">
    <div class="realName-header">
      <img src="../../assets/dui.png" style="width: 3vh;height: 3vh;vertical-align: middle;">
      <span style="font-size: 2vh;color: #f8f8f8">您已实名认证</span>
    </div>
    <div class="realName-body">
      <div class="body-container">
        <div style="display: flex;flex-direction: column;width: 90%;font-size: 2vh;">
          <span style="padding-left: 2vh;color: #f8f8f8;margin-top: 1vh;padding-top: 1vh">姓名:</span>
          <span style="padding-left: 2vh;color: #f8f8f8;">{{realNameInfo.trueName}}</span>
          <span style="padding-left: 2vh;color: #f8f8f8;margin-top: 1vh">身份证号:</span>
          <span style="padding-left: 2vh;color: #f8f8f8;">{{realNameInfo.cerNumber}}</span>
        </div>
      </div>
    </div>
  </div>

  <div  v-else class="isRealNo">
    <div class="realNo-body">
      <van-cell-group class="content">
        <van-field class="field" v-model="realForm.trueName" label="真实姓名" placeholder="请输入真实姓名" />
        <van-field class="field" v-model="realForm.cerNumber" label="身份证号" placeholder="请输入身份证号" />
      </van-cell-group>
    </div>
      <div class="realNo-footer">
      <van-button @click="subRealName">立即认证</van-button>
    </div>
  </div>

</div>
</template>

<script>

import { getRealName, realNameApi } from '@/api/user'

export default {
  name: 'realName',
  data(){
    return{
      isSuccess:false,
      realForm:{
        trueName:'',
        cerNumber:''
      },
      realNameInfo:{
        trueName: '',
        cerNumber: ''
      }
    }
  },
  created() {
    console.log(this.$route.query.isSuccess)
    this.isSuccess=this.$route.query.isSuccess
    console.log(this.isSuccess)
    this.getRealNameInfo()
  },
  methods:{
    subRealName(){
      realNameApi(this.realForm).then(res=>{
        console.log(res)
        if(res.code==200){
          this.$router.push({
            path:'/about'
          })
        }
      })
    },
    getRealNameInfo(){
      getRealName().then(res=>{
        console.log(res)
        this.realNameInfo=res.data
        console.log(this.realNameInfo)
      })
    }
  }
}
</script>

<style>
.realName-container {
  /* 你的命名空间 */
  height: 100vh;
  box-sizing: border-box;
  background-color:rgb(10,9,26);
  display: flex;
  flex-direction: column;
  /*justify-content: center;*/
  align-items: center;
  position: relative;
  width: 100%;
}
.realName-header{
  margin-top: 5vh;
}
.realName-body{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 20vh;
  /*background: url(@/assets/realName.png) no-repeat;*/
  /*margin: 0 auto;*/
}
.body-container{
  display: flex;
  /*flex-direction: column;*/
  justify-content: center;
  align-items: center;
  width: 100%;
  min-height: 20vh;
  background: url(@/assets/realName.png) no-repeat center;
}
.van-cell-group{
  background-color: #1F2058 ;
  border-radius: 2vh;
  min-height: 15vh;

}
.van-cell{
  background: none;
  min-height: 4vh;
  line-height: 4vh;
}
[class*=van-hairline]::after{
  border: 0;
}
.realNo-body{
  margin-top: 20vh;
  border-radius: 2vh;
}
.realNo-footer{
  width: 80%;
  display: flex;
  justify-content: center;
  /*margin-top: 30vh;*/
  /*margin-bottom: 0;*/
  position: absolute;
  bottom: 3vh;
}
.van-button--normal {
  padding: 0 4vw;
  font-size: 3.733333vw;
  width: 80%;
  border-radius: 2vh;
}
.van-field__control{
  color: #f8f8f8;
}
.van-field__label{
  color: #f8f8f8;
}
.van-button--default {
  color: #323233;
  background: linear-gradient(to right,#CC16FF, #45A3FB);
  border: 1px solid #ebedf0;
}

</style>
