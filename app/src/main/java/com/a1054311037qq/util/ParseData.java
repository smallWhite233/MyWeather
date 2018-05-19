package com.a1054311037qq.util;

import android.util.Log;

import com.a1054311037qq.bean.TodayWeather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by 晓白 on 2018/5/19.
 * 数据解析
 */

public class ParseData {
    /**
     * xml解析天气信息
     */
    public static TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        //由于标签元素相同，通过计数来分辨，解析未来几天的天气数据
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if ((xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if ((xmlPullParser.getName().equals("fengli") && fengliCount == 0)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if ((xmlPullParser.getName().equals("date") && dateCount == 0)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText().substring(3));//去掉几日，只保留星期
                                dateCount++;
                            } else if ((xmlPullParser.getName().equals("high") && highCount == 0)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            } else if ((xmlPullParser.getName().equals("low") && lowCount == 0)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            } else if ((xmlPullParser.getName().equals("type") && typeCount == 0)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第一天
                            else if ((xmlPullParser.getName().equals("date") && dateCount == 1)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate1(xmlPullParser.getText().substring(3));
                                dateCount++;
                            } else if ((xmlPullParser.getName().equals("high") && highCount == 1)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh1(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            } else if ((xmlPullParser.getName().equals("low") && lowCount == 1)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow1(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            } else if ((xmlPullParser.getName().equals("type") && typeCount == 1)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第二天
                            else if ((xmlPullParser.getName().equals("date") && dateCount == 2)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate2(xmlPullParser.getText().substring(3));
                                dateCount++;
                            } else if ((xmlPullParser.getName().equals("high") && highCount == 2)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh2(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            } else if ((xmlPullParser.getName().equals("low") && lowCount == 2)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow2(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            } else if ((xmlPullParser.getName().equals("type") && typeCount == 2)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType2(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第三天
                            else if ((xmlPullParser.getName().equals("date") && dateCount == 3)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate3(xmlPullParser.getText().substring(3));
                                dateCount++;
                            } else if ((xmlPullParser.getName().equals("high") && highCount == 3)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh3(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            } else if ((xmlPullParser.getName().equals("low") && lowCount == 3)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow3(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            } else if ((xmlPullParser.getName().equals("type") && typeCount == 3)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType3(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第四天
                            else if ((xmlPullParser.getName().equals("date") && dateCount == 4)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate4(xmlPullParser.getText().substring(3));
                                dateCount++;
                            } else if ((xmlPullParser.getName().equals("high") && highCount == 4)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh4(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            } else if ((xmlPullParser.getName().equals("low") && lowCount == 4)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow4(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            } else if ((xmlPullParser.getName().equals("type") && typeCount == 4)) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType4(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;


                    //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

}
