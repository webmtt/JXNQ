import StaticClass from '../mapApi/clazz/Static';
import _array from 'lodash/array';
import _lang from 'lodash/lang';
import _util from 'lodash/util';
import _function from 'lodash/function';
import _object from 'lodash/fp/object';

export default class CommonTool extends StaticClass {
    /**
     * 
     * @param {*} object 
     * @param {*} source 
     */
    static merge(object, source) {
        return _object.merge(object, source);
    }

    static uniq(array) {
        return _array.uniq(array);
    }

    static isFunction(value) {
        return _lang.isFunction(value);
    }

    static bind(func, context) {
        return _function.bind(func, context);
    }

    static bindAll(object, methodNames) {
        let allMethodNames = methodNames;
        if (!allMethodNames) {
            allMethodNames = [];
            let target = object;
            let allPropertyNames = [];

            while (target) {
                const propertyNames = Object.getOwnPropertyNames(target);
                allPropertyNames = allPropertyNames.concat(propertyNames);

                target = Object.getPrototypeOf(target);
            }

            allPropertyNames.forEach(propertyName => {
                const property = object[propertyName];

                // 跳过非函数的属性
                if (!this.isFunction(property)) {
                    return;
                }

                allMethodNames.push(propertyName);
            });

            allPropertyNames = this.uniq(allPropertyNames);
        }

        return _util.bindAll(object, allMethodNames);
    }
}