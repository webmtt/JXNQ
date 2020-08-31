package com.htht.cn.jiaxing.service;

import com.htht.cn.jiaxing.model.CoverRateVO;
import com.htht.cn.jiaxing.model.ImageDisplayVO;
import com.htht.cn.jiaxing.model.ImageInfoModel;

import java.util.Map;

public interface GrowMonitorService {
    ImageDisplayVO imageQuery(String imageId, String plantCover);

    int updateImageInfo(ImageInfoModel imageInfoModel);

    Map<Object, Object> rateQuery();
}
