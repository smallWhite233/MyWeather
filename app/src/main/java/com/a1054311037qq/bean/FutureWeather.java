package com.a1054311037qq.bean;

/**
 * Created by 晓白 on 2017/2/17.
 * 未来天气的实体类
 */

public class FutureWeather {
    private String future_date;//日期
    private	String future_high;//最高温
    private	String future_low;//最低温
    private	String future_type;//天气类型

    public String getFuture_date() {
        return future_date;
    }

    public void setFuture_date(String future_date) {
        this.future_date = future_date;
    }

    public String getFuture_high() {
        return future_high;
    }

    public void setFuture_high(String future_high) {
        this.future_high = future_high;
    }

    public String getFuture_low() {
        return future_low;
    }

    public void setFuture_low(String future_low) {
        this.future_low = future_low;
    }

    public String getFuture_type() {
        return future_type;
    }

    public void setFuture_type(String future_type) {
        this.future_type = future_type;
    }

    public String toString(){
        return "FutureWeather{"+
                "high="+future_high+
                ", low="+future_low+
                ", type="+future_type+
                ", date="+future_date+
                '}';
    }

}
