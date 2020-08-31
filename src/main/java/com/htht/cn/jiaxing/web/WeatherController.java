package com.htht.cn.jiaxing.web;

import com.alibaba.fastjson.JSONObject;
import com.htht.cn.jiaxing.model.WeatherVO;
import com.htht.cn.jiaxing.service.WeatherService;
import com.htht.cn.jiaxing.utils.JavaForPython;
import com.htht.cn.jiaxing.utils.Result;
import com.htht.cn.jiaxing.utils.wCounter.GeneImage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * zw
 * 2020.03.04
 * 气象服务
 * **/
@Slf4j
@RestController
@Api(description = "气象服务接口")
public class WeatherController{


	@Autowired
	WeatherService weatherService;


    @ApiOperation("气象服务通过站名查询气象信息")
    @ApiImplicitParams({@ApiImplicitParam(
				name = "stationName",
				value = "站点中文名",
				required = true,
				dataType = "String",
				paramType = "query"
			), @ApiImplicitParam(
				name = "time",
				value = "查询时间 yyyyMMddHH",
				required = true,
				dataType = "String",
				paramType = "query"
				)})
    @RequestMapping(
        value = {"/weather/queryDetail"},
        method = {RequestMethod.GET}
    )
    public Result query(@RequestParam(value = "stationName",required = true) String stationName, @RequestParam(value = "time",required = true) String time) {
        WeatherVO weatherVO = this.weatherService.queryByNameTime(stationName, time);
        return Result.ok(weatherVO);
    }

	@ApiOperation("气象服务时间显示png图片")
	@ApiImplicitParams({@ApiImplicitParam(
			name = "attr",
			value = "气象属性 雨 rain 压 wind 温 temp 湿 humi",
			required = true,
			dataType = "String",
			paramType = "query"
	), @ApiImplicitParam(
			name = "time",
			value = "查询时间 yyyyMMddHH",
			required = true,
			dataType = "String",
			paramType = "query"
	)})
	@RequestMapping(
			value = {"/weather/queryImage"},
			method = {RequestMethod.GET}
	)
	public Result queryImage(@RequestParam(value = "attr",required = true) String attr, @RequestParam(value = "time",required = true) String time) {
		String path = this.weatherService.showPicture(attr, time);
		return Result.ok(path);
	}

}

