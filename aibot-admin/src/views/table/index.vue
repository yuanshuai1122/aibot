<template>
  <div class="app-container">
    <el-form ref="queryParams" :model="queryParams" label-width="80px" :inline="true">
      <el-form-item label="账号">
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
    </el-form>
    <!--    用户列表-->
    <el-table
      :data="tableData"
      style="width: 100%">
      <el-table-column
        prop="date"
        label="日期"
        width="180">
      </el-table-column>
      <el-table-column
        prop="name"
        label="姓名"
        width="180">
      </el-table-column>
      <el-table-column
        prop="address"
        label="地址">
      </el-table-column>
    </el-table>
  </div>

</template>

<script>
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
      tableData:[]

    }
  },
}
</script>

<style scoped>

</style>
