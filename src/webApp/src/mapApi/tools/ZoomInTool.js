import ZoomTool from './ZoomTool';

export default class ZoomInTool extends ZoomTool {
    constructor(options) {
        super(options);

        // 地图缩放工具
        this.name = 'ZoomInTool';

        // 缩放的方向
        this.flag = true;
    }

    /**
     * 点击地图级别放大的监听函数;
     * @method onclick;
     */
    onClick() {
        if (!this.running) {
            return;
        }
        this.zoomIn()
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