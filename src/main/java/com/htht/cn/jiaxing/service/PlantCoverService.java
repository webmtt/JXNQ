package com.htht.cn.jiaxing.service;

import com.htht.cn.jiaxing.model.CoverRateVO;
import com.htht.cn.jiaxing.model.ImageDisplayVO;
import com.htht.cn.jiaxing.model.ImageInfoModel;

import java.util.Map;

public interface PlantCoverService {
	ImageDisplayVO imageQuery(String imageName);

	int updateImageInfo(ImageInfoModel imageInfoModel);

    Map<Object, Object> rateQuery();
}
