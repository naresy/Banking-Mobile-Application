package com.example.connect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.connect.helper.DatabaseHandler;
import com.example.connect.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class finger_print extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ProgressDialog pDialog;
    String FinalJSonObject,ParseResult,fetching_url="https://lmaniya.com.np/hunt_new/get_user.php",fetch_id,fetch_email,fetch_name,fetch_crt;
    HashMap<String,String> ResultHash = new HashMap<>();
    HttpParse httpParse = new HttpParse();
    private SessionManager session;
    private DatabaseHandler db;
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);

        sharedPreferences = getSharedPreferences("biometric", 0);

        String unique_id = sharedPreferences.getString("unique_id", "");


        db = new DatabaseHandler(getApplicationContext());
        pDialog=new ProgressDialog(this);

        // session manager
        session = new SessionManager(getApplicationContext());

        TextView msgtex = findViewById(R.id.msgtext);
        final LottieAnimationView loginbutton = findViewById(R.id.login);

        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("You can use the fingerprint sensor to login");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("This device doesnot have a fingerprint sensor");
                loginbutton.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("The biometric sensor is currently unavailable");
                loginbutton.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("Your device doesn't have fingerprint saved,please check your security settings");
                loginbutton.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(finger_print.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                hashCode();
                HttpWebCall(unique_id);


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("QR")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });
    }




        private void HttpWebCall(final String PreviousListViewClickedItem){

            class HttpWebCallFunction extends AsyncTask<String,Void,String> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    pDialog = ProgressDialog.show(finger_print.this,"Loading Data",null,true,true);
                    pDialog.setCanceledOnTouchOutside(false);
                }

                @Override
                protected void onPostExecute(String httpResponseMsg) {


                    super.onPostExecute(httpResponseMsg);


                    pDialog.dismiss();

                    //Storing Complete JSon Object into String Variable.
                    FinalJSonObject = httpResponseMsg ;

                    //Parsing the Stored JSOn String to GetHttpResponse Method.
                    new finger_print.GetHttpResponse(finger_print.this).execute();

                }

                @Override
                protected String doInBackground(String... params) {
                    ResultHash.put("unique_id",params[0]);
                    ParseResult = httpParse.postRequest(ResultHash, fetching_url);

                    return ParseResult;
                }
            }

            HttpWebCallFunction httpWebCallFunction = new HttpWebCallFunction();

            httpWebCallFunction.execute(PreviousListViewClickedItem);
        }


        // Parsing Complete JSON Object.
        @SuppressLint("StaticFieldLeak")
        private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
            public Context context;

            public GetHttpResponse(Context context) {
                this.context = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... arg0) {
                try {
                    if (FinalJSonObject != null) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(FinalJSonObject);

                            JSONObject jsonObject;

                            for (int i = 0; i < jsonArray.length(); i++) {
                                jsonObject = jsonArray.getJSONObject(i);

                                fetch_id = jsonObject.getString("unique_id");
                                fetch_email = jsonObject.getString("email");
                                fetch_name = jsonObject.getString("name");
                                fetch_crt = jsonObject.getString("created_at");


                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                db.addUser(fetch_id, fetch_name, fetch_email, fetch_crt);
                Intent upanel = new Intent(finger_print.this, MainActivity.class);
                upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(upanel);
                session.setLogin(true,1);
                finish();

            }
        }

}
