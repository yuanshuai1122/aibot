<template>
  <div class="app-container">
    <div class="layout-content">
      <keep-alive v-if="$route.meta.keepAlive">
        <router-view></router-view>
      </keep-alive>
      <router-view v-else></router-view>
    </div>
    <div class="layout-footer">
      <TabBar :data="tabbars" @change="handleChange" />
    </div>
  </div>
</template>

<script>
import TabBar from '@/components/TabBar'
export default {
  name: 'AppLayout',
  data() {
    return {
      tabbars: [
        {
          title: '对话',
          to: {
            name: 'Chat'
          },
          icon: 'comment-o'
        },
        {
          title: '创作',
          to: {
            name: 'Classify'
          },
          icon: 'edit'
        },
        {
          title: '任务',
          to: {
            name: 'Task'
          },
          icon: 'flag-o'
        },
        {
          title: '关于我',
          to: {
            name: 'About'
          },
          icon: 'user-o'
        }
      ]
    }
  },
  components: {
    TabBar
  },
  methods: {
    handleChange(v) {
      console.log('tab value:', v)
    }
  }
}
</script>
