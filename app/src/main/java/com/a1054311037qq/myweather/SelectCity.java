package com.a1054311037qq.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;
import android.view.LayoutInflater;
import android.text.TextWatcher;
import android.text.Editable;

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

public class SelectCity extends FragmentActivity {

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.select_city);

    }
}
