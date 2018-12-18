package com.av.m.sa3edny.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.av.m.sa3edny.R;
import com.av.m.sa3edny.ui.login.User;
import com.google.gson.Gson;

import static com.av.m.sa3edny.utils.Variables.USER_DATA;

public class SplashActivity extends AppCompatActivity {

    private int splash_timeout=2000;
    private Handler timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initial();
    }

    public void initial(){

        timer=new Handler();
        timer.postDelayed(new Runnable() {
                              @Override
                              public void run() {
                                  try {
                                      String userData=PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(USER_DATA,null);
                           /*           if(userData==null)
                                          startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                      else {*/
                                          Gson gson=new Gson();
                                          User user=gson.fromJson(userData,User.class);
                                          startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra(USER_DATA,user));
                                    //  }
                                  }catch (Exception e){e.printStackTrace();}
                                  finally {
                                      finish();
                                  }
                              }
                          }
                ,splash_timeout );


    }
}

