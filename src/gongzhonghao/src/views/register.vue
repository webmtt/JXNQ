<template>
  <div class="register">
    <div class="content-part">
      <h2>注册</h2>

      <mt-field label="用户名：" placeholder="请输入用户名" @blur.native.capture="validateUsername" :state="userModel.userName.status" v-model="userModel.userName.value"></mt-field>
      <mt-field label="密　码：" placeholder="请输入密码" @blur.native.capture="validatePassword" type="password" :state="userModel.password.status" v-model="userModel.password.value"></mt-field>
      <mt-field label="确认密码：" placeholder="请确认密码" @blur.native.capture="validateRePassword" type="password" :state="userModel.repassword.status" v-model="userModel.repassword.value"></mt-field>
      <mt-field label="手 机 号：" placeholder="请输入手机号" @blur.native.capture="validatePhone" type="tel" :state="userModel.mobile.status" v-model="userModel.mobile.value"></mt-field>

      <div class="fn-button">
        <mt-button type="primary" @click="register" size="large">确 定</mt-button>
      </div>
    </div>
  </div>
</template>

<script>
import { MessageBox, Indicator } from 'mint-ui';
import {register} from '@/api/user'
import { validZhUsername, validPhone } from '@/utils/validate';
export default {
  name: 'register',
  data () {
    return {
      userModel: {
        userName: {value: '', status: ''},
        password: {value: '', status: ''},
        repassword: {value: '', status: ''},
        mobile: {value: '', status: ''}
      }
    }
  },
  methods: {
    getParams() {
      const obj = {};
      for (let i in this.userModel) {
        obj[i] = this.userModel[i].value
      }
      return obj;
    },
    register() {
      const params = this.getParams();
      Indicator.open();
      register(params)
        .then(res => MessageBox.alert('注册成功', '提示'))
        .then(res => Indicator.close())
        .then(res => this.$router.push(`/login`))
        .catch(err => {})
        .finally(() => Indicator.close());
    },

    validateUsername() {
      if (!validZhUsername(this.userModel.userName.value)) {
        this.userModel.userName.status = 'error';
      } else {
        this.userModel.userName.status = 'success';
      }
    },

    validatePassword(rule, value, callback) {
      if (this.userModel.password.value.length < 6) {
        this.userModel.password.status = 'error';
      } else {
        this.userModel.password.status = 'success';
      }
    },
    validateRePassword() {
      if (this.userModel.repassword.value.length < 6 || this.userModel.repassword.value != this.userModel.password.value) {
        this.userModel.repassword.status = 'error';
      } else {
        this.userModel.repassword.status = 'success';
      }
    },

    validatePhone(rule, value, callback) {
      if (!validPhone(this.userModel.mobile.value)) {
        this.userModel.mobile.status = 'error';
      } else {
        this.userModel.mobile.status = 'success';
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.register {
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
.fn-button {
  padding: 20px 10px;
}
a {
  color: #42b983;
}
</style>
