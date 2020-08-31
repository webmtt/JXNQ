<template>
  <div class="app-container">
    <div class="userSnapShot">
      <el-avatar :size="120" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
      <div>{{name}}</div>
      <el-button size="small" @click="logout" style="background: transparent; color: #fff">账户退出</el-button>
    </div>
    <div class="settings">
      <ul>
        <li><router-link :class="{'active': /detail/.test(currentRoute)}" :to="routeLists.detail">基本信息</router-link></li>
        <li><router-link :class="{'active': /settings/.test(currentRoute)}" :to="routeLists.setting">用户设置</router-link></li>
      </ul>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      routeLists: {
        detail: '/userManage/detail',
        setting: '/userManage/settings'
      }
    }
  },
  computed: {
    currentRoute () {
      return this.$route.path;
    },
    ...mapGetters(['name'])
  },
  methods: {
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/variables.scss";
  .userSnapShot {
    color: #{$whiteText};
    text-align: center;
    padding: 20px 0;
    div {
      padding: 10px;
    }
  }

  .settings ul {
    padding: 0;
    margin-top: 40px;
    list-style: none;
    color: #{$whiteText};
    border-top: 1px solid #eeeeee;
      li {
        height: 50px;
        line-height: 50px;
        text-align: center;
        border-bottom: 1px solid #eeeeee
      }
    .active {
      color: #{$activeText};
    }
  }
</style>

