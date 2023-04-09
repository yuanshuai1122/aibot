<template>
  <div class="dashboard-container">
    <ul class="card-item">

      <li>
        <h2>今日新增用户</h2>
        <h3>{{dataInfo.todayUsers}}</h3>
<!--        <div class="card-item-data"></div>-->
        <div class="card-item-info">
          <span>总计用户数:</span>
          <span style="float: right">{{dataInfo.totalUsers}}人</span>
        </div>
      </li>
      <li>
        <h2>今日订单数</h2>
        <h3>{{dataInfo.todayOrders}}</h3>
        <div class="card-item-info">
          <span>总计订单数:</span>
          <span style="float: right">{{dataInfo.totalOrders}}笔</span>
        </div>
      </li>
      <li>
        <h2>今日收款金额</h2>
        <h3>{{dataInfo.todayAmount}}</h3>
        <div class="card-item-info">
          <span>总计收款金额:</span>
          <span style="float: right">{{dataInfo.totalAmount}}元</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { getDataList, getLoginInfo } from '@/api/dashboard'

export default {
  name: 'Dashboard',
  data(){
    return{
      dataInfo:{},
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  created() {
    this.getInfo()
  },
  methods:{
    // 获取首页信息
    getInfo(){
      getDataList().then(res=>{
        this.dataInfo=res.data
      })
    },

  },
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
.card-item {
  margin: 0;
  padding: 0;
  list-style: none;
  display: flex;
}

.card-item li {
  margin-right: 25px;
  background: #f3f6f9;
  width: calc((100% - 75px) / 3);
  padding: 24px;
  border-radius: 5px;
}

.card-item li:last-child {
  margin-right: 0;
}

.card-item li h2 {
  margin: 0;
  color: rgba(0, 0, 0, 0.45);
  font-size: 15px;
  display: flex;
  justify-content: space-between;
  letter-spacing: 1px;
}

.card-item li h2 i {}

.card-item li h3 {
  margin: 10px 0 0;
  height: 50px;
  color: rgba(0, 0, 0, 0.85);
  font-size: 30px;
  line-height: 31px;
  white-space: nowrap;
  text-overflow: ellipsis;
  word-break: break-all;
}

.card-item li h3 i {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: #108ee9;
  position: relative;
  float: left;
  margin-right: 12px;
  font-size: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  align-content: center;
  color: #fff;
}

.card-item-data {
  color: rgba(0, 0, 0, 0.45);
  font-size: 15px;
  height: 52px;
  line-height: 52px;
  margin-top: 12px;
}

.card-item-icon {
  height: 52px;
  margin-bottom: 20px;
}

.card-item-icon img {
  width: 100%;
}

.card-item-info {
  margin-top: 8px;
  padding-top: 20px;
  border-top: 1px solid #e8e8e8
}
</style>
