package com.example.itadvertsapp;

import static javax.xml.transform.OutputKeys.ENCODING;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
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
    private ImageButton btnDelete, btnEdit;
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
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        contentWebView.getSettings().setDomStorageEnabled(true);
        contentWebView.setWebViewClient(new WebViewClient());
        contentWebView.setWebChromeClient(new WebChromeClient());
        //#endregion
        loadPostContent();


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PostContentActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to delete this post?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletePost();
                                Intent intent = new Intent(PostContentActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostContentActivity.this, EditPostActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);

            }
        });


        SharedPreferences mPrefs = getSharedPreferences("My_Pref",0);
        String token = mPrefs.getString("Authorization", "");
        String email = mPrefs.getString("Email", "");
        String author = mPrefs.getString("Author", "");
        Log.d(TAG, "SharedPreference author: "+author);
        Log.d(TAG, "SharedPreference token: "+token);
        Log.d(TAG, "SharedPreference email: "+email);

        if(token.equals("")){
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        }else if(email.equals(author))
        {
            btnEdit.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        } else
        {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.GONE);
        }

        SharedPreferences preferences = getSharedPreferences("My_Pref", 0);
//        preferences.edit().remove("Email").apply();
        preferences.edit().remove("Author").apply();
    }

    private void deletePost() {
        String url = "https://technical-training-3.herokuapp.com/api/posts/" + postId + "/";
        Log.d(TAG, "deletePost: "+url);
        
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(PostContentActivity.this, "Successfully deleted post", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PostContentActivity.this, "Only the author can delete the post", Toast.LENGTH_SHORT).show();
            }
        }){
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

    public void loadPostContent() {
        progressDialog.show();

        SharedPreferences mPrefs = getSharedPreferences("My_Pref",0);
        String token = mPrefs.getString("Authorization", "");
        Log.d(TAG, "loadPostContent: Token= "+token);
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
                    String category = object.getJSONArray("category").getJSONObject(0).getString("name");
                    Log.d(TAG, "onResponse: category"+category);
                    String salary_from = object.getString("salary_from");
                    String salary_to = object.getString("salary_to");
                    String contact_email = object.getString("contact_email");
                    String author = object.getString("author");
                    String created_on = object.getString("created_on");

                    SharedPreferences mPrefs = getSharedPreferences("My_Pref", 0);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("Author", ""+author);
                    editor.apply();

                    //set data

                    contentTitle.setText(title);
                    contentCompany.setText("Company name: " + company);
                    contentLocation.setText("Location: " + location);
                    contentCategory.setText("Required language: " + category);
                    contentSalary.setText("Salary bracket: " + salary_from + "-" + salary_to + "$");
                    contentContact.setText("Contact:" + contact_email);
                    contentAuthor.setText(author);
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