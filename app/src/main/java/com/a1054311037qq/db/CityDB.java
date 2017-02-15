package com.a1054311037qq.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.a1054311037qq.bean.City;

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

                City item=new City(province,city,number,firstPY,allPY,allFirstPY);
                list.add(item);
            }
            return list;

    }

}
