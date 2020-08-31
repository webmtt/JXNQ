<template>
  <div class="toolView">

        <!-- 基础工具 -->
        <div class="tool base">
          <img @click="toggleBaseLayer()" style="width: 30px;margin: 5px;cursor: pointer" :src="toggleLayerImage[toggleLayerImage.active].url" />
          <img @click="startTool('ZoomInTool', $event)" ref="ZoomInTool" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/放大-静止.png`)" />
          <img @click="startTool('ZoomOutTool', $event)" ref="ZoomOutTool" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/缩小-静止.png`)" />
        </div>

        <!-- 测量工具 -->
        <div class="tool measure">
          <img v-for="(item, index) in measureOptions" :key="index" @click="startTool(item.name, $event)" :ref="item.name" @mousemove="item.url = 'activeUrl'" @mouseleave="item.url = 'defaultUrl'" style="width: 30px;margin: 5px;cursor: pointer" :src="item[item.url]" />
        </div>

        <!-- 稻卖油图层控制工具 -->
        <div v-if="['growManage','landManage'].indexOf($route.path.slice(1))>-1" class="tool layer">
          <img @click="handleClick('水稻')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/稻-${activeLayer.indexOf('水稻') > -1 ? '滑过' : '静止'}.png`)" />
          <img @click="handleClick('小麦')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/麦-${activeLayer.indexOf('小麦') > -1 ? '滑过' : '静止'}.png`)" />
          <img @click="handleClick('油菜')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/油-${activeLayer.indexOf('油菜') > -1 ? '滑过' : '静止'}.png`)" />
        </div>
        <!-- 其相管理图层控制工具 -->
        <div v-if="['meteorologicalService'].indexOf($route.path.slice(1))>-1" class="tool layer">
          <img @click="handleImageLayerClick('temp')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/低温-${activeLayer.indexOf('temp') > -1 ? '滑过' : '静止'}.png`)" />
          <img @click="handleImageLayerClick('humi')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/湿度-${activeLayer.indexOf('humi') > -1 ? '滑过' : '静止'}.png`)" />
          <img @click="handleImageLayerClick('rain')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/降水-${activeLayer.indexOf('rain') > -1 ? '滑过' : '静止'}.png`)" />
          <img @click="handleImageLayerClick('wind')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/风速-${activeLayer.indexOf('wind') > -1 ? '滑过' : '静止'}.png`)" />
        </div>
        <div v-if="['meteorologicalService'].indexOf($route.path.slice(1))>-1" class="tool layer">
          <img @click="handleTempLayerClick('水稻2019分布')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/稻-${activeTempLayer.indexOf('水稻2019分布') > -1 ? '滑过' : '静止'}.png`)" />
          <img @click="handleTempLayerClick('嘉兴_气象站点')" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/站-${activeTempLayer.indexOf('嘉兴_气象站点') > -1 ? '滑过' : '静止'}.png`)" />
          <img @click="handleTempLayerClick()" style="width: 30px;margin: 5px;cursor: pointer" :src="require(`../../assets/images/棚-滑过.png`)" />
        </div>

        <!-- 气象页面图例 -->
        <div style="position: absolute;bottom: 0;" class="tool layer">
          <img v-if="['landManage'].indexOf($route.path.slice(1))>-1" style="width: 30px;margin-bottom: 5px;" :src="require(`../../assets/images/lengend/作物图例.png`)" />
          <img v-if="['growManage'].indexOf($route.path.slice(1))>-1" style="width: 30px;margin-bottom: 5px;" :src="require(`../../assets/images/lengend/长势监测图例.png`)" />
          <img v-if="['vegetationManage'].indexOf($route.path.slice(1))>-1" style="width: 30px;margin-bottom: 5px;" :src="require(`../../assets/images/lengend/植被覆盖度图例.png`)" />
          <img v-if="['meteorologicalService'].indexOf($route.path.slice(1))>-1 && activeLayer.length" style="width: 30px;margin-bottom: 5px;" :src="qixinagLengendUrl" />
        </div>
  </div>
</template>

<script>
import { mapState } from 'vuex';
import * as turf from '@turf/turf';
import {deepClone, gengeratePlayTimes} from '@/utils/index';
import ToolController from '@/mapApi/tools/ToolController';
import {getWeatherImage} from '@/api/meteorologicalService'
import {baseMaps} from '@/config/scence';
export default {
  data() {
    return {
      run: false,
      toolCtrl: null,
      layer: ['水稻','油菜','小麦'],
      activeLayer: [],
      activeTempLayer: ['水稻2019分布', '嘉兴_气象站点'],
      measureOptions: [
        {name: 'MeasureDistanceTool', defaultUrl: require('../../assets/images/测距-静止.png'), activeUrl: require('../../assets/images/测距-滑过.png'), url: 'defaultUrl'},
        {name: 'MeasureAreaTool', defaultUrl: require('../../assets/images/测面积-静止.png'), activeUrl: require('../../assets/images/测面积-滑过.png'), url: 'defaultUrl'},
        {name: 'RemoveDrawedLayerTool', defaultUrl: require('../../assets/images/清除-静止.png'), activeUrl: require('../../assets/images/清除-滑过.png'), url: 'defaultUrl'}
      ],
      toggleLayerImage: {
        image: {url: require('../../assets/images/切换影像图.png'), styleUrl: 'ff8080816e688287016f17bd23cb046c'},
        street: {url: require('../../assets/images/切换行政图.png'), styleUrl: 'ff808081709f8a9b01709fa9ea740007'},
        active: 'image',
      },
      baseLayers: {},
      qixinagLengendUrl: '',
      lengendMaps: {
        temp: require('../../assets/images/lengend/温度图例.png'),
        humi: require('../../assets/images/lengend/湿度图例.png'),
        rain: require('../../assets/images/lengend/降水图例.png'),
        wind: require('../../assets/images/lengend/风速图例.png')
      }
    }
  },
  computed: {
    ...mapState('app', ['sysMap']),
    ...mapState('map', ['lengendLayers']),
  },
  watch: {
    lengendLayers: {
      handler(value) {
        this.activeLayer = this.layer.filter(item => value.indexOf(item) > -1);
      },
      immediate: true
    },

    '$route.path'(newValue) {
      if (newValue.slice(1) != 'meteorologicalService') {
        this.clearWeatherImamgeLayers();
      }
    }
  },
  mounted () {
    this.toolCtrl = ToolController.getInstance();
    this.toolCtrl.loadTools(this.$refs);
    
    this.initBaseLayers();
  },
  methods: {
    initBaseLayers() {
      Promise.all(Object.keys(baseMaps).map(item => fetch(baseMaps[item]).then(res => res.json()).then(res => ({[item]: res}))))
        .then(res => res.forEach(item => this.baseLayers[Object.keys(item)[0]] = item[Object.keys(item)[0]]));
    },
    hiddeLayers(name) {
      this.baseLayers[name].layers.forEach(item => this.sysMap.getLayer(item.id) && this.sysMap.setLayoutProperty(item.id, 'visibility', 'none'))
    },
    addSourceAndLayers(name) {
      Object.keys(this.baseLayers[name].sources).forEach(item => {
        if (!this.sysMap.getSource(item)) {
          this.sysMap.addSource(item, this.baseLayers[name].sources[item])
        }
      });
      this.baseLayers[name].layers.forEach(item => this.sysMap.getLayer(item.id) ? this.sysMap.setLayoutProperty(item.id, 'visibility', 'visible') : this.sysMap.addLayer(item, '高德影像'));
    },
    toggleBaseLayer() {
      this.toggleLayerImage.active = this.toggleLayerImage.active === 'image' ? 'street' : 'image';

      if (this.toggleLayerImage.active == 'image') {
        this.hiddeLayers('street');
        this.addSourceAndLayers('image');
      } else {
        this.hiddeLayers('image');
        this.addSourceAndLayers('street');
      }
    },
    handleClick(flag) {
      this.activeLayer = [flag];
      this.$store.dispatch('map/changeLengendLayers', {key: 'lengendLayers', value: this.activeLayer});
    },

    createImageLayersConfig(urls) {
      const imageExtend = [[120.295342, 31.030258],[121.274652, 31.030258],[121.274652, 30.262318],[120.295342, 30.262318]];
      return urls.map((item, index) => {
        return {
          'id': item.id,
          'metadata': {'imageType': 'weatherImage'},
          'type': 'raster',
          'source': { 'type': 'image',"url": `${process.env.VUE_APP_BASE_API+item.url}`,"coordinates": imageExtend},
          'layout': {'visibility': index == 0 ? 'visible': 'none'}
        }
      });
    },
    /**
     * 点击图像图层;
     * @method handleImageLayerClick
     */
    handleImageLayerClick(flag) {
      // 删掉之前加载的
      this.clearWeatherImamgeLayers();
      if (flag === this.activeLayer[0]) {
        this.activeLayer = [];
        // 设置播放图层;
        this.$store.dispatch('map/changePlayLayers', {key: 'playLayers', value: []});
        return;
      }
      this.qixinagLengendUrl = this.lengendMaps[flag];
      this.activeLayer = [flag];
      const times = gengeratePlayTimes();
      const allLayerParams = times.map(item => ({attr: flag, time: item.paramTime}));
      const layerPromises = allLayerParams.map(item => getWeatherImage(item));
      Promise.all(layerPromises)
        .then(res => {
          const imageUrls = res.map((item, index) => ({id: times[index]['paramTime'], url: item.returnMessage}));
          const imageLayers = this.createImageLayersConfig(imageUrls);
          imageLayers.forEach(layerConfig => this.sysMap.addLayer(layerConfig, '嘉兴_气象站点'));

          // 设置全部业务图层
          this.$store.dispatch('map/changeBusinessLayers', {key: 'businessLayers', value: imageUrls.map(item => item.id)});
          // 设置播放图层;
          this.$store.dispatch('map/changePlayLayers', {key: 'playLayers', value: imageUrls.map(item => item.id)});
        })
    },

    // 水稻2019分布
    handleTempLayerClick(flag) {
      if (!flag) return;
      const layer = this.sysMap.getStyle().layers.filter(item => item.id === flag);
      if (!layer.length) return;
      if (this.activeTempLayer.indexOf(flag) != -1) {
        this.sysMap.setLayoutProperty(flag, 'visibility', 'none')
        this.activeTempLayer = this.activeTempLayer.filter(item => item != flag)
      } else {
        this.sysMap.setLayoutProperty(flag, 'visibility', 'visible')
        this.activeTempLayer.push(flag)
      }
    },

    clearWeatherImamgeLayers() {
      const removeLayers = this.sysMap.getStyle().layers.filter(item => item.metadata && item.metadata.imageType === 'weatherImage');
      removeLayers.forEach(item => {
        this.sysMap.removeLayer(item.id);
        this.sysMap.removeSource(item.source)
      });
    },
    startTool(name, e) {
      this.toolCtrl.activeToolByToolName(name);
    }
  }
}
</script>

<style lang="scss" scoped>
.toolView {
    position: absolute;
    z-index: 100;
    width: 40px;
    height: 100%;
    background: #1e1f1ec2;
    right: 0;
    top: 0;
    padding: 0 5px;
    .tool {
        display: flex;
        width: 100%;
        flex-direction: column;
        justify-content: center;
        align-items: center;
    }
    .base, .measure {
      padding: 5px 0;
      border-bottom: 1px solid #ccc;
    }
    .layer {
      padding: 5px 0;
      border-bottom: 1px solid #ccc;
    }
}
</style>

