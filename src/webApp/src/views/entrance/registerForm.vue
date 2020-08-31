<template>
  <el-form class="login-form" :rules="registerRules" ref="form" :model="form" label-width="80px">
    <div class="title-container"><h3 class="title">注 册</h3></div>
    <el-form-item label-width="100px" prop="userName" style="letter-spacing: 1px" label="用 户 名：">
      <el-input v-model="form.userName"></el-input>
    </el-form-item>
    <el-form-item label-width="100px" prop="sex" label="性　　别：">
      <el-radio v-model="form.sex" label="男">男</el-radio>
      <el-radio v-model="form.sex" label="女">女</el-radio>
    </el-form-item>
    <el-form-item label-width="100px" prop="pwd" label="设置密码：">
      <el-input ref="password" :type="passwordType" v-model="form.pwd"></el-input>
      <span class="show-pwd" @click="showPwd">
        <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
      </span>
    </el-form-item>
    <el-form-item label-width="100px" prop="rePwd" label="确认密码：">
      <el-input ref="rePasswordType" :type="rePasswordType" v-model="form.rePwd"></el-input>
      <span class="show-pwd" @click="showRePwd">
        <svg-icon :icon-class="rePasswordType === 'password' ? 'eye' : 'eye-open'" />
      </span>
    </el-form-item>
    <el-form-item label-width="100px" prop="companyName" label="单位名称：">
      <el-input v-model="form.companyName"></el-input>
    </el-form-item>
    <el-form-item label-width="100px" prop="address" label="单位地址：">
      <el-select style="width: 120px" v-model="form.address.city" placeholder="市">
        <el-option
          v-for="item in citys"
          :key="item.code"
          :label="item.name"
          :value="item.code">
        </el-option>
      </el-select>
      <el-select style="width: 120px" @change="areaChange" v-model="form.address.area" clearable placeholder="区">
        <el-option
          v-for="item in areas"
          :key="item.code"
          :label="item.name"
          :value="item.code">
        </el-option>
      </el-select>
      <el-select style="width: 120px" v-model="form.address.street" clearable placeholder="街道">
        <el-option
          v-for="item in streets"
          :key="item.code"
          :label="item.name"
          :value="item.code">
        </el-option>
      </el-select>
    </el-form-item>
    <div class="loginHanle">
      <el-button @click="goBackToLogin" style="flex: 1" type="success">返 回</el-button>
      <el-button @click="handleRegister" style="flex: 1" type="primary">注 册</el-button>
    </div>
  </el-form>
</template>

<script>
import { validUsername } from '@/utils/validate';
import {citys, areas, streets} from '@/config/area';
import {register} from '@/api/user'
export default {
  name: 'register',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('用户名不合法!'))
      } else {
        callback()
      }
    }
    const validateCompanyName = (rule, value, callback) => {
      if (value.length < 3) {
        callback(new Error('单位名称不合法,长度必须大于3位'))
      } else {
        callback()
      }
    }

    var validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.form.rePwd !== '') {
          this.$refs.form.validateField('rePwd');
        }
        callback();
      }
    };
    var validateRePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.form.pwd) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    
    return {
      form: {
        userName: '',
        sex: '男',
        pwd: '',
        rePwd: '',
        companyName: '',
        address: {
          city: '3304',
          area: '',
          street: ''
        }
      },
      citys,
      areas,
      streets,
      value: '',
      registerRules: {
        userName: [{ required: true, trigger: 'blur', validator: validateUsername }],
        sex: [{ required: true, trigger: 'blur' }],
        pwd: [{ required: true, trigger: 'blur', validator: validatePassword }],
        rePwd: [{ required: true, trigger: 'blur', validator: validateRePassword }],
        companyName: [{ required: true, trigger: 'blur', validator: validateCompanyName }],
        address: [{ required: true, trigger: 'change' }]
      },
      rePasswordType: 'password',
      passwordType: 'password',
    }
  },
  watch: {},
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = '';
      } else {
        this.passwordType = 'password';
      }
      this.$nextTick(() => {
        this.$refs.password.focus();
      })
    },
    showRePwd () {
      if (this.rePasswordType === 'password') {
        this.rePasswordType = '';
      } else {
        this.rePasswordType = 'password';
      }
      this.$nextTick(() => {
        this.$refs.rePasswordType.focus();
      })
    },
    /**
     * 区改变;
     * @method areaChange
     */
    areaChange (value) {
      if (value) {
        this.streets = streets.filter(item => item.areaCode === value);
      } else {
        this.streets = [];
        this.form.address.street = '';
      }
    },
    /**
     * 返回到登陆;
     * @method goBackToLogin
     */
    goBackToLogin () {
      this.$router.push({ path: '/login' });
    },
    getNameByCode (code, sourceData) {
      const data = sourceData.filter(item => item.code === code);
      if (data.length) {
        return data[0].name;
      } else {
        throw new Error('区划code不正确');
      }
    },
    handleRegister() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true;
          const param = {
            user_name: this.form.userName,
            gender: this.form.sex,
            organization: this.form.companyName,
            address: `${this.getNameByCode(this.form.address.city, citys)}${this.getNameByCode(this.form.address.area, areas)}${this.getNameByCode(this.form.address.street, streets)}`,
            user_password:this.form.pwd,
          };
          register(param)
            .then(res => {
              this.$alert('注册成功', '提示', {
                confirmButtonText: '确定',
                callback: action => {
                  this.$router.push({ path: '/login' });
                }
              });
            })
            .finally(res => this.loading = false);
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
        justify-content: space-around;
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
