package com.a1054311037qq.myweather;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a1054311037qq.bean.TodayWeather;
import com.a1054311037qq.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mUpdateBtn;
    private	TextView city_name_Tv,cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv;
    private	ImageView weatherImg,pmImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        mUpdateBtn=(ImageView)findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);//设置点击按钮的监听器
        if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
            Log.d("myWeather","网络已连接");//Log类用来查看调试信息
            Toast.makeText(MainActivity.this,"网络已连接！",Toast.LENGTH_LONG).show();
        }
        else{
            Log.d("myWeather","网络未连接！");
            Toast.makeText(MainActivity.this,"网络未连接！",Toast.LENGTH_LONG).show();
        }
        initView();
    }
    /**
     * 初始化控件内容
     */
    void initView(){
        city_name_Tv=(TextView)findViewById(R.id.title_city_name);//某某城市天气
        cityTv=(TextView)findViewById(R.id.city);//城市名
        timeTv=(TextView)findViewById(R.id.updatetime);//发布时间
        humidityTv=(TextView)findViewById(R.id.humidity);//湿度
        climateTv=(TextView)findViewById(R.id.condition);//天气状况类型,晴或雨...
        temperatureTv=(TextView)findViewById(R.id.temperature);//温度
        pmDataTv=(TextView)findViewById(R.id.pm25_data);//pm2.5数值
        pmQualityTv=(TextView)findViewById(R.id.pm25_quality);//空气质量
        windTv=(TextView)findViewById(R.id.wind);//风力
        weekTv=(TextView)findViewById(R.id.today_week);//今日星期

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        climateTv.setText("N/A");
        temperatureTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        windTv.setText("N/A");
        weekTv.setText("N/A");
    }
    /**
     *
     * @param view 检查网络状态，调用查询天气函数
     */
    @Override
    public void onClick(View view){
        if (view.getId()==R.id.title_update_btn){
            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            String cityCode=sharedPreferences.getString("main_city_code","101010100");//读取城市id
            Log.d("myWeather",cityCode);

            if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络已连接");//Log类用来查看调试信息
                queryWeatherCode(cityCode);//通过城市id查询其天气
            }
            else{
                Log.d("myWeather","网络未连接！");
                Toast.makeText(MainActivity.this,"网络未连接！",Toast.LENGTH_LONG).show();
            }

        }
    }
    /**
    *@param cityCode 查询天气数据
    */
    private void queryWeatherCode(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;//通过城市id获取其天气的url地址
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                TodayWeather todayWeather=null;//今日天气数据对象
                try{
                    URL url=new URL(address);
                    con=(HttpURLConnection)url.openConnection();//建立连接
                    con.setRequestMethod("GET");//请求方式 GET
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in=con.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String str;
                    while ((str=reader.readLine())!=null){
                        response.append(str);//添加 读取的每一行数据
                        Log.d("myWeather",str);//天气数据
                    }
                    String responseStr=response.toString();//获取的天气数据
                    Log.d("myWeather",responseStr);

                    todayWeather=parseXML(responseStr);
                    if (todayWeather!=null){
                        Log.d("myWeather",todayWeather.toString());//调试今日天气信息
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (con!=null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    /**
     * 网络数据解析的函数
     */
    private TodayWeather parseXML(String xmldata){
        TodayWeather todayWeather=null;
        //由于标签元素相同，通过计数来分辨，解析未来几天的天气数据
        int	fengxiangCount=0;
        int	fengliCount	=0;
        int	dateCount=0;
        int	highCount=0;
        int	lowCount=0;
        int	typeCount=0;

        try {
            XmlPullParserFactory fac=XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser=fac.newPullParser();
            xmlPullParser.setInput(new	StringReader(xmldata));
            int	eventType=xmlPullParser.getEventType();
            Log.d("myWeather","parseXML");
            while (eventType!= XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    //判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    //判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")){
                            todayWeather=new TodayWeather();
                        }
                        if (todayWeather!=null){
                            if (xmlPullParser.getName().equals("city")){
                                eventType=xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            }
                            else if ((xmlPullParser.getName().equals("updatetime")){
                                eventType=xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            }
                            else if ((xmlPullParser.getName().equals("wendu")){
                                eventType=xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            }
                            else if ((xmlPullParser.getName().equals("pm25")){
                                eventType=xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            }
                            else if ((xmlPullParser.getName().equals("quality")){
                                eventType=xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            }
                            else if ((xmlPullParser.getName().equals("shidu")){
                                eventType=xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            }
                            else if ((xmlPullParser.getName().equals("fengxiang")&&fengxiangCount==0)){
                                eventType=xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            }
                            else if ((xmlPullParser.getName().equals("fengli")&&fengliCount==0)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            }
                            else if ((xmlPullParser.getName().equals("high")&&highCount==0)){
                                eventType=xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            }
                            else if ((xmlPullParser.getName().equals("low")&&lowCount==0)){
                                eventType=xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            }
                            else if ((xmlPullParser.getName().equals("date")&&dateCount==0)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            }
                            else if ((xmlPullParser.getName().equals("type")&&typeCount==0)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    //判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
                //进入下一个元素并触发相应事件
                eventType=xmlPullParser.next();
            }
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return todayWeather;
    }

}
