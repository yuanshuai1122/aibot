<template>
<div class="app-container">
  <el-form ref="queryParams" :model="queryParams" :inline="true" label-width="100px">
    <el-form-item label="商品名称">
      <el-input v-model="queryParams.productName"></el-input>
    </el-form-item>
    <el-form-item label="用户昵称">
      <el-input v-model="queryParams.nickName"></el-input>
    </el-form-item>
    <el-form-item label="订单状态">
      <el-select v-model="queryParams.orderStatus" placeholder="请选择">
        <el-option
          v-for="item in statusList"
          :key="item.value"
          :label="item.name"
          :value="item.value">
        </el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
    <el-button type="primary" @click="selectData">查询</el-button>
    <el-button type="primary" @click="resetData">重置</el-button>
    </el-form-item>
  </el-form>
  <el-table
    :data="statusData"
    style="width: 100%">
    <el-table-column
      prop="nickName"
      label="昵称"
      width="180">
    </el-table-column>
    <el-table-column
      prop="productName"
      label="商品名称">
    </el-table-column>
    <el-table-column
      prop="quantity"
      label="数量">
    </el-table-column>
    <el-table-column
      prop="totalAmount"
      label="总价">
    </el-table-column>
    <el-table-column
      prop="orderStatus"
      label="状态">
    </el-table-column>
    <el-table-column
      prop="orderTime"
      label="下单时间">
    </el-table-column>
    <el-table-column
      prop="payTime"
      label="支付时间">
    </el-table-column>
  </el-table>
  <pagination v-show="totalCount > 0" :total="totalCount" :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize" @pagination="getList" />

</div>
</template>

<script>
import { orderListApi,orderStatusApi } from '@/api/order'

export default {
  name: 'index',
  data(){
    return{
      //查询参数
      queryParams:{
        pageNum:1,
        pageSize:10,
        productName:'',
        nickName:'',
        orderStatus:''
      },
      //订单状态列表
      statusList:[],
    //  订单列表
      statusData:[],
      //订单总数
      totalCount:0,
    }
  },
  created() {
    this.getList()
    this.getStatusList()
  },
  methods:{
  //获取订单列表
    getList(){
      orderListApi(this.queryParams.pageNum,this.queryParams.pageSize,this.queryParams.productName,this.queryParams.nickName,this.queryParams.orderStatus).then(res=>{
        console.log(res)
        this.statusData=res.data.orderList
        this.totalCount=res.data.totalCount
      })
    },
    //查询订单状态
    getStatusList(){
      orderStatusApi().then(res=>{
        console.log(res)
        this.statusList=res.data
      })
    },
    //查询
    selectData(){
      this.getList()
    },
    //重置
    resetData(){
      this.queryParams={
        pageNum:1,
        pageSize:10,
        productName:'',
        nickName:'',
        orderStatus:''
      }
      this.getList()
    },

  },
}
</script>

<style scoped>

</style>
