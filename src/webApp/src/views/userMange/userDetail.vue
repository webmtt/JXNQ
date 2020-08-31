<template>
  <div class="app-container">
    <!-- tab页 -->
    <el-tabs style="height: 100%" type="border-card">
        <el-button size="small" @click="editUserInfo" style="background: transparent;margin: 0 0 10px 0;float: right">编辑</el-button>
        <el-tab-pane label="基本信息">
          <el-table v-loading="listLoading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(56, 61, 65, 0.8)" :data="tableData" stripe border :show-header="false" style="width: 100%">
          <el-table-column
            prop="key"
            label="日期"
            width="200">
          </el-table-column>
          <el-table-column
            prop="value"
            label="姓名">
          </el-table-column>
        </el-table>
        </el-tab-pane>
    </el-tabs>
    <!-- 用户编辑框 -->
    <el-dialog
      title="基本信息编辑"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-form v-if="userModel" :model="userModel" :rules="rules" ref="ruleForm" label-width="100px">
        <el-form-item label="姓名：" prop="user_name">
          <el-input size="small" v-model="userModel.user_name"></el-input>
        </el-form-item>
        <el-form-item label="性别：" prop="gender">
          <el-radio-group v-model="userModel.gender">
            <el-radio size="small" label="男">男</el-radio>
            <el-radio size="small" label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="手机号：" prop="phone_number">
          <el-input size="small" v-model="userModel.phone_number"></el-input>
        </el-form-item>
        <el-form-item label="单位名称：" prop="organization">
          <el-input size="small" v-model="userModel.organization"></el-input>
        </el-form-item>
        <el-form-item label="单位地址：" prop="address">
          <el-input size="small" v-model="userModel.address"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {userDetail,editUser} from '@/api/user'
import * as auth from '@/utils/auth';

export default {
  data() {
    return {
      listLoading: false,
      dialogVisible: false,
      userModel: null,
      originUserModel: {
          user_id: null,
          user_name: null,
          gender: '男',
          phone_number: null,
          address: null,
          organization: null,
        },
        rules: {
          user_name: [
            { required: true, message: '姓名为必填项', trigger: 'blur' },
            { min: 3, max: 12, message: '长度在 3 到 12 个字符', trigger: 'blur' }
          ],
          gender: [
            { required: true, message: '性别为必填项', trigger: 'change' }
          ],
          phone_number: [
            { required: true, message: '手机号为必填项', trigger: 'change' }
          ],
          organization: [
            { required: true, message: '公司名称为必填项', trigger: 'change' }
          ],
          address: [
            { required: true, message: '公司地址为必填项', trigger: 'change' }
          ]
      },
      tableData: [],
        dataMap: {
          user_name: '姓名：',
          gender: '性别：',
          phone_number: '手机号：',
          address: '地址：',
          organization: '单位地址：',
        }
    }
  },
  methods: {
    getUserInfo(user_id) {
      this.listLoading = true;
      userDetail({user_id})
        .then(res => {
          this.userModel = this.setUserModel(res.data[0]);
          this.tableData = Object.keys(this.userModel).filter(item => item != 'user_id').map(item => ({key: this.dataMap[item], value: this.userModel[item]}));
        })
        .catch(err => {})
        .finally(res => this.listLoading = false);
    },
    editUserInfo () {
      this.dialogVisible = true;
    },
    setUserModel (userItem) {
      const obj = {};
      Object.keys(this.originUserModel).forEach(key => obj[key] = userItem[key]);
      return obj;
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          editUser(this.userModel)
            .then(res => {
              this.$message({ type: 'success', message: '编辑用户成功!' });
              this.$store.dispatch('user/getInfo')
                .then(res => {
                  this.userModel = this.setUserModel(res);
                  this.tableData = Object.keys(this.userModel).filter(item => item != 'user_id').map(item => ({key: this.dataMap[item], value: this.userModel[item]}));
                })
            })
            .catch(err => this.$message({ type: 'error', message: '编辑用户失败!'}))
            .finally(res => this.dialogVisible = false);
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleClose (done) {
      this.resetForm('ruleForm');
      done();
    }
  },
  mounted() {
    this.getUserInfo(auth.getToken());
  }
}
</script>

<style lang="scss" scoped></style>

