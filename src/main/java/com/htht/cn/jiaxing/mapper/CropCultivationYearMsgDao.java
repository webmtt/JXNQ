package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.CropCultivationYearMsg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface CropCultivationYearMsgDao {

    List<CropCultivationYearMsg> query(@Param("district")String district, @Param("year")String year, @Param("cropBreeds")String cropBreeds);

    List<CropCultivationYearMsg> getCropByDistrict(@Param("district")String district, @Param("year")String year, @Param("cropBreeds")String cropBreeds);

    List<CropCultivationYearMsg> cropLineChart(@Param("district")String district, @Param("maxYear")int maxYear,@Param("minYear")int minYear, @Param("cropBreeds")String cropBreeds);

    Float querySumByDistrict(@Param("district")String district, @Param("year")String year,@Param("cropBreeds")String cropBreeds);
}
