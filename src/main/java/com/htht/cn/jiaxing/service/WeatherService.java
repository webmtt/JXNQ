package com.htht.cn.jiaxing.service;

import com.htht.cn.jiaxing.model.WeatherVO;
import com.htht.cn.jiaxing.model.dto.CropCultivationDTO;

import java.util.List;

public interface WeatherService {

    String showPicture(String attr, String time);

    WeatherVO queryByNameTime(String stationName, String time);
}
