package com.a1054311037qq.bean;

/**
 * Created by 晓白 on 2017/1/15.
 */

public class TodayWeather {
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
                '}';
    }
}
