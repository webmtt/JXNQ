package com.htht.cn.jiaxing.model.dto;

public class ImageInfoSDTO {
    private long id;
    private String attr;
    private String time;
    private String imagePath;
    private String createdDate;

    public ImageInfoSDTO() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAttr() {
        return this.attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String toString() {
        return "ImageInfoSDTO{id=" + this.id + ", attr='" + this.attr + '\'' + ", time='" + this.time + '\'' + ", image_path='" + this.imagePath + '\'' + ", createdDate='" + this.createdDate + '\'' + '}';
    }
}