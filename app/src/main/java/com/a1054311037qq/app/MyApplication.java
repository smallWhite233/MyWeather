package com.a1054311037qq.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.content.Context;

import com.a1054311037qq.bean.City;
import com.a1054311037qq.db.CityDB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 晓白 on 2017/1/22.
 */

public class MyApplication extends Application {
    private static final String TAG="MyApp";
    private static MyApplication myApplication;
    public CityDB mCityDB;
    private List<City> provinceList;
    private List<City> allCityList;

    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"MyApplicaton-Oncreate");
        myApplication=this;
        mCityDB=openCityDB();//打开数据库
        initCityList();//初始化城市列表

    }
    private void initCityList(){
        new Thread(new Runnable(){
            public void run(){
                prepareProvinceList();
            }
        }).start();
    }
    private boolean prepareProvinceList() {
        provinceList=new ArrayList<City>();
        provinceList = mCityDB.queryProvinces();//获取省份
        int i = 0;
        //遍历省份
        for (City city : provinceList) {
            String cityName = city.getProvince();//获取城市名称
            Log.d(TAG, "省份:" + cityName);//在控制台打印出所有城市
            i++;
        }
        Log.d(TAG, "i=" + i);
        return true;
    }

    //创建getInstance方法，单例模式
    public static MyApplication getInstance(){
        if (myApplication==null){
            myApplication=new MyApplication();
        }
        return myApplication;
    }

    //创建打开数据库的方法
    public CityDB openCityDB(){
        String path="/data"+ Environment.getDataDirectory().getAbsolutePath()+ File.separator+getPackageName()+
                File.separator+"databases1"+File.separator+CityDB.CITY_DB_NAME;//File.separator是文件分隔符,支持跨平台

        File db=new File(path);//找到path路径下的数据库,实例化
        Log.d(TAG,path);

        //路径下的文件不存在，则创建
        if (!db.exists()){
            String	pathfolder="/data" +Environment.getDataDirectory().getAbsolutePath()
                    +File.separator	+getPackageName()
                    +File.separator	+"databases1"
                    +File.separator;
            File dirFirstFolder=new File(pathfolder);
            //如果文件夹不存在,则创建一个该路径下的文件夹
            if (!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.d(TAG,"mkdirs");//如出现,表示创建了一个文件夹
            }
            Log.d(TAG,"db is not exists");
            try {
                InputStream is=getAssets().open("city.db");
                FileOutputStream fos=new FileOutputStream(db);
                int len=-1;
                byte[] buffer=new byte[1024];
                while ((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }
}
