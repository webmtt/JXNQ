package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.dto.ImageInfoSDTO;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ImageInfoMapper {
    @Select({"select image_path as imagePath from image_info t where t.attr=#{attr} and t.time=#{time} "})
    ImageInfoSDTO selectByAttrTime(@Param("attr") String var1, @Param("time") String var2);

    @Insert("<script>" +
            "insert into image_info(image_path,attr,time) " +
            "values" +
            "<foreach item = 'imageInfoSDTO'  collection = 'lists' separator=',' >" +
            " (#{imageInfoSDTO.imagePath},#{imageInfoSDTO.attr},#{imageInfoSDTO.time})" +
            "  </foreach> " +
            "</script>")
    int insertBath(@Param("lists")List<ImageInfoSDTO> lists);

    @Delete("delete from image_info where createdDate<=#{date}")
    void deleteMonthAgo(Date date);
}