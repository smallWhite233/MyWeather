package com.a1054311037qq.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.a1054311037qq.bean.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * CityDB操作类
 * Created by 晓白 on 2017/1/22.
 */
public class CityDB {
    public static final String CITY_DB_NAME="city.db";
    public static final String CITY_TABLE_NAME="city";
    private SQLiteDatabase db;

    private static final String TAG="MyApp";

    //构造函数
    public CityDB(Context context,String path){
        db=context.openOrCreateDatabase(path,Context.MODE_PRIVATE,null);//打开path路径下的数据库
    }

    //获取所有城市列表
    public List<City> getAllCity(){
            List<City> list=new ArrayList<City>();
            Cursor c=db.rawQuery("select * from "+CITY_TABLE_NAME,null);//rawQuery()方法用于执行select语句
            while (c.moveToNext()){
                String province=c.getString(c.getColumnIndex("province"));
                String city=c.getString(c.getColumnIndex("city"));
                String number=c.getString(c.getColumnIndex("number"));
                String firstPY=c.getString(c.getColumnIndex("firstpy"));
                String allPY=c.getString(c.getColumnIndex("allpy"));
                String allFirstPY=c.getString(c.getColumnIndex("allfirstpy"));
                City item=new City();
                item.setProvince(province);
                item.setCity(city);
                item.setNumber(number);
                list.add(item);
            }
            return list;
    }
    //查询省份
    public List<City> queryProvinces(){
        List<City> list=new ArrayList<City>();
        Cursor c=db.rawQuery("select distinct province from "+CITY_TABLE_NAME,null);//rawQuery()方法用于执行select语句
        while (c.moveToNext()){
            String province=c.getString(c.getColumnIndex("province"));
            City item=new City();
            item.setProvince(province);
            list.add(item);
        }
        return list;
    }
    //查询地级市(直辖市的区)
    public List<City> queryCities(String province){
        List<City> list=new ArrayList<>();
        List<City> cityList=new ArrayList<>();//地级市列表
        int i=1;
        int n=0;
        //根据选中省份查询其下的城市
        Cursor c=db.rawQuery("select * from "+CITY_TABLE_NAME+" where province=?",new String[]{province});
        while (c.moveToNext()){
            String city=c.getString(c.getColumnIndex("city"));
            String number=c.getString(c.getColumnIndex("number"));
            City item=new City();
            item.setCity(city);
            item.setNumber(number);
            list.add(item);
            n++;//记录list下标最大值

        }
        Log.d("城市数量","n="+n);
        cityList.add(list.get(0));//添加第一个地级市
        while (i<n){//遍历
            String cnum1=list.get(i).getNumber().substring(5,7);//地级市(直辖市的区)的两位标识数字
            String cnum2=list.get(i-1).getNumber().substring(5,7);//地级市(直辖市的区)的两位标识数字
            if (!cnum1.equals(cnum2)){
                cityList.add(list.get(i));//筛选出地级市(直辖市的区)
            }
            i++;
        }
        return cityList;//返回包含城市名称和代码的列表
    }
    //查询市内的县
    public List<City> queryCounties(String selectedCity,String cityCode){
        List<City> countyList=new ArrayList<>();//地级市列表
        //根据选中城市，筛选出县级市
        Cursor c=db.rawQuery("select * from "+CITY_TABLE_NAME+" where number like ?",new String[]{cityCode.substring(0,7)+"%"});
        while (c.moveToNext()){//查询出的每一行
            String city=c.getString(c.getColumnIndex("city"));
            String number=c.getString(c.getColumnIndex("number"));
            //if(number.substring(5,7).equals(cityCode.substring(5,7))){//筛选出县级市
                City item=new City();
                item.setCity(city);
                item.setNumber(number);
                countyList.add(item);
            //}
        }
        return countyList;//返回包含城市名称和代码的列表
    }

}
