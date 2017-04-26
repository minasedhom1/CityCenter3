package com.av.lenovo.sa3edny.services;

import android.app.IntentService;
import android.content.Intent;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.classes.Notification;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Mina on 4/24/2017.
 */

public class CheckNotificationService extends IntentService {

    public CheckNotificationService() {
        super("notificatioThread");
    }
     ArrayList<Notification> newNotifications=new ArrayList<>();
    @Override
    protected void onHandleIntent(Intent intent) {
/*

        try {
    final StringRequest request = new StringRequest(Request.Method.GET, Urls.URL_GET_NOTIFICATIONS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JsonElement root = new JsonParser().parse(response);
                        response = root.getAsString();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Gson gson = new Gson();
                            Notification notification = gson.fromJson(object.toString(), Notification.class);
                            newNotifications.add(notification);
                        }

                        MainActivity.bagde_number.setText(8);
                      //  newNotifications.size() + "", getApplicationContext());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                        Intent localIntent = new Intent("GETNOTS")
//                                // Puts the status into the Intent
//                                .putExtra("NOTIFICATIONS", Variables.notificationList);
//                        // Broadcasts the Intent to receivers in this app.
//
//                        LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(localIntent);

                }

            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Methods.toast(Methods.onErrorVolley(error), getApplicationContext());

        }
    });
    VolleySingleton.getInstance().addToRequestQueue(request);
}catch (Exception e){  e.printStackTrace();
}
*/

    }


}
