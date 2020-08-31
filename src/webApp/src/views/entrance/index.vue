<template>
  <div :style="bgStyle" class="login-register-container">
    <div class="header">嘉兴农业气象灾害监测预警平台</div>
    <div class="main-view">
      <transition  name="fade" mode="out-in">
          <router-view></router-view>
      </transition>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      bgStyle: {
        backgroundImage: `url(${require('@/assets/bg.jpg')})`,
        backgroundSize: '100% 100%'
      },
      loading: false,
    }
  },
  watch: {},
  methods: {}
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */
$bg:#283443;
$light_gray:#fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-register-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-register-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;
      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }
  .el-checkbox {
      color: #ffffff!important;
  }
  .el-link.el-link--info {
      color: #ffffff;
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.5);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
  .el-form-item__label {
    color: #ffffff
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:#eee;

.login-register-container {
  min-height: 100%;
  width: 100%;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  .header {
    height: 50px;
    line-height: 50px;
    font-size: 24px;
    color: #ffff;
    margin-left: 50px;
    font-weight: bold;
    letter-spacing: 2px;
  }
  .main-view {
    display: flex;
    flex: 1;
    justify-content: center;
    align-items: center;
  }
}
</style>
