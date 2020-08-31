package com.htht.cn.jiaxing.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: gpsDataDump
 * @description: 气象服务参数配置类
 * @author: lixulei
 * @create: 2020-04-07 15:09
 **/

@Component
@ConfigurationProperties(prefix = "weather-param")
public class WeatherParamConfiguration {

    private Map<String,Map<String,String>> paramMaps = new HashMap<>();

    public Map<String, Map<String, String>> getParamMaps() {
        return paramMaps;
    }

    public void setParamMaps(Map<String, Map<String, String>> paramMaps) {
        this.paramMaps = paramMaps;
    }
}
