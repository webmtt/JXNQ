package com.htht.cn.jiaxing.model;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class LandsQueryVO {
	@ApiModelProperty(value="账户名" ,required=true)
	private String accountName;
	@ApiModelProperty(value="手机号" ,required=true)
	private String mobile;
	@ApiModelProperty(value="地块信息" ,required=true)
	private List<LandBlockModel> blocks ;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public List<LandBlockModel> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<LandBlockModel> blocks) {
		this.blocks = blocks;
	}

	@Override
	public String toString() {
		return "LandsQueryVO{" +
				"accountName='" + accountName + '\'' +
				", mobile='" + mobile + '\'' +
				", blocks=" + blocks +
				'}';
	}
}
