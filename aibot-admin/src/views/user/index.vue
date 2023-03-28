<template>
  <div class="app-container">
    <el-form ref="queryParams" :model="queryParams" :inline="true">
      <el-form-item label="手机号">
        <el-input v-model="queryParams.account"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="queryParams.nickName"></el-input>
      </el-form-item>
      <el-form-item label="真实姓名">
        <el-input v-model="queryParams.trueName"></el-input>
      </el-form-item>
      <el-form-item label="身份证号">
        <el-input v-model="queryParams.cerNumber"></el-input>
      </el-form-item>
      <el-button type="primary" @click="selectData">查询</el-button>
      <el-button type="primary" @click="resetData">重置</el-button>
    </el-form>
<!--    用户列表-->
    <el-table
      :data="tableData"
      style="width: 100%"
      border>
      <el-table-column
        label="序号"
        type="index"
        width="120">
      </el-table-column>
      <el-table-column
        prop="account"
        label="手机号"
        width="150">
      </el-table-column>
      <el-table-column
        prop="trueName"
        label="真实姓名"
        width="150">
      </el-table-column>
      <el-table-column
        prop="nickName"
        label="昵称"
        width="150">
      </el-table-column>
      <el-table-column
        prop="role"
        label="角色"
        width="150">
      </el-table-column>
      <el-table-column
        prop="shareCode"
        label="邀请码"
        width="150">
      </el-table-column>
      <el-table-column
        prop="userParentName"
        label="上级用户昵称"
        width="150">
      </el-table-column>
      <el-table-column
        label="操作">
      </el-table-column>
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize" @pagination="getList" />
<!--    <el-pagination-->
<!--      background-->
<!--      layout="prev, pager, next"-->
<!--      :total="total"-->
<!--      :page-size.sync="queryParams.pageSize"-->
<!--      @size-change="getList">-->
<!--    </el-pagination>-->
  </div>

</template>

<script>
import {getUserList} from '@/api/table.js'
export default {
  name: 'index',
  data(){
    return {
      //查询参数
      queryParams:{
        account:'',
        nickName:'',
        trueName:'',
        cerNumber:'',
        pageSize:10,
        pageNum:1
      },
      //列表数据
      tableData:[],
      //总条数
      total:0

    }
  },
  created() {
    this.getList()
  },
  methods:{
  //  获取用户列表
    getList(){
      getUserList(this.queryParams).then(res=>{
        console.log(res)
        this.tableData=res.data.userList
        this.total=res.data.totalCount
      })
    },
    selectData(){
      this.getList()
    },
    resetData(){
      this.queryParams={
        account:'',
        nickName:'',
        trueName:'',
        cerNumber:'',
        pageSize:10,
        pageNum:1
      }
      this.getList()
    },
  },
}
</script>

<style scoped>

</style>
