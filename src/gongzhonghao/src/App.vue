<template>
  <div id="app">
    <mt-header v-if="$route.path != '/login'" :fixed="true" :title="title">
      <router-link slot="left" to>
        <mt-button icon="back" @click="$router.back(-1)">返回</mt-button>
      </router-link>
      <mt-button v-if="$route.path == '/'" @click="logout" slot="right">退出</mt-button>
    </mt-header>

    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      title: ''
    }
  },
  watch: {
    '$route'(newRoute) {
      this.title = newRoute.meta.title;
    }
  },
  methods: {
    async logout() {
      await this.$store.dispatch('user/logout');
      this.$router.push(`/login?redirect=${this.$route.fullPath}`);
    }
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
