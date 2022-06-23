package com.example.itadvertsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView postsRV;
    private ImageButton addBtn;

    private String url = "";
    private ArrayList<ModelPost> postArrayList;
    private AdapterPost adapterPost;
    private ProgressDialog progressDialog;

    private static final String TAG = "MAIN_TAG";


    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences("My_Pref", 0);
        preferences.edit().remove("Authorization").apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postsRV = findViewById(R.id.postsRV);
        addBtn = findViewById(R.id.addBtn);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");

        postArrayList = new ArrayList<>();
        postArrayList.clear();

        loadPosts();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences mPrefs = getSharedPreferences("My_Pref",0);
                String token = mPrefs.getString("Authorization", "");

                if(token.equals("")){
                    Toast.makeText(MainActivity.this, "Log in to add post", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                    startActivity(intent);
                }

            }
        });



    }

    private void loadPosts() {
        progressDialog.show();

        url = "https://technical-training-3.herokuapp.com/api/posts/";
        Log.d(TAG, "loadPosts: URL = "+url);

        //GET Request

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: "+response);

                //try for json data
                try{
                    //get data
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i <= jsonArray.length()-1; i++){
                        try{
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String title = object.getString("title");
                            String company = object.getString("company");
//                            String category = "placeholder";
                            String category = object.getJSONArray("category").getJSONObject(0).getString("name");
                            String salary_from = object.getString("salary_from");
                            String salary_to = object.getString("salary_to");
                            String location = object.getString("location");
                            String formatted_date = object.getString("formatted_date");

                            ModelPost modelPost = new ModelPost(
                                    ""+ id,
                                    ""+ title,
                                    ""+ company + " / " + formatted_date,
                                    "Language: "+category,
                                    "Salary bracket: "+ salary_from + "-" +salary_to,
                                    "",
                                    ""+ location,
                                    ""
                            );
                            postArrayList.add(modelPost);

                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse catch 1: "+e.getMessage());
                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    //adapter set up
                    adapterPost = new AdapterPost(MainActivity.this, postArrayList);
                    postsRV.setAdapter(adapterPost);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    Log.d(TAG, "onResponse catch 2: "+e.getMessage());
                    Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                Toast.makeText(MainActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        //request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}