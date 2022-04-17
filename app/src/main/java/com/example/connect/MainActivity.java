package com.example.connect;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.connect.helper.DBHandler;
import com.example.connect.helper.DatabaseHandler;
import com.example.connect.helper.Functions;
import com.example.connect.helper.SessionManager;
import com.example.connect.helper.userinfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int Id_home = 1;
    private final int id_payment = 2;
    private final int id_qr = 3;
    private final int id_transcation = 4;
    private final int id_contact = 5;
    SessionManager sessionManager;
    LinearLayout my_profile, biometric, logout;
    AlertDialog.Builder alertDialog;
    public MeowBottomNavigation bottomNavigation;
    SharedPreferences sharedPreferences;


    //
    private DatabaseHandler dbv;
    private HashMap<String,String> user = new HashMap<>();
  public   String usersid,name,username,lastlogin, value;
    private ListView obj;

    ImageView b_icon;
    TextView finger_set,login_user;
    //
SharedPreferences shared;
// get all qr history
    LinearLayout getData;
  public   DBHandler db;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
         db = new DBHandler(getApplicationContext());
        dbv = new DatabaseHandler(getApplicationContext());
        user=dbv.getUserDetails();
        usersid=user.get("uid");
        name=user.get("name");
        username=user.get("email");
        b_icon=findViewById(R.id.b_image);
        login_user=findViewById(R.id.name);
        login_user.setText(name);
        finger_set=findViewById(R.id.fingerprint);
        shared=getSharedPreferences("biometric",0);
        value  =usersid.replaceAll("[^a-zA-Z0-9]","");
        SharedPreferences.Editor editor=shared.edit();
        editor.putString("unique_id",usersid);
        editor.apply();
        // get data
        getData=findViewById(R.id.container);
        populatedata();

// bottom navigation
        bottomNavigation = findViewById(R.id.negation_main);
        bottomNavigation.add(new MeowBottomNavigation.Model(Id_home, R.drawable.ic_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(id_payment, R.drawable.ic_payment));
        bottomNavigation.add(new MeowBottomNavigation.Model(id_qr, R.drawable.ic_qr));
        bottomNavigation.add(new MeowBottomNavigation.Model(id_transcation, R.drawable.ic_activity));
        bottomNavigation.add(new MeowBottomNavigation.Model(id_contact, R.drawable.ic_contact));
        my_profile = findViewById(R.id.my_profile);
        sessionManager=new SessionManager(getApplicationContext());
        alertDialog = new AlertDialog.Builder(this);
        biometric = findViewById(R.id.setup);
        sharedPreferences=getSharedPreferences("setup",0);
        String check_value=sharedPreferences.getString("finger","");
        if (check_value.equals("enable"))
        {
            finger_set.setText("Disable Biometric Login");
        }

            else{
            finger_set.setText("Setup Biometric Login");

        }
            String option_print=finger_set.getText().toString();
            if (option_print.equals("Disable Biometric Login"))

            {
                biometric.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alertDialog.setMessage("Do you want to Disable the Biometric Login..?")
                                .setIcon(R.drawable.connect)
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("finger","Disable");
                                        editor.apply();
                                        logoutUser();
                                        startActivity(new Intent(MainActivity.this,login_activity.class));
                                        finger_set.setText("SetUp Biometric Login");


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();



                    }
                });
            }
            else
            {
                biometric.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alertDialog.setMessage("Do you want to Enable the Biometric Login..?")
                                .setIcon(R.drawable.connect)
                                .setCancelable(false)
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor editor=sharedPreferences.edit();
                                        editor.putString("finger","enable");
                                        editor.apply();
                                        logoutUser();
                                        startActivity(new Intent(MainActivity.this,login_activity.class));
                                        finger_set.setText("Enable");


                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.show();



                    }
                });
            }





        //


// logout
            logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setMessage("Do you want to logout..?")
                        .setCancelable(false)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                logoutUser();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {


            }
        });

        if (!sessionManager.isLoggedIn()) {
            logoutUser();
        }

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                String name;
                switch (item.getId()) {
                    case Id_home:
                        name = "Home";
                        break;
                    case id_payment:
                        name = "Payment";

                        break;
                    case id_transcation:
                        name = "Activity";
                        break;
                    case id_contact:
                        name = "Contact";
                        startActivity(new Intent(MainActivity.this,read_user.class));
                        break;

                    default:
                        name = "QR pay";

                }
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                startActivity(new Intent(MainActivity.this,scanned.class));
            }
        });
        bottomNavigation.setCount(id_payment, "Payment");
        bottomNavigation.setCount(id_transcation, "Activity");
        bottomNavigation.setCount(Id_home, "Home");
        bottomNavigation.setCount(id_contact, "User");
        bottomNavigation.setCount(id_qr, "QR pay");
        bottomNavigation.show(id_qr, true);
    }













    public void populatedata(){
        ArrayList<userinfo> userinfoArrayList=db.getUser(value);
        for(int i=0; i<userinfoArrayList.size();i++)
        {
          userinfo uinfo=userinfoArrayList.get(i);
        }
        for(userinfo info:userinfoArrayList)
        {
            View view= LayoutInflater.from(this).inflate(R.layout.list_row,null);
            TextView amout=view.findViewById(R.id.location);
            TextView name_v=view.findViewById(R.id.name);
            TextView ref=view.findViewById(R.id.reference);
            TextView amt=view.findViewById(R.id.designation);
            name_v.setText(info.getName());
            amout.setText(info.getAccount());
            ref.setText(info.getReference());
            amt.setText(info.getAmount());
            getData.addView(view);

        }


    }

    private void logoutUser() {
        sessionManager.setLogin(false, 5);
        // Launching the login activity
        Functions logout = new Functions();
        logout.logoutUser(getApplicationContext());
        Intent intent = new Intent(MainActivity.this, login_activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    //load json
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("user.json");
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
    //
}