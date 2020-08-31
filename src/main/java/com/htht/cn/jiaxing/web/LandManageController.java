package com.htht.cn.jiaxing.web;

import com.htht.cn.jiaxing.model.CropCultivationYearMsg;
import com.htht.cn.jiaxing.service.LandManageService;
import com.htht.cn.jiaxing.utils.Result;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * zw
 * 2020.02.19
 * 田地管理
 * **/
@RestController
@RequestMapping("/landManage")
@Api(description = "天地管理")
public class LandManageController{

	private Logger logger = LoggerFactory.getLogger(LandManageController.class);

	@Autowired
	LandManageService landManageService;

	@RequestMapping("/query")
	public Result query(@RequestParam(value = "district",required = false)String district,
						@RequestParam(value = "year",required = false)String  year,
						@RequestParam(value = "cropBreeds",required = false)String cropBreeds) {
		List<CropCultivationYearMsg> list = landManageService.query(district,year,cropBreeds);
			return Result.ok().put("data",list);
	}

	@RequestMapping("/cropLineChart")
	public Result cropLineChart(@RequestParam(value = "district",required = false)String district) {
		List<Map> list = landManageService.cropLineChart(district);
		return Result.ok().put("data",list);
	}

	@RequestMapping("/getCropByDistrict")
	public Result getCropByDistrict(@RequestParam(value = "district",required = false)String district,
									@RequestParam(value = "year",required = false)String  year) {
		Map list = landManageService.getCropByDistrict(district,year);
		return Result.ok().put("data",list);
	}

}
