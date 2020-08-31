import BaseClass from '../clazz/BaseClass';
import * as turf from '@turf/turf';

export default class Tool extends BaseClass {
    constructor(options) {
        super(options);
        // 工具名称
        this.name = this.constructor.name;
        // 工具函数;
        this.turf = turf;
        // 地图对象;
        this.map = null;
        // 工具绑定的dom/map对象;
        this.target = null;
        // 工具控制器;
        this.toolController = null;
        // 
        this.running = false;
    }

    setTarget(target) {
        this.target = target;
    }

    setMap(map) {
        this.map = map;
        // this._bindEvent();
    }

    /**
     * 设置当前工具的控制器;
     * @param {*} controller 
     */
    setToolController(controller) {
        this.toolController = controller;
    }

    /**
     * 依赖对象事件绑定;需要子类继承实现;
     * @method _bindEvent
     */
    _bindEvent() {
        throw new Error('该方法需要子类重写');
    }

     /**
     * 依赖对象事件绑定;需要子类继承实现;
     * @method _bindEvent
     */
    _unbindEvents() {
        throw new Error('该方法需要子类重写');
    }

    /**
     * 激活工具;
     * @method active;
     */
    active() {
        this.running = true;

        this._bindEvent();


        return true;
    }

    /**
     * 关闭工具;
     * @method active;
     */
    deActive() {
        this.running = false;

        // 解除事件绑定;
        this._unbindEvents();
    }
}