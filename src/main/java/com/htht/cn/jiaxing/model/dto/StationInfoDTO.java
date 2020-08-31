package com.htht.cn.jiaxing.model.dto;

public class StationInfoDTO {
    private String stationID;
    private String stationName;
    private float lat;
    private float lon;
    private float height;
    private String country;
    private String town;
    private String village;
    private String address;

    public String getStationID() {
        return stationID;
    }

    public void setStationID(String stationID) {
        this.stationID = stationID;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "StationInfoDTO{" +
                "stationID='" + stationID + '\'' +
                ", stationName='" + stationName + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", height=" + height +
                ", country='" + country + '\'' +
                ", town='" + town + '\'' +
                ", village='" + village + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
