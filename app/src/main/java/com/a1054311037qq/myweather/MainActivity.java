package com.a1054311037qq.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.a1054311037qq.bean.Suggestion;
import com.a1054311037qq.bean.TodayWeather;
import com.a1054311037qq.bean.FutureWeather;
import com.a1054311037qq.service.AutoUpdateService;
import com.a1054311037qq.util.NetUtil;
import com.a1054311037qq.util.ParseData;
import com.bumptech.glide.Glide;

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
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mUpdateBtn,mSelectCity,mLocation;
    private ImageView bingPicImg;
    private TextView city_name_Tv, cityTv, timeTv, humidityTv, weekTv, pmDataTv, pmQualityTv, temperatureTv, temperature_range_Tv, climateTv, windTv, wind_degree_Tv;

    private LinearLayout futureLayout;//未来天气

    private LinearLayout zhishuLayout;//生活指数

    private static final int UPDATE_TODAY_WEATHER = 1;//定义一个变量用来判断状态

    /**
     * 消息机制 Handler
     */
    private Handler mhandler = new Handler() {
        //接收到消息后处理（为子线程提供一个mhandler,当子线程完成时提交消息给主线程(MainActivity)，主线程调用对应函数,更新信息）
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);//更新展示今日天气信息
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * @param savedInstanceState 活动创建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //实现背景图和状态栏融合，Android5.0以上支持
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();//获取当前活动的decorview
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);//表示活动的布局会显示在状态栏上面
            getWindow().setStatusBarColor(Color.TRANSPARENT);//将状态栏设置成透明色
        }
        setContentView(R.layout.weather_info);//加载界面

        //初始化控件
        initView();

        //初始化，判断缓存
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String weatherInfo = sharedPreferences.getString("weatherInfo", null);//天气缓存
        String bingPic = sharedPreferences.getString("bing_pic", null);//背景图片缓存

        //天气缓存不为空
        if (weatherInfo != null) {
            //直接解析展示
            TodayWeather todayWeather = ParseData.parseXML(weatherInfo);
            Log.d("缓存：", todayWeather.toString());
            updateTodayWeather(todayWeather);
        } else {
            //缓存为空时，若网络正常，
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                queryWeatherCode("101010100");//默认查询并展示北京的天气
            } else {
                Log.d("myWeather", "网络未连接！");
                Toast.makeText(MainActivity.this, "网络未连接！", Toast.LENGTH_LONG).show();
                initView();
            }
        }
        //判断背景图片缓存
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);//Glide图片加载框架
        }else{
            loadBingPic();//请求加载背景图片
        }
        //设置刷新按钮的点击监听器
        mUpdateBtn.setOnClickListener(this);
        //选择城市的监听器
        mSelectCity.setOnClickListener(this);
        //定位的点击监听
        mLocation.setOnClickListener(this);
    }

    /**
     * 获取控件对应id，初始化控件内容
     */
    void initView() {
        mSelectCity = (ImageView) findViewById(R.id.title_city_manager);
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mLocation = (ImageView) findViewById(R.id.title_location);
        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);//背景图片
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);//某某城市
        //cityTv = (TextView) findViewById(R.id.city);//城市名
        timeTv = (TextView) findViewById(R.id.updatetime);//发布时间
        humidityTv = (TextView) findViewById(R.id.humdity);//湿度
        climateTv = (TextView) findViewById(R.id.weather_type);//天气状况类型,晴或雨...
        temperatureTv = (TextView) findViewById(R.id.temperature);//当前温度
        temperature_range_Tv = (TextView) findViewById(R.id.temperature_range);//今日温度范围
        pmDataTv = (TextView) findViewById(R.id.pm25_data);//pm2.5数值
        pmQualityTv = (TextView) findViewById(R.id.pm25_quality);//空气质量
        windTv = (TextView) findViewById(R.id.wind);//风向
        wind_degree_Tv = (TextView) findViewById(R.id.wind_degree);//风力
        weekTv = (TextView) findViewById(R.id.today_week);//今日星期
        //未来天气
        futureLayout = (LinearLayout) findViewById(R.id.weather_future);//未来几天天气布局
        //生活建议
        zhishuLayout =(LinearLayout) findViewById(R.id.zhishu_layout);

        city_name_Tv.setText("N/A");
        //cityTv.setText("N/A");
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
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        //刷新
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String cityCode = sharedPreferences.getString("cityCode", null);//读取城市id 北京101010100
            Log.d("myWeather", cityCode);
            //检查网络状态，并调用查询函数
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络已连接");//Log类用来查看调试信息
                queryWeatherCode(cityCode);//通过城市id查询其天气
            } else {
                Log.d("myWeather", "网络未连接！");
                Toast.makeText(MainActivity.this, "网络未连接！", Toast.LENGTH_LONG).show();
            }

        }
        //选择城市
        if (view.getId() == R.id.title_city_manager) {
            // 跳转到SelectCity活动
            Intent intent = new Intent(this, SelectCity.class);
            startActivityForResult(intent, 1);//1是请求码requestCode
        }
        //定位
        if (view.getId() == R.id.title_location) {
            Toast.makeText(MainActivity.this, "定位暂不可用，后续努力加上哈", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * onActivityResult函数，接收返回的城市数据
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //获取传回的数据
            String selctedCityName = data.getStringExtra("cityName");
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("MainActivity", "选择的城市:" + selctedCityName + ":" + newCityCode);
            //缓存已选的城市代码，自动刷新时需要
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
            editor.putString("cityCode", newCityCode);//键值cityCode：缓存的城市代码
            editor.apply();
            //检查网络状态，根据返回的城市代码，查询天气
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络已连接");//Log类用来查看调试信息
                queryWeatherCode(newCityCode);//通过城市id查询其天气
            } else {
                Log.d("myWeather", "网络未连接！");
                Toast.makeText(MainActivity.this, "网络未连接！", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * @param cityCode 查询天气数据，解析xml数据存到实体类，调用消息机制，更新展示天气信息
     */
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;//通过城市id获取其天气的url地址
        Log.d("myWeather", address);
        //缓存去查询天气的城市代码
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        editor.putString("cityCode", cityCode);
        editor.apply();
        //网络请求天气信息
        NetUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                //线程转换，UI操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                TodayWeather todayWeather = null;//天气信息实体对象
                //获取的天气信息
                String responseStr = response.body().string();
                Log.d("myWeather", responseStr);
                //缓存获取的天气信息
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString("weatherInfo", responseStr);
                editor.apply();
                //调用解析函数，将解析的数据存到todayWeather实体中
                try {
                    todayWeather = ParseData.parseXML(responseStr);
                    //判断
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());//解析后筛选的天气信息
                        //通过消息机制,传回数据
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;//设置msg消息状态
                        msg.obj = todayWeather;//msg消息内容是todayWeather对象
                        mhandler.sendMessage(msg);
                    }
                    else {
                        Toast.makeText(MainActivity.this,"更新天气信息失败",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        //请求加载背景图片
        loadBingPic();
        //子线程:通过原生HttpURLConnection去请求天气信息
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;//今日天气数据对象
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();//建立连接
                    con.setRequestMethod("GET");//请求方式 GET
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);//添加 每一行数据
                        Log.d("myWeather", str);//天气数据
                    }
                    //获取的天气信息
                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    //缓存获取的天气信息
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                    editor.putString("weatherInfo", responseStr);
                    editor.apply();
                    //调用解析函数，将解析的数据存到todayWeather实体中
                    todayWeather = parseXML(responseStr);
                    //判断
                    if (todayWeather != null) {
                        Log.d("myWeather", todayWeather.toString());//解析后筛选的天气信息
                        //通过消息机制,传回数据
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;//设置msg消息状态
                        msg.obj = todayWeather;//msg消息内容是todayWeather对象
                        mhandler.sendMessage(msg);
                    } else {
                        Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();*/
    }

    /**
     * updateTodayWeather方法处理实体类中的数据，展示天气信息到组件
     */
    private void updateTodayWeather(TodayWeather todayWeather) {
        timeTv.setText(todayWeather.getUpdatetime());//发布时间
        //今日天气展示
        city_name_Tv.setText(todayWeather.getCity());
        humidityTv.setText(todayWeather.getShidu());
        climateTv.setText(todayWeather.getType());
        temperatureTv.setText(todayWeather.getWendu() + "°");//当前温度
        temperature_range_Tv.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());//今日温度范围
        windTv.setText(todayWeather.getFengxiang());
        wind_degree_Tv.setText(todayWeather.getFengli());
        weekTv.setText(todayWeather.getDate());//今日星期
        if (todayWeather.getPm25() == null) {
            pmDataTv.setText("暂无pm2.5");
        } else {
            pmDataTv.setText(todayWeather.getPm25());
        }
        pmQualityTv.setText(todayWeather.getQuality());

        //未来几天的天气展示
        futureLayout.removeAllViews();
        for(FutureWeather futureWeather : todayWeather.getFutureWeatherList()){
            Log.d("未来天气：",futureWeather.toString());
            //动态加载future_item子布局
            View view= LayoutInflater.from(this).inflate(R.layout.future_item,futureLayout,false);
            //子布局里设置相应数据
            TextView future_date=(TextView) view.findViewById(R.id.future1_date);
            TextView future_type=(TextView) view.findViewById(R.id.future1_type);
            TextView future_range=(TextView) view.findViewById(R.id.future1_temperature);
            future_date.setText(futureWeather.getFuture_date());
            future_type.setText(futureWeather.getFuture_type());
            future_range.setText(futureWeather.getFuture_low()+"~"+futureWeather.getFuture_high());

            futureLayout.addView(view);
        }
        //生活建议展示
        zhishuLayout.removeAllViews();//先移除子布局
        for (Suggestion s:todayWeather.getSuggestion()){
            Log.d("生活建议：",s.toString());
            View view= LayoutInflater.from(this).inflate(R.layout.suggestion_item,zhishuLayout,false);//动态加载子布局
            //子布局里设置相应数据
            TextView zhishu_name=(TextView) view.findViewById(R.id.zhishu_name_value);
            //TextView zhishu_value=(TextView) view.findViewById(R.id.zhishu_value);
            TextView zhishu_detail=(TextView) view.findViewById(R.id.zhishu_detail);
            zhishu_name.setText(s.getZhishu_name()+"："+s.getZhishu_value());
            //zhishu_value.setText(s.getZhishu_value());
            zhishu_detail.setText(s.getZhishu_detail());
            //再添加到父布局中
            zhishuLayout.addView(view);
        }
        Toast.makeText(MainActivity.this, "更新成功", Toast.LENGTH_SHORT).show();

        //激活后台自动更新服务
        if (todayWeather !=null){
            Intent intent=new Intent(this, AutoUpdateService.class);
            startService(intent);
        }else{
            Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 加载必应Bing每日一图
     */
    private void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";//获取必应每日一图的接口,返回图片链接
        //网络请求
        NetUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                //缓存图片
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                //线程转换，进行UI操作
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(MainActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }

}