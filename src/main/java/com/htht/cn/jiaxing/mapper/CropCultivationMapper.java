package com.htht.cn.jiaxing.mapper;

import com.htht.cn.jiaxing.model.dto.CropCultivationDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface CropCultivationMapper {

	@Select("<script>" +
			"select id,district,year,crop_breeds as cropBreeds,CULTIVATION_AREA as cultivationArea,createdDate,creater from CROP_CULTIVATION_YEAR_MSG where district=#{district} and year = #{year}" +
			"<if test='cropBreeds != null'>AND crop_breeds = #{cropBreeds}</if>" +
			"</script>")
	List<CropCultivationDTO> getByDistrictAndYear(@Param("district")String district,@Param("year")int year,@Param("cropBreeds")String cropBreeds);


	@Insert("insert into CROP_CULTIVATION_YEAR_MSG(" +
			"district,year,crop_breeds,CULTIVATION_AREA ,createdDate,creater)" +
			"values(" +
			"#{cropCultivationDTO.district},#{cropCultivationDTO.year},#{cropCultivationDTO.cropBreeds},#{cropCultivationDTO.cultivationArea},#{cropCultivationDTO.createdDate},#{cropCultivationDTO.creater}" +
			")")
	int insertOne(@Param("cropCultivationDTO") CropCultivationDTO cropCultivationDTO);


	@Insert("<script>" +
			"insert into CROP_CULTIVATION_YEAR_MSG(" +
			"district,year,crop_breeds,CULTIVATION_AREA ,createdDate,creater)" +
			"values" +
			"<foreach item = 'cropCultivationDTO' index = 'index' collection = 'crops' separator=',' >" +
			" (#{cropCultivationDTO.district},#{cropCultivationDTO.year},#{cropCultivationDTO.cropBreeds},#{cropCultivationDTO.cultivationArea},#{cropCultivationDTO.createdDate},#{cropCultivationDTO.creater})" +
			"  </foreach> " +
			"</script>")
	int insertMany(@Param("crops") List<CropCultivationDTO> crops);
}
