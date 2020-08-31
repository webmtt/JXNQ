package com.htht.cn.jiaxing.model.dto;

import java.util.Date;

public class WeatherDataDTO {
    private long id;
    private String rain;
    private String humidity;
    private String temp;
    private String wind;
    private String time;
    private Date createdDate;
    private String stationName;

    public WeatherDataDTO() {
    }

    public String getStationName() {
        return this.stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRain() {
        return this.rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getHumidity() {
        return this.humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp() {
        return this.temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind() {
        return this.wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String toString() {
        return "WeatherDataDTO{id=" + this.id + ", rain='" + this.rain + '\'' + ", humidity='" + this.humidity + '\'' + ", temp='" + this.temp + '\'' + ", wind='" + this.wind + '\'' + ", time='" + this.time + '\'' + ", createdDate=" + this.createdDate + ", stationName='" + this.stationName + '\'' + '}';
    }
}
