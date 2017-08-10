package com.av.lenovo.sa3edny;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.av.lenovo.sa3edny.classes.ThemeApp;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    final String url="http://sa3ednyservice.azurewebsites.net/Sodic/Setting/GetTheme/Get";
    ThemeApp themeApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_splash);
/*
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JsonElement root = new JsonParser().parse(response);
                        response = root.getAsString();  //not .toString
                        try {
                            JSONObject object = new JSONObject(response);
                            Gson gson = new Gson();
                            themeApp = gson.fromJson(object.toString(), ThemeApp.class);
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("Theme", themeApp));
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error","Volley Error");
            }
        });


      //           VolleySingleton.getInstance().addToRequestQueue(stringRequest);*/

        startActivity(new Intent(SplashActivity.this, MainActivity.class));

    }
}
