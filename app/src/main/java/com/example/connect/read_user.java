package com.example.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.connect.helper.DatabaseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class read_user extends AppCompatActivity {
    ListView my_view;
    public   String usersid,name,username,lastlogin, value;
    private DatabaseHandler dbv;
    private HashMap<String,String> user = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_user);
        my_view=findViewById(R.id.my_list);
        dbv = new DatabaseHandler(getApplicationContext());
        user=dbv.getUserDetails();
        usersid=user.get("uid");
        name=user.get("name");
        username=user.get("email");
        upload();
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("users");
            ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String formula_value = jo_inside.getString("name");
                String url_value = jo_inside.getString("username");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("name", formula_value);
                m_li.put("username", url_value);

                formList.add(m_li);
            }
            ListAdapter listAdapter=new SimpleAdapter(this,formList,R.layout.user_info,new String[]{"name","username","lastlogin"},new int[]{R.id.name,R.id.username,R.id.last});
            my_view.setAdapter(listAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("userinfo.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void upload() {

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String strDate = dateFormat.format(date);

        List<String> list = new ArrayList<String>();
        list.add(name);
        list.add(username);
        list.add(strDate);
        JSONArray array = new JSONArray();
        for(int i = 0; i < list.size(); i++) {
            array.put(list.get(i));
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("users", array);

        } catch(JSONException e) {
            e.printStackTrace();
        }
        System.out.println(obj.toString());
        Toast.makeText(read_user.this,obj.toString(),Toast.LENGTH_SHORT).show();
    }
}