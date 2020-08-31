package com.htht.cn.jiaxing.model;

public class WeatherVO {
    private String stationName;
    //风
    private String wind;
    //温度
    private String temperature;
    //湿度
    private String humidity;

    private String rain;

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "WeatherVO{" +
                "stationName='" + stationName + '\'' +
                ", wind='" + wind + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", rain='" + rain + '\'' +
                '}';
    }
}
