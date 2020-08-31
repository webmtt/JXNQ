<template>
  <div style="display:flex;flex-direction: column" class="app-container vegetationManage monitor">
    <el-button-group class="classTab">
      <el-button type="default" size="small"><router-link to="/landManage">田地管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/growManage">长势管理</router-link></el-button>
      <el-button type="primary" size="small"><router-link to="/vegetationManage">植被管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/meteorologicalService">气象服务</router-link></el-button>
    </el-button-group>
    
    <div class="contentWrap">
      <div class="showContent">
        <div class="contentTitle">
          <span>植被覆盖情况</span>
          <i @click="dialogFormVisible = true" class="el-icon-edit-outline"></i>
        </div>
        <div style="display: flex;align-items: center;" class="contentGraph">
          <el-table
            height="250px"
            ref="detailTable"
            :row-style="setRowStyle"
            :data="tableData"
            empty-text="无"
            border
            :show-header="false"
            style="width: 100%">
            <el-table-column
              prop="date"
              label="姓名"
              width="100">
            </el-table-column>
            <el-table-column
              prop="name"
              label="地址">
            </el-table-column>
          </el-table>
        </div>
      </div>
      <div class="showContent">
        <div class="contentTitle"><span>植被覆盖率变化</span></div>
        <div class="contentGraph" ref="coverRate"></div>
      </div>
    </div>
    <!-- 编辑弹框 -->
    <el-dialog :close-on-click-modal="false" top="25vh" class="feedbackEdit" title="编辑植被覆盖情况" width="400px" :modal="false" :append-to-body="true" :visible.sync="dialogFormVisible">
      <el-form :model="form">
        <el-form-item label="影像源：" :label-width="formLabelWidth">
          <el-input v-model="form.imageSrc" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="总体描述：" :label-width="formLabelWidth">
          <el-input v-model="form.imageDesc" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="影响分析：" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.imageAnalysis"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="setDetail" :loading="loading">{{loading ? '更新中' : '确 定'}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import echarts from 'echarts';
import {scenceConfigs} from '@/config/scence';
import {coverRateOption} from './echartConfig';
import Qs from 'qs';
import {getSeasonsData,getLayerDetail,updateLayerDetail} from '@/api/vegetationManage'
export default {
  data() {
    return {
      dialogFormVisible: false,
      loading: false,
      form: {
        id: '',
        imageSrc: '',
        imageDesc: '',
        imageAnalysis: '',
      },
      formLabelWidth: '85px',
      bussinessLayers: [],
      conditionModel: {
        timeValue: null,
      },
      value: '',
      tableData: []
    }
  },
  methods: {
    setDetail () {
      this.loading = true;
      updateLayerDetail(this.form, {'Content-Type': 'application/json'})
        .then(res => {
          this.dialogFormVisible = false;
          this.getLayerDetails(this.currentShowLayers[0]);
          this.$message({ type: 'success', message: '编辑成功!' });
        })
        .catch(err => this.$message({ type: 'error', message: '编辑成功!' }))
        .finally(res => this.loading = false);
    },
    getShowLayers() {
      // 再按年时间过滤
      const yearFilter = this.bussinessLayers.filter(item => new RegExp(`${this.conditionModel.timeValue}`).test(item.id)).map(item => item.id)
      
      const days = yearFilter.map(item => ({key: parseInt(item.split('_')[2]+item.split('_')[3]), value: item})).sort((a,b) => a.key - b.key);

      const playeLayers = days.map(item => item.value);
      // 设置播放图层;
      this.$store.dispatch('map/changePlayLayers', {key: 'playLayers', value: playeLayers});

      return [playeLayers[0]];
    },
    showLineCharts() {
      const coverRate = echarts.init(this.$refs.coverRate);
      getSeasonsData({year: this.conditionModel.timeValue})
        .then(res => {
          const times = Object.keys(res.data).filter(item => item != 'season');
          const source = [
            ['时间', ...times],
            ...res.data.season.map((item, index) => {
              const temp = times.map((innerItem, innerIndex) => res.data[innerItem][index]);
              temp.unshift(item);
              return temp
            })
          ];
          coverRateOption.dataset.source = source;
          coverRate.setOption(coverRateOption);
        });
    },
    getLayerDetails(imageName) {
      getLayerDetail({imageName})
        .then(res => {
          this.form = Object.assign({},res.data);
          this.tableData = [
            { date: '影像源：', name: res.data.imageSrc || '-' },
            { date: '总体描述：', name: res.data.imageDesc || '-' },
            { date: '影响分析：', name: res.data.imageAnalysis || '-' }]
        });
    },
    setRowStyle(row) {
      const pieces = Math.floor(parseInt(this.$refs.detailTable.height) / 6);
      return {
        height: row.rowIndex == 0 ? `${pieces}px` : row.rowIndex == 1 ? `${pieces * 2}px` : `${pieces * 2.9}px`
      }
    }
  },
  computed: {
    currentShowLayers() {
      return this.$store.state.map.showLayers
    }
  },
  watch: {
    'conditionModel': {
      handler(newValue) {
        if (!this.conditionModel.timeValue) return;
        const showLayers = this.getShowLayers();
        this.$store.dispatch('map/changeActiveLayers', {key: 'showLayers', value: showLayers});
        this.$store.dispatch('map/changeLengendLayers', {key: 'lengendLayers', value: []});

        // 年份变化重新请求
        this.showLineCharts();
      },
      deep: true,
      immediate: true
    },
    currentShowLayers(value) {
      this.getLayerDetails(value[0]);
    }
  },
  mounted() {
    fetch(scenceConfigs[this.$route.path.slice(1)].scenceUrl)
      .then(res => res.json())
      .then(res => {
        this.bussinessLayers = res.layers.filter(item => ['植被覆盖'].indexOf(item.id.split('_')[0]) > -1);
        // 初始化默认选择数据;
        this.timeOptions = Array.from(new Set(this.bussinessLayers.map(item => item.id.split('_')[1]))).map(item => ({value: item, label: item}));
        this.conditionModel.timeValue = this.timeOptions[0].value;

        // 设置全部业务图层
        this.$store.dispatch('map/changeBusinessLayers', {key: 'businessLayers', value: this.bussinessLayers.map(item => item.id)});
        this.$store.dispatch('map/changePlayYears', {key: 'playerYears', value: this.timeOptions});
      });
  }
}
</script>

<style scoped>
.filterPart {
  height: 40px;
  align-items: center;
}
.contentWrap {
   flex: 1;
   display: flex;
   flex-direction: column
}
.showContent {
  margin-top: 10px;
  flex: 1;
  display: flex;
  flex-direction: column;
}
.contentTitle span {
  color: #17cdbd;
  padding-left: 15px;
  font-weight: bold;
  border-left: 4px solid #17cdbd;
}
.contentTitle i {
  color: #17cdbd;
  cursor: pointer;
  font-size: 20px;
  float: right;
}
.contentGraph {
  flex: 1;
}
</style>
<style lang="scss">
.vegetationManage .el-table {
  background: transparent
}
.vegetationManage .el-table th, .vegetationManage .el-table tr {
  background: transparent;
  color: #ffffff
}
.vegetationManage .el-table--enable-row-hover .el-table__body tr:hover>td {
  background: transparent
}
.feedbackEdit {
  .el-dialog{
    background: #1e1f1ede;
    .el-dialog__header {
      color: #fff;
      background: #17cdbd;
      display: flex;
      span {
        color: #fff;
      }
      .el-dialog__close {
        color: #fff
      }
    }
    .el-dialog__body {
      padding: 25px 25px 0px;
      textarea, input {
        background: transparent
      }
    }
    .el-dialog__footer {
      button {
        background: transparent;
        border-radius: 25px;
        width: 150px;
        border: 1px solid #17cdbd;
        color: #fff;
        &:hover {
          background: #17cdbd
        }
      }
    }
    .el-input__inner, .el-textarea__inner{
      color: #ffffff
    }
    .el-form-item__label {
      color: #fff
    }
  }
}
</style>

