<template>
  <div style="display:flex;flex-direction: column;" class="app-container monitor">
    <el-button-group class="classTab">
      <el-button type="default" size="small"><router-link to="/landManage">田地管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/growManage">长势管理</router-link></el-button>
      <el-button type="default" size="small"><router-link to="/vegetationManage">植被管理</router-link></el-button>
      <el-button type="primary" size="small"><router-link to="/meteorologicalService">气象服务</router-link></el-button>
    </el-button-group>
    
    <div class="contentWrap">
      <div style="flex: 3" class="showContent">
        <div class="contentTitle"><span>七天天气预报</span></div>
        <div style="position: relative;flex: 1;">
        <div style="display: flex;padding: 10px 0;justify-content: space-around;height: 100%;" class="contentGraph">
          <div style="width: 40px;background: #555b5e;color: #fff;display: flex;flex-direction: column;font-size: 13px;align-items: center;padding: 10px 0 0 0" v-for="(item, index) in preCastOptions" :key="index">
            <span class="w-info">{{item.day}}</span>
            <span class="w-info">{{item.date}}</span>
            <el-tooltip :content="weahterText(item.weather)" placement="top">
            <img class="w-info" style="height: 32px;cursor: pointer" :src="item.url" />
            </el-tooltip>
            <span class="w-info">{{item.temp}}</span>
          </div>
        </div>
        <div style="position: absolute;width: 100%;height: 50%;bottom: 5px;" ref="weekWeather"></div>
        </div>
      </div>
      <div style="flex: 5" class="showContent">
        <div class="contentTitle"><span>农业气象灾害预警</span></div>
        <div class="contentGraph">
          <div class="contentItem">
            <div style="padding: 10px;color: #fff;">作物选择：</div>
            <el-select style="margin: 0 10px;width: 260px" size="mini" v-model="cropValue" placeholder="请选择">
              <el-option v-for="item in cropOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
            </el-select>
          </div>

          <div class="contentItem">
            <div style="padding: 10px;color: #fff">农业灾害天气指标：</div>
            <div style="display: flex; align-items: center;">
              <el-select style="margin: 0 10px;width: 260px" size="mini" v-model="weatherValue" placeholder="请选择">
                <el-option v-for="item in classOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
              </el-select>
              <!-- 条件设置 -->
              <el-popover
                v-if="conditionModel"
                popper-class="condtionBox"
                placement="right"
                width="240"
                title="设置筛选条件"
                trigger="click">
                <div v-if="weatherValue=='qiujidiwen'" class="block">
                  <div class="demonstration">日平均气温(℃)</div>
                  <el-input size="mini" v-model="conditionModel.qiujidiwenThreshold" type="number"><template slot="prepend">﹤</template></el-input>
                  <div class="demonstration">连续天数(天)</div>
                  <el-input size="mini" v-model="conditionModel.qiujidiwenStep" type="number" min="0"><template slot="prepend">≥</template></el-input>
                </div>
                <div v-if="weatherValue=='fenniejianhuan'" class="block">
                  <div class="demonstration">日最高气温(℃)</div>
                  <el-input size="mini" v-model="conditionModel.feiniejianhuanThreshold" type="number"><template slot="prepend">﹤</template></el-input>
                  <div class="demonstration">出现天数(天)</div>
                  <el-input size="mini" v-model="conditionModel.feiniejianhuanStep" type="number" min="0"><template slot="prepend">＞</template></el-input>
                </div>
                <div v-if="weatherValue=='baoyu'" class="block">
                  <div class="demonstration">日雨量(mm)</div>
                  <el-input size="mini" v-model="conditionModel.baoyuThreshold" type="number" min="0"><template slot="prepend">≥</template></el-input>
                </div>
                <div v-if="weatherValue=='honglao'" class="block">
                  <div class="demonstration">日雨量(mm)</div>
                  <el-input size="mini" v-model="conditionModel.honglaoThreshold1" type="number" min="0"><template slot="prepend">≥</template></el-input>
                  <div class="demonstration">三日雨量(mm)</div>
                  <el-input size="mini" v-model="conditionModel.honglaoThreshold2" type="number" min="0"><template slot="prepend">≥</template></el-input>
                  <div class="demonstration">五日雨量(mm)</div>
                  <el-input size="mini" v-model="conditionModel.honglaoThreshold3" type="number" min="0"><template slot="prepend">≥</template></el-input>
                </div>
                <div v-if="weatherValue=='lianyinyu'" class="block">
                  <div class="demonstration">日雨量(mm)</div>
                  <el-input size="mini" v-model="conditionModel.lianyinyuThreshold" type="number" min="0"><template slot="prepend">＞</template></el-input>
                  <div class="demonstration">连续天数(天)</div>
                  <el-input size="mini" v-model="conditionModel.lianyinyuStep" type="number" min="0"><template slot="prepend">≥</template></el-input>
                </div>
                <div v-if="weatherValue=='lanyang'" class="block">
                  <div class="demonstration">日平均气温(℃)</div>
                  <el-input size="mini" v-model="conditionModel.lanyangTeThreshold" type="number"><template slot="prepend">﹤</template></el-input>
                  <div class="demonstration">日雨量(mm)</div>
                  <el-input size="mini" v-model="conditionModel.lanyangPrThreshold" type="number" min="0"><template slot="prepend">＞</template></el-input>
                  <div class="demonstration">持续时间(天)</div>
                  <el-input size="mini" v-model="conditionModel.lanyangStep" type="number" min="0"><template slot="prepend">≥</template></el-input>
                </div>
                <div class="block">
                  <el-button :loading="loading" @click="setConfig" style="padding: 5px 12px;width: 100%;background:transparent;border: 1px solid #17cdbd;color:#fff" size="small" round>确定</el-button>
                </div>
                <i slot="reference" style="font-size: 22px;color: #ccc;cursor: pointer;margin-left: 15px" class="el-icon-setting"></i>
              </el-popover>
            </div>
            
          </div>

          <div class="contentItem">
            <div style="padding: 10px;color: #fff;">选择农业气象灾害类型：</div>
            <div class="checkboxPart" style="display: flex;flex-wrap: wrap;">
              <el-checkbox-group v-model="checkboxGroupValue" size="small">
                <el-checkbox
                  v-for="(item, index) in classOptions"
                  :style="{ 'margin-left': index==0 ? '10px': ''}"
                  :key="index" style="margin-bottom: 10px;min-width: 120px"
                  :label="item.value"
                  :disabled="layerGroupValue.indexOf(item.value) == -1"
                  border>
                  {{item.label}}
                </el-checkbox>
              </el-checkbox-group>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import mapboxgl from 'mapbox-gl';
import {weekWeatherOption} from './echartsConfig';
import { mapState } from 'vuex';
import {weahterMaps} from '@/config/area'
import {gengeratePlayTimes} from '@/utils/index';
import echarts from 'echarts';
import {getStationInfo, getWeatherImage, queryWeather,getInfo,getSatus,setWeatherConfig} from '@/api/meteorologicalService'
export default {
  data() {
    return {
      value: true,
      loading: false,
      snapPopup: null,
      checked: true,
      rangeValue: [3, 5],
      preCastOptions: [],
      checkboxGroupValue: [],
      layerGroupValue: [],
      conditionModel: null,
      classOptions: [{
        value: 'lanyang',
        label: '烂秧'
      },{
        value: 'fenniejianhuan',
        label: '分蘖减缓'
      },{
        value: 'qiujidiwen',
        label: '秋季低温'
      },{
        value: 'lianyinyu',
        label: '连阴雨'
      },{
        value: 'baoyu',
        label: '暴雨'
      },{
        value: 'honglao',
        label: '洪涝'
      }],
      cropOptions: [{
        value: '1',
        label: '水稻'
      }],
      cropValue: '1',
      weatherValue: 'lanyang',
    }
  },
  computed: {
    ...mapState('app', ['sysMap']),
    showLayers() {
      return this.$store.state.map.showLayers
    },
    marks () {
      return {
        [this.rangeValue[0]]: this.rangeValue[0]+'',
        [this.rangeValue[1]]: this.rangeValue[1]+''
      }
    }
  },
  watch: {
    sysMap: {
      handler(value, oldValue) {
        value && !oldValue && this.bindShowSnapShortOnMouseMove();
      },
      immediate: true
    },
    weatherValue: {
      handler(value, oldValue) {
        value && this.getDefaultValue(value);
      },
      immediate: true
    },
    checkboxGroupValue: {
      handler(value, oldValue) {
        value && this.setImageLayer();
      },
      immediate: true
    }
  },
  methods: {
    weahterText(index) {
      return weahterMaps[index];
    },
    setConfig() {
      this.loading = true;
      setWeatherConfig({type: this.weatherValue, param: this.conditionModel})
        .then(res => {
          if (res.returnCode == 0) {
            this.$message({ type: 'success', message: '灾害天气指标设置成功!' });
          } else {
            this.$message({ type: 'error', message: '灾害天气指标设置失败!' });
          }
        })
        .catch(err => this.$message({ type: 'error', message: '灾害天气指标设置失败!' }))
        .finally(res => this.loading = false);
    },
    getDefaultValue (param) {
      this.conditionModel = null;
      getInfo({param})
        .then(res => {
          this.conditionModel = res.data;
        });
    },
    setImageLayer() {
      this.clearWeatherImamgeLayers();
      if (!this.checkboxGroupValue.length) return;
      const allPromies = this.checkboxGroupValue.map(item => getInfo({param: item}));
      Promise.all(allPromies)
        .then(res => {
          const urls = res.map((item,index) => ({url: item.data.url, id: this.checkboxGroupValue[index]}));
          const imageLayers = this.createImageLayersConfig(urls);
          imageLayers.forEach(layerConfig => this.sysMap.addLayer(layerConfig, '嘉兴_气象站点'));
        });
    },
    clearWeatherImamgeLayers() {
      const removeLayers = this.sysMap.getStyle().layers.filter(item => item.metadata && item.metadata.imageType === 'disasterImage');
      removeLayers.forEach(item => {
        this.sysMap.removeLayer(item.id);
        this.sysMap.removeSource(item.source)
      });
    },
    createImageLayersConfig(urls) {
      const imageExtend = [[120.25, 31.05],[121.30, 31.05],[121.30, 30.25],[120.25, 30.25]];
      return urls.map((item, index) => {
        return {
          'id': item.id,
          'metadata': {'imageType': 'disasterImage'},
          'type': 'raster',
          'source': { 'type': 'image',"url": `${process.env.VUE_APP_BASE_API+item.url}`,"coordinates": imageExtend}
        }
      });
    },
    showStationInfo(e) {
      this.snapPopup && this.snapPopup.remove();
      const coordinates = e.features[0].geometry.coordinates.slice();
      const times = gengeratePlayTimes();
      const params = {stationName: e.features[0].properties.StationNam, time: this.showLayers[0] || times[times.length - 1]['paramTime']};
      getStationInfo(params)
        .then(res => {
          let properties = res.data || {humidity:null, temperature: null, press: null, wind: null};
          const obj = [
              {title: '湿度', value: properties.humidity || '缺省'},
              {title: '温度', value: properties.temperature || '缺省'},
              {title: '气压', value: properties.press || '缺省'},
              {title: '风速', value: properties.wind || '缺省'},
          ];

          let html = '<div>'
          html += `<div style="font-weight: bold; padding-bottom: 2px;border-bottom:1px dotted #ccc">${params.stationName}</div>`;
          obj.forEach(item => {
              html += `<div style="display: flex;padding: 2px 5px"><div style="width: 70px;font-weight: bold">${item.title}: </div><div style="flex: 1">${item.value}</div></div>`
          });
          html += '</div>'
          
          this.snapPopup.setLngLat(coordinates).setHTML(html).addTo(this.sysMap);
        });
    },
    bindShowSnapShortOnMouseMove () {
        this.sysMap.on('mouseenter', '嘉兴_气象站点', () => {
          this.sysMap.getCanvas().style.cursor = 'pointer'
        });
        this.sysMap.on('mouseleave', '嘉兴_气象站点', () => {
          this.sysMap.getCanvas().style.cursor = ''
        });
        this.snapPopup = new mapboxgl.Popup({closeButton: true,closeOnClick: false})

        this.sysMap.on('click', '嘉兴_气象站点', this.showStationInfo);
    },
    getweekday(date){
      var weekArray = new Array("日", "一", "二", "三", "四", "五", "六");
      var week = weekArray[new Date(date).getDay()];
      return week;
    },
    transformToWeatherLists(weatherLists) {
      const temp = Object.entries(weatherLists).map(([key, value]) => ({time: key, ...value})).sort((a, b) => new Date(a.time).getTime() - new Date(b.time).getTime());
      return temp.map(item => ({
        longDate: item.time,
        day: this.getweekday(item.time),
        date: item.time.substr(5),
        weather: item.Weather,
        url: require(`../../assets/weather/${item.Weather}.png`),
        max: Math.round(item.Max_temperature),
        min: Math.round(item.Min_temperature),
        temp: `${Math.round(item.Min_temperature)}~${Math.round(item.Max_temperature)}`
      }));
    }
  },
  mounted() {
    // 设置播放图层;
    this.$store.dispatch('map/changePlayLayers', {key: 'playLayers', value: []});
    this.$store.dispatch('map/changePlayYears', {key: 'playerYears', value: []});
    // 获取预报数据;
    const weekWeather = echarts.init(this.$refs.weekWeather);
    queryWeather()
      .then(res => {
        this.preCastOptions = this.transformToWeatherLists(res.data);
        const source = this.preCastOptions.map(item => ({
          '时间': item.longDate,
          '低温': item.min,
          '高温': item.max,
        }));
        weekWeatherOption.dataset.source = source;
        weekWeather.setOption(weekWeatherOption);
      });
      // 获得默认值;
      getSatus()
        .then(res => {
          this.checkboxGroupValue = Object.keys(res.data).filter(item => res.data[item]);
          this.layerGroupValue = Object.keys(res.data).filter(item => res.data[item]);
          this.$store.dispatch('app/setInfoWeathers', this.checkboxGroupValue);
        })
  },
  destroyed() {
    this.snapPopup && this.snapPopup.remove();
    this.sysMap.off('click', '嘉兴_气象站点', this.showStationInfo);
    this.sysMap.off('mouseenter', '嘉兴_气象站点');
    this.sysMap.off('mouseleave', '嘉兴_气象站点');
    this.clearWeatherImamgeLayers();
  }
}
</script>

<style scoped>
.contentWrap {
   flex: 1;
   display: flex;
   margin-top: 10px;
   flex-direction: column;
}
.showContent {
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

.grid-content {
  height: 60px;
  display: flex;
  position: relative;
  justify-content: center;
  align-items: center;
  border: 1px solid #17cdbd;
  color: #17cdbd;
  cursor: pointer;
}
.gap-row {
  margin-top: 10px
}
.isActive {
  background: #17cdbd;
  color: #ffffff
}
.demonstration {
  padding: 5px 0
}
</style>
<style>
.w-info{
  padding: 6px 0
}

.condtionBox {
  padding: 0;
  left: 335px!important;
  background-color: #383d41;
  color: #ffffff;
  border: none
}
.condtionBox .el-input__inner {
  padding: 0 0 0 10px;
}
.condtionBox .block {
  padding: 10px 20px
}
.condtionBox .block .el-slider__marks-text{
  color: #ffffff
}
.condtionBox .el-popover__title{
  background: #2dafa3;
  height: 40px;
  line-height: 40px;
  padding-left: 10px;
  color: #ffffff
}

.condtionBox .popper__arrow {
  border-right-color: #383d41!important;
}

.condtionBox .popper__arrow::after {
  border-right-color: #383d41!important;
}
.condtionBox .el-slider__bar {
  background-color: #17cdbd!important;
}
.condtionBox .el-slider__button {
  border-color: #17cdbd!important;
}

.checkboxPart .el-checkbox.is-bordered.is-checked {
  border-color: #17cdbd;
}
.checkboxPart .el-checkbox__input.is-checked+.el-checkbox__label {
  color: #17cdbd;
}
.checkboxPart .el-checkbox.is-bordered.el-checkbox--small {
    padding: 15px 15px 5px 15px;
    border-radius: 3px;
    height: 50px;
}
.checkboxPart .el-checkbox__input.is-checked .el-checkbox__inner, .el-checkbox__input.is-indeterminate .el-checkbox__inner {
    background-color: #17cdbd;
    border-color: #409EFF;
}
</style>
