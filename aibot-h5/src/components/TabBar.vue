<template>
  <div>
    <van-tabbar class="tab-bar" fixed route v-model="active" @change="handleChange">
      <van-tabbar-item class="tab-item" v-for="(item, index) in data" :to="item.to" :icon="item.icon" :key="index">
        {{ item.title }}
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>
<script>
export default {
  name: 'TabBar',
  props: {
    defaultActive: {
      type: Number,
      default: 0
    },
    data: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      active: this.defaultActive
    }
  },
  methods: {
    handleChange(value) {
      this.$emit('change', value)
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.tab-bar {
  position: fixed;
  bottom: 0;
  display: flex;
  width: 100%;
  height: 7vh;
}

.tab-item {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 14px;
}
</style>
