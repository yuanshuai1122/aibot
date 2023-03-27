<template>
  <div class="app-container">
    <div class="chat-center">
      <div v-for="item in messageList">
        <van-cell :value="item.message">
          <template #title>
              <van-image
                v-if="item.role=='user'"
                style="margin: 1vh"
                width="5vh"
                height="5vh"
                fit="contain"
                :src="avatar"

              />
            <van-image
              v-else
              style="margin: 1vh"
              width="5vh"
              height="5vh"
              fit="contain"
              src="https://img01.yzcdn.cn/vant/cat.jpeg"
            />
          </template>
        </van-cell>
      </div>
    </div>

    <div class="chat-footer">
      <van-cell-group style="background: none">
        <van-field style="background: none" v-model="content" label="" placeholder="发送消息给AI" >
          <template #button>
            <van-button size="small" type="default" @click="sendBtn">发送</van-button>
          </template>
        </van-field>
      </van-cell-group>

    </div>
  </div>

</template>

<script>
import { getUserInfo } from "@/api/user.js";
import { chatSign } from "@/api/message";
import api from "@/api";
import { baseApi } from "@/config/env.development";
import { getToken } from "@/utils/auth";
import { EventSourcePolyfill } from "event-source-polyfill";

export default {
  name: 'chat',
  data(){
    return{
      //用户头像
      avatar:'',
      //用户输入内容
      content:'',
      //  用户发送
      messageList:[
      ],
      userSend:'',
      //  用户接受
      userHear:'',
      chatMessage: '',
      messageListIndex: null,
    }
  },
  created(){
    this.getUser()
  },
  methods:{
    //  获取用户详细信息
    getUser(){
      getUserInfo().then(res=>{
        console.log(res)
        this.avatar=res.data.avatar
      })
    },
    getMessage(data) {
      // 请求流式签名
      chatSign(data).then(res => {
        // 获取最后一个下标
        this.messageListIndex = this.messageList.length
        // 提前创建好回复
        this.messageList.push({
          role: 'chat',
          message: ''
        })
        // 开始推流
        const eventSource = new EventSourcePolyfill(baseApi + api.ChatStream + "?signKey=" + res.data, {
          headers: {
            authorization: getToken()
          }
        });
        // 订阅成功
        eventSource.addEventListener("open", function(e) {
          console.log('open successfully')
        })
        // 后端推流
        eventSource.addEventListener("message", (event) => {
          const message = JSON.parse(event.data);
          // 推流结束
          if (message.content == null) {
            console.log("推流完毕，断开连接");
            this.chatMessage = ''
            eventSource.close()
            return
          }
          console.log(message.content);
          // 收集推流值
          this.chatMessage = this.chatMessage + message.content
          console.log(this.chatMessage);
          // 流式更新
          this.messageList[this.messageListIndex].message = this.chatMessage
        })
      })
    },
    //用户发送
    sendBtn(){

      const userMessage = this.content

      const obj={
        role: 'user',
        message: userMessage
      }
      this.messageList.push(obj)
      // this.userSend=this.content
      this.content=''

      // 请求推流接口
      const chatReq = {
        prompt: userMessage,
        conversionId: ""
      }
      this.getMessage(chatReq)

    },

  },
}
</script>

<style scoped>
.chat-footer{
  position: fixed;
  bottom: 7vh;
  width: 95%;
  padding: 1vh 1vh 0 1vh;
  background: none;
}
.van-cell{
  width: 95%;
  display: flex;
  /*justify-content: center;*/
  align-items: center;
  /*height: 5vh;*/
  border-radius: 1vh;
  border: 0.1vh solid #cccccc;
  padding: 1vh;
  margin: 1vh;
}
.van-button{
  border-radius: 1vh;
  width: 8vh;
}
.chat-center{
  height: 87.2vh;
  overflow-y: auto
}
.van-cell__title, .van-cell__value {
  -webkit-box-flex: 1;
   -webkit-flex: 0;
   flex: inherit;

}
.van-cell__value>span{
  display: inline-block;
  text-align: left;
  word-break: break-all;
}
</style>
