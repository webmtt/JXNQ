package com.htht.cn.jiaxing.service;

import com.htht.cn.jiaxing.model.CropCultivationYearMsg;

import java.util.List;
import java.util.Map;

public interface LandManageService {
	List<CropCultivationYearMsg> query(String district,String year,String cropBreeds);


	Map getCropByDistrict(String district,String year);


	List<Map> cropLineChart(String district);
}
