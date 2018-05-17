package com.a1054311037qq.myweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.a1054311037qq.app.MyApplication;
import com.a1054311037qq.db.CityDB;
import com.a1054311037qq.bean.City;
/**
 * A simple {@link Fragment} subclass.遍历省份城市的碎片
 */
public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE=0;

    public static final int LEVEL_CITY=1;

    public static final int LEVEL_COUNTY=2;

    public static final int LEVEL_QU=3;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList= new ArrayList<>();

    private List<City> provinceList=new ArrayList<>();//省列表

    private List<City> cityList;//城市列表

    private List<City> countyList;//县(直辖市城区)列表

    private City selectedProvince;//选中的省

    private City selectedCity;//选中的城市

    private int currentLevel;//当前选中的级别

    private MyApplication app=MyApplication.getInstance();//创建一个实例
    private CityDB cityDB;

    public ChooseAreaFragment() {
        // Required empty public constructor
    }
    @Override
    //获取一些控件的实例
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.choose_area,container,false);//加载布局文件
        titleText=(TextView) view.findViewById(R.id.title_text);//标题
        backButton=(Button) view.findViewById(R.id.back_button);//返回按钮
        listView=(ListView) view.findViewById(R.id.list_view);

        adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);//listViw填充数据
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        //设置点击监听事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position,long id){
                if(currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(position);//通过position选中的省份（直辖市）
                    showCities();
                }
                else if(currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(position);//选中城市
                    showCounties();
                }
                //县级市或直辖市的城区，直接传回数据
                else if(currentLevel==LEVEL_COUNTY||currentLevel==LEVEL_QU){
                    if (currentLevel==LEVEL_COUNTY) {
                        selectedCity = countyList.get(position);//选中县级市,返回countyList
                    }
                    else if(currentLevel==LEVEL_QU){
                        selectedCity=cityList.get(position);//选中城区时，返回的还是cityList
                    }
                    Intent i=new Intent(getActivity(),MainActivity.class);
                    i.putExtra("cityName",selectedCity.getCity());//保存已选的城市名称
                    i.putExtra("cityCode",selectedCity.getNumber());//保存已选的城市代码
                    Log.d("选择的城市:",selectedCity.getCity()+":"+selectedCity.getNumber());

                    SelectCity selectCity=(SelectCity) getActivity();//获取当前的activity
                    selectCity.setResult(Activity.RESULT_OK,i);//返回数据
                    getActivity().finish();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener(){//设置返回按钮的监听
            @Override
            public void onClick(View v){
                if (currentLevel==LEVEL_COUNTY){
                    showCities();
                }
                else if (currentLevel==LEVEL_CITY||currentLevel==LEVEL_QU){
                    showProvinces();
                }
            }
        });
        try {
            showProvinces();//默认展示各省
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    *查询所有省，调用数据库查询方法，并展示到组件
     */
    private void showProvinces(){
        titleText.setText("中国");//标题展示
        backButton.setVisibility(View.GONE);//隐藏返回按钮
        try {
            provinceList=app.mCityDB.queryProvinces();//prepareProvinceList();//查询省
            dataList.clear();
            for (City province : provinceList) {
                dataList.add(province.getProvince());
                Log.d("省展示", province.getProvince());
            }
            adapter.notifyDataSetChanged();//通知数据已改变
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
    *查询该省下的所有城市，调用数据库查询方法，并展示到组件
     */
    private void showCities(){
        titleText.setText(selectedProvince.getProvince());//标题展示
        backButton.setVisibility(View.VISIBLE);//显示返回按钮
        Log.d("点击选择",selectedProvince.getProvince());
        cityList=app.mCityDB.queryCities(selectedProvince.getProvince());//查询城市.返回城市名称和代码
        dataList.clear();//清除原展示数据
        for(City city:cityList){
            dataList.add(city.getCity());
        }
        adapter.notifyDataSetChanged();//通知数据已改变
        listView.setSelection(0);
        //如果是直辖市
        if (selectedProvince.getProvince().equals("北京")||selectedProvince.getProvince().equals("上海")
                ||selectedProvince.getProvince().equals("天津")||selectedProvince.getProvince().equals("重庆")){
            currentLevel=LEVEL_QU;//直接选中城区，显示天气
        }
        else {
            currentLevel = LEVEL_CITY;
        }
    }
    /*
    *查询该城市的县级市，调用数据库查询方法，并展示到组件
     */
    private void showCounties(){
        titleText.setText(selectedCity.getCity());//标题展示
        backButton.setVisibility(View.VISIBLE);//显示返回按钮
        Log.d("点击选择",selectedCity.getCity());
        countyList=app.mCityDB.queryCounties(selectedCity.getCity(),selectedCity.getNumber());
        dataList.clear();//清除原展示数据
        for (City county : countyList) {
                dataList.add(county.getCity());
        }
        adapter.notifyDataSetChanged();//通知数据已改变
        listView.setSelection(0);
        currentLevel = LEVEL_COUNTY;

    }

}
