package com.htht.cn.jiaxing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 地块儿
 * */
@Setter
@Getter
@ToString
public class LandBlockModel {

	@ApiModelProperty(value="地块编号" ,required=false)
	private String id;

	@ApiModelProperty(value="作物种类" ,required=true)
	private String crops;
	//市
	@ApiModelProperty(value="市" ,required=true)
	private String city;
	//区,县
	@ApiModelProperty(value="区县" ,required=false)
	private String district;

	//乡村
	@ApiModelProperty(value="详细地址" ,required=false)
	private String address;


	//面积 亩
	@ApiModelProperty(value="面积" ,required=false)
	private float area;

}
