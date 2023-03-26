<template>
  <div>1234</div>

</template>

<script>
import {getUserInfo} from "@/api/user.js"
export default {
  name: 'chat',
  created(){
    this.getUser()
  },
  methods:{
  //  获取用户详细信息
    getUser(){
      getUserInfo().then(res=>{
        console.log(res)
      })

    },

  },
}
</script>

<style scoped>

</style>
