<template>
  <div class="app-container">
    <el-tabs style="height: 100%" @tab-click="clickTab" :value="currentPath" type="border-card">
        <el-tab-pane v-for="(item, index) in tabs" :key="index" :label="item.label" :name="item.routePath"></el-tab-pane>
        <router-view />
    </el-tabs>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  data() {
    return {
      tabs: []
    }
  },
  computed: {
    currentPath () {
      return this.$route.path;
    },
    ...mapGetters(['role'])
  },
  methods: {
    clickTab (vNode) {
      this.$router.push({path: vNode.name});
    }
  },
  mounted() {
    const tabs = [
      {label: '密码修改', routePath: '/userManage/settings/accountSecurity'},
      {label: '账户管理', routePath: '/userManage/settings/accountManage'}
    ];
    this.tabs = this.role ? tabs.slice(0, 1) : tabs;
  }
}
</script>

<style lang="scss" scoped>
 
</style>

