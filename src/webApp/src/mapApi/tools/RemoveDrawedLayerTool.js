import Tool from './Tool';

export default class RemoveDrawedLayerTool extends Tool {
    constructor(options) {
        super(options);

        // 地图缩放工具
        this.name = 'RemoveDrawedLayerTool';
    }

    /**
     * 点击地图级别放大的监听函数;
     * @method onclick;
     */
    onClick() {
        if (!this.running) {
            return;
        }
        this.removeMeasureLayers()
    }

    removeMeasureLayers() {
        console.log('removeMeasureLayers');
        this.map.getLayer('measure-points') && this.map.removeLayer('measure-points');
        this.map.getLayer('drawing-lines') && this.map.removeLayer('drawing-lines');
        this.map.getLayer('drawed-lines') && this.map.removeLayer('drawed-lines');

        this.map.getLayer('measure-points') && this.map.removeLayer('measure-points');
        this.map.getLayer('drawing-lines') && this.map.removeLayer('drawing-lines');
        this.map.getLayer('drawed-lines') && this.map.removeLayer('drawed-lines');
        
        this.map.getSource('geojson') && this.map.removeSource('geojson');
    }

    active() {
        super.active();

        // 特殊处理
        this.onClick();
        
        return true;
    }

    /**
     * @method _bindEvent
     */
    _bindEvent() {
        this.target.onclick = this.onClick;
    }

    _unbindEvents() {
        this.target.onclick = null;
    }
}