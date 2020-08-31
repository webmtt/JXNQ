package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.dto.StationInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StationInfoMapper {
    @Select("select stationID,stationName,lat,lon from station_info")
    List<StationInfoDTO> getStationLists();
}
