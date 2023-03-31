<template>
  <div class="app-container">
    <el-form ref="form" :model="levelForm" label-width="100px">
      <el-form-item label="分销最大层级:">
        <span style="font-size: 20px">{{levelForm.maxLevel}}</span>
<!--        <el-input style="width: 80px" v-model="levelForm.maxLevel" :disabled="true"></el-input>-->
      </el-form-item>
      <span style="font-size: 14px;color: red">{{levelForm.desc}}</span>
    </el-form>
    <!--    用户列表-->
    <el-table
      :data="tableData"
      style="width: 100%"
      border>
      <el-table-column
        label="序号"
        type="index"
        width="120"
      align="center">
      </el-table-column>
      <el-table-column
        prop="tenantName"
        label="商户名称"
        align="center">
      </el-table-column>
      <el-table-column
        prop="level"
        label="分销等级"
        align="center">
      </el-table-column>
      <el-table-column
        prop="name"
        label="级别名称"
        align="center">
      </el-table-column>
      <el-table-column
        prop="rate"
        label="分佣比例"
        align="center">
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        align="center">
      </el-table-column>
      <el-table-column
        prop="updateTime"
        label="更新时间"
        align="center">
      </el-table-column>
      <el-table-column
        label="操作">
        <template slot-scope="scope">
        <el-button type="text" @click="editDistribution(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
<!--    分销修改弹窗-->
    <el-dialog
      title="提示"
      :visible.sync="editDialog"
      width="30%"
      :before-close="handleClose">
      <el-form ref="form" :model="infoForm" label-width="80px">
        <el-form-item label="商户名称">
          <el-input v-model="infoForm.tenantName" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="分销等级">
          <el-input v-model="infoForm.level" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="级别名称">
          <el-input v-model="infoForm.name"></el-input>
        </el-form-item>
        <el-form-item label="分佣比例">
          <el-input type="number" v-model="infoForm.rate" placeholder="请输入1~100" oninput="if(this.value>100)value=100"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="editDialog = false">取 消</el-button>
    <el-button type="primary" @click="updateDis">确 定</el-button>
  </span>
    </el-dialog>
  </div>

</template>

<script>
import { getDistriList, getMaxLevel, getInfoApi, updateApi } from '@/api/distribution.js'
export default {
  name: 'index',
  data(){
    return {
      levelForm:{
        maxLevel:'',
        desc:''
      },
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
      total:0,
      //编辑弹窗
      editDialog:false,
    //详情表单
      infoForm:{},
    //  操作主键id
    }
  },
  created() {
    this.getList()
  },
  methods:{
    //  获取用户列表
    getList(){
      getDistriList().then(res=>{
        console.log(res)
        this.tableData=res.data
      })
      getMaxLevel().then(res=>{
        console.log(res)
        this.levelForm.maxLevel=res.data.maxLevel
        this.levelForm.desc=res.data.desc
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
    //修改
    editDistribution(row){
      this.id=row.id
      this.editDialog=true
    //  获取详情
      getInfoApi(row.id).then(res=>{
        console.log(res)
        this.infoForm=res.data
      })
    },
    handleClose(){
      this.editDialog=false
    },
  //  确定修改
    updateDis(){
      const params={
        id:this.id,
        name:this.infoForm.name,
        rate:this.infoForm.rate
      }
      updateApi(params).then(res=>{
        console.log(res)
        if(res.code==200){
          this.$message({
            message: '修改成功',
            type: 'success'
          });
        }
        this.editDialog=false
      })
    },
  },
}
</script>

<style scoped>

</style>
