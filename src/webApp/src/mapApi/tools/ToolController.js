import Sington from '../clazz/Sington';
import ZoomInTool from './ZoomInTool';
import ZoomOutTool from './ZoomOutTool';
import MeasureAreaTool from './MeasureAreaTool';
import MeasureDistanceTool from './MeasureDistanceTool';
import RemoveDrawedLayerTool from './RemoveDrawedLayerTool';

export default class ToolController extends Sington {
    constructor(options) {
        super(options);

        // 地图对象;
        this.map = null;

        // 工具容器
        this.tools = {};

        // 当前工具;
        this.currentTool = null;

        // 当前工具的运行状态;
        this.running = false;
    }

    /**
     * 根据工具名称开启某个工具;
     * @method activeToolByToolName
     * @param {@} toolName 
     * @returns {Boolean}
     */
    activeToolByToolName(toolName) {
        if (!this.map) {
            throw new Error('工具控制类未注册到地图中!');
        }
        // 激活流程运行状态;
        this.continue();

        if (!this.tools.hasOwnProperty(toolName)) {
            return false;
        }

        const tool = this.tools[toolName];

        if (this.currentTool) {
            this.currentTool.deActive();
        }

        if (!tool.active()) {
            return false;
        }

        this.currentTool = tool;

        return true;
    }

    /**
     * 将该类实例挂在到地图上;
     * @param {*} map 
     */
    addToMap(map) {
        this.map = map;
        // 给所有的工具绑定地图实例
        Object.values(this.tools).forEach(element => element.setMap(this.map));
    }

    /**
     * 根据工具名称获取工具实例;
     * @param {*} name 
     */
    getToolByName(name) {
        if (!this.tools.hasOwnProperty(name)) {
            throw new Error(`${name}工具不存在!`);
        }
        return this.tools[name];
    }

    /**
     * 装载地图工具;
     * @method loadTools
     */
    loadTools(targets) {
        // 装配地图缩放工具;
        this.registerTool(new ZoomInTool());
        this.registerTool(new ZoomOutTool());
        // 装配面积测量工具;
        this.registerTool(new MeasureAreaTool());
        // 装配距离测量工具;
        this.registerTool(new MeasureDistanceTool());
        // 装配删除测量图层工具;
        this.registerTool(new RemoveDrawedLayerTool());

        // 为工具绑定target
        for (let [toolName, target] of Object.entries(targets)) {
            this.bindTarget(toolName, target);
        }
    }

    /**
     * 为工具绑定启动dom对象;
     * @param {*} toolName 
     * @param {*} target 
     */
    bindTarget(toolName, target) {
        const tool = this.getToolByName(toolName);
        tool.setTarget(target);
    }

    /**
     * 添加工具
     * @method registerTool
     */
    registerTool(tool) {
        if (this.tools.hasOwnProperty(tool.name)) {
            return;
        }

        tool.setToolController(this);
        this.tools[tool.name] = tool;
    }

    /**
     * 控制器运行状态激活
     * @method continue;
     */
    continue() {
        this.running = true;
    }

    /**
     * 控制器运行状态暂停
     * @method continue;
     */
    pause() {
        this.running = false;
    }
}