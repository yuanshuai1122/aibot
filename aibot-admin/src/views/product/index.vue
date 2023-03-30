<template>
  <div class="app-container">
    <el-form ref="queryParams" :model="queryParams" :inline="true" label-width="100px">
      <el-form-item label="商品名称">
        <el-input v-model="queryParams.productName"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="selectData">查询</el-button>
        <el-button type="primary" @click="resetData">重置</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="productData"
      style="width: 100%">
      <el-table-column
        prop="productName"
        label="商品名称"
        width="180">
      </el-table-column>
      <el-table-column
        prop="productPrice"
        label="商品价格">
      </el-table-column>
      <el-table-column
        prop="imgUrl"
        label="商品背景"
        width="180">
        <template slot-scope="scope">
        <el-image
          style="width: 100px; height: 100px"
          :src="scope.row.imgUrl"
          fit="fill">
        </el-image>
        </template>
      </el-table-column>
      <el-table-column
        prop="count"
        label="数量"
        width="180">
      </el-table-column>
      <el-table-column
        prop="putStatus"
        label="上架状态">
        <template slot-scope="scope">
          <span v-if="scope.row.putStatus===1">上架</span>
          <span v-if="scope.row.putStatus===0">下架</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="productDescription"
        label="商品描述">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间">
      </el-table-column>
    </el-table>
    <pagination v-show="totalCount > 0" :total="totalCount" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

  </div>
</template>

<script>
import { productListApi } from '@/api/product'

export default {
  name: 'index',
  data(){
    return{
      //查询参数
      queryParams:{
        pageNum:1,
        pageSize:10,
        productName:'',
      },
      //  订单列表
      productData:[],
      //订单总数
      totalCount:0,
    }
  },
  created() {
    this.getList()
  },
  methods:{
    //获取订单列表
    getList(){
      productListApi(this.queryParams.pageNum,this.queryParams.pageSize,this.queryParams.productName).then(res=>{
        console.log(res)
        this.productData=res.data.productList
        this.totalCount=res.data.totalCount
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
      }
      this.getList()
    },

  },
}
</script>

<style scoped>

</style>
