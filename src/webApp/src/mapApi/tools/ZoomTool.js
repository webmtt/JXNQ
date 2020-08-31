import Tool from './Tool';

export default class ZoomTool extends Tool {
    constructor(options) {
        super(options);

        // 地图缩放工具基类名称;
        this.name = 'ZoomTool';

        // 缩放的步长;
        this.step = 1;

        // 当前的缩放级别;
        this.level = 0;

        // 地图的最大缩放级别;
        this.maxLevel = 1;

        // 地图的最小缩放级别;
        this.minLevel = 0;

        // 缩放的方向,默认为正方向;
        this.flag = true;
    }

    /**
     * 启动工具，绑定工具依赖的监听对象;
     * @method _start;
     */
    active() {
        super.active();

        // 特殊处理
        this.onClick();
        
        return true;
    }

    /**
     * 关闭工具;
     * @method active;
     */
    deActive() {
        super.deActive();

        return true;
    }

    /**
     * 获得合法的缩放级别;
     * @method getValidateZoom;
     */
    getValidateZoom() {
        let currentZoom = this.map.getZoom();
        let maxZoom = this.map.getMaxZoom();
        let minZoom = this.map.getMinZoom();

        let zoom = this.flag ? currentZoom + this.step : currentZoom - this.step;
        if (this.flag) {
            zoom = zoom > maxZoom ? maxZoom : zoom;
        } else {
            zoom = zoom < minZoom ? minZoom : zoom;
        }

        return zoom;
    }

    /**
     * 地图级别放大方法;
     * @method zoomIn
     */
    zoomIn() {
        let zoom = this.getValidateZoom();

        this.map.setZoom(zoom);

        return zoom;
    }

    /**
     * 地图级别缩小方法;
     * @method zoomIn
     */
    zoomOut() {
        let zoom = this.getValidateZoom();

        this.map.setZoom(zoom);

        return zoom;
    }
}