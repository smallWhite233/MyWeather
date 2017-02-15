package com.a1054311037qq.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a1054311037qq.bean.TodayWeather;
import com.a1054311037qq.util.NetUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;

public class MainActivity extends Activity implements View.OnClickListener{
    private ImageView mUpdateBtn;
    private ImageView mSelectCity;
    private	TextView city_name_Tv,cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,temperature_range_Tv,climateTv,windTv,wind_degree_Tv;
    private	ImageView weatherImg,pmImg;

    private static final int UPDATE_TODAY_WEATHER=1;//定义一个变量用来判断状态

    /**
     * 消息机制 Handler
     */
    private Handler mhandler=new Handler(){
        //接收到消息后处理（为子线程提供一个mhandler,当子线程完成时提交消息给主线程(MainActivity)，主线程调用对应函数,更新信息）
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);//更新今日天气数据
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * @param savedInstanceState 新建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);//加载界面

        //设置刷新按钮的点击监听器
        mUpdateBtn=(ImageView)findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);


        //初始化之后，判断网络状态，直接加载数据
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String cityCode=sharedPreferences.getString("cityCode","101010100");//读取城市id
        Log.d("myWeather",cityCode);

        if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
            Log.d("myWeather","网络已连接");//Log类用来查看调试信息
            queryWeatherCode(cityCode);//通过城市id查询其天气
        }
        else{
            Log.d("myWeather","网络未连接！");
            Toast.makeText(MainActivity.this,"网络未连接！",Toast.LENGTH_LONG).show();
        }

        //点击选择城市的监听器
        mSelectCity=(ImageView)findViewById(R.id.title_city_manager);
        mSelectCity.setOnClickListener(this);
        initView();
    }

    /**
     * 获取控件对应的id，初始化控件内容
     */
    void initView(){
        city_name_Tv=(TextView)findViewById(R.id.title_city_name);//某某城市天气
        cityTv=(TextView)findViewById(R.id.city);//城市名
        timeTv=(TextView)findViewById(R.id.updatetime);//发布时间
        humidityTv=(TextView)findViewById(R.id.humdity);//湿度
        climateTv=(TextView)findViewById(R.id.weather_type);//天气状况类型,晴或雨...
        temperatureTv=(TextView)findViewById(R.id.temperature);//当前温度
        temperature_range_Tv=(TextView)findViewById(R.id.temperature_range);//今日温度范围
        pmDataTv=(TextView)findViewById(R.id.pm25_data);//pm2.5数值
        pmQualityTv=(TextView)findViewById(R.id.pm25_quality);//空气质量
        windTv=(TextView)findViewById(R.id.wind);//风向
        wind_degree_Tv=(TextView)findViewById(R.id.wind_degree);//风力
        weekTv=(TextView)findViewById(R.id.today_week);//今日星期

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        climateTv.setText("N/A");
        temperatureTv.setText("N/A");
        temperature_range_Tv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        windTv.setText("N/A");
        wind_degree_Tv.setText("N/A");
        weekTv.setText("N/A");
    }

    /**
     * 实现View.OnClickListener接口的抽象方法 onClick()
     * @param view
     */
    @Override
    public void onClick(View view){
        //刷新
        if (view.getId()==R.id.title_update_btn){
            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            String cityCode=sharedPreferences.getString("cityCode","101010100");//读取城市id
            Log.d("myWeather",cityCode);
            //检查网络状态，并调用查询函数
            if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络已连接");//Log类用来查看调试信息
                queryWeatherCode(cityCode);//通过城市id查询其天气
            }
            else{
                Log.d("myWeather","网络未连接！");
                Toast.makeText(MainActivity.this,"网络未连接！",Toast.LENGTH_LONG).show();
            }

        }
        //选择城市
        if (view.getId()==R.id.title_city_manager){
            //跳转到SelectCity活动
            Intent i=new Intent(this,SelectCity.class);
            startActivityForResult(i,1);//1是请求码requestCode
        }
    }

    /**
     * onActivityResult函数，用于接收返回的数据
     */
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode==1&&resultCode==RESULT_OK){
            String newCityCode=data.getStringExtra("cityCode");
            Log.d("myWeather","选择的城市代码为："+newCityCode);

            //检查网络状态，并调用查询函数
            if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络已连接");//Log类用来查看调试信息
                queryWeatherCode(newCityCode);//通过城市id查询其天气
            }
            else{
                Log.d("myWeather","网络未连接！");
                Toast.makeText(MainActivity.this,"网络未连接！",Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
    *@param cityCode 查询天气数据的子线程
    */
    private void queryWeatherCode(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;//通过城市id获取其天气的url地址
        Log.d("myWeather",address);
        //子线程
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

                    todayWeather=parseXML(responseStr);//调用解析函数
                    if (todayWeather!=null){
                        Log.d("myWeather",todayWeather.toString());//调试今日天气信息

                        Message msg=new Message();
                        msg.what=UPDATE_TODAY_WEATHER;//设置msg消息状态
                        msg.obj=todayWeather;//msg消息内容是todayWeather对象
                        mhandler.sendMessage(msg);
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
     * 网络数据解析函数
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
            xmlPullParser.setInput(new StringReader(xmldata));
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
                            else if (xmlPullParser.getName().equals("updatetime")){
                                eventType=xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("wendu")){
                                eventType=xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("pm25")){
                                eventType=xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("quality")){
                                eventType=xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            }
                            else if (xmlPullParser.getName().equals("shidu")){
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
                                todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            }
                            else if ((xmlPullParser.getName().equals("low")&&lowCount==0)){
                                eventType=xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            }
                            else if ((xmlPullParser.getName().equals("date")&&dateCount==0)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setDate(xmlPullParser.getText().substring(3));//去掉几日，只保留星期
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

    /**
     * updateTodayWeather函数,利用TodayWeather对象更新UI控件的数据
     */
    void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime());//发布时间
        humidityTv.setText(todayWeather.getShidu());
        climateTv.setText(todayWeather.getType());
        temperatureTv.setText(todayWeather.getWendu()+"°");//当前温度
        temperature_range_Tv.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());//今日温度范围
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        windTv.setText(todayWeather.getFengxiang());
        wind_degree_Tv.setText(todayWeather.getFengli());
        weekTv.setText(todayWeather.getDate());//今日星期

        Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
    }

}
