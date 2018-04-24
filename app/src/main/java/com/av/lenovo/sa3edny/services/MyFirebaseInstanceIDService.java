package com.av.lenovo.sa3edny.services;

import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.utils.Urls;
import com.av.lenovo.sa3edny.utils.Variables;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mina on 2/14/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();


    @Override
    public  void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("tok",refreshedToken);
        Variables.DEVICE_TOKEN=refreshedToken;
        // Saving reg id to shared preferences
         storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_ADD_DEVICE_TOKEN + token, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                root = new JsonParser().parse(root.getAsString());   //double parse
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("Status");
                    if(status.matches("Success saved")||status.matches("Already Exists"))
                    { PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("TOKEN_SAVED", true).apply();
                   // Toast.makeText(getApplicationContext(), "Notification is Working!", Toast.LENGTH_LONG).show();
                        }
                    else Toast.makeText(getApplicationContext(), "Notification is NOT Working!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
       // RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        Boolean b=PreferenceManager.getDefaultSharedPreferences(this).getBoolean("TOKEN_SAVED",false);
        if(!b){
          //  queue.add(postReq);
            VolleySingleton.getInstance().addToRequestQueue(postReq);
        }
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

  private void storeRegIdInPref(String token)
  {
      PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("TOKEN",token).apply();
  }


}
