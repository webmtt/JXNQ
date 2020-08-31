package com.htht.cn.jiaxing.web;

import com.htht.cn.jiaxing.constant.Consts;
import com.htht.cn.jiaxing.constant.CropBreedsEnum;
import com.htht.cn.jiaxing.constant.DistrictEnum;
import com.htht.cn.jiaxing.model.ImageDisplayVO;
import com.htht.cn.jiaxing.model.ImageInfoModel;
import com.htht.cn.jiaxing.service.GrowMonitorService;
import com.htht.cn.jiaxing.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

/*
*
* zw
* 长势监管
*
* */
@Slf4j
@RestController
@Api(description = "长势监管")
public class GrowMonitorController extends BaseController{

    @Autowired
    GrowMonitorService growMonitorService;
    @GetMapping("/growing/imageQuery")
    public Result imageQuery(
            @ApiParam(value = "图层id")@RequestParam(value = "imageName",required = true)String imageName
    ){
            ImageDisplayVO growMonitorVO = growMonitorService.imageQuery(imageName, Consts.GROW_MONITOR);
            if(null == growMonitorVO) {
                return failue(Consts.IMAGE_UNFIND,"图片信息未找到");
            }else {
                return success(growMonitorVO);
            }
    }

    @PostMapping("/growing/infoInput")
    public Result input(@Validated @RequestBody ImageInfoModel imageInfoModel){
        log.info("start input image info:"+imageInfoModel);
        int i = growMonitorService.updateImageInfo(imageInfoModel);
        if(i ==1) {
            return success();
        }else {
            return failue();
        }
    }
}
