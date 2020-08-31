<template>
  <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
    <div class="title-container">
        <h3 class="title">登 录</h3>
    </div>

    <el-form-item prop="username">
        <span class="svg-container"><svg-icon icon-class="user" /></span>
        <el-input
        ref="username"
        v-model="loginForm.username"
        placeholder="用户名"
        name="username"
        type="text"
        tabindex="1" />
    </el-form-item>

    <el-form-item prop="password">
        <span class="svg-container"><svg-icon icon-class="password" /></span>
        <el-input
        :key="passwordType"
        ref="password"
        v-model="loginForm.password"
        :type="passwordType"
        placeholder="密码"
        name="password"
        tabindex="2" />
        <span class="show-pwd" @click="showPwd">
        <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
    </el-form-item>
    <!--  -->
    <div class="pwdHanle">
        <el-checkbox v-model="checked">记住密码</el-checkbox>
    </div>
    <!--  -->
    <div class="loginHanle">
        <el-button :loading="loading" type="success" style="flex: 1;margin-bottom:30px;" @click.native.prevent="goToRegister">注册</el-button>
        <el-button :loading="loading" type="primary" style="flex: 1;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
    </div>
    </el-form>
</template>

<script>
import { validUsername } from '@/utils/validate';
import * as auth from '@/utils/auth';

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('用户名不合法!'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码长度不能小于6位'))
      } else {
        callback()
      }
    }
    return {
      checked: true,
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
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
  mounted () {
    if (auth.getUser()) {
      this.loginForm = JSON.parse(auth.getUser());
    }
    this.checked = !!auth.getUser();
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    goToRegister () {
      this.$router.push({ path: '/register' });
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          // 处理记住密码;
          this.checked ? auth.rememberUser(this.loginForm) : auth.deleteUser();

          this.$store.dispatch('user/login', this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || '/' });
            this.loading = false;
          }).catch(() => {
            this.loading = false;
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-form {
    position: relative;
    width: 520px;
    overflow: hidden;
    margin-top: -50px;
    background: #001011c7;
    padding: 30px 20px;
    .title-container {
        position: relative;
        .title {
        font-size: 26px;
        color: $light_gray;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
        }
    }
    .pwdHanle {
        display: flex;
        margin: 0 0 20px 0;
        justify-content: space-between;
    }
    .loginHanle {
        display: flex;
    }
}

.svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
</style>
