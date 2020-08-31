import ZoomTool from './ZoomTool';

export default class ZoomOutTool extends ZoomTool {
    constructor(options) {
        super(options);

        // 地图缩放工具
        this.name = 'ZoomOutTool';

        // 缩放的方向
        this.flag = false;
    }

    /**
     * 点击地图级别缩小的监听函数;
     * @method onclick;
     */
    onClick() {
        if (!this.running) {
            return;
        };

        this.zoomOut()
    }

    /**
     * @method _bindEvent
     */
    _bindEvent() {
        this.target.onclick = this.onClick
    }

    _unbindEvents() {
        this.target.onclick = null;
    }
}