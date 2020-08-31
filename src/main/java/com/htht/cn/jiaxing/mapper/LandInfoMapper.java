package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.LandBlockModel;
import com.htht.cn.jiaxing.model.dto.FarmerLandRelationDTO;
import com.htht.cn.jiaxing.model.dto.LandInfoDTO;
import com.htht.cn.jiaxing.model.dto.OfficialAccountUserDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LandInfoMapper {

     @Insert("<script>" +
                "insert into land_info(id,binder,crop,address,area,district,city) VALUES(#{lands.id},#{lands.binder},#{lands.crop},#{lands.address},#{lands.area},#{lands.district},#{lands.city})" +
                "</script>")
        void insertOne(@Param("lands") LandInfoDTO lands);

    @Update("<script>" +
            "update land_info set updatedDate=now()" +
            "<if test='landInfoDTO.binder !=null and landInfoDTO.binder !=\"\"'>, binder=#{landInfoDTO.binder} </if>" +
            "<if test='landInfoDTO.crop !=null and landInfoDTO.crop !=\"\"'>,crop=#{landInfoDTO.crop} </if>" +
            "<if test='landInfoDTO.address !=null and landInfoDTO.address !=\"\"'>,address=#{landInfoDTO.address} </if>" +
            "<if test='landInfoDTO.city !=null and landInfoDTO.city !=\"\"'>,city=#{landInfoDTO.city} </if>" +
            "<if test='landInfoDTO.district !=null and landInfoDTO.district !=\"\"'>,district=#{landInfoDTO.district} </if>" +
            "<if test='landInfoDTO.area !=null and landInfoDTO.area !=\"\"'>,area=#{landInfoDTO.area} </if>" +
            " where id = #{landInfoDTO.id}" +
            "</script>")
    void update(@Param("landInfoDTO") LandInfoDTO landInfoDTO);

    @Select("select t.id,t.area, t.crop as crops,t.address,t.city,t.district from land_info t where t.binder = #{farmerId} order by t.createdDate desc")
    List<LandBlockModel> selectByFarmer(@Param("farmerId") Long farmerId);

    @Delete("delete from land_info where id = #{landId}")
    void delete(@Param("landId") String landId);

    @Select("select * from farmer_land_relation u,(select binder from land_info t where t.id=#{landId}) d "+
            "where u.farmerid = d.binder")
    FarmerLandRelationDTO selectFarmerByLandId(String landId);

}
