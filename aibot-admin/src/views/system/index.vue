<template>
  <div class="app-container">
    <el-table
      :data="apiKeyData"
      style="width: 100%">
      <el-table-column
        type="index"
        label="序号"
        align="center">
      </el-table-column>
      <el-table-column
        prop="apiKey"
        label="key"
        align="center">
      </el-table-column>
      <el-table-column
        prop="account"
        label="所属账号"
        align="center">
      </el-table-column>
      <el-table-column
        prop="status"
        label="状态"
        align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-color="#13ce66"
            inactive-color="#ff4949"
          @change="statusChange(scope.row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center">
        <template slot-scope="scope">
          <el-button type="text" @click="keyDetail(scope.row)">查询额度</el-button>
        </template>
      </el-table-column>


    </el-table>


  <el-dialog
    title="额度查询"
    :visible.sync="editDialog"
    width="30%"
    :before-close="handleClose">
    <el-form ref="form" :model="infoForm" label-width="80px">
      <el-form-item label="总额度">
        <el-input v-model="infoForm.totalAmount" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="已用额度">
        <el-input v-model="infoForm.totalUsage" :disabled="true"></el-input>
      </el-form-item>
      <el-form-item label="剩余额度">
        <el-input v-model="infoForm.remainAmount" :disabled="true"></el-input>
      </el-form-item>
    </el-form>
  </el-dialog>


  </div>

</template>

<script>

import { getKeyInfo, getKeyList, updateKeyStatus } from '@/api/system'
import el from 'element-ui/src/locale/lang/el'

export default {
  name: 'index',
  data(){
    return{
      apiKeyData: [],
      isStatus: false,
      editDialog: false,
      infoForm: {},

    }
  },
  created() {
    this.getList()
  },
  methods:{
    // 获取key列表
    getList(){
      getKeyList().then(res => {
        this.apiKeyData = res.data
        console.log(this.apiKeyData)
      })
    },
    statusChange(row) {
      console.log(row)
      const updateDate = {
        id: row.id,
        status: row.status
      }
      updateKeyStatus(updateDate).then(res => {
        console.log(res)
        this.$message.success('更新key状态成功')
      })

    },
    keyDetail(row) {
      // 调用查询key信息接口
      getKeyInfo({
        apiKey: row.apiKey
      }).then(res => {
        console.log(res)
        if (res.code === 200) {
          this.infoForm = res.data
          this.editDialog = true
        }else {
          this.$message.error("系统繁忙，请稍后重试")
        }

      })


    },
    handleClose(){
      this.editDialog=false
    },

  },
}
</script>

<style scoped>

</style>
