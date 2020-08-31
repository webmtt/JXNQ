package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.dto.PlantCoverRateDTO;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlantCoverRateMapper {

    @Select("select t.id,t.year,t.season,t.rate,t.crop from plant_cover_rate t")
    List<PlantCoverRateDTO> selectList();
}
