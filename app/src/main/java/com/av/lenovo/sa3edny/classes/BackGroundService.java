package com.av.lenovo.sa3edny.classes;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mido on 4/3/2017.
 */

public class BackGroundService extends IntentService {
    public BackGroundService() {
        super("HI SERVICE");
    }

    ArrayList<Item> items;

    @Override
    protected void onHandleIntent(Intent intent) {
        // Gets data from the incoming Intent
        //String dataString = workIntent.getDataString();
/*
        Log.d("INTENT_DATA", intent.getData().toString());
*/
        //items = new ArrayList<>();
        try {
            Methods.getFavIds(getApplicationContext());

            final StringRequest request = new StringRequest(Request.Method.GET, Urls.URL_ALL_ITEM_SEARCH, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        Variables.searchList.clear();
                        JsonElement root = new JsonParser().parse(response);
                        response = root.getAsString();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Item item = new Item();
                            item.setId(jsonObject.getString("ItemID"));
                            item.setName(Html.fromHtml(jsonObject.getString("Name_En")).toString());
                            item.setDescription(Html.fromHtml(jsonObject.getString("Description_En")).toString());
                            item.setPhone1(jsonObject.getString("Phone1"));
                            Variables.searchList.add(item);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent localIntent = new Intent("GETITEMS")
                            // Puts the status into the Intent
                            .putExtra("SEARCHITEMS", Variables.searchList);
                    // Broadcasts the Intent to receivers in this app.

                    LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(localIntent);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                  /*  String message = null;
                    if (volleyError instanceof NetworkError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ServerError) {
                        message = "The server could not be found. Please try again after some time!!";
                    } else if (volleyError instanceof AuthFailureError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof ParseError) {
                        message = "Parsing error! Please try again after some time!!";
                    } else if (volleyError instanceof NoConnectionError) {
                        message = "Cannot connect to Internet...Please check your connection!";
                    } else if (volleyError instanceof TimeoutError) {
                        message = "Connection TimeOut! Please check your internet connection.";
                    }
                    Methods.toast(message,getApplicationContext());*/
                    Methods.toast(Methods.onErrorVolley(volleyError), getApplicationContext());

                }
            });
            VolleySingleton.getInstance().addToRequestQueue(request);
        }catch (Exception e){
            Methods.toast(e.getMessage(),getApplicationContext());}

    }
}