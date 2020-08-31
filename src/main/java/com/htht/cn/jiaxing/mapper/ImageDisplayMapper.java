package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.ImageDisplayVO;
import com.htht.cn.jiaxing.model.ImageInfoModel;
import com.htht.cn.jiaxing.model.dto.ImageDisplayDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ImageDisplayMapper {

    @Select("<script>" +
            "select t.id, t.guide,t.imageAnalysis,t.imageDesc,t.imageSrc " +
            "from image_display_info t where type=#{plantCover} and imageName = #{imageName}" +
            " limit 1" +
            "</script>")
    ImageDisplayVO imageQuery(@Param("imageName") String imageName, @Param("plantCover") String plantCover);

    @Update("<script>" +
            "update image_display_info set updatedDate = now() " +
            "<if test='imageInfoModel.guide != null and imageInfoModel.guide != \"\"'>, guide = #{imageInfoModel.guide}</if>" +
            "<if test='imageInfoModel.imageAnalysis != null and imageInfoModel.guide != \"\"'>, imageAnalysis = #{imageInfoModel.imageAnalysis}</if>" +
            "<if test='imageInfoModel.imageDesc != null and imageInfoModel.imageDesc != \"\"'>, imageDesc = #{imageInfoModel.imageDesc}</if>" +
            "<if test='imageInfoModel.imageSrc != null and imageInfoModel.imageSrc != \"\"'>, imageSrc = #{imageInfoModel.imageSrc}</if>" +
            " where id = #{imageInfoModel.id}" +
            "</script>")
    int updateImageInfo(@Param("imageInfoModel") ImageInfoModel imageInfoModel);

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("INSERT INTO `jxnq`.`image_display_info` (   `imageDesc`, `imageSrc`, `imageAnalysis`, `guide`, `createdDate`,type,imageName) " +
            "VALUES ( #{growMonitorDTO.imageDesc}, " +
            "#{growMonitorDTO.imageSrc}, #{growMonitorDTO.imageAnalysis}, #{growMonitorDTO.guide}, #{growMonitorDTO.createdDate},#{growMonitorDTO.type},#{growMonitorDTO.imageName})")
    int imageInsert(@Param("growMonitorDTO") ImageDisplayDTO growMonitorDTO);
}
