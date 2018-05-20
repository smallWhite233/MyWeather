package com.a1054311037qq.util;

import android.util.Log;

import com.a1054311037qq.bean.FutureWeather;
import com.a1054311037qq.bean.Suggestion;
import com.a1054311037qq.bean.TodayWeather;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 晓白 on 2018/5/19.
 * 数据解析：pull解析
 */

public class ParseData {
    /**
     * xml解析天气信息
     */
    public static TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        List<FutureWeather> list_future=new ArrayList<FutureWeather>();//实例化一个未来天气的日期集合
        List<FutureWeather> list_date=new ArrayList<FutureWeather>();//实例化一个未来天气的日期集合
        List<FutureWeather> list_high=new ArrayList<FutureWeather>();//实例化一个未来天气的最高温集合
        List<FutureWeather> list_low=new ArrayList<FutureWeather>();//实例化一个未来天气的最低温集合
        List<FutureWeather> list_type=new ArrayList<FutureWeather>();//实例化一个未来天气的类型集合

        List<Suggestion> list_zhishu=new ArrayList<Suggestion>();//生活指数集合
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
            xmlPullParser.setInput(new StringReader(xmldata));//读取输入
            int eventType = xmlPullParser.getEventType();
            Log.d("xml文档标签",":"+eventType);
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件,如：resp
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();//实例化实体类对象后不为null
                        }
                        if (todayWeather != null) {
                            //每一次循环实例化一个对象,避免数据重复
                            FutureWeather item=new FutureWeather();
                            Suggestion zhishu_item=new Suggestion();

                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();//让解析器指向city属性的值
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
                            } /*else if ((xmlPullParser.getName().equals("date") && dateCount == 0)) {
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
                            }*/
                            //循环解析天气信息

                                    else if (xmlPullParser.getName().equals("date")) {
                                        eventType = xmlPullParser.next();
                                        if (dateCount == 0) {//今天日期
                                            todayWeather.setDate(xmlPullParser.getText().substring(3));//去掉几日，只保留星期
                                        } else {
                                            //添加未来几天的日期
                                            item.setFuture_date(xmlPullParser.getText().substring(3));
                                            list_date.add(item);
                                            Log.d("未来日期：", xmlPullParser.getText().substring(3));
                                        }
                                        dateCount++;
                                    } else if (xmlPullParser.getName().equals("high")) {
                                        eventType = xmlPullParser.next();
                                        if (highCount == 0) {//今天的最高温
                                            todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());//去掉汉字
                                        } else {
                                            //添加未来几天的最高温
                                            item.setFuture_high(xmlPullParser.getText().substring(2).trim());
                                            list_high.add(item);
                                            Log.d("未来最高温：", xmlPullParser.getText().substring(2).trim());
                                        }
                                        highCount++;
                                    } else if (xmlPullParser.getName().equals("low")) {
                                        eventType = xmlPullParser.next();
                                        if (lowCount == 0) {//今天最低温
                                            todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                        } else {
                                            //添加未来几天的最低温
                                            item.setFuture_low(xmlPullParser.getText().substring(2).trim());
                                            list_low.add(item);
                                            Log.d("未来最低温：", xmlPullParser.getText().substring(2).trim());
                                        }
                                        lowCount++;
                                    } else if (xmlPullParser.getName().equals("day")){//白天
                                           eventType=xmlPullParser.nextTag();
                                           if (xmlPullParser.getName().equals("type")) {//type有白天和夜晚的
                                            eventType = xmlPullParser.next();
                                            if (typeCount == 0) {//今天天气类型
                                                todayWeather.setType(xmlPullParser.getText());
                                            } else {
                                                //添加未来几天的天气类型
                                                item.setFuture_type(xmlPullParser.getText());
                                                list_type.add(item);
                                                Log.d("未来天气类型：", xmlPullParser.getText());
                                            }
                                            typeCount++;
                                            }
                                    }
                            //生活指数
                            else if (xmlPullParser.getName().equals("zhishu")){//
                                eventType=xmlPullParser.nextTag();//跳到下一个标签
                                int count=0;
                                while (count<3) {//循环三次，取三个子标签的值
                                    if (xmlPullParser.getName().equals("name")) {
                                        eventType = xmlPullParser.next();
                                        zhishu_item.setZhishu_name(xmlPullParser.getText());
                                        eventType = xmlPullParser.next();
                                    }
                                    else if (xmlPullParser.getName().equals("value")) {
                                        eventType = xmlPullParser.next();
                                        zhishu_item.setZhishu_value(xmlPullParser.getText());
                                        eventType = xmlPullParser.next();
                                    }
                                    else if (xmlPullParser.getName().equals("detail")){
                                        eventType = xmlPullParser.next();
                                        zhishu_item.setZhishu_detail(xmlPullParser.getText());
                                        eventType = xmlPullParser.next();
                                    }
                                    eventType=xmlPullParser.next();//跳到下一个标签
                                    count++;
                                }
                                list_zhishu.add(zhishu_item);
                            }
                        }
                        break;

                    //判断当前事件是否为标签元素结束事件，如：/resp
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }

            //将未来天气分别添加
            for (int i=0;i<4;i++){
                FutureWeather a=new FutureWeather();
                a.setFuture_date(list_date.get(i).getFuture_date());
                a.setFuture_high(list_high.get(i).getFuture_high());
                a.setFuture_low(list_low.get(i).getFuture_low());
                a.setFuture_type(list_type.get(i).getFuture_type());
                list_future.add(a);
                Log.d("未来日期：",a.toString());
            }
            //将未来几天的天气集合list添加到todayWeather实体类中
            todayWeather.setFutureWeatherList(list_future);
            //添加生活建议的指数
            for(Suggestion s:list_zhishu){
                Log.d("生活指数：",s.getZhishu_name());
            }
            todayWeather.setSuggestion(list_zhishu);

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

}
