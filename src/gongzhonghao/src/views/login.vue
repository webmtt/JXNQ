<template>
  <div class="login">
    <div class="content-part">
      <img class="login-icon" src="../assets/logo.jpg" />
      <mt-field label="用户名：" :disableClear="true" placeholder="请输入用户名" @blur.native.capture="validateUsername" :state="userModel.username.status" v-model="userModel.username.value"></mt-field>
      <mt-field label="密　码：" :disableClear="true" placeholder="请输入密码" @blur.native.capture="validatePassword" :state="userModel.password.status" type="password" v-model="userModel.password.value"></mt-field>

      <div class="fn-button">
        <mt-button type="primary" @click="login" size="large">登 录</mt-button>
        <router-link to="/register">注册用户</router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { validZhUsername } from '@/utils/validate';
import { Indicator } from 'mint-ui';
export default {
  name: 'login',
  data () {
    return {
      userModel: {
        username: {value: '韩梅梅', status: ''},
        password: {value: '123456', status: ''},
        redirect: undefined
      }
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
    login() {
      Indicator.open();
      const params = {username: this.userModel.username.value, password: this.userModel.password.value};
      this.$store.dispatch('user/login', params)
        .then(() => this.$router.push({ path: this.redirect || '/' }))
        .catch(() => {})
        .finally(() => Indicator.close());
    },
    validateUsername() {
      if (this.userModel.username.value) {
        if (!validZhUsername(this.userModel.username.value)) {
          this.userModel.username.status = 'error';
        } else {
          this.userModel.username.status = 'success';
        }
      } else {
        this.userModel.username.status = '';
      }
    },

    validatePassword(rule, value, callback) {
      if (this.userModel.password.value) {
        if (this.userModel.password.value.length < 6) {
          this.userModel.password.status = 'error';
        } else {
          this.userModel.password.status = 'success';
        }
      } else {
        this.userModel.password.status = '';
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
a {
  color: #42b983;
}
.login {
  padding: 0 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  .content-part {
    width: 90%;
    margin-top: -120px;
  }
}
.login-icon {
  display: inline-block;
  border-radius: 100px;
  width: 150px;
}
.fn-button {
  padding: 20px 10px;
  a {
    float: right;
    padding: 10px 0;
  }
}
</style>
