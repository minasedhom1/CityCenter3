package com.av.lenovo.sa3edny.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.classes.VolleySingleton;

/**
 * Created by Mina on 4/24/2017.
 */

public class CheckNotificationService extends IntentService {

    public CheckNotificationService() {
        super("notificatioThread");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
         Log.d("CheckNotif","onHanldeIntent");
        Intent localIntent = new Intent("GETBADGE")
                // Puts the status into the Intent
                .putExtra("BADGE", 1 );
        // Broadcasts the Intent to receivers in this app.
        Log.d("BageInService","done");

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }


}
