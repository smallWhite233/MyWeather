package com.a1054311037qq.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * Created by 晓白 on 2017/1/15.
 */

/**
 * 网络状态检查
 */
public class NetUtil{
    public static final int NETWORN_NONE=0;
    public static final int NETWORN_WIFI=1;
    public static final int NETWORN_MOBILE=2;
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
}
