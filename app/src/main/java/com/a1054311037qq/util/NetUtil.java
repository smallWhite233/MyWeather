package com.a1054311037qq.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetUtil{
    public static final int NETWORN_NONE=0;
    public static final int NETWORN_WIFI=1;
    public static final int NETWORN_MOBILE=2;
    /**
     * Created by 晓白 on 2017/1/15.
     * 网络状态检查
     */
    public static int getNetworkState(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null){
            return NETWORN_NONE;
        }
        int nType=networkInfo.getType();
        if (nType==ConnectivityManager.TYPE_MOBILE){
            return NETWORN_MOBILE;
        }
        else if (nType==ConnectivityManager.TYPE_WIFI){
            return NETWORN_WIFI;
        }
        return NETWORN_NONE;
    }
    /**
     * Created by white dove on 2018/5/19
     * OkHttp框架，网络请求方法。第一个参数：请求地址。第二个参数：回调参数.
     */
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);//enqueue方法内部运行子线程，执行HTTP请求并回调结果
    }
}
