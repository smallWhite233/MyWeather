package com.a1054311037qq.bean;

/**
 * Created by 晓白 on 2018/5/20.
 * 生活指数的实体类
 */

public class Suggestion {
    private String zhishu_name;
    private String zhishu_value;
    private String zhishu_detail;

    public String getZhishu_name() {
        return zhishu_name;
    }

    public void setZhishu_name(String zhishu_name) {
        this.zhishu_name = zhishu_name;
    }

    public String getZhishu_value() {
        return zhishu_value;
    }

    public void setZhishu_value(String zhishu_value) {
        this.zhishu_value = zhishu_value;
    }

    public String getZhishu_detail() {
        return zhishu_detail;
    }

    public void setZhishu_detail(String zhishu_detail) {
        this.zhishu_detail = zhishu_detail;
    }

    public String toString(){
        return "Suggestion{"+
                "name="+zhishu_name+
                ",value="+zhishu_value+
                ", detail="+zhishu_detail+
                '}';
    }
}
