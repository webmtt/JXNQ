<template>
  <div style="display:flex;flex-direction: column;" class="app-container growManage monitor">
    <el-button-group class="classTab">
      <el-button type="default" size="small"><router-link to="/landManage">田地管理</router-link></el-button>
      <el-button type="primary" size="small"><router-link to="/growManage">长势管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/vegetationManage">植被管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/meteorologicalService">气象服务</router-link></el-button>
    </el-button-group>
    
    <div class="filterPart" style="display: flex">
      <!-- <el-select style="margin: 0 10px" size="mini" v-model="conditionModel.cityValue" placeholder="请选择">
        <el-option v-for="item in cityOptions" :key="item.name" :label="item.name" :value="item.code"></el-option>
      </el-select> -->
      <el-select style="margin: 0 10px" size="mini" v-model="conditionModel.cropValue" placeholder="请选择">
        <el-option v-for="item in cropOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
      <el-select style="margin: 0 10px" size="mini" v-model="conditionModel.timeValue" placeholder="请选择">
        <el-option v-for="item in timeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </div>
    <div class="contentWrap">
      <div class="showContent">
        <div class="contentTitle">
          <span>作物涨势情况</span>
          <i @click="dialogFormVisible = true" class="el-icon-edit-outline"></i>
        </div>
        <div style="display: flex;align-items: center;padding: 10px 0 0 0" class="contentGraph">
          <el-table
            height="250px"
            :data="tableData"
            :row-style="setRowStyle"
            ref="detailTable"
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
        <div class="contentTitle"><span>作物种植指导</span></div>
        <div style="margin-top: 10px;color: #fff" class="contentGraph">
          {{guide || '无'}}
        </div>
      </div>
    </div>
    <!-- 编辑弹框 -->
    <el-dialog top="20vh" :close-on-click-modal="false" class="feedbackEdit" title="编辑作物长势情况" width="400px" :modal="false" :append-to-body="true" :visible.sync="dialogFormVisible">
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
        <el-form-item label="种植指导：" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.guide"></el-input>
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
import { mapState } from 'vuex';
import {citys, areas} from '@/config/area';
import {scenceConfigs} from '@/config/scence';
import {updateLayerDetail, getLayerDetail} from '@/api/vegetationManage'
export default {
  data() {
    return {
      dialogFormVisible: false,
      loading: false,
      guide: '',
      form: {
        id: '',
        imageSrc: '',
        imageDesc: '',
        imageAnalysis: '',
        guide: ''
      },
      formLabelWidth: '85px',
      bussinessLayers: [],
      cityOptions: citys.concat(areas),
      timeOptions: [],
      cropOptions: [],
      conditionModel: {
        cityValue: '3304',
        timeValue: null,
        cropValue: null
      },
      tableData: []
    }
  },
  computed: {
    ...mapState('map', ['lengendLayers', 'showLayers']),
  },
  watch: {
    'conditionModel': {
      handler(newValue) {
        if (!this.conditionModel.cropValue) return;
        this.$store.dispatch('map/changeActiveLayers', {key: 'showLayers', value: this.getShowLayers()});
        this.$store.dispatch('map/changeLengendLayers', {key: 'lengendLayers', value: this.conditionModel.cropValue.split(',')});
      },
      deep: true,
      immediate: true
    },
    lengendLayers(value) {
      if (value.length === 3) {
        this.conditionModel.cropValue = '水稻,油菜,小麦'
      } else {
        this.conditionModel.cropValue = value[0];
      }
    },
    showLayers(value) {
      this.getLayerDetails(value[0]);
    }
  },
  methods: {
    setRowStyle(row) {
      const pieces = Math.floor(parseInt(this.$refs.detailTable.height) / 6);
      return {
        height: row.rowIndex == 0 ? `${pieces}px` : row.rowIndex == 1 ? `${pieces * 2}px` : `${pieces * 2.9}px`
      }
    },
    setDetail () {
      this.loading = true;
      updateLayerDetail(this.form, {'Content-Type': 'application/json'})
        .then(res => {
            this.dialogFormVisible = false;
            this.getLayerDetails(this.showLayers[0]);
            this.$message({ type: 'success', message: '编辑成功!' });
        })
        .catch(err => this.$message({ type: 'error', message: '编辑成功!' }))
        .finally(res => this.loading = false);
    },
    getLayerDetails(imageName) {
      getLayerDetail({imageName})
        .then(res => {
          this.form = Object.assign({}, res.data);
          this.guide = res.data.guide;
          this.tableData = [
            { date: '影像源：', name: res.data.imageSrc || '-' },
            { date: '总体描述：', name: res.data.imageDesc || '-' },
            { date: '影响分析：', name: res.data.imageAnalysis || '-' }]
        });
    },
    getShowLayers() {
      // 按类别过滤
      const classFilter = this.bussinessLayers.filter(item => {
        const classTypes = this.conditionModel.cropValue.split(',');
        const pattern = classTypes.length > 1 ? new RegExp(`${classTypes.join('|')}`) : new RegExp(classTypes[0]);
        return pattern.test(item.id);
      });
      // 再按年时间过滤
      const yearFilter = classFilter.filter(item => new RegExp(`${this.conditionModel.timeValue}`).test(item.id)).map(item => item.id)
      
      const days = yearFilter.map(item => ({key: parseInt(item.split('_')[2]+item.split('_')[3]), value: item})).sort((a,b) => a.key - b.key);

      const playeLayers = days.map(item => item.value);
      // 设置播放图层;
      this.$store.dispatch('map/changePlayLayers', {key: 'playLayers', value: playeLayers});

      return [playeLayers[0]];
    }
  },
  mounted() {
    fetch(scenceConfigs[this.$route.path.slice(1)].scenceUrl)
      .then(res => res.json())
      .then(res => {
        this.bussinessLayers = res.layers.filter(item => ['水稻','油菜','小麦'].indexOf(item.id.split('_')[0]) > -1);
        // 初始化默认选择数据;
        this.timeOptions = Array.from(new Set(this.bussinessLayers.map(item => item.id.split('_')[1]))).map(item => ({value: item, label: item}));
        const cropOptions = Array.from(new Set(this.bussinessLayers.map(item => item.id.split('_')[0])));
        this.cropOptions = cropOptions.map(item => ({value:  item, label: item}))

        this.conditionModel.cropValue = this.cropOptions[0].value;
        this.conditionModel.timeValue = this.timeOptions[0].value;

        // 设置全部业务图层
        this.$store.dispatch('map/changeBusinessLayers', {key: 'businessLayers', value: this.bussinessLayers.map(item => item.id)});
        this.$store.dispatch('map/changePlayYears', {key: 'playerYears', value: []});
      });
  }
}
</script>

<style scoped>
.filterPart {
  height: 50px;
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
.growManage .el-table {
  background: transparent!important;
}
.growManage .el-table th, .growManage .el-table tr {
  background: transparent!important;
  color: #ffffff
}
.growManage .el-table--enable-row-hover .el-table__body tr:hover>td {
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
    .el-form-item__label {
      color: #fff
    }
    .el-input__inner, .el-textarea__inner{
      color: #ffffff
    }
  }
}
</style>

