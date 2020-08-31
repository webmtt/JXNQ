<template>
  <div class="navbar">
    <!-- <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb class="breadcrumb-container" /> -->
    <div style="width: 40%;flex: 1 1 0%;font-size: 22px;display: flex;align-items: center;padding-left: 20px;letter-spacing: 2px;">嘉兴农业气象灾害监测预警平台</div>
    <div style="display: flex;align-items: center;width: 300px">{{timeString}}</div>
    
    <div style="display: flex;align-items: center;justify-content: center;width: 80px;">
      <el-popover
        placement="bottom"
        trigger="hover">
        <el-table stripe size="mini" :show-header="false" :data="gridData">
          <el-table-column :show-overflow-tooltip="true" width="100" label="日期">
            <template slot-scope="scope">
              <el-button style="padding: 3px 12px;border-radius: 12px;" size="mini">{{scope.row.date}}</el-button>
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" width="200" property="address" label="地址"></el-table-column>
        </el-table>
        <el-badge is-dot class="item" slot="reference"><i style="font-size: 22px" class="el-icon-bell"></i></el-badge>
      </el-popover>
    </div>
    
    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="hover">
        <div class="avatar-wrapper">
          <span style="display: inline-block;padding: 0 5px">您好, <b>{{name}}</b></span>
          <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu style="text-align: center" slot="dropdown" class="user-dropdown">
          <router-link to="/landManage">
            <el-dropdown-item>
              首页
            </el-dropdown-item>
          </router-link>
          <router-link to="/userManage">
            <el-dropdown-item>
              个人中心
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">退 出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import {getSatus} from '@/api/meteorologicalService'

export default {
  data() {
    return {
      gridData: [],
        infoMaps: [
          {ename: 'lanyang',name: '烂秧', info: '受温度和降水影响，未来几天会可能出现水稻烂秧灾害，请注意关注'},
          {ename: 'fenniejianhuan',name: '分蘖减缓', info: '受温度影响，未来可能会出现水稻分蘖减缓，请注意关注'},
          {ename: 'qiujidiwen',name: '秋季低温', info: '受连续几日的温度影响，未来可能出现水稻秋季低温，请注意关注'},
          {ename: 'lianyinyu',name: '连阴雨', info: '受降水和日照时数影响，未来几天可能会出现连阴雨 ，请注意关注'},
          {ename: 'baoyu',name: '暴雨', info: '未来几天可能会出现暴雨，请注意关注'},
          {ename: 'honglao',name: '洪涝', info: '受降水影响，未来几天可能会出现洪涝；请注意关注'}
        ]
    }
  },
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'timeString',
      'name',
      'infoWeathers'
    ])
  },
  watch: {
    infoWeathers: {
      handler(value) {
        this.gridData = this.infoMaps.filter(item => value.indexOf(item.ename) > -1).map(item => ({date: item.name, address: item.info}));
      },
      immediate: true
    },
  },
  created() {
    getSatus()
        .then(res => {
          const checkedStatus = Object.keys(res.data).filter(item => res.data[item]);
          this.$store.dispatch('app/setInfoWeathers', checkedStatus);
        });
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  display: flex;
  justify-content: space-between;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin: 0 40px 0 10px;
      height: 100%;
      .avatar-wrapper {
        position: relative;
        height: 100%;
        display: flex;
        align-items: center;
        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
