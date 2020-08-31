package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.dto.WeatherDataDTO;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface WeatherDataMapper {
    @Select({"select t.rain,t.humidity,t.temp,t.wind from weather_data t where t.stationName=#{name} and t.time like '${time}%' order by t.createdDate desc limit 1"})
    WeatherDataDTO selectByNameAndTime(@Param("name") String var1, @Param("time") String var2);

    @Insert({"<script>INSERT INTO weather_data (rain, humidity, temp, wind, stationName, time) VALUES (#{weatherDataDTO.rain},  #{weatherDataDTO.humidity}, #{weatherDataDTO.temp}, #{weatherDataDTO.wind}, #{weatherDataDTO.stationName}, #{weatherDataDTO.time})</script>"})
    int insertOne(@Param("weatherDataDTO") WeatherDataDTO var1);

    @Insert({"<script>" +
            "INSERT INTO weather_data (rain, humidity, temp, wind, stationName, time) VALUES " +
            "<foreach item = 'weatherDataDTO'  collection = 'weatherDataDTOs' separator=',' >" +
            " (#{weatherDataDTO.rain},  #{weatherDataDTO.humidity}, #{weatherDataDTO.temp}, #{weatherDataDTO.wind}, #{weatherDataDTO.stationName}, #{weatherDataDTO.time})" +
            "  </foreach> " +
            "</script>"})
    int insertBatch(@Param("weatherDataDTOs") List<WeatherDataDTO> weatherDataDTOs);

    @Delete("delete from weather_data where createdDate<=#{date}")
    void deleteMonthAgo(@Param("date") Date date);
}
