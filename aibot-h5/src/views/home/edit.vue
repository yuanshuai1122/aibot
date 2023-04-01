<template>
  <div class="about-container">
    <van-tabs v-model="active">
      <van-tab title="文字创作">
        <van-grid :gutter="20" :column-num="3" style="margin-top: 2vh">
          <van-grid-item v-for="value in dataList" :key="value" icon="photo-o" text="文字">
            <div class="image">
            <van-image fit="fill" :src="value.imgUrl" />
              <p class="data">{{ value.useCount }}人使用></p>
            </div>
            <span style="font-size: 3vh;color: #f8f8f8">{{ value.title }}</span>
            <span style="font-size: 1vh;color: #f8f8f8">{{ value.des }}</span>
          </van-grid-item>
        </van-grid>
      </van-tab>
      <van-tab title="图片创作">内容 2</van-tab>
    </van-tabs>
  </div>

</template>

<script>
import {getEditList} from "@/api/user.js"
export default {
  name: 'Edit',
  data() {
    return {
      active: 2,
      dataList:[]
    };
  },
  created() {
    this.getList()
  },
  methods:{
  //  获取创作列表
    getList(){
      getEditList().then(res=>{
        console.log(res)
        this.dataList=res.data
      })
    }
  }
}
</script>

<style>
.image{
  position: relative;
}
.data{
  position: absolute;
  bottom: 1vh;
  left: 1vh;
  font-size: 1vh;
  width: 12vh;
  height: 2.5vh;
  background-color: aquamarine;
  border-radius: 1vh;
  line-height: 2.5vh;
  padding-left: 1vh;
}
.about-container {
  /* 你的命名空间 */
//background: #fff;
  /*height: 100%;*/
  min-height: 100vh;
  box-sizing: border-box;
  background-color:rgb(10,9,26);

}
.van-tabs__nav{
  background: none;
  color: white;
}
.van-tab--active {
  color: white;
  font-weight: 500;
  /*background: radial-gradient(circle at center, #CACACA, transparent 25%);*/

}
.element.style {
  flex-basis: 30%;
  padding-right: 20px;
}
[class*=van-hairline]::after{
  border: 0;
}
.van-grid-item__content{
  background: none;
  padding: 1vh 0;
}
</style>
