<template>
  <div class="index">
    <div class="userInfo">
      <img width="60px" height="60px" src="../assets/user.png" />
      <div class="userDetail">
        <div><b>作业员：</b>{{info && info.userName}}</div>
        <div><b>电　话：</b>{{info && info.mobile}}</div>
      </div>
      <mt-button type="danger" @click="addOrEditLand">新增</mt-button>
    </div>
    <!-- 地块列表v-infinite-scroll="loadMore" infinite-scroll-disabled="loading" -->
    <div class="landLists">
      <div class="card" v-for="(item, index) in cropLists" :key="index">
        <div class="listItem">
          <div class="label">地块编号：</div><div class="content">#{{index+1}}</div>
          <div>
            <a class="edit" @click="addOrEditLand(item)" href="javascript:void(0)">编辑</a>
            <a class="delete" @click="deleteLand(item)" href="javascript:void(0)">删除</a>
          </div>
        </div>
        <div class="listItem">
          <div class="label">种植作物：</div>
          <div class="content">{{item.crops}}</div>
        </div>
        <div class="listItem">
          <div class="label">地块位置：</div><div class="content">{{item.city + item.district + item.address}}</div>
        </div>
        <div class="listItem">
          <div class="label">种植面积：</div><div class="content">{{item.area}}亩</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { MessageBox, Popup, InfiniteScroll, Indicator, Toast } from 'mint-ui';
import { mapGetters } from 'vuex'
import * as landApi from '@/api/land';
export default {
  name: 'HelloWorld',
  data () {
    return {
      cropLists: [],
      loading: false,
      pagation: {
        pageSize: 4,
        page: 1,
        total: -1
      }
    }
  },
  computed: {
    ...mapGetters(['info', 'id'])
  },
  methods: {
    /**
     * 删除地块
     * @method deleteLand
     */
    deleteLand(deleteItem) {
      MessageBox({
        title: '提示',
        message: '确定要删除吗?',
        showCancelButton: true,
        closeOnClickModal: false
      }).then(res => {
        if (res === 'confirm') {
          landApi.deleteLand({landId: deleteItem.id})
            .then(res => MessageBox.alert(`删除地块成功`, '提示'))
            .then(res => this.getListData())
            .then(res => this.cropLists = res);
        }
      });
    },
    /**
     * 新增或编辑地块
     * @method deleteLand
     */
    addOrEditLand(itemData) {
      if (this.cropLists.length === 3 && !itemData.id) {
        return Toast({ message: '一个用户不能绑定3个以上的地块', duration: 3 * 1000 });
      }
      this.$router.push({ path: 'updateLand', query: {data: itemData}})
    },
    getListData() {
      Indicator.open();
      return landApi.getLandList({userid: this.id}).then(res => res.data.blocks || []).finally(res => Indicator.close());
    },
    // loadMore() {
    //   if (this.pagation.total < 0) return;
    //   if (this.pagation.page == Math.ceil(this.pagation.total / this.pagation.pageSize)) {
    //     return Toast({ message: '没有更多数据了', duration: 2000 });
    //   }

    //   Indicator.open({ text: '加载中...', spinnerType: 'fading-circle' });

    //   this.pagation.page++;
    //   setTimeout(() => {
    //     this.getListData().then(res => {
    //       this.cropLists.push(...res.list);
    //       Indicator.close();
    //     });
    //   }, 1000)
    // }
  },
  created() {
    this.getListData().then(res => this.cropLists = res);
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss" scoped>
.index {
  padding: 0 10px;
  display: flex;
  flex-direction: column;;
  justify-content: center;
  align-items: center;
  height: 100%;
  .userInfo {
    height: 100px;
    width: 100%;
    margin-top: 40px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px dotted #bba7a7;
    .userDetail {
      flex: 1;
      height: 100%;
      padding: 20px;
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: flex-start;
    }
  }
  .landLists {
    flex: 1;
    width: 100%;
    overflow-y:scroll;
    // overflow-y: auto;
  }
  .card {
    height: 150px;
    margin-bottom: 15px;
    box-shadow: rgba(0,0,0,0.06) 0px 1px 1px 1px;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    .listItem {
      display: flex;
      .label {
        width: 100px;
      }
      .content {
        flex: 1;
        text-align: left;
        padding-left: 20px;
      }
      a {
        padding: 0 5px;
        &.edit {
          color: #26a2ff;
        }
        &.delete {
          color: #ef4f4f
        }
      }
    }
  }

  .popBox {
    width:85%;
    height: 200px;
    display: flex;
    flex-direction: column;
    .box-header {
      height: 30px;
      line-height: 30px;
      padding: 0 10px;
      text-align: left;
      font-weight: bold;
    }
    .box-content {
      flex: 1;
      display: flex;
      padding: 10px;
      flex-direction: column;
      .listItem {
        display: flex
      }
    }
    .box-footer {
      height: 40px;
    }
  }
}
</style>
