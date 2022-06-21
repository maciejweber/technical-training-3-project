package com.example.itadvertsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton btnLogin, btnRegister, btnGuest;
    private EditText emailEditText_login, passwordEditText_login;
    private TextView titleTextView;
    private ImageView logoImage, barImage;
    private ProgressDialog progressDialog;

    private static final String TAG = "LOGIN_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region init views
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        emailEditText_login = findViewById(R.id.emailEditText_login);
        passwordEditText_login = findViewById(R.id.passwordEditText_login);
        titleTextView = findViewById(R.id.titleTextView);
        logoImage = findViewById(R.id.logoImage);
        barImage = findViewById(R.id.barImage);
        btnGuest = findViewById(R.id.btnGuest);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        //#endregion

        btnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("My_Pref", 0);
                preferences.edit().remove("Authorization").apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(emailEditText_login.getText().toString().equals("")|| passwordEditText_login.getText().toString().equals("")){

                    Toast.makeText(LoginActivity.this, "Both fields are required", Toast.LENGTH_SHORT).show();

                } else
                {
                    userLogin();

                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        progressDialog.show();
        String email = emailEditText_login.getText().toString();
        String password = passwordEditText_login.getText().toString();

        String url = "https://technical-training-3.herokuapp.com/api/auth/login/";

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("email", email);
            jsonObject.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "userLogin error: "+e.getMessage());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onResponse: "+response);
                        Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();

                        try {
                            String auth = response.getString("token");
                            SharedPreferences mPrefs = getSharedPreferences("My_Pref", 0);
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putString("Authorization", "Token "+auth);
                            editor.apply();

                            String token = mPrefs.getString("Authorization", "");
                            Toast.makeText(LoginActivity.this, "TOKEN ="+token, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

}