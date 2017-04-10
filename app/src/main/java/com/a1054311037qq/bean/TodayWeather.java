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
    //未来第1天
    private String date1;
    private	String	high1;//最高温
    private	String	low1;//最低温
    private	String	type1;//天气类型
    public String getDate1() {return date1;}
    public void setDate1(String date1) {
        this.date1 = date1;
    }
    public String getHigh1() {
        return high1;
    }
    public void setHigh1(String high1) {
        this.high1 = high1;
    }
    public String getLow1() {
        return low1;
    }
    public void setLow1(String low1) {
        this.low1 = low1;
    }
    public String getType1() {
        return type1;
    }
    public void setType1(String type1) {
        this.type1 = type1;
    }
    //未来第2天
    private String date2;
    private	String	high2;//最高温
    private	String	low2;//最低温
    private	String	type2;//天气类型
    public String getDate2() {return date2;}
    public void setDate2(String date2) {
        this.date2 = date2;
    }
    public String getHigh2() {
        return high2;
    }
    public void setHigh2(String high2) {
        this.high2 = high2;
    }
    public String getLow2() {
        return low2;
    }
    public void setLow2(String low2) {
        this.low2 = low2;
    }
    public String getType2() {
        return type1;
    }
    public void setType2(String type2) {
        this.type2 = type2;
    }
    //未来第3天
    private String date3;
    private String high3;//最高温
    private String low3;//最低温
    private String type3;//天气类型
    public String getDate3() {return date3;}
    public void setDate3(String date3) {
        this.date3 = date3;
    }
    public String getHigh3() {
        return high3;
    }
    public void setHigh3(String high3) {
        this.high3 = high3;
    }
    public String getLow3() {
        return low3;
    }
    public void setLow3(String low3) {
        this.low3 = low3;
    }
    public String getType3() {
        return type1;
    }
    public void setType3(String type3) {
        this.type3 = type3;
    }
    //未来第4天
    private String date4;
    private String high4;//最高温
    private String low4;//最低温
    private String type4;//天气类型
    public String getDate4() {return date4;}
    public void setDate4(String date4) {
        this.date4 = date4;
    }
    public String getHigh4() {
        return high4;
    }
    public void setHigh4(String high4) {
        this.high4 = high4;
    }
    public String getLow4() {
        return low4;
    }
    public void setLow4(String low4) {
        this.low4 = low4;
    }
    public String getType4() {
        return type1;
    }
    public void setType4(String type4) {
        this.type4 = type4;
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

                ", date1="+date1+
                ", type1="+type1+
                ", future1_high="+high1+
                ", future1_low="+low1+
                ", date2="+date2+
                ", type2="+type2+
                ", future2_high="+high2+
                ", future2_low="+low2+
                ", date3="+date3+
                ", type3="+type3+
                ", future3_high="+high3+
                ", future3_low="+low3+
                ", date4="+date4+
                ", type4="+type4+
                ", future4_high="+high4+
                ", future4_low="+low4+
                '}';
    }
}