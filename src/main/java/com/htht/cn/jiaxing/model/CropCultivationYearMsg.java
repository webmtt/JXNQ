package com.htht.cn.jiaxing.model;

public class CropCultivationYearMsg {

    private Integer id;

    private String district;

    private String districtTag;

    private String year;

    private String cropBreeds;

    private Float cultivationArea;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrictTag() {
        return districtTag;
    }

    public void setDistrictTag(String districtTag) {
        this.districtTag = districtTag;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCropBreeds() {
        return cropBreeds;
    }

    public void setCropBreeds(String cropBreeds) {
        this.cropBreeds = cropBreeds;
    }

    public Float getCultivationArea() {
        return cultivationArea;
    }

    public void setCultivationArea(Float cultivationArea) {
        this.cultivationArea = cultivationArea;
    }
}
