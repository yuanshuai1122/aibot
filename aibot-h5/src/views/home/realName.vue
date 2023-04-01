<template>
<div class="realName-container">
  <div  v-if="isSuccess" class="isReal">
    <div class="realName-header">
      <img src="../../assets/dui.png" style="width: 3vh;height: 3vh;vertical-align: middle;">
      <span style="font-size: 2vh;color: #f8f8f8">您已实名认证</span>
    </div>
    <div class="realName-body">
      <div class="body-container">
        <div style="display: flex;flex-direction: column;width: 90%;font-size: 2vh;">
          <p style="padding-left: 2vh;color: #f8f8f8;margin-top: 1vh;padding-top: 2vh">姓名</p>
          <p style="padding-left: 2vh;color: #f8f8f8;margin-top: 1vh"></p>
          <p style="padding-left: 2vh;color: #f8f8f8;margin-top: 1vh">身份证号</p>
          <p style="padding-left: 2vh;color: #f8f8f8;margin-top: 1vh"></p>
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
<!--      <p style="color: #f8f8f8;font-size: 2vh;margin: 2vh 2vh">根据法律法规要求，实名信息须与注册手机号持有人相符， 且年满20周岁（不含）以上，60周岁（含）以下。 信息安全保障中，以下信息仅用于身份确认，未经您同意不 会用于其他用途。</p>-->
    </div>
    <div class="realNo-footer">
      <van-button>立即认证</van-button>
    </div>
  </div>

</div>
</template>

<script>

import { realNameApi } from '@/api/user'

export default {
  name: 'realName',
  data(){
    return{
      isSuccess:false,
      realForm:{
        trueName:'',
        cerNumber:''
      }
    }
  },
  created() {
    this.isSuccess=this.$route.query.isSuccess
  },
  methods:{
    subRealName(){
      realNameApi(this.realForm).then(res=>{
        if(res.code==200){
          this.$router.push({
            path:'/about'
          })
        }
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
  min-height: 20vh;

}
.van-cell{
  background: none;
  min-height: 10vh;
  line-height: 8vh;
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
.van-button--default {
  color: #323233;
  background: linear-gradient(to right,#CC16FF, #45A3FB);
  border: 1px solid #ebedf0;
}

</style>
