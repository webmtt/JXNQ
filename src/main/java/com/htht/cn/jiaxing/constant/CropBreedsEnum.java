package com.htht.cn.jiaxing.constant;

/**
 * 2020.2.19
 * zw
 * 农作物种类信息
 * */
public enum CropBreedsEnum {

	RICE("rice","水稻"),
	RAPE("rape","油菜"),
	WHEAT("wheat","麦子");



	private String code;
	private String comment;
	CropBreedsEnum(String code, String comment) {
		this.code = code;
		this.comment = comment;
	}

	public String getCode() {
		return code;
	}

	public String getComment() {
		return comment;
	}
}
