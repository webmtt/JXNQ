import Util from '../../utils/CommonTool';

export default class BaseClass {
    /**
     * 构造函数
     * @param  {object}    options 可选参数
     * @return {undefined}
     */
    constructor(options) {
        // 为类所有的方法绑定this;
        Util.bindAll(this);

        this.setOptions(options);
    }

    /**
     * 设置可选参数
     * @param  {object}    options 可选参数
     * @return {undefined}
     */
    setOptions(options) {
        if (options) {
            this.options = Util.merge(this.options || {}, options);
        }
    }
}