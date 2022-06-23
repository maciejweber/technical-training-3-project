package com.example.itadvertsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPostActivity extends AppCompatActivity {

    private TextView newPostTextView, titleTV, companyTV, contentTV, salary_fromTV,
            salary_toTV, categoryTV, locationTV, contactTV;
    private EditText titleET, companyET, contentET,
            salary_fromET, salary_toET, contactET;
    private Spinner categorySpinner, locationSpinner;
    private ImageView barImage;
    private AppCompatButton btnPost;

    private static final String TAG = "ADD_POST_TAG";

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        //region init UI views
        newPostTextView = findViewById(R.id.newPostTextView);
        titleTV = findViewById(R.id.titleTV);
        companyTV = findViewById(R.id.companyTV);
        contentTV = findViewById(R.id.contentTV);
        salary_fromTV = findViewById(R.id.salary_fromTV);
        salary_toTV = findViewById(R.id.salary_toTV);
        categoryTV = findViewById(R.id.categoryTV);
        locationTV = findViewById(R.id.locationTV);
        contactTV = findViewById(R.id.contactTV);
        titleET = findViewById(R.id.titleET);
        companyET = findViewById(R.id.companyET);
        contentET = findViewById(R.id.contentET);
        salary_fromET = findViewById(R.id.salary_fromET);
        salary_toET = findViewById(R.id.salary_toET);
        contactET = findViewById(R.id.contactET);
        categorySpinner = findViewById(R.id.categorySpinner);
        locationSpinner = findViewById(R.id.locationSpinner);
        barImage = findViewById(R.id.barImage);
        btnPost = findViewById(R.id.btnPost);
        //#endregion

        //spinner adapters

        List<String> locations = Arrays.asList("San Diego", "Los Angeles", "New York", "Houston", "Austin", "San Francisco");
        List<String> categories = Arrays.asList("C++", "C#", "Python", "JavaScript", "Java");

        ArrayAdapter<String> locationsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categories);

        locationSpinner.setAdapter(locationsAdapter);
        categorySpinner.setAdapter(categoriesAdapter);


        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleET.getText().toString().equals("") || companyET.getText().toString().equals("") ||
                        contactET.getText().toString().equals("") || salary_fromET.getText().toString().equals("") ||
                        salary_toET.getText().toString().equals("") || contactET.getText().toString().equals(""))
                {
                    Toast.makeText(AddPostActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if(titleET.getText().length() >= 50)
                {
                    Toast.makeText(AddPostActivity.this, "Your title is too long, max characters allowed = 50", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addNewPost();
                    Intent intent = new Intent(AddPostActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void addNewPost() {
        String title = titleET.getText().toString();
        String company = companyET.getText().toString();
        String content = contentET.getText().toString();
        String salary_from = salary_fromET.getText().toString();
        String salary_to = salary_toET.getText().toString();
        String contact = contactET.getText().toString();
        long location = locationSpinner.getSelectedItemId() + 5;
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(3);

        JSONArray array = new JSONArray();
        for(int i = 0; i<list.size(); i++){
            array.put(list.get(i));
        }

        Log.d(TAG, "RAW ARRAYLIST => " + list);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String object = gson.toJson(list);
        Log.d(TAG, "converted jsonobject => "+object);


        String url = "https://technical-training-3.herokuapp.com/api/posts/";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("company", company);
            jsonObject.put("content", content);
            jsonObject.put("salary_from", salary_from);
            jsonObject.put("salary_to", salary_to);
            jsonObject.put("contact_email", contact);
            jsonObject.put("location", location);
            jsonObject.put("category", array);


            Log.d(TAG, "addNewPost: "+jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(AddPostActivity.this, "New post created", Toast.LENGTH_SHORT).show();
                //Toast.makeText(AddPostActivity.this, "Response "+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Toast.makeText(AddPostActivity.this, "Blad: Podaj poprawne dane", Toast.LENGTH_SHORT).show();
                Toast.makeText(AddPostActivity.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddPostActivity.this);
        requestQueue.add(objectRequest);

    }
}
