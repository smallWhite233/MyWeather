package com.a1054311037qq.bean;

/**
 * Created by 晓白 on 2017/1/15.
 */

import java.util.ArrayList;
import java.util.List;

public class TodayWeather {
    public List<FutureWeather> futureWeatherList=new ArrayList<>();//未来天气的实体类的集合
    public List<Suggestion> suggestion=new ArrayList<>();//生活指数的实体类

    //今日天气
    private	String	city;
    private	String	updatetime;//发布时间
    private	String	wendu;//现在温度
    private	String	shidu;
    private	String	pm25;
    private	String	quality;//空气质量
    private	String	fengxiang;
    private	String	fengli;
    private	String	date;//今日日期
    private	String	high;//最高温
    private	String	low;//最低温
    private	String	type;//天气类型

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getUpdatetime() {
        return updatetime;
    }
    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }
    public String getWendu() {
        return wendu;
    }
    public void setWendu(String wendu) {
        this.wendu = wendu;
    }
    public String getPm25() {
        return pm25;
    }
    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }
    public String getQuality() {
        return quality;
    }
    public void setQuality(String quality) {
        this.quality = quality;
    }
    public String getFengxiang() {
        return fengxiang;
    }
    public void setFengxiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }
    public String getFengli() {
        return fengli;
    }
    public void setFengli(String fengli) {
        this.fengli = fengli;
    }
    public String getShidu() {
        return shidu;
    }
    public void setShidu(String shidu) {
        this.shidu = shidu;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getHigh() {
        return high;
    }
    public void setHigh(String high) {
        this.high = high;
    }
    public String getLow() {
        return low;
    }
    public void setLow(String low) {
        this.low = low;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    //未来天气
    public List<FutureWeather> getFutureWeatherList() {
        return futureWeatherList;
    }

    public void setFutureWeatherList(List<FutureWeather> futureWeatherList) {
        this.futureWeatherList = futureWeatherList;
    }

    //生活指数
    public List<Suggestion> getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(List<Suggestion> suggestion) {
        this.suggestion = suggestion;
    }

    @Override
    public String toString(){
        return "TodayWeather{"+"city="+city+
                ", updatetime="+updatetime+
                ", wendu="+wendu+
                ", high="+high+
                ", low="+low+
                ", type="+type+
                ", pm25="+pm25+
                ", quality="+quality+
                ", fengxiang="+fengxiang+
                ", fengli="+fengli+
                ", shidu="+shidu+
                ", date="+date+

                ", date1="+futureWeatherList.get(0).getFuture_date()+
                ", type1="+futureWeatherList.get(0).getFuture_type()+
                ", future1_high="+futureWeatherList.get(0).getFuture_high()+
                ", future1_low="+futureWeatherList.get(0).getFuture_low()+
                ", date2="+futureWeatherList.get(1).getFuture_date()+
                ", type2="+futureWeatherList.get(1).getFuture_type()+
                ", future2_high="+futureWeatherList.get(1).getFuture_high()+
                ", future2_low="+futureWeatherList.get(1).getFuture_low()+
                ", date3="+futureWeatherList.get(2).getFuture_date()+
                ", type3="+futureWeatherList.get(2).getFuture_type()+
                ", future3_high="+futureWeatherList.get(2).getFuture_high()+
                ", future3_low="+futureWeatherList.get(2).getFuture_low()+
                ", date4="+futureWeatherList.get(3).getFuture_date()+
                ", type4="+futureWeatherList.get(3).getFuture_type()+
                ", future4_high="+futureWeatherList.get(3).getFuture_high()+
                ", future4_low="+futureWeatherList.get(3).getFuture_low()+
                '}';
    }
}