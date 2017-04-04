package com.example.lenovo.citycenter.classes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.Fragments.SearchFragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mido on 4/2/2017.
 */

public class GetAllItemsService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
JSONArray jsonArray;
    JSONObject jsonObject;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Methods.getFavIds(getApplicationContext());
/*        final StringRequest request1=new StringRequest(Request.Method.GET, Urls.URL_ALL_ITEM_SEARCH,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();
                    jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        Item item=new Item();
                        item.setId(jsonObject.getString("ItemID"));
                        item.setName(Html.fromHtml(jsonObject.getString("Name_En")).toString());
                        item.setDescription(Html.fromHtml(jsonObject.getString("Description_En")).toString());
                        item.setPhone1(jsonObject.getString("Phone1"));

                        //  itemArrayList.add(item);
                        Variables.searchList.add(item);
                    }

                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(error.toString(),getApplicationContext());
            }});*/

        final StringRequest request=new StringRequest(Request.Method.GET,Urls.URL_GET_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Variables.notificationList.clear();
                            JsonElement root=new JsonParser().parse(response);
                            response = root.getAsString();
                            jsonArray=new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Gson gson = new Gson();
                                Notification notification=gson.fromJson(object.toString(),Notification.class);
                                Variables.notificationList.add(notification);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(error.toString(),getApplicationContext());

            }});
       // VolleySingleton.getInstance().addToRequestQueue(request1);
        VolleySingleton.getInstance().addToRequestQueue(request);
        return super.onStartCommand(intent, flags, startId);
    }
}
