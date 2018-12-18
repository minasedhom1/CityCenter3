package com.av.m.sa3edny.ui;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.av.m.sa3edny.ui.home.categories.addNewItem.AddNewItemFargment;
import com.av.m.sa3edny.ui.login.LoginActivity;
import com.av.m.sa3edny.ui.login.User;
import com.av.m.sa3edny.ui.newRequest.NewOrderPage;
import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.utils.Variables;
import com.av.m.sa3edny.R;
import com.av.m.sa3edny.ui.home.categories.cats.GrandCinema;
import com.av.m.sa3edny.ui.items.ItemsFragment;
import com.av.m.sa3edny.ui.home.categories.notification.NotificationsListFragment;
import com.av.m.sa3edny.ui.home.categories.search.SearchFragment;
import com.av.m.sa3edny.services.BackGroundService;
import com.av.m.sa3edny.classes.GetDataRequest;
import com.av.m.sa3edny.ui.home.categories.cats.CategoriesFragment;

import com.bumptech.glide.Glide;
import com.facebook.Profile;
import com.google.gson.Gson;
import me.leolin.shortcutbadger.ShortcutBadger;

import static com.av.m.sa3edny.utils.Variables.USER_DATA;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


public class MainActivity extends AppCompatActivity
 implements NavigationView.OnNavigationItemSelectedListener {

    public Fragment fragment = null;
    public Class fragmentClass = null;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    //private CallbackManager callbackManager;
    TextView faceName;
    ImageView drawer_profile;
    public static Profile profile;
    View navHed;
    Button tryConnect;
    public static Typeface font;
    public static FloatingActionButton fab;
    NavigationView navigationView;
    TextView bagde_number;

    private com.github.clans.fab.FloatingActionButton request_pickup_btn,my_orders_btn,add_new_shop_btn;

    Button signOut_btn;
    @Override
      protected void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
        String userData=PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(USER_DATA, "");
        Gson gson=new Gson();
        User user=gson.fromJson(userData,User.class);
        if(user!=null)
            Variables.ACCOUNT_ID = user.getAccountID();


        tryConnect= findViewById(R.id.try_connect_btn);
        fab = findViewById(R.id.fab);
        font = Typeface.createFromAsset(getAssets(), "fontawesome/fontawesome-webfont.ttf");
        Toolbar toolbar = findViewById(R.id.toolbar);
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
        /*likebtn= (LikeView) navHed.findViewById(R.id.FB_like_btn);
        likebtn.setObjectIdAndType("https://www.facebook.com/sa3ednyapps/",LikeView.ObjectType.PAGE);

*/      if(user!=null&&user.getName()!=null)
        faceName.setText(user.getName());
        if(user!=null&&user.getImg()!=null)
        Glide.with(getBaseContext()).load(user.getImg()).apply(bitmapTransform(new jp.wasabeef.glide.transformations.CropCircleTransformation())).into(drawer_profile);

        drawer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*fragmentClass = VoucherFragment.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                        Bundle bundle=new Bundle();
                        bundle.putString("ItemID","23");
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).commit();
                        drawer.closeDrawer(GravityCompat.START, true);*/
            }
        });

        signOut_btn=findViewById(R.id.sign_out_btn);
        if(Variables.ACCOUNT_ID==null)
           signOut_btn.setText("Sign in");
           signOut_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Variables.ACCOUNT_ID==null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
                else {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(USER_DATA, null).apply();
                    goLogOutAlert();
                }
               /* startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();*/
               // finishAffinity();
            }
        });

 /*---------------------------------------------------------------------------------------------------------------------------------*/
       /* FacebookSdk.sdkInitialize(MainActivity.this);
        CallbackManager callbackManager = CallbackManager.Factory.create();*/
/*---------------------------------------------------------------------------------------------------------------------------------*/
  //hr@innovativesoftlabs.com - nishantmakhija11@gmail.com
 //check for internet availability
        if(isNetworkAvailable()){
            showEveryThing();
            tryConnect.setVisibility(View.GONE);
        }
        else {
            tryConnect.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this,"No connection, Open it and try again",Toast.LENGTH_LONG).show();
        }

        tryConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    tryConnect.setVisibility(View.GONE);
                    showEveryThing();
                }

                else{
                    Methods.toast("No connection, Open it and try again",MainActivity.this);
                }
            }
        });

        request_pickup_btn =findViewById(R.id.request_pickup_btn);
        request_pickup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                NewOrderPage page= NewOrderPage.newInstance("New Title");
                page.show(fm,"tag");
                //startActivity(new Intent(MainActivity.this,RequestActivity.class));
            }
        });

        add_new_shop_btn=findViewById(R.id.add_new_shop_btn);
        add_new_shop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewItemFargment page=AddNewItemFargment.newInstance("New title");
                page.show(fragmentManager,"tag");
            }
        });

       my_orders_btn=findViewById(R.id.my_orders_btn);
       my_orders_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               MyOrdersFragment page=MyOrdersFragment.newInstance("New title");
               page.show(fragmentManager,"tag");

           }
       });
    }


    private void showEveryThing()
        {
        mainFrag(); //Goods categories

 /*------------------------------------------------------Signature-------------------------------------------------------------------------------------*/
        View notif= findViewById(R.id.notify_icon);
         bagde_number = (TextView) findViewById(R.id.badge_number);
         if(PreferenceManager.getDefaultSharedPreferences(this).getInt("BADGE_NUMBER",-1)>0){
             bagde_number.setVisibility(View.VISIBLE);
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
           // notifyFrag();
            replaceFragment(new NotificationsListFragment(),"Notifications");
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
    @Override
    protected void onResume() {
        super.onResume();
       //startService(new Intent(MainActivity.this, CheckNotificationService.class));
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(getSupportFragmentManager().findFragmentById(R.id.frag_holder).getClass().getSimpleName().equals("CategoriesFragment")){
              finish();
        }
       else if( Variables.ITEM_PATH.equals("Notifications")&&getSupportFragmentManager().findFragmentById(R.id.frag_holder).getClass().getSimpleName().equals("SingleItemFragment")){
                 replaceFragment(new NotificationsListFragment(),"Notifications");
            }

        else if( Variables.ITEM_PATH.equals("Search")&&getSupportFragmentManager().findFragmentById(R.id.frag_holder).getClass().getSimpleName().equals("SingleItemFragment")) {
            replaceFragment(new SearchFragment(),"Search");
        }
/*        else if(getSupportFragmentManager().findFragmentById(R.id.frag_holder).getClass().getSimpleName().equals("CategoriesFragment")){
            super.onBackPressed();
       }*/
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
            if(Variables.ACCOUNT_ID==null){
               // startActivity(new Intent(MainActivity.this, LoginActivity.class));
                fragmentClass = null;
                goLoginAlert();
            }
            else {
                fragmentClass = ItemsFragment.class;
                GetDataRequest.setUrl(Urls.URL_GET_FAVOURITES_FOR_ID+Variables.ACCOUNT_ID); //Favourite.class;
            }
        }
       else if (id == R.id.nav_search) {
            fragmentClass = SearchFragment.class;
        }
/*        else if (id == R.id.nav_addItem) {
            fragmentClass = AddNewItemFargment.class;
        }*/
     /*   else if (id == R.id.nav_contact_us) {
            fragmentClass = ContactUsFragment.class;
        }*/
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(fragmentClass!=null) {
            Variables.ITEM_PATH = item.getTitle().toString();
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit).replace(R.id.frag_holder, fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START, true);
        }
        return true;
    }

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }*/

    public void  mainFrag() {
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

    private void replaceFragment(Fragment targetFragment,String path){
        Variables.ITEM_PATH=path;
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_up,R.anim.slide_down)
                .replace(R.id.frag_holder, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("")
                .commit();
    }

    public void goLoginAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("You need to Login first!")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    public void goLogOutAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to sign out?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog dialog=builder.create();
        dialog.show();
    }



  /* public void getAccID()
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
    }*/



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



 /*public void LoginFB_request()
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
*/


}


