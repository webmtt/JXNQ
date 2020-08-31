/**
 * 静态类的基类
 */
export default class Static {
    /**
     * 构造函数
     * @return {undefined}
     */
    constructor() {
        throw new Error(`静态类${this.constructor.name}不可实例化。`);
    }
}