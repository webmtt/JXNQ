<template>
  <div v-loading="listLoading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading" element-loading-background="rgba(56, 61, 65, 0.8)" style="height: 100%" class="app-container accountManage">
    <div style="text-align: right; padding: 0 0 10px 0;">
      <el-button size="small" @click="addOrEditUserBox()"  type="success">新建</el-button>
      <el-button size="small" @click="batchDelete" :disabled="!isBatchSelected" type="success">批量删除</el-button>
    </div>
    <el-table
      :data="list"
      :height="tableHeight"
      border
      size="small"
      fit
      ref="tableList"
      @selection-change="handleSelectionChange"
      highlight-current-row>
      <el-table-column align="center" type="selection" label="选择" width="55">
      </el-table-column>
      <el-table-column align="center" label="序号" width="100">
        <template slot-scope="scope">
          {{ getIndex(scope.$index) }}
        </template>
      </el-table-column>
      <el-table-column align="center" width="150" label="角色">
        <template slot-scope="scope">
          {{ ['超级管理员', '普通用户'][scope.row.user_type] }}
        </template>
      </el-table-column>
      <el-table-column label="账号名称" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.user_name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="单位" align="center">
        <template slot-scope="scope">
          {{ scope.row.organization || '-' }}
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="创建时间" align="center">
        <template slot-scope="scope">
          {{ transformDataTime(scope.row.regtime) }}
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150" align="center">
        <template slot-scope="scope">
          <el-button @click="addOrEditUserBox(scope.row)" type="text" size="small">编辑</el-button>
          <el-button @click="deleteUser(scope.row.user_id)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination v-if="pagation.total" style="float: right; margin-top: 10px" background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pagation.page"
      :page-sizes="[10, 15, 20]"
      :page-size="pagation.size"
      layout="total, sizes, prev, pager, next"
      :total="pagation.total"></el-pagination>
    <!-- 用户编辑框 -->
    <el-dialog
      :title="isAdd ? '新增用户' : '编辑用户'"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-form v-if="userModel" :model="userModel" :rules="rules" ref="ruleForm" label-width="100px">
        <el-form-item label="角色类型" prop="user_type">
          <el-select v-model="userModel.user_type" placeholder="请选择角色类型">
            <el-option label="超级管理员" :value="0"></el-option>
            <el-option label="普通用户" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="性别：" prop="gender">
          <el-radio-group v-model="userModel.gender">
            <el-radio size="small" label="男" >男</el-radio>
            <el-radio size="small" label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号名称：" prop="user_name">
          <el-input size="small" v-model="userModel.user_name"></el-input>
        </el-form-item>
        <el-form-item label="单位名称：" prop="organization">
          <el-input size="small" v-model="userModel.organization"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="addOrEditUser('ruleForm')">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { userList, addUser, deleteUser, editUser } from '@/api/user'
import * as auth from '@/utils/auth';
import moment from 'moment';
export default {
  data() {
    return {
      list: null,
      isAdd: true,
      listLoading: true,
      dialogVisible: false,
      isBatchSelected: false,
      pagation: {
        page: 1,
        size: 10,
        total: 0
      },
      userModel: null,
      originUserModel: {
        user_id: null,
        user_name: null,
        gender: '男',
        organization: null,
        user_type: 0, // 用户角色
      },
      rules: {
          user_type: [
            { required: true, message: '角色为必填项', trigger: 'change' }
          ],
          gender: [
            { required: true, message: '性别不能为空', trigger: 'change' }
          ],
          user_name: [
            { required: true, message: '账户为必填项', trigger: 'blur' }
          ],
          organization: [
            { required: true, message: '公司名称为必填项', trigger: 'blur' }
          ]
      }
    }
  },
  computed: {
    tableHeight () {
      return document.documentElement.offsetHeight - 220 || 300;
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    transformDataTime(time) {
      return moment(time).format('YYYY-MM-DD hh:mm:ss');
    },
    /**
     * 请求用户列表;
     * @method fetchData
     */
    fetchData() {
      this.listLoading = true
      return userList(this.pagation)
        .then(res => {
          this.list = res.data.list;
          this.pagation.total = res.data.total || this.list.length
        })
        .catch(err => {
          this.list = [];
          this.pagation.total = 0;
        })
        .finally(() => {
          this.listLoading = false;
          this.$refs.tableList.bodyWrapper.scrollTop = 0;
        });
    },

    setUserModel (userItem) {
      const obj = {};
      Object.keys(this.originUserModel).forEach(key => obj[key] = userItem[key]);
      return obj;
    },

    /**
     * 弹框;
     * @method editUser
     */
    addOrEditUserBox (userItem) {
      this.dialogVisible = true;
      this.isAdd = !userItem;
      this.userModel = userItem ? this.setUserModel(userItem) : this.setUserModel(this.originUserModel);
    },

    addOrEditUser (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          // 保存逻辑;
          const fn = this.isAdd ? addUser : editUser;
          fn(this.userModel)
            .then(res => {
              this.$message({ type: 'success', message: `${this.isAdd ? '新增': '编辑'}用户成功!` });
              this.fetchData();
              // 如果改的这里页面得更新
              if (!this.isAdd && this.userModel.user_id == auth.getToken()) {
                this.$store.dispatch('user/getInfo');
              }
            })
            .catch(err => this.$message({ type: 'error', message: `${this.isAdd ? '新增': '编辑'}用户失败!` }))
            .finally(res => this.dialogVisible = false);
        } else {
          return false;
        }
      });
      
    },

    /**
     * 重置表单
     * @method resetForm
     */
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    /**
     * 关闭模态框前做的处理;
     * @handleClose 
     */
    handleClose (done) {
      this.resetForm('ruleForm');
      // this.userModel = null;
      if (done instanceof Function) {
        done();
      } else {
        this.dialogVisible = false
      }
    },

    /**
     * 删除用户;
     * @method deleteUser
     */
    deleteUser (user_id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteUser({user_id})
          .then(res => this.fetchData())
          .catch(err => this.$message({ type: 'error', message: err }));
      })
      .catch(err => {});
    },

    /**
     * 每页显示条数变化;
     * @method handleSizeChange
     */
    handleSizeChange (size) {
      this.pagation.size = size;
      this.pagation.page = 1;
      this.fetchData();
    },

    /**
     * 页码发生变化;
     * @method handleCurrentChange
     */
    handleCurrentChange (page) {
      this.pagation.page = page;
      this.fetchData();
    },

    /**
     * 获取分页后的序号;
     * @method getIndex
     */
    getIndex (index) {
      return (this.pagation.page - 1) * this.pagation.size + index + 1;
    },

    handleSelectionChange (selections) {
      this.isBatchSelected = !!selections.length;
    },

    batchDelete () {
      this.$confirm(`此操作将永久删除选中的${this.$refs.tableList.selection.length}条数据, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const user_id = this.$refs.tableList.selection.map(item => item.user_id).join();
        deleteUser({user_id})
          .then(res => this.fetchData())
          .catch(err => this.$message({ type: 'error', message: err }));
      });
    }
  },
  mounted () {}
}
</script>

<style lang="scss"></style>

