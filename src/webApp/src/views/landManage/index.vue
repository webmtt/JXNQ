<template>
  <div style="display:flex;flex-direction: column" class="app-container monitor">
    <el-button-group class="classTab">
      <el-button type="primary" size="small"><router-link to="/landManage">田地管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/growManage">长势管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/vegetationManage">植被管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/meteorologicalService">气象服务</router-link></el-button>
    </el-button-group>

    <div class="filterPart" style="display: flex">
      <el-select style="margin: 0 10px" size="mini" v-model="conditionModel.cityValue" placeholder="请选择">
        <el-option v-for="item in cityOptions" :key="item.code" :label="item.name" :value="item.ename"></el-option>
      </el-select>
      <el-select style="margin: 0 10px" size="mini" v-model="conditionModel.cropValue" placeholder="请选择">
        <el-option v-for="item in cropOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
      <el-select style="margin: 0 10px" size="mini" v-model="conditionModel.timeValue" placeholder="请选择">
        <el-option v-for="item in timeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </div>
    <div class="contentWrap">
      <div class="showContent">
        <div class="contentTitle"><span>作物面积占比</span></div>
        <div class="contentGraph" ref="percent"></div>
      </div>
      <div class="showContent">
        <div class="contentTitle"><span>作物面积统计图</span></div>
        <div class="contentGraph" ref="areaStatics"></div>
      </div>
      <div class="showContent">
        <div class="contentTitle"><span>作物种植面积变化</span></div>
        <div class="contentGraph" ref="areaChange"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import mapboxgl from 'mapbox-gl';
import {scenceConfigs} from '@/config/scence';
import {citys, areas} from '@/config/area';
import {getAreaStatics, getCropByDistrict, getcropLineChart} from '@/api/landManage'
import {percentageOption,areaChangeOption,areaStaticsOption} from './echartConfig';
import echarts from 'echarts';
export default {
  data() {
    return {
      count: 0,
      highLightFeatureId: null,
      timer: null,
      changeTimes: 0,
      activeIndex: 0,
      snapPopup: null,
      areaData: null,
      hoveredStateId: '',
      conditionModel: {
        cityValue: '',
        timeValue: null,
        cropValue: null
      },
      cityOptions: citys.concat(areas),
      timeOptions: [],
      cropOptions: [],
    }
  },
  computed: {
    ...mapState('map', ['lengendLayers']),
    ...mapState('app', ['sysMap']),
  },
  watch: {
    'conditionModel': {
      handler(newValue, oldValue) {
        if (!this.conditionModel.cropValue) return;
        this.initPieCharts();
        this.initBarCharts();
        this.initLineBars();
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
    sysMap: {
      handler(value, oldValue) {
        value && !oldValue && this.bindShowSnapShortOnMouseMove();
      },
      immediate: true
    }
  },
  methods: {
    getShowLayers() {
      // 按类别过滤
      const classFilter = this.bussinessLayers.filter(item => {
        const classTypes = this.conditionModel.cropValue.split(',');
        const pattern = classTypes.length > 1 ? new RegExp(`${classTypes.join('|')}`) : new RegExp(classTypes[0]);
        return pattern.test(item.id);
      });
      // 再按年时间过滤
      const yearFilter = classFilter.filter(item => new RegExp(`${this.conditionModel.timeValue}`).test(item.id)).map(item => item.id)
      
      return yearFilter;
    },
    /**
     * 设置默认激活扇区;
     * @method activeDefaultAreaGraph;
     */
    activeDefaultAreaGraph (charts) {
      charts.dispatchAction({ type: 'highlight', dataIndex: this.activeIndex });
      charts.on('mouseover', (e) => {
        if (e.dataIndex !== this.activeIndex) {
          charts.dispatchAction({ type: 'downplay', dataIndex: this.activeIndex });
          this.activeIndex = e.dataIndex;
        }
      });
      charts.on('mouseout', e => charts.dispatchAction({ type: 'highlight', dataIndex: e.dataIndex }));
    },

    getCropName(param) {
      const nameMap = {
        rape: '油菜',
        wheat: '小麦',
        rice: '水稻'
      }
      const key = this.conditionModel.cityValue ? 'cropBreeds' : 'district';
      if (key === 'cropBreeds') {
        return nameMap[param]
      } else {
        return param.split('-')[1];
      }
    },

    getData () {
      const crop = this.conditionModel.cropValue.split(',').length > 1 ? '' : this.conditionModel.cropValue.split()[0]
      const params = {district: this.conditionModel.cityValue, year: this.conditionModel.timeValue, cropBreeds: crop};
      const key = this.conditionModel.cityValue ? 'cropBreeds' : 'district'
      return getAreaStatics(params)
        .then(res => {
          const obj = {};
          res.data.forEach(item => {
            if (!obj[item[key]]) {
              obj[item[key]] = {value: item.cultivationArea, name: this.getCropName(item[key])}
            }
            obj[item[key]].value += item.cultivationArea;
          });
          return {originData: res.data, transformData: Object.values(obj).map(item => {
            item.value = parseFloat(item.value.toFixed(2));
            return item;
          })}
        });
    },

    initPieCharts() {
      if (this.conditionModel.cropValue.split(',').length == 1) return;
      this.changeTimes++;
      const percentage = echarts.init(this.$refs.percent);
      this.getData().then(res => {
        percentageOption.series[0].data = res.transformData;
        percentage.setOption(percentageOption);
        this.activeDefaultAreaGraph(percentage);
      })
    },
    initBarCharts(res) {
      if (this.conditionModel.cropValue.split(',').length == 1 || this.conditionModel.cityValue) return;
      const areaStatics = echarts.init(this.$refs.areaStatics);
      const cropMap = {rape: '油菜', wheat: '小麦', rice: '水稻'};
      const obj = {};
      this.getData().then(res => {
        res.originData.forEach(item => {
        if (!obj[cropMap[item['cropBreeds']]]) {
            obj[cropMap[item['cropBreeds']]] = []
          }
          obj[cropMap[item['cropBreeds']]].push({value: item.cultivationArea, name: item['district'].split('-')[1]});
        });
        const source =  { '地区': obj['水稻'].map(item => item.name),'水稻': obj['水稻'].map(item => item.value), '油菜': obj['油菜'].map(item => item.value), '小麦': obj['小麦'].map(item => item.value)};
        areaStaticsOption.dataset.source = source;
        areaStatics.setOption(areaStaticsOption);
      })
    },
    initLineBars() {
      const params = {district: this.conditionModel.cityValue};
      const areaChange = echarts.init(this.$refs.areaChange);
      getcropLineChart(params)
        .then(res => {
          const source = {
            '时间': Object.values(res.data.filter(item => Object.keys(item)[0] === 'year')[0])[0],
            '水稻': Object.values(res.data.filter(item => Object.keys(item)[0] === 'rice')[0])[0],
            '油菜': Object.values(res.data.filter(item => Object.keys(item)[0] === 'rape')[0])[0],
            '小麦': Object.values(res.data.filter(item => Object.keys(item)[0] === 'wheat')[0])[0],
          };
          areaChangeOption.dataset.source = source
          areaChange.setOption(areaChangeOption);
        });
    },
    transformName(zName) {
      const nameMap = [
          {"name":"南湖区", "ename": 'NH'},
          {"name":"秀洲区", "ename": 'XZ'},
          {"name":"嘉善县", "ename": 'JS'},
          {"name":"海盐县", "ename": 'HY'},
          {"name":"海宁市", "ename": 'HN'},
          {"name":"平湖市", "ename": 'PH'},
          {"name":"桐乡市", "ename": 'TX'}
      ];
      return nameMap.filter(item => item.name === zName)[0].ename;
    },
    showInfo (event) {
      this.snapPopup && this.snapPopup.remove();

      if (!event.features || !event.features.length) return;
      const feature = event.features[0].properties;
      const coordinates = [event.lngLat.lng, event.lngLat.lat];
      this.getAreaData(feature).then(res => {
        let html = '<div>';
        let arr = [
          {title: '区域名称', value: this.areaData.NAME},
          {title: '作物总面积', value: (this.areaData.wheats + this.areaData.rapes + this.areaData.rices).toFixed(2) + '万亩'},
          {title: '小麦总面积', value: this.areaData.wheats + '万亩'},
          {title: '油菜总面积', value: this.areaData.rapes + '万亩'},
          {title: '水稻总面积', value: this.areaData.rices + '万亩'}
        ]
        arr.forEach(item => {
            html += `<div style="display: flex;padding: 2px 5px"><div style="width: 70px;font-weight: bold">${item.title}: </div><div style="flex: 1">${item.value}</div></div>`
        });
        html += '</div>';
        this.snapPopup.setLngLat(coordinates).setHTML(html).addTo(this.sysMap);
      });
    },
    getAreaData(feature) {
      const params = {district: this.transformName(feature.NAME), year: this.conditionModel.timeValue};
      return getCropByDistrict(params).then(res => this.areaData = Object.assign({}, res.data, feature));
    },
    moveCallback(e) {
      // const feature = e.features[0];
      // if (this.highLightFeatureId == e.features[0].properties.NAME) return;
      // this.highLightFeatureId = e.features[0].properties.NAME;
      this.sysMap.getCanvas().style.cursor = 'pointer';
      // const highLightLayer = {'id': 'highLightLayer','type': 'fill','source': {'type': 'geojson','data': {'type': 'Feature','geometry': feature.geometry}},'layout': {},'paint': {'fill-color': '#088','fill-opacity': 0.8}};
      // if (!this.sysMap.getLayer('highLightLayer')) {
      //   this.sysMap.addLayer(highLightLayer);
      // } else {
      //   this.sysMap.getLayer('highLightLayer') && this.sysMap.removeLayer('highLightLayer');
      //   this.sysMap.getSource('highLightLayer') && this.sysMap.removeSource('highLightLayer');
      //   this.sysMap.addLayer(highLightLayer);
      // }
    },
    leaveCallBack() {
      this.sysMap.getCanvas().style.cursor = '';
      // this.highLightFeatureId = null
      // this.sysMap.getLayer('highLightLayer') && this.sysMap.removeLayer('highLightLayer');
      // this.sysMap.getSource('highLightLayer') && this.sysMap.removeSource('highLightLayer');
    },
    bindShowSnapShortOnMouseMove () {
      this.sysMap.on('mousemove', '嘉兴市县界_fill', this.moveCallback);
      this.sysMap.on('mouseleave', '嘉兴市县界_fill', this.leaveCallBack);

      this.sysMap.on('click', '嘉兴市县界_fill', this.showInfo);
    }
  },
  mounted() {
    // 设置播放图层;
    this.$store.dispatch('map/changePlayLayers', {key: 'playLayers', value: []});

    fetch(scenceConfigs[this.$route.path.slice(1)].scenceUrl)
      .then(res => res.json())
      .then(res => {
        this.bussinessLayers = res.layers.filter(item => ['水稻','油菜','小麦'].indexOf(item.id.split('_')[0]) > -1);
        // 初始化默认选择数据;
        this.timeOptions = Array.from(new Set(this.bussinessLayers.map(item => item.id.split('_')[1]))).map(item => ({value: item, label: item}));
        const cropOptions = Array.from(new Set(this.bussinessLayers.map(item => item.id.split('_')[0])));
        this.cropOptions = cropOptions.map(item => ({value:  item, label: item}))

        if (this.cropOptions.length > 1) this.cropOptions.unshift({value:  '水稻,油菜,小麦', label: '全部作物'});

        this.conditionModel.cropValue = this.cropOptions[0].value;
        this.conditionModel.timeValue = this.timeOptions[0].value;

        // 设置全部业务图层
        this.$store.dispatch('map/changeBusinessLayers', {key: 'businessLayers', value: this.bussinessLayers.map(item => item.id)});
        this.$store.dispatch('map/changePlayYears', {key: 'playerYears', value: []});
      });
    
    // 初始化地图信息气泡;
    this.snapPopup = new mapboxgl.Popup({closeButton: true,closeOnClick: false})
  },
  destroyed () {
    this.snapPopup && this.snapPopup.remove();
    this.sysMap.off("mousemove", "嘉兴市县界_fill", this.moveCallback);
    this.sysMap.off("mouseleave", "嘉兴市县界_fill", this.leaveCallBack);
    this.sysMap.off("click", "嘉兴市县界_fill", this.showInfo);
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
.contentGraph {
  flex: 1;
}
</style>

