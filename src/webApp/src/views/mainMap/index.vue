<template>
  <div id="map">
    <time-player></time-player>
  </div>
</template>

<script>
import store from '@/store';
import mapboxgl from 'mapbox-gl';
import {deepClone} from '@/utils/index';
import timePlayer from '../../components/TimePlayer'
import {scenceConfigs, baseMaps} from '@/config/scence';
import ToolController from '@/mapApi/tools/ToolController';
export default {
  data() {
    return {
      currentScence: null,
      map: null,
      toolCtrl: null,
      specialScenceInfos: {}
    }
  },
  components: {timePlayer},
  computed: {
    showLayers() {
      return this.$store.state.map.showLayers
    },
    businessLayers() {
        return this.$store.state.map.businessLayers
    }
  },
  watch: {
      '$route.path'(oldValue, newValue) {
          this.changeScene(scenceConfigs[this.$route.path.slice(1)])
      },
      showLayers: {
          handler(newValue, oldValue) {
            if (!this.map) return;
            this.setActiveLayers(newValue, oldValue)
          },
          deep: true,
          immediate: true
      }
  },
  methods: {
    /**
     * 获取地图上加载的所有图层;
     * @method getAllLoadedMapLayers
     */
    getAllLoadedMapLayers () {
        return this.map.getStyle().layers || [];
    },

      /**
       * 根据传入的配置图层名称，得到对应的地图图层;
       * @method getAllLoadedMapLayers
       */
      getNeedShowMapLayers (layersParams) {
          let configLayers = [];
          let configMapLayers = [];
          layersParams.forEach(item => configLayers = configLayers.concat(item.layers || item));

          configLayers.forEach(item => {
              const pattern = new RegExp(item);
              this.getAllLoadedMapLayers().forEach(innerItem => pattern.test(innerItem.id) && configMapLayers.push(innerItem));
          });

          return configMapLayers;
      },
      clearTimer() {
          this.loadTimer && window.clearTimeout(this.loadTimer);
      },

      /**
       * 切换地图服务后，删除前一个服务所有的图层,不删除会导致图层很多降低效率;
       * @method removeCurrentScenceLayersFromMap
       */
      removeCurrentScenceLayersFromMap() {
          if (!this.currentScence) return;
          const allLScenceLayers = this.specialScenceInfos[this.currentScence.scenceName].layers;
          allLScenceLayers.forEach(item => this.map.getLayer(item.id) && this.map.removeLayer(item.id));
      },

      /**
       * 
       */
      resetScenceData (scence) {
          this.currentScence = deepClone(scence)
          this.currentMapServerUrl = this.currentScence.scenceUrl;
      },

      /**
       * 场景数据变化的执行的方法(切换地图服务)
       * @method changeScene
       */
      changeScene (scence) {
          if (!scence) return;
          if (this.currentMapServerUrl == scence.scenceUrl) return;

          this.removeCurrentScenceLayersFromMap();
          this.resetScenceData(scence);

          const sources = this.specialScenceInfos[scence.scenceName].sources;

          Object.keys(sources).forEach(item => !this.map.getSource(item) && this.map.addSource(item, sources[item]));
          this.specialScenceInfos[scence.scenceName].layers.forEach(item => !this.map.getLayer(item.id) && this.map.addLayer(item));
      },
      // 根据条件对图层进行控制;
      setActiveLayers(newLayers) {
          const showLayers = newLayers;
          const hiddeLayers = this.businessLayers.filter(item => showLayers.indexOf(item) === -1);
        
          showLayers.forEach(item => this.map.getLayer(item) && this.map.setLayoutProperty(item, 'visibility', 'visible'));
          hiddeLayers.forEach(item => this.map.getLayer(item) && this.map.setLayoutProperty(item, 'visibility', 'none'));
      },
      /**
       * 绑定其他组件传来的事件
       * @method bindEventFromComponents
       */
      bindEventFromSubComponents () {
          // 监听定位事件
          this.$bus.on('locationToPoint', e => this.map.flyTo({center: e.coordinates, zoom: e.zoom}));

          this.$bus.emit('fatherComLoaded');
      },

      /**
       * 绑定地图内部事件;
       * @method bindMapEvents
       */
      bindMapEvents () {
          this.map.on('load', () => {

            this.bindEventFromSubComponents()

            // 将工具控制器绑定到地图上;
            ToolController.getInstance().addToMap(this.map);
          });
      },
      /**
       * 地图初始化;
       * @method initMap
       */
      initMap() {
        this.map = new mapboxgl.Map({
            container: 'map',
            doubleClickZoom: false,
            center: [120.80, 30.67],
            zoom: 9.1,
            style: baseMaps.image
        });

        Promise.all(Object.keys(scenceConfigs).map(item => fetch(scenceConfigs[item].scenceUrl).then(res => res.json()).then(res => ({[item]: res}))))
        .then(res => {
            res.forEach(item => {
                const key = Object.keys(item)[0];
                const value = item[key];
                this.specialScenceInfos[key] = value;
            });
            this.changeScene(scenceConfigs[this.$route.path.slice(1)]);
            // 控制图层;
            this.setActiveLayers(this.showLayers)
        });

        store.dispatch('app/setSysMap', this.map);

        this.bindMapEvents();
      },
  },
  mounted () {
      console.log('map loading');
      this.initMap();
  },
  destroyed () {
      console.log('map die')
  }
}
</script>

<style scoped>
#map {
  width: 100%;
  height: 100%;
}
</style>

