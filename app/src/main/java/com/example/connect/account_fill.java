package com.example.connect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.helper.DBHandler;
import com.example.connect.helper.DatabaseHandler;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class account_fill extends AppCompatActivity {
    public  static  String data;
    EditText amt,act,act_name;
    TextView my_act,cancel,send_fund;
    DBHandler dbHandler;
    private DatabaseHandler db;
    private HashMap<String,String> user = new HashMap<>();
    String usersid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_fill);
        amt=findViewById(R.id.amunt);
        act=findViewById(R.id.account_number);
        act_name=findViewById(R.id.account_name);
        my_act=findViewById(R.id.my_account);
        cancel=findViewById(R.id.fund_cancel);
        send_fund=findViewById(R.id.fund_transfer);
        data= getIntent().getStringExtra("value");
        dbHandler=new DBHandler(account_fill.this);
        db = new DatabaseHandler(getApplicationContext());
        user = db.getUserDetails();

        // session manager

        usersid=user.get("uid");
        String uid=usersid.replaceAll("[^a-zA-Z0-9]","");

        send_fund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amout=amt.getText().toString();
                String account=act.getText().toString();
                String account_name=act_name.getText().toString();
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String strDate = dateFormat.format(date);
                Random r=new Random();
                int reference=r.nextInt(1000000-100000)+100000;
                if(amout.isEmpty() && account.isEmpty() && account_name.isEmpty())
                {
                    Toast.makeText(account_fill.this, "Fill the all data", Toast.LENGTH_SHORT).show();
                    return;
                }
                dbHandler.add_information(account_name,account,strDate, String.valueOf(reference),uid,amout);
                Toast.makeText(account_fill.this, "Transaction Detail saved...", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(account_fill.this,export_value.class);
                intent.putExtra("amount",amout);
                intent.putExtra("reference",String.valueOf(reference));
                intent.putExtra("date",strDate);
                intent.putExtra("account",account);
                startActivity(intent);

            }
        });



try {
    JSONObject jobj = new JSONObject(data);
    String accountNumber =jobj.getString("accountNumber");
    String account_name=jobj.getString("accountName");
    act.setText(accountNumber);
    act_name.setText(account_name);

}
catch (  JSONException e)
{

}

    }











}