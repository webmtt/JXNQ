package com.htht.cn.jiaxing.service.impl;

import com.htht.cn.jiaxing.mapper.WeatherDisasterDao;
import com.htht.cn.jiaxing.model.WeatherDisasterParameter;
import com.htht.cn.jiaxing.service.WeatherDisasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WeatherDisasterServiceImpl implements WeatherDisasterService {

    @Autowired
    private WeatherDisasterDao weatherDisasterDao;

    @Override
    public String getParams(String type) {
        return weatherDisasterDao.getParams(type);
    }

    @Override
    public int insert(WeatherDisasterParameter parameter) {
        return weatherDisasterDao.insert(parameter);
    }
}
