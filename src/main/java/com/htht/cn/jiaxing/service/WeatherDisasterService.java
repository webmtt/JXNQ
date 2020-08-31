package com.htht.cn.jiaxing.service;

import com.htht.cn.jiaxing.model.WeatherDisasterParameter;

import java.util.Map;

public interface WeatherDisasterService {

    String getParams(String type);

    int insert(WeatherDisasterParameter parameter);

}
