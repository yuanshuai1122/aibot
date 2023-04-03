<template>
  <div class="container">
    <!-- 组件引入的方式，应提供双方的头像，加载历史消息的方式（函数）和发送消息的方式（函数） -->
    <ChatBox ref="chat"
      :sourceAvatar="sourceAvatar" :targetAvatar="targetAvatar"
      :loadHistory="loadHistory" :sendMessage="sendMessage" />
  </div>
</template>

<script>
import ChatBox from '../../components/chat'
import { chatSign } from '@/api/message'
import { EventSourcePolyfill } from 'event-source-polyfill'
import { baseApi } from '@/config/env.development'
import api from '@/api'
import { getToken } from '@/utils/auth'
//import '../../components/chat.css'

export default {
  name: 'ChatDemo',
  components: {
    ChatBox
  },
  data () {
    return {
      sourceAvatar: 'https://qianp.com/uploads/images/xiaz/2023/0104/1672816810529.jpg',
      targetAvatar: 'https://i.pinimg.com/236x/d0/bc/43/d0bc4356568d3d01181d029b8b5caf28.jpg',
      // 最后一个索引
      messageListIndex: null,
      // 收集推流消息
      chatMessage: "",
    }
  },
  methods: {
    // 定义加载历史消息的方式，该函数应该返回一个对象（`{ messages, hasMore }`），或者是返回该对象的 Promise （异步）。
    loadHistory () {
      return {
        // 消息数据，字段如下，应以时间的倒序给出。
        messages: [
          { text: "Really cute!", time: new Date(2020, 8, 4), direction: 'sent' },
          { text: "Hey, I'm a bear!", time: new Date(2020, 7, 4), direction: 'received' },
          { text: 'Hello, who are you?', time: new Date(2020, 7, 4), direction: 'sent' },
        ],
        // 定义是否还有历史消息，如果为 false，将停止加载。读者可将其改为 true 演示一下自动滚动更新的效果。
        hasMore: false
      }
    },

    // 定义发送消息的方式。如果发送成功，应该返回成功发送的消息数据，或者 Promise.
    sendMessage ({ text }) {
      // 请求推流接口
      const chatReq = {
        prompt: text,
        conversionId: ""
      }
      this.getMessage(chatReq)

      return {
        text,
        time: new Date(),
        direction: 'sent'
      }

    },

    // 该函数演示如何加载新消息（一般通过 WebSocket 实时收取）
    receiveMessage (message) {
      this.$refs.chat.appendNew(message)
    },

    // 发送消息
    getMessage(data) {
      // 请求流式签名
      chatSign(data).then(res => {
        // 获取最后一个下标
        this.messageListIndex = this.$refs.chat.messages.length
        // 提前创建好回复
        this.$refs.chat.appendNew(
          {
            text: "",
            time: new Date(),
            direction: 'received'
          }
        )
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
          console.log("2222")
          console.log(message.content);
          console.log("3333")
          // 收集推流值
          this.chatMessage = this.chatMessage + message.content
          console.log(this.chatMessage);
          // 流式更新
          this.$refs.chat.messages[this.messageListIndex].text = this.chatMessage
        })
      })
    },
  }
}
</script>

<style lang="scss" scoped>
.container {
  height: 93vh;
}
</style>
