package com.a1054311037qq.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.a1054311037qq.app.MyApplication;
import com.a1054311037qq.db.CityDB;
import com.a1054311037qq.bean.City;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by 晓白 on 2017/1/19.
 */

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;//返回按钮
    private TextView mTitleName;//显示当前所选城市

    private CityDB mCityDB;
    private ListView mListView;
    private List<City> mCityList;
    private MyApplication CityList;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);

        mBackBtn=(ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        mTitleName=(TextView)findViewById(R.id.title_select_name);
        mListView=(ListView)findViewById(R.id.list_view_select);//城市列表

        CityList=(MyApplication) getApplication();//MyApplication对象
        mCityList=CityList.getCityList();//获取MyApplication对象的城市列表函数
        //ArrayList<Map<String, Object>> data = new ArrayList<Map<String,Object>>();

        Intent intent=getIntent();//获取传递过来的数据
        String cityN=intent.getStringExtra("cityName");
        mTitleName.setText("当前城市: "+cityN);
        System.out.println(cityN);

        int i=0;
        final ArrayList<String> mdata=new ArrayList<String>();
        final ArrayList<String> mdata1=new ArrayList<String>();
        final ArrayList<Map<String,Object>> mdata2=new ArrayList<Map<String,Object>>();

        for (City city : mCityList){
            i++;
            Map<String, Object> item = new HashMap<String, Object>();
            String cityName=city.getCity();//获取城市名称
            String cityCode=city.getNumber();//获取城市代码
            if (cityName.length()>=1) {
                mdata.add(cityName);
                mdata1.add(cityCode);
                //item.put("i",i);
                item.put("cityName",cityName);//获取城市名称
                item.put("cityCode",cityCode);//获取城市代码
                mdata2.add(item);
            }
            else{
                mdata.add("白");
            }
        }
        final ArrayAdapter adapter=new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,mdata);
        //SimpleAdapter adapter2 =new SimpleAdapter(this,mdata,android.R.layout.simple_list_item_1,new String[]{"cityName"},new int[]{R.id.list_view_select});
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent();
                i.putExtra("cityName",mdata.get(position));//获取当前点击的城市
                i.putExtra("cityCode",mdata1.get(position));
                updateCity(i);//更新显示当前城市

                //设置城市代码缓存
                SharedPreferences settings= (SharedPreferences)getSharedPreferences("shared", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("cityCode",mdata1.get(position));
                editor.commit();

                String ccc=mdata1.get(position);
                Log.d("ccc:",ccc);
            }
        });

    }

    /**
     *更新当前所选城市
     */
    public void updateCity(Intent data){
        mTitleName.setText("当前城市: "+data.getStringExtra("cityName")/*+data.getStringExtra("cityCode")*/);

}

    /**
     * 实现View.OnClickListener接口的抽象方法 onClick()
     * @param view
     */
    public void onClick(View view){
        switch (view.getId()){
            case R.id.title_back:
                //传递数据
                Intent i=new Intent();

                SharedPreferences sharedPreferences=getSharedPreferences("shared",MODE_PRIVATE);
                String newcityCode=sharedPreferences.getString("cityCode","101020100");//读取城市id
                Log.d("点击返回的城市代码：",newcityCode);

                i.putExtra("cityCode",newcityCode);//第二个参数是缺省值"101020100"上海
                setResult(RESULT_OK,i);//i传递城市代码

                finish();//结束当前activity活动进程
                break;
            default:
                break;
        }
    }
}
