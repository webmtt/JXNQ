package com.htht.cn.jiaxing.service.impl;

import com.alibaba.fastjson.JSON;
import com.htht.cn.jiaxing.mapper.ImageInfoMapper;
import com.htht.cn.jiaxing.mapper.WeatherDataMapper;
import com.htht.cn.jiaxing.model.WeatherVO;
import com.htht.cn.jiaxing.model.dto.ImageInfoSDTO;
import com.htht.cn.jiaxing.model.dto.WeatherDataDTO;
import com.htht.cn.jiaxing.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    ImageInfoMapper imageInfoMapper;


    @Autowired
    WeatherDataMapper weatherDataMapper;

    @Override
    public WeatherVO queryByNameTime(String stationName, String time) {
        WeatherDataDTO weatherDataDTO = this.weatherDataMapper.selectByNameAndTime(stationName, time);
        if (weatherDataDTO != null) {
            WeatherVO weatherVO = new WeatherVO();
            weatherVO.setStationName(stationName);
            weatherVO.setHumidity(weatherDataDTO.getHumidity());
            weatherVO.setWind(weatherDataDTO.getWind());
            weatherVO.setTemperature(weatherDataDTO.getTemp());
            weatherVO.setRain(weatherDataDTO.getRain());
            return weatherVO;
        } else {
            return null;
        }
    }

    public String showPicture(String attr, String time) {
        ImageInfoSDTO imageInfoSDTO = this.imageInfoMapper.selectByAttrTime(attr, time);
        return null == imageInfoSDTO ? "" : imageInfoSDTO.getImagePath();
    }


}
