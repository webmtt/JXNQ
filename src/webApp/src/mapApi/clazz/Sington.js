import BaseClass from './BaseClass';

export default class Sington extends BaseClass {
    /**
     * 单例基类构造函数;
     * @param {*} options 
     */
    constructor(options) {
        super(options);
    }

    /**
     * 销毁单例实例对象
     * @return {undefined}
     */
    destroy() {
        this.constructor.instance.delete(this.constructor.name);
    }

    /**
     * 获取单例类实例;
     * @param {*} options 
     */
    static getInstance(options) {
        if (!this.instance) {
            this.instance = new Map();
            this.instance.set(this.name, new this(options));
        }
        
        return this.instance.get(this.name);
    }
}