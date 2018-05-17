package com.a1054311037qq.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a1054311037qq.bean.TodayWeather;
import com.a1054311037qq.bean.FutureWeather;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mUpdateBtn;
    private ImageView mSelectCity;
    private	TextView city_name_Tv,cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,temperature_range_Tv,climateTv,windTv,wind_degree_Tv;
    private	ImageView weatherImg,pmImg;
    private TextView future1_date,future1_type,future1_range,future2_date,future2_type,future2_range,
            future3_date,future3_type,future3_range,future4_date,future4_type,future4_range;

    private static final int UPDATE_TODAY_WEATHER=1;//定义一个变量用来判断状态

    /**
     * 消息机制 Handler
     */
    private Handler mhandler=new Handler(){
        //接收到消息后处理（为子线程提供一个mhandler,当子线程完成时提交消息给主线程(MainActivity)，主线程调用对应函数,更新信息）
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);//调用updateTodayWeather方法，更新今日天气数据
                    //String city1=((TodayWeather) msg.obj).getCity().toString();
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * @param savedInstanceState 创建时
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);//加载界面
        //初始化控件
        initView();
        //设置刷新按钮的点击监听器
        mUpdateBtn=(ImageView)findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        //初始化，判断缓存
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String weatherInfo=sharedPreferences.getString("weatherInfo",null);//读取城市id

        //缓存不为空，直接解析
        if (weatherInfo!=null){
            TodayWeather todayWeather=parseXML(weatherInfo);
            Log.d("缓存：",todayWeather.toString());
            updateTodayWeather(todayWeather);
        }
        else{
            //缓存为空时，若网络正常，
            if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                queryWeatherCode("101010100");//默认查询并展示北京的天气
            }
            else {
                Log.d("myWeather","网络未连接！");
                Toast.makeText(MainActivity.this,"网络未连接！",Toast.LENGTH_LONG).show();
                initView();
            }
        }
        //选择城市的监听器
        mSelectCity=(ImageView)findViewById(R.id.title_city_manager);
        mSelectCity.setOnClickListener(this);
    }
    /**
     * 获取控件对应id，初始化控件内容
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
        //未来四天
        future1_date=(TextView)findViewById(R.id.future1_date);
        future1_type=(TextView)findViewById(R.id.future1_type);
        future1_range=(TextView)findViewById(R.id.future1_temperature);
        future2_date=(TextView)findViewById(R.id.future2_date);
        future2_type=(TextView)findViewById(R.id.future2_type);
        future2_range=(TextView)findViewById(R.id.future2_temperature);
        future3_date=(TextView)findViewById(R.id.future3_date);
        future3_type=(TextView)findViewById(R.id.future3_type);
        future3_range=(TextView)findViewById(R.id.future3_temperature);
        future4_date=(TextView)findViewById(R.id.future4_date);
        future4_type=(TextView)findViewById(R.id.future4_type);
        future4_range=(TextView)findViewById(R.id.future4_temperature);

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
            SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String cityCode=sharedPreferences.getString("cityCode",null);//读取城市id 北京101010100
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
            // 跳转到SelectCity活动
            Intent intent=new Intent(this,SelectCity.class);
            startActivityForResult(intent,1);//1是请求码requestCode
        }
    }
    /**
     * onActivityResult函数，接收返回的数据
     */
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (requestCode==1&&resultCode==RESULT_OK){
            //获取传回的数据
            String selctedCityName=data.getStringExtra("cityName");
            String newCityCode=data.getStringExtra("cityCode");
            Log.d("MainActivity","选择的城市:"+selctedCityName+":"+newCityCode);
            //缓存已选的城市代码，自动刷新时需要
            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            editor.putString("cityCode",newCityCode);//键值cityCode：缓存的城市代码
            editor.apply();
            //检查网络状态，根据返回的城市代码，查询天气
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
    *@param cityCode 查询天气数据，解析xml数据存到实体类，调用消息机制，更新天气信息
    */
    private void queryWeatherCode(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;//通过城市id获取其天气的url地址
        Log.d("myWeather",address);

        //子线程:去请求天气信息
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
                        response.append(str);//添加 每一行数据
                        Log.d("myWeather",str);//天气数据
                    }
                    //获取的天气信息
                    String responseStr=response.toString();
                    Log.d("myWeather",responseStr);
                    //缓存获取的天气信息
                    SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                    editor.putString("weatherInfo",responseStr);
                    editor.apply();
                    //调用解析函数，将解析的数据存到todayWeather实体中
                    todayWeather=parseXML(responseStr);
                    //判断
                    if (todayWeather!=null){
                        Log.d("myWeather",todayWeather.toString());//解析后筛选的天气信息
                        //通过消息机制,传回数据
                        Message msg=new Message();
                        msg.what=UPDATE_TODAY_WEATHER;//设置msg消息状态
                        msg.obj=todayWeather;//msg消息内容是todayWeather对象
                        mhandler.sendMessage(msg);
                    }
                    else {
                        Toast.makeText(MainActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
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
     * 网络数据xml解析函数
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
                            else if ((xmlPullParser.getName().equals("date")&&dateCount==0)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setDate(xmlPullParser.getText().substring(3));//去掉几日，只保留星期
                                dateCount++;
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
                            else if ((xmlPullParser.getName().equals("type")&&typeCount==0)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第一天
                            else if ((xmlPullParser.getName().equals("date")&&dateCount==1)){
                                eventType=xmlPullParser.next();
                                todayWeather.setDate1(xmlPullParser.getText().substring(3));
                                dateCount++;
                            }
                            else if ((xmlPullParser.getName().equals("high")&&highCount==1)){
                                eventType=xmlPullParser.next();
                                todayWeather.setHigh1(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            }
                            else if ((xmlPullParser.getName().equals("low")&&lowCount==1)){
                                eventType=xmlPullParser.next();
                                todayWeather.setLow1(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            }
                            else if ((xmlPullParser.getName().equals("type")&&typeCount==1)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setType1(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第二天
                            else if ((xmlPullParser.getName().equals("date")&&dateCount==2)){
                                eventType=xmlPullParser.next();
                                todayWeather.setDate2(xmlPullParser.getText().substring(3));
                                dateCount++;
                            }
                            else if ((xmlPullParser.getName().equals("high")&&highCount==2)){
                                eventType=xmlPullParser.next();
                                todayWeather.setHigh2(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            }
                            else if ((xmlPullParser.getName().equals("low")&&lowCount==2)){
                                eventType=xmlPullParser.next();
                                todayWeather.setLow2(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            }
                            else if ((xmlPullParser.getName().equals("type")&&typeCount==2)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setType2(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第三天
                            else if ((xmlPullParser.getName().equals("date")&&dateCount==3)){
                                eventType=xmlPullParser.next();
                                todayWeather.setDate3(xmlPullParser.getText().substring(3));
                                dateCount++;
                            }
                            else if ((xmlPullParser.getName().equals("high")&&highCount==3)){
                                eventType=xmlPullParser.next();
                                todayWeather.setHigh3(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            }
                            else if ((xmlPullParser.getName().equals("low")&&lowCount==3)){
                                eventType=xmlPullParser.next();
                                todayWeather.setLow3(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            }
                            else if ((xmlPullParser.getName().equals("type")&&typeCount==3)){
                                eventType=xmlPullParser.next() ;
                                todayWeather.setType3(xmlPullParser.getText());
                                typeCount++;
                            }
                            //未来第四天
                            else if ((xmlPullParser.getName().equals("date")&&dateCount==4)){
                                eventType=xmlPullParser.next();
                                todayWeather.setDate4(xmlPullParser.getText().substring(3));
                                dateCount++;
                            }
                            else if ((xmlPullParser.getName().equals("high")&&highCount==4)){
                                eventType=xmlPullParser.next();
                                todayWeather.setHigh4(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                highCount++;
                            }
                            else if ((xmlPullParser.getName().equals("low")&&lowCount==4)){
                                eventType=xmlPullParser.next();
                                todayWeather.setLow4(xmlPullParser.getText().substring(2).trim());//去掉汉字和摄氏度符号
                                lowCount++;
                            }
                            else if ((xmlPullParser.getName().equals("type")&&typeCount==4)){
                                eventType=xmlPullParser.next() ;
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
     * updateTodayWeather函数处理实体类中的数据，展示天气信息
     */
    public void updateTodayWeather(TodayWeather todayWeather){
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime());//发布时间
        humidityTv.setText(todayWeather.getShidu());
        climateTv.setText(todayWeather.getType());
        temperatureTv.setText(todayWeather.getWendu()+"°");//当前温度
        temperature_range_Tv.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());//今日温度范围
        windTv.setText(todayWeather.getFengxiang());
        wind_degree_Tv.setText(todayWeather.getFengli());
        weekTv.setText(todayWeather.getDate());//今日星期

        //未来4天的天气信息
        future1_date.setText(todayWeather.getDate1());
        future1_type.setText(todayWeather.getType1());
        future1_range.setText(todayWeather.getLow1()+"~"+todayWeather.getHigh1());
        future2_date.setText(todayWeather.getDate2());
        future2_type.setText(todayWeather.getType2());
        future2_range.setText(todayWeather.getLow2()+"~"+todayWeather.getHigh2());
        future3_date.setText(todayWeather.getDate3());
        future3_type.setText(todayWeather.getType3());
        future3_range.setText(todayWeather.getLow3()+"~"+todayWeather.getHigh3());
        future4_date.setText(todayWeather.getDate4());
        future4_type.setText(todayWeather.getType4());
        future4_range.setText(todayWeather.getLow4()+"~"+todayWeather.getHigh4());

        if (todayWeather.getPm25()==null){
            pmDataTv.setText("暂无pm2.5");
        }
        else {
            pmDataTv.setText(todayWeather.getPm25());
        }
        pmQualityTv.setText(todayWeather.getQuality());

        Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
    }

}
