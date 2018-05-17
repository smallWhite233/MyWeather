package com.a1054311037qq.bean;

/**
 * Created by 晓白 on 2017/1/22.
 */

public class City {
    private	String	province;
    private	String	city;
    private	String	number;//城市代码
    private	String	firstPY;
    private	String	allPY;
    private	String	allFristPY;

    //构造函数
    //public City(String province,String city,String number,String firstPY,String allPY,String allFirstPY){}

    //public City(String province){this.province=province;}
    public City(){}

    public void setProvince(String province){
        this.province=province;
    }

    public void setCity(String city){
        this.city=city;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public void setAllFristPY(String allFristPY) {
        this.allFristPY = allFristPY;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }

    public String getProvince(){return province;}

    public String getCity(){return city;}

    public String getNumber(){return number;}

    public String getFirstPY(){return firstPY;}

    public String getAllPY(){return allPY;}

    public String getAllFristPY(){return allFristPY;}

}
