package com.a1054311037qq.myweather;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.a1054311037qq.util.NetUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView mUpdateBtn;
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
    }
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
    *@param cityCode
    */
    private void queryWeatherCode(String cityCode){
        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;//通过城市id获取其天气的url地址
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con=null;
                try{
                    URL url=new URL(address);
                    con=(HttpURLConnection)url.openConnection();//建立连接
                    con.setRequestMethod("GET");//请求方式GET
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in=con.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String str;
                    while ((str=reader.readLine())!=null){
                        response.append(str);
                        Log.d("myWeather",str);//天气数据
                    }
                    String responseStr=response.toString();
                    Log.d("myWeather",responseStr);
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
}
