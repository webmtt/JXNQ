package com.htht.cn.jiaxing.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CropCultivationDTO {
	private long id;
	//地区 嘉兴市、南湖区、秀洲区、嘉善县、海盐县、海宁市、平湖市、桐乡市
	private String district;
	private int year;
	//作物品种 水稻、油菜、麦子
	private String cropBreeds;
	//种植面积 单位：万亩
	private float cultivationArea;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createdDate;
	private String creater;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCropBreeds() {
		return cropBreeds;
	}

	public void setCropBreeds(String cropBreeds) {
		this.cropBreeds = cropBreeds;
	}

	public float getCultivationArea() {
		return cultivationArea;
	}

	public void setCultivationArea(float cultivationArea) {
		this.cultivationArea = cultivationArea;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	@Override
	public String toString() {
		return "CropCultivationDTO{" +
				"id=" + id +
				", district='" + district + '\'' +
				", year=" + year +
				", cropBreeds='" + cropBreeds + '\'' +
				", cultivationArea=" + cultivationArea +
				", createdDate=" + createdDate +
				", creater='" + creater + '\'' +
				'}';
	}
}
