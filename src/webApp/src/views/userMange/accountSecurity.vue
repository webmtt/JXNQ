<template>
<el-form ref="modifyPassword" :model="modifyPassword" :rules="modifyPasswordRules" class="modifyPassword-form" auto-complete="on" label-width="80px">
    <el-form-item prop="newPassword" label="新密码">
        <el-input
        :key="passwordType"
        ref="newPassword"
        v-model="modifyPassword.newPassword"
        :type="passwordType"
        placeholder="新密码"
        name="newPassword"
        tabindex="2"
        show-password
         />
    </el-form-item>
    <el-form-item prop="surePassword" label="确认密码">
        <el-input
        :key="passwordType"
        ref="surePassword"
        v-model="modifyPassword.surePassword"
        :type="passwordType"
        placeholder="确认密码"
        name="surePassword"
        tabindex="3" 
        show-password/>
      </el-form-item>

     <div class="sureModifyPassword">
        <el-button :loading="loading"  type="success" style="flex: 1;margin-bottom:30px;width:380px; background-color:#20ada3" @click="handleModifyPassword">确定修改</el-button>
    </div>
    </el-form>
    
</template>

<script>
import {modifyPassword} from '@/api/user'
import * as auth from '@/utils/auth';
export default {
  name:'modifyPassword',
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码长度不能小于6位'))
      } else {
        callback()
      }
    }

    const validateRePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.modifyPassword.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    }
    return {
     modifyPassword: {
        newPassword: '',
        surePassword:''
      },
      modifyPasswordRules:{
        newPassword: [{ required: true, trigger: 'blur', validator: validatePassword }],
        surePassword: [{ required: true, trigger: 'blur', validator: validateRePassword }],

      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,

    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      })
    },
    handleModifyPassword() {
      this.$refs.modifyPassword.validate(valid => {
        if (valid) {
          modifyPassword({user_id: auth.getToken(), user_password: this.modifyPassword.newPassword})
            .then(res => {
              this.$alert('密码修改成功', '提示', {
                confirmButtonText: '确定',
                callback: async action => {
                  await this.$store.dispatch('user/logout')
                  this.$router.push(`/login?redirect=${this.$route.fullPath}`)
                }
              });
            })
            .catch(err => this.$message({ type: 'error', message: '修改密码失败!'}))
        } else {
          return false
        }
      })
    }

  }
}
</script>

<style lang="scss" scoped>


.modifyPassword-form{
  position: relative;
    width: 420px;
    overflow: hidden;
    
    padding: 30px 20px;
    
    .sureModifyPassword{
      text-align: center
    }
    }

</style>

