package com.av.m.sa3edny.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.av.m.sa3edny.utils.Variables;
import com.av.m.sa3edny.ui.MainActivity;
import com.google.firebase.messaging.RemoteMessage;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by mido on 2/14/2017.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
   static String title,msg;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("tag", "From: " + remoteMessage.getFrom());
//        Log.d("tag", "Notification Message Body: " + remoteMessage.getNotification().getBody());
      /* title=remoteMessage.getNotification().getTitle();*/
     /*   msg=remoteMessage.getNotification().getBody();*/
        //getNotification()
        sendNotification(remoteMessage.getData().get("body"));//  Toast.makeText(MyFirebaseMessagingService.this.getApplicationContext(),remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();


        ShortcutBadger.applyCount(getApplicationContext(), ++Variables.badgeCount );
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("BADGE_NUMBER", Variables.badgeCount).apply();

        Intent localIntent = new Intent("BADGENUM")
                // Puts the status into the Intent
                .putExtra("BADGENUM", PreferenceManager.getDefaultSharedPreferences(this).getInt("BADGE_NUMBER",-1));
        // Broadcasts the Intent to receivers in this app.

        LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(localIntent);
    }

    private void sendNotification(String msg) {
        //MainActivity.bagde_number.setVisibility(View.VISIBLE);
        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.getApplicationContext());
                        mBuilder.setSmallIcon(getApplicationInfo().icon) //get application icon
                        //    .setNumber(++increase)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setContentTitle("Sa3edny")
                        .setContentText(msg);
        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(1, mBuilder.build());
    }
}
