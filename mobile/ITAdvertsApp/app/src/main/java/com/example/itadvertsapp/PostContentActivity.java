package com.example.itadvertsapp;

import static javax.xml.transform.OutputKeys.ENCODING;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostContentActivity extends AppCompatActivity {

    private WebView contentWebView;
    private TextView contentTitle,contentCompany,contentLocation,contentCategory,
            contentSalary,contentContact,contentAuthor,contentDate;
    private String postId;
    private ProgressDialog progressDialog;
    private String category;
    private static final String TAG = "POST_CONTENT_TAG";

    private ActionBar actionBar;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PostContentActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_content);

//        actionBar = getSupportActionBar();
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading post content...");

        postId = getIntent().getStringExtra("postId");
        Log.d(TAG, "onCreate: POST ID= "+postId);


        //region init views
        contentWebView = findViewById(R.id.contentWebView);
        contentTitle = findViewById(R.id.contentTitle);
        contentCompany = findViewById(R.id.contentCompany);
        contentLocation = findViewById(R.id.contentLocation);
        contentCategory = findViewById(R.id.contentCategory);
        contentSalary = findViewById(R.id.contentSalary);
        contentContact = findViewById(R.id.contentContact);
        contentAuthor = findViewById(R.id.contentAuthor);
        contentDate = findViewById(R.id.contentDate);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setDomStorageEnabled(true);
        contentWebView.setWebViewClient(new WebViewClient());
        contentWebView.setWebChromeClient(new WebChromeClient());
        //#endregion

        loadPostContent();
    }

    private void loadPostContent() {
        progressDialog.show();

        SharedPreferences mPrefs = getSharedPreferences("My_Pref",0);
        String token = mPrefs.getString("Authorization", "");
        Toast.makeText(this, "POST CONTENT TOKEN="+token, Toast.LENGTH_SHORT).show();
        String url = "https://technical-training-3.herokuapp.com/api/posts/" + postId;
        Log.d(TAG, "loadPostContent: URL = " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(TAG, "onResponse: " + response);

                try {
                    //get data
                    JSONObject object = new JSONObject(response);
                    String title = object.getString("title");
                    String content = object.getString("content");
                    String company = object.getString("company");
                    String location = object.getJSONObject("location").getString("name");

//                    String language = "";
//                    for(int i = 0; i<object.getJSONArray("category").length(); i++){
//                        language = object.getJSONArray("category").getJSONObject(i).getString("name");
//                        Toast.makeText(PostContentActivity.this, ""+language, Toast.LENGTH_SHORT).show();
//                    }

                    String category = object.getJSONArray("category").getJSONObject(0).getString("name");
                    Log.d(TAG, "onResponse: category"+category);
                    String salary_from = object.getString("salary_from");
                    String salary_to = object.getString("salary_to");
                    String contact_email = object.getString("contact_email");
                    String author = object.getString("author");
                    String created_on = object.getString("created_on");

                    // date conversion
                    String gmtDate = created_on;
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); //   2022-06-06T06:53:00-07:00
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy K:mm a"); // 06/06/2022 15:53
                    String formattedDate = "";
                    try {
                        Date date = dateFormat.parse(gmtDate);
                        formattedDate = dateFormat2.format(date);


                    } catch (Exception e) {
                        formattedDate = created_on;
                        e.printStackTrace();
                    }
                    //set data

                    contentTitle.setText(title);
                    contentCompany.setText("Company name: " + company);
                    contentLocation.setText("Located in " + location);
                    contentCategory.setText("Required language: " + category);
                    contentSalary.setText("Salary bracket: " + salary_from + "-" + salary_to);
                    contentContact.setText("Contact us: " + contact_email);
                    contentAuthor.setText("Post by user: "+author);
                    contentDate.setText(created_on);
                    contentWebView.loadDataWithBaseURL(null, content, "text/html", ENCODING, null);

//                    }

                } catch (JSONException e) {
                    Log.d(TAG, "onResponse: " + e.getMessage());
                    Toast.makeText(PostContentActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostContentActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences mPrefs = getSharedPreferences("My_Pref",0);
                String token = mPrefs.getString("Authorization", "");
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Authorization", ""+token);
                return hashMap;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}