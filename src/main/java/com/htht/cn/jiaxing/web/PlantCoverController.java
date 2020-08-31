package com.htht.cn.jiaxing.web;

import com.htht.cn.jiaxing.constant.Consts;
import com.htht.cn.jiaxing.model.CoverRateVO;
import com.htht.cn.jiaxing.model.ImageDisplayVO;
import com.htht.cn.jiaxing.model.ImageInfoModel;
import com.htht.cn.jiaxing.service.PlantCoverService;
import com.htht.cn.jiaxing.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Map;

/**
 * zw
 * 植被覆盖
 * */
@Slf4j
@RestController
@Api(description = "植被覆盖")
public class PlantCoverController extends BaseController {
	@Autowired
	PlantCoverService plantCoverService;


	@GetMapping("/plantCover/imageQuery")
	public Result imageQuery(
			@ApiParam(value = "图片名")@RequestParam(value = "imageName",required = false)String imageName
	){ ImageDisplayVO imageDisplayVO = plantCoverService.imageQuery(imageName);
		if(null == imageDisplayVO) {
			return failue(Consts.IMAGE_UNFIND,"图片未找到");
		}else {
			return success(imageDisplayVO);
		}
	}

	@PostMapping("/plantCover/infoInput")
	public Result input( @RequestBody ImageInfoModel imageInfoModel){
		log.info("start input image info:"+imageInfoModel);
		int i = plantCoverService.updateImageInfo(imageInfoModel);
		if(i ==1) {
			return success();
		}else {
			return failue();
		}
	}

	@GetMapping("/plantCover/coverRate")
	public Result queryRate(){
		log.info("start coverRate:");
		Map<Object, Object> map = plantCoverService.rateQuery();
		return success(map);
	}
}
