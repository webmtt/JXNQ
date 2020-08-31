package com.htht.cn.jiaxing.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class LandInfoDTO {
	private String id;//,
	private Long binder;//,'绑定人id',
	private String crop;//, '种植作物',
	private String address;//, '地址',
	private String city;//, '市',
	private String district;//, '区县',
	private Float area;//种植面积
	private Date createdDate;//,
	private Date updatedDate;//,


}
