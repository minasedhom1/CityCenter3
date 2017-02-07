package com.example.lenovo.citycenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.Classes.Category;
import com.example.lenovo.citycenter.Fragments.Categories;
import com.example.lenovo.citycenter.Fragments.ContactUs;
import com.example.lenovo.citycenter.Fragments.Favourite;
import com.example.lenovo.citycenter.Fragments.GrandCinema;
import com.example.lenovo.citycenter.Fragments.LatestOffers;
import com.example.lenovo.citycenter.Fragments.Notifications;
import com.example.lenovo.citycenter.Fragments.WhatsNew;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.widget.LikeView;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static String ACCOUNT_ID;

    private GoogleApiClient client;

    /*----------*/
    public Fragment fragment = null;
    public Class fragmentClass = null;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    /*-----------------*/
    int count = 0;
    /*-----------------*/
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    TextView faceName;
    ImageView imageView;
    Profile profile;
    View navHed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(MainActivity.this);
        callbackManager = CallbackManager.Factory.create();

        LoginManager loginManager=LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken tok;
                tok = AccessToken.getCurrentAccessToken();
                Log.d("UserID", tok.getUserId());
               // Toast.makeText(MainActivity.this, tok.getUserId(),Toast.LENGTH_LONG).show();
                StringRequest postReq=new StringRequest(Request.Method.POST, Variables.URL_POST_FBID_GET_ACC_ID +tok.getUserId(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonElement root=new JsonParser().parse(response);
                        root=new JsonParser().parse(root.getAsString());   //double parse
                        response = root.getAsString();
                        try {
                            JSONObject obj =new JSONObject(response);
                            ACCOUNT_ID = obj.getString("ID");
                           // Toast.makeText(MainActivity.this,Variables.ACCOUNT_ID,Toast.LENGTH_LONG).show();
                            Log.d("ACCOUNTID",ACCOUNT_ID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(MainActivity.this,response,Toast.LENGTH_LONG).show();

                    }
                },null);
                RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
                queue.add(postReq);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));

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
        //navigationView.getMenu().getItem(0).setChecked(true);
        navHed = navigationView.getHeaderView(0);
        faceName = (TextView) navHed.findViewById(R.id.name_tv);
        imageView = (ImageView) navHed.findViewById(R.id.prof_image);

        View header_view=findViewById(R.id.header_layout);
        final ImageView home_prof= (ImageView) findViewById(R.id.home_prof);
        final TextView home_name= (TextView) findViewById(R.id.home_name);
        LikeView likeView= (LikeView) findViewById(R.id.fb_like_btn);
        likeView.setLikeViewStyle(LikeView.Style.BUTTON);
        likeView.setObjectIdAndType("https://www.facebook.com/sa3ednyapps/",LikeView.ObjectType.PAGE);


        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    faceName.setText(currentProfile.getName());
                    home_name.setText("Welcome "+currentProfile.getFirstName()+"!");
                    Picasso.with(getBaseContext()).load(currentProfile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(imageView);
                    Picasso.with(getBaseContext()).load(currentProfile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(home_prof);

                } else {
                    imageView.setImageResource(R.mipmap.ic_launcher);
                    faceName.setText("no00000");
                }
            }
        };
        profileTracker.startTracking();

        profile = Profile.getCurrentProfile();
        if (profile != null) {
            faceName.setText(profile.getName());
            home_name.setText("Welcome "+profile.getFirstName()+"!");
            Picasso.with(getBaseContext()).load(profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(imageView);
            Picasso.with(getBaseContext()).load(profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(home_prof);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher);
            faceName.setText("none");
        }

       mainFrag();

        View image = findViewById(R.id.logo_header);
        image.setSoundEffectsEnabled(false);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signture();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }


    @Override
    public void onBackPressed() {
        // startActivity(new Intent(getBaseContext(),MainActivity.class));
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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


        } else if (id == R.id.nav_notify) {
            fragmentClass = Notifications.class;

        } else if (id == R.id.nav_contact_us) {
            fragmentClass = ContactUs.class;

        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void signture() {
        count++;
        if (count == 10) {
            Toast.makeText(MainActivity.this, "Mina Raafat \n 01275791088 ", Toast.LENGTH_SHORT).show();
            count = 0;
        }
    }



























 /*-----------------------------------------------------------------*/

 /*   public class MyCustomListAdapter extends ArrayAdapter<category> {


        public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<category> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            try {

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.category_group, parent, false);

                categoryIcon = (ImageView) view.findViewById(R.id.shopPic);
                shopName = (TextView) view.findViewById(R.id.shopNameTextView);
                shopDetails = (TextView) view.findViewById(R.id.shopDetailsTextview);
                explore = (Button) view.findViewById(R.id.exploe_btn);

                myCategory = categoryArrayList.get(position);

                shopName.setText(Html.fromHtml(myCategory.get_name()), TextView.BufferType.SPANNABLE);
                shopDetails.setText(myCategory.get_details());
*/
/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    /*            Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(myCategory.get_icon())).getBitmap();
                Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 20, 20, mpaint);// Round Image Corner 100 100 100 100
                categoryIcon.setImageBitmap(imageRounded);
                categoryIcon.setPadding(10,10,10,10);*/
/*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/
    //  categoryIcon.setImageResource(myCategory.get_icon());
/*
                shopName.setTextSize(16f);
                explore.setTextSize(13f);
                explore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                    }
                });

                return view;

            } catch (Exception e) {
                return null;
            }

        }


    }*/


    void faceBookStuff() {



     }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    void mainFrag()
    {
        fragmentClass = Categories.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager.beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
    }
}