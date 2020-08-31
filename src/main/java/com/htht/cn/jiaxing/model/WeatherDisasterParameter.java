package com.htht.cn.jiaxing.model;

import java.util.Date;

public class WeatherDisasterParameter {

    private Integer id;

    private Date createTime;

    private String dType;

    private String dParameter;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }

    public String getdParameter() {
        return dParameter;
    }

    public void setdParameter(String dParameter) {
        this.dParameter = dParameter;
    }
}
