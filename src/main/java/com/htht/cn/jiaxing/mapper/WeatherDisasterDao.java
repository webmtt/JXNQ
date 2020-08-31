package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.WeatherDisasterParameter;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface WeatherDisasterDao {

    String getParams(String type);

    int insert(WeatherDisasterParameter parameter);

}
