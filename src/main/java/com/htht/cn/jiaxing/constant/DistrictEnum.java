package com.htht.cn.jiaxing.constant;

/**
 * 市区名管理
 *
 * 嘉兴市、南湖区、秀洲区、嘉善县、海盐县、海宁市、平湖市、桐乡市
 * **/
public enum DistrictEnum {
	JX("jx","嘉兴市"),
	NH("nh","南湖区"),
	XZ("xz","秀洲区"),
	JS("js","嘉善县"),
	HY("hy","海盐县"),
	HN("hn","海宁市"),
	PH("ph","平湖市"),
	TX("tx","桐乡市");
	private String code;
	private String msg;
	DistrictEnum(String code,String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
