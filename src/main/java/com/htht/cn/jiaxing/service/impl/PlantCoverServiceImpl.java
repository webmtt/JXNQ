package com.htht.cn.jiaxing.service.impl;

import com.htht.cn.jiaxing.constant.Consts;
import com.htht.cn.jiaxing.model.ImageDisplayVO;
import com.htht.cn.jiaxing.model.ImageInfoModel;
import com.htht.cn.jiaxing.service.GrowMonitorService;
import com.htht.cn.jiaxing.service.PlantCoverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PlantCoverServiceImpl implements PlantCoverService{

	@Autowired
	GrowMonitorService growMonitorService;

	@Override
	public ImageDisplayVO imageQuery(String imageName) {
		ImageDisplayVO imageDisplayVO = growMonitorService.imageQuery(imageName, Consts.PLANT_COVER);
		return imageDisplayVO;
	}

	@Override
	public int updateImageInfo(ImageInfoModel imageInfoModel) {
		imageInfoModel.setType(Consts.PLANT_COVER);
		return growMonitorService.updateImageInfo(imageInfoModel);
	}

	@Override
	public Map<Object, Object> rateQuery() {
		return growMonitorService.rateQuery();
	}
}
