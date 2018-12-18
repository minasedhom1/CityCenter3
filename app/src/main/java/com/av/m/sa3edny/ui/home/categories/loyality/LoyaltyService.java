package com.av.m.sa3edny.ui.home.categories.loyality;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.classes.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * Created by Mina on 7/25/2017.
 */

public class LoyaltyService extends IntentService {

    public LoyaltyService()
    {
        super("");
    }
    LoyaltyClass loyaltyObject;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        loyaltyObject=new LoyaltyClass();

        String id=intent.getStringExtra("ItemID");

        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                Urls.URL_GET_LOYALTY_DATA_FOR_ITEM + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root=new JsonParser().parse(response);
                response = root.getAsString();
                Gson gson=new Gson();
                loyaltyObject= gson.fromJson(response,LoyaltyClass.class);
                loyaltyObject.isVisite();
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent("loyaltyService").putExtra("loyaltyObject",loyaltyObject));
            }
        },null);

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }
}
