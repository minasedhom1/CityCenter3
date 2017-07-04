package com.av.lenovo.sa3edny;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.classes.ThemeApp;
import com.av.lenovo.sa3edny.fragments.ItemsFragment;
import com.av.lenovo.sa3edny.fragments.NotificationsListFragment;
import com.av.lenovo.sa3edny.fragments.SearchFragment;
import com.av.lenovo.sa3edny.services.BackGroundService;
import com.av.lenovo.sa3edny.classes.GetDataRequest;
import com.av.lenovo.sa3edny.fragments.CategoriesFragment;
import com.av.lenovo.sa3edny.fragments.ContactUsFragment;
import com.av.lenovo.sa3edny.fragments.GrandCinema;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import me.leolin.shortcutbadger.ShortcutBadger;


public class MainActivity extends AppCompatActivity
 implements NavigationView.OnNavigationItemSelectedListener {

    public Fragment fragment = null;
    public Class fragmentClass = null;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    private CallbackManager callbackManager;
    TextView faceName;
    ImageView drawer_profile;
    public static Profile profile;
    View navHed;
    Button tryConnect;
    public static Typeface font;
    public static FloatingActionButton fab;
    NavigationView navigationView;
    LikeView likebtn;
    TextView bagde_number;
    ImageView banner_img;
    ThemeApp themeApp;



    @Override
      protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);


        themeApp=(ThemeApp)getIntent().getSerializableExtra("Theme");
        ImageView logo =(ImageView)findViewById(R.id.logo_header);
        Picasso.with(getBaseContext()).load("http://sa3ednyadmin.azurewebsites.net/IMG/"+themeApp.getAppLogo()).into(logo);
        banner_img = (ImageView) findViewById(R.id.banner_img);
        Picasso.with(this).load("http://sa3ednyadmin.azurewebsites.net/IMG/"+themeApp.getAppBanner()).into(banner_img);


        tryConnect= (Button) findViewById(R.id.try_connect_btn);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        font = Typeface.createFromAsset(getAssets(), "fontawesome/fontawesome-webfont.ttf");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View menu_icon=findViewById(R.id.nav_icon);
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
          navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHed = navigationView.getHeaderView(0);
        faceName = (TextView) navHed.findViewById(R.id.name_tv);
        drawer_profile = (ImageView) navHed.findViewById(R.id.prof_image);
        likebtn= (LikeView) navHed.findViewById(R.id.FB_like_btn);
        likebtn.setObjectIdAndType("https://www.facebook.com/sa3ednyapps/",LikeView.ObjectType.PAGE);

        drawer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  fragmentClass = VoucherFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).commit();
                drawer.closeDrawer(GravityCompat.START, true);*/
            }
        });
 /*---------------------------------------------------------------------------------------------------------------------------------*/
        FacebookSdk.sdkInitialize(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();
 /*---------------------------------------------------------------------------------------------------------------------------------*/

 //check for internet availability
        if(isNetworkAvailable())
        {
            showEveryThing();
            tryConnect.setVisibility(View.GONE);
        }
        else
        {
        tryConnect.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this,"No connection, Open it and try again",Toast.LENGTH_LONG).show();
        }

        tryConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable())
                {   tryConnect.setVisibility(View.GONE);
                    showEveryThing();}
                else
                {Methods.toast("No connection, Open it and try again",MainActivity.this);}
            }
        });

    }


private void showEveryThing()
{
        mainFrag(); //Goods categories

        if (AccessToken.getCurrentAccessToken() != null) //if the user has not logged in yet
        {
            Variables.ACCOUNT_ID = PreferenceManager.getDefaultSharedPreferences(this).getString("AccountID", "NothingFound");
            if (Variables.ACCOUNT_ID.matches("NothingFound")) {
                getAccID();
            } else Log.d("ACCID", Variables.ACCOUNT_ID);

        } else {
            LoginFB_request();
        }
 /*---------------------------------------------------------------------------------------------------------------------------------*/

 /*------------------------------------------------------check if there is a logged in FB acc---------------------------------------------------------------------------*/
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            faceName.setText(profile.getName());
            Picasso.with(getBaseContext()).load(profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(drawer_profile);
        } else {
            drawer_profile.setImageResource(R.mipmap.prof1);
            faceName.setText("");
        }
 /*----------------------------------------------------------Tracks profile changes-----------------------------------------------------------------------*/
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    faceName.setText(currentProfile.getName());
                    //   home_name.setText("Welcome " + currentProfile.getFirstName() + "!");
                    Picasso.with(getBaseContext()).load(currentProfile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(drawer_profile);
                    //     Picasso.with(getBaseContext()).load(currentProfile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(home_prof);
                } else {
                    drawer_profile.setImageResource(R.mipmap.prof1);
                    faceName.setText("");
                }
            }
        };
        profileTracker.startTracking();
 /*-------------------------------------------------------------Notification intent--------------------------------------------------------------------*/

 /*------------------------------------------------------Signature-------------------------------------------------------------------------------------*/
        View notif= findViewById(R.id.notify_icon);
         bagde_number = (TextView) findViewById(R.id.badge_number);
         if(PreferenceManager.getDefaultSharedPreferences(this).getInt("BADGE_NUMBER",-1)>0)
         {  bagde_number.setVisibility(View.VISIBLE);
             bagde_number.setText(PreferenceManager.getDefaultSharedPreferences(this).getInt("BADGE_NUMBER",-1)+"");
         }

        notif.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        bagde_number.setVisibility(View.GONE);
        ShortcutBadger.removeCount(getApplicationContext());
            Variables.badgeCount=0;
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt("BADGE_NUMBER", Variables.badgeCount).apply();
           // Variables.badgeCount=0;
            notifyFrag();
    }

    }
    );

   // if(Variables.badgeCount>0){bagde_number.setText(Variables.badgeCount+"");}

    IntentFilter statusIntentFilter = new IntentFilter("BADGENUM");
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try{

                int b=intent.getIntExtra("BADGENUM",-1);
                Log.d("inBroadCast",b+"..");
                bagde_number.setVisibility(View.VISIBLE);
                bagde_number.setText(b  +"");

            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
            broadcastReceiver,
            statusIntentFilter);
}


/*-------------------------------------------------------------------------------------------------------------------------------------------*/

    private void notifyFrag() {
        Variables.ITEM_PATH="Notifications";
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag_holder);
        if (fragment instanceof NotificationsListFragment) { }
        else
        { fragmentClass = NotificationsListFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).replace(R.id.frag_holder, fragment).commit();
        }

    }



    private void searchFrag() {
        Variables.ITEM_PATH="Search";
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag_holder);
        if (fragment instanceof NotificationsListFragment) { }
        else
        { fragmentClass = SearchFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).replace(R.id.frag_holder, fragment).commit();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
       // startService(new Intent(MainActivity.this, CheckNotificationService.class));
        try {
            if (Variables.searchList.size() == 0) {
                Intent mServiceIntent = new Intent(getApplicationContext(), BackGroundService.class);
                startService(mServiceIntent);
            } else {//getApplication();
        }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void LoginFB_request()
    {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("UserID", AccessToken.getCurrentAccessToken().getUserId());
                getAccID();
            }

            @Override
            public void onCancel() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setMessage("Login Cancelled!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
                Methods.toast("Login Cancelled", MainActivity.this);
                likebtn.setVisibility(View.GONE);
                drawer_profile.setVisibility(View.GONE);
            }

            @Override
            public void onError(FacebookException error) {
                Methods.toast("Error happend", MainActivity.this);
            }
        });
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(fragmentClass.getSimpleName().equals("CategoriesFragment")){
            super.onBackPressed();
        }
       else if( Variables.ITEM_PATH.equals("Notifications")&&getSupportFragmentManager().findFragmentById(R.id.frag_holder).getClass().getSimpleName().equals("SingleItemFragment"))
       {   notifyFrag(); }

        else if( Variables.ITEM_PATH.equals("Search")&&getSupportFragmentManager().findFragmentById(R.id.frag_holder).getClass().getSimpleName().equals("SingleItemFragment"))
        {
            searchFrag();
        }
        else mainFrag();
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation banner_layout item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mallstores) {
            fragmentClass = CategoriesFragment.class;
        } else if (id == R.id.nav_grandcinema) {
            fragmentClass = GrandCinema.class;

        } else if (id == R.id.nav_whatsnew) {
            fragmentClass = ItemsFragment.class; // LatestOffers.class;
            GetDataRequest.setUrl(Urls.URL_GET_NEW_ITEMS);

        } else if (id == R.id.nav_latest_offers) {
           fragmentClass = ItemsFragment.class; // LatestOffers.class;
           GetDataRequest.setUrl(Urls.URL_GET_LATEST_OFFERS_ITEMS );

        } else if (id == R.id.nav_fav) {
          fragmentClass = ItemsFragment.class;
            GetDataRequest.setUrl(Urls.URL_GET_FAVOURITES_FOR_ID); //Favourite.class;
        }
        else if (id == R.id.nav_search) {
            fragmentClass = SearchFragment.class;
        }

        else if (id == R.id.nav_contact_us) {
            fragmentClass = ContactUsFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Variables.ITEM_PATH = item.getTitle().toString();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frag_holder, fragment).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START, true);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void     mainFrag() {
        navigationView.getMenu().getItem(0).setChecked(true);
        fragmentClass = CategoriesFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).commit();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


   public void getAccID()
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
                Methods.toast(Methods.onErrorVolley(error), getApplicationContext());

            }
        });
        VolleySingleton.getInstance().addToRequestQueue(postReq);

    }


    /*    void keyhash() {
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
*/

}


