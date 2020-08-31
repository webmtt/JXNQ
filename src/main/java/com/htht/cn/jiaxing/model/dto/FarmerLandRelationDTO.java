package com.htht.cn.jiaxing.model.dto;

import java.util.Date;

public class FarmerLandRelationDTO {
	private String id;// '主键',
	private Long farmerid;
	private String farmername;//'farmername',
	private String mobile;// 'mobile',
	private String block1;// '地块1',
	private String block2;// '地块2',
	private String block3;// '地块3',
	private Date createdDate;

	public long getFarmerid() {
		return farmerid;
	}

	public void setFarmerid(long farmerid) {
		this.farmerid = farmerid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFarmername() {
		return farmername;
	}

	public void setFarmername(String farmername) {
		this.farmername = farmername;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBlock1() {
		return block1;
	}

	public void setBlock1(String block1) {
		this.block1 = block1;
	}

	public String getBlock2() {
		return block2;
	}

	public void setBlock2(String block2) {
		this.block2 = block2;
	}

	public String getBlock3() {
		return block3;
	}

	public void setBlock3(String block3) {
		this.block3 = block3;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "FarmerLandRelationDTO{" +
				"id=" + id +
				", farmername='" + farmername + '\'' +
				", mobile='" + mobile + '\'' +
				", block1=" + block1 +
				", block2=" + block2 +
				", block3=" + block3 +
				", createdDate=" + createdDate +
				'}';
	}
}
