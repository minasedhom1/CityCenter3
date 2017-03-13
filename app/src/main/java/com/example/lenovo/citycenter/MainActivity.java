package com.example.lenovo.citycenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.classes.Item;
import com.example.lenovo.citycenter.Fragments.Categories;
import com.example.lenovo.citycenter.Fragments.ContactUs;
import com.example.lenovo.citycenter.Fragments.Favourite;
import com.example.lenovo.citycenter.Fragments.GrandCinema;
import com.example.lenovo.citycenter.Fragments.LatestOffers;
import com.example.lenovo.citycenter.Fragments.Notifications;
import com.example.lenovo.citycenter.Fragments.WhatsNew;
import com.example.lenovo.citycenter.classes.VolleySingleton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.LikeView;
import com.google.android.gms.common.api.BooleanResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Fragment fragment = null;
    public Class fragmentClass = null;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private CallbackManager callbackManager;
    TextView faceName;
    ImageView imageView;
    Profile profile;
    View navHed;
  //  RequestQueue queue;
    public  static  Typeface font;
    private static String DEVICE_TOKEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFrag();
     //    DEVICE_TOKEN = FirebaseInstanceId.getInstance().getToken();
         font= Typeface.createFromAsset(getAssets(), "fontawesome/fontawesome-webfont.ttf" );
         Log.d("token_device",PreferenceManager.getDefaultSharedPreferences(this).getString("TOKEN","null")) ;

/*---------------------------------------------------------------------------------------------------------------------------------*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        //toggle.setHomeAsUpIndicator(R.drawable.nav_icon);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        toggle.setHomeAsUpIndicator(R.drawable.nav_icon);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHed = navigationView.getHeaderView(0);
        faceName = (TextView) navHed.findViewById(R.id.name_tv);
        imageView = (ImageView) navHed.findViewById(R.id.prof_image);

       /* View header_view = findViewById(R.id.header_layout);
        final ImageView home_prof = (ImageView) header_view.findViewById(R.id.home_prof);
        final TextView home_name = (TextView) header_view.findViewById(R.id.home_name);
        LikeView likeView = (LikeView) header_view.findViewById(R.id.fb_like_btn);
        likeView.setLikeViewStyle(LikeView.Style.BUTTON);
        likeView.setObjectIdAndType("https://www.facebook.com/sa3ednyapps/", LikeView.ObjectType.PAGE);*/
 /*---------------------------------------------------------------------------------------------------------------------------------*/
        FacebookSdk.sdkInitialize(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();
      //  queue = Volley.newRequestQueue(MainActivity.this);
 /*---------------------------------------------------------------------------------------------------------------------------------*/

        if (AccessToken.getCurrentAccessToken() != null)
        {
            Variables.ACCOUNT_ID = PreferenceManager.getDefaultSharedPreferences(this).getString("AccountID", "defaultStringIfNothingFound");
            Log.d("ACCID",Variables.ACCOUNT_ID);

/*            if(Variables.ACCOUNT_ID==null)
                getAccID();*/
        }
      else {
            LoginManager loginManager = LoginManager.getInstance();
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {

                    Log.d("UserID", AccessToken.getCurrentAccessToken().getUserId());
                    getAccID();
                }
                @Override
                public void onCancel() {
                    AlertDialog.Builder alertDialog =new AlertDialog.Builder(MainActivity.this) ;
                    alertDialog.setMessage("Are you sure you do NOT want to login?")
                            .setIcon(R.mipmap.staron)
                    .setNegativeButton("No,I'll Signin next time", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                    Methods.toast("Login Cancelled",MainActivity.this);
                }
                @Override
                public void onError(FacebookException error) {
                    Methods.toast("Error happend",MainActivity.this);
                }
            });
          loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
 /*---------------------------------------------------------------------------------------------------------------------------------*/


 /*------------------------------------------------------check if ther is a logged in FB acc---------------------------------------------------------------------------*/
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            faceName.setText(profile.getName());
         //   home_name.setText("Welcome " + profile.getFirstName() + "!");
            Picasso.with(getBaseContext()).load(profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(imageView);
        //    Picasso.with(getBaseContext()).load(profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(home_prof);
        } else {
            imageView.setImageResource(R.mipmap.prof1);
            faceName.setText("");
        }

 /*----------------------------------------------------------Tracks profile changes-----------------------------------------------------------------------*/
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    faceName.setText(currentProfile.getName());
                 //   home_name.setText("Welcome " + currentProfile.getFirstName() + "!");
                    Picasso.with(getBaseContext()).load(currentProfile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(imageView);
               //     Picasso.with(getBaseContext()).load(currentProfile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(home_prof);
                } else {
                    imageView.setImageResource(R.mipmap.prof1);
                    faceName.setText("");
                }
            }
        };
        profileTracker.startTracking();
 /*-------------------------------------------------------------check if token is saved or not--------------------------------------------------------------------*/
      //Boolean b=PreferenceManager.getDefaultSharedPreferences(this).getBoolean("TOKEN_SAVED",false);
      //  add_device_token();  //add your device token to DB
       // add_device_token();
 /*--------------------------------------------------------------------------------------------------------------------------------------------*/

 /*------------------------------------------------------Signature-------------------------------------------------------------------------------------*/
        View image = findViewById(R.id.logo_header);
        image.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {Methods.signture(MainActivity.this);}});
/*-------------------------------------------------------------------------------------------------------------------------------------------*/

    }


    @Override
    public void onBackPressed() {
/*     Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frag_holder);
        Methods.toast(currentFragment.getClass().getSimpleName(),this);*/
       // fragmentManager.popBackStackImmediate();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mallstores) {
            fragmentClass = Categories.class;
        } else if (id == R.id.nav_grandcinema) {
            fragmentClass = GrandCinema.class;

        } else if (id == R.id.nav_whatsnew) {
            fragmentClass = WhatsNew.class;

        } else if (id == R.id.nav_latest_offers) {
            fragmentClass = LatestOffers.class;

        } else if (id == R.id.nav_fav) {
            fragmentClass = Favourite.class;


        }/* else if (id == R.id.nav_notify) {
            fragmentClass = Notifications.class;

        } */
        else if (id == R.id.nav_contact_us) {
            fragmentClass = ContactUs.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
//.addToBackStack(fragment.getClass().getName()) //for back stack
        Variables.ITEM_PATH=item.getTitle().toString();
        fragmentManager.beginTransaction().replace(R.id.frag_holder, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START, true);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    void mainFrag() {
        fragmentClass = Categories.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().addToBackStack("cat").replace(R.id.frag_holder, fragment).commit();
    }

    /*public void add_device_token() {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_ADD_DEVICE_TOKEN + DEVICE_TOKEN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                root = new JsonParser().parse(root.getAsString());   //double parse
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("Status");
                    if(status.matches("Success saved")||status.matches("Already Exists"))
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("TOKEN_SAVED", true).apply();
                    Toast.makeText(MainActivity.this, status, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        queue.add(postReq);
    }*/




    void keyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.lenovo.citycenter",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
}

    void getAccID()
    {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_POST_FBID_GET_ACC_ID + AccessToken.getCurrentAccessToken().getUserId(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                root = new JsonParser().parse(root.getAsString());   //double parse
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    Variables.ACCOUNT_ID = obj.getString("ID");
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("AccountID", Variables.ACCOUNT_ID).apply();
                    // mainFrag();
                    Log.d("ACCOUNTID", Variables.ACCOUNT_ID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
           }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(error.toString(),MainActivity.this);
            }
        });
        // RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
     //   queue.add(postReq);
        VolleySingleton.getInstance().addToRequestQueue(postReq);
       // queue.start();
    }
}


