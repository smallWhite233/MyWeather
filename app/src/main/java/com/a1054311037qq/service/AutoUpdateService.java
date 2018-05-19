package com.a1054311037qq.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.a1054311037qq.bean.TodayWeather;
import com.a1054311037qq.myweather.MainActivity;
import com.a1054311037qq.util.NetUtil;
import com.a1054311037qq.util.ParseData;
import com.bumptech.glide.Glide;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    //后台服务的定时执行
    public int onStartCommand(Intent intent,int flags,int startId){
        updateWeather();
        updateBingPic();
        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);//获取一个实例
        int anHour=6*60*60*1000;//6小时的毫秒数
        long triggerAtTime= SystemClock.elapsedRealtime()+anHour;//触发时间
        Intent i=new Intent(this,AutoUpdateService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);//指定要处理的服务
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);//设置定时任务
        return super.onStartCommand(intent,flags,startId);
    }
    /**
     * 更新天气信息，只存储到SharedPreferences缓存里
     */
    private void updateWeather(){
        //获取缓存的城市代码
        SharedPreferences sharedPreference=PreferenceManager.getDefaultSharedPreferences(this);
        String cityCode=sharedPreference.getString("cityCode",null);
        if (cityCode!=null){
            final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;//通过城市id获取其天气的url地址
            //网络请求天气信息
            NetUtil.sendOkHttpRequest(address, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    TodayWeather todayWeather = null;//天气信息实体对象
                    //获取的天气信息
                    String responseStr = response.body().string();
                    todayWeather = ParseData.parseXML(responseStr);
                    if (todayWeather != null) {
                        //缓存后台更新的天气信息
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                        editor.putString("weatherInfo", responseStr);
                        editor.apply();
                    }
                }
            });
        }
    }
    /**
     * 后台更新必应每日一图
     */
    private void updateBingPic(){
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
                //后台更新缓存图片
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(AutoUpdateService.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
            }
        });
    }
}
