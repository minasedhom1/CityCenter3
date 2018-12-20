package com.av.m.sa3edny.ui.newRequest;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.av.m.sa3edny.R;
import com.av.m.sa3edny.networkUtilities.ApiClient;
import com.av.m.sa3edny.networkUtilities.ApiInterface;
import com.av.m.sa3edny.ui.newRequest.dataModel.OrderAddress;
import com.av.m.sa3edny.utils.FilePath;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemOrderPage extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.OnConnectionFailedListener
        {

   // RecyclerView recyclerView;
    ImageView attach_file_iv,first_attach_remove_btn,second_attach_remove_btn,
            third_attach_remove_btn,remove_record_btn,send_audio_iv,back_btn;
    FrameLayout first_attach_layout,second_attach_layout,third_attach_layout,record_layout;
    ArrayList<FrameLayout> attach_list;
    int attachments_num=0;
    public static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAPTURE_IMAGE_REQUEST =2;
    public static final int CAPTURE_AUDIO_REQUEST =3;
    public static final int PLACE_PICKER_REQUEST=4;
    TextView request_btn;//,cancel_btn;
    ArrayList<String> filePaths;
   // Button add_address_btn;
    ProgressBar progressBar;
    EditText comment_et;

    private Location myLocation;
    private GoogleApiClient googleApiClient;

    private final static int REQUEST_ID_FINE_LOCATION_PERMISSIONS=6;
    private final static int REQUEST_CHECK_SETTINGS_GPS=5;

    private  boolean TAG_CURRENT_LOCATION = true ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_order_page);
        progressBar=findViewById(R.id.progress_bar);
        comment_et=findViewById(R.id.comment_et);
        attach_list=new ArrayList<>();
        filePaths=new ArrayList<>(3);
        /*recyclerView = findViewById(R.id.recycler_view);
        //setLayout Manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        MyAdapterTest myAdapter =new MyAdapterTest(getApplicationContext());
        recyclerView.setAdapter(myAdapter);
*/

        first_attach_layout=findViewById(R.id.first_attach_layout);
        second_attach_layout=findViewById(R.id.second_attach_layout);
        third_attach_layout=findViewById(R.id.third_attach_layout);

        attach_list.add(first_attach_layout);
        attach_list.add(second_attach_layout);
        attach_list.add(third_attach_layout);

        first_attach_remove_btn=findViewById(R.id.first_attach_remove_btn);
        first_attach_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachments_num--;
                first_attach_layout.setVisibility(View.GONE);
                filePaths.remove(0);
            }
        });
        second_attach_remove_btn=findViewById(R.id.second_attach_remove_btn);
        second_attach_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachments_num--;
                second_attach_layout.setVisibility(View.GONE);
                filePaths.remove(attachments_num);
            }
        });
        third_attach_remove_btn=findViewById(R.id.third_attach_remove_btn);
        third_attach_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                third_attach_layout.setVisibility(View.GONE);
                attachments_num--;
                filePaths.remove(attachments_num);
            }
        });

        attach_file_iv=findViewById(R.id.attach_file_iv);
        attach_file_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attachments_num==3){
                    Toast.makeText(ItemOrderPage.this, "Max 3 files.", Toast.LENGTH_SHORT).show();
                }
                else {
                    showPopup(view);

                }

            }
        });
        record_layout=findViewById(R.id.record_layout);
        remove_record_btn=findViewById(R.id.remove_record_btn);
        remove_record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                record_layout.setVisibility(View.GONE);
            }
        });
        send_audio_iv=findViewById(R.id.send_audio_iv);
        send_audio_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecorder();
            }
        });

        request_btn=findViewById(R.id.request_btn);
        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpGClient();
                getMyLocation();
                //ItemOrderPage.this.finish();
            }
            });
       /* cancel_btn=findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ItemOrderPage.this, "Order Cancelled", Toast.LENGTH_SHORT).show();
                ItemOrderPage.this.finish();
            }
        });*/

        back_btn=findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

       /* add_address_btn=findViewById(R.id.add_address_btn);
        add_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open_location_picker(view);
               // startActivity(new Intent(ItemOrderPage.this,PickLocationActivity.class));
                setUpGClient();
                getMyLocation();
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data!=null){
                try {
                    final InputStream imageStream = getContentResolver().openInputStream(data.getData());
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    Bitmap deoded=compressJpgPhoto(selectedImage);
                    Uri tempUri = getImageUri(getApplicationContext(),deoded);
                    add_img(tempUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == CAPTURE_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
            if(data!=null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                try {
                    Bitmap deoded=compressJpgPhoto(photo);
                    Uri tempUri = getImageUri(getApplicationContext(),deoded);
                    add_img(tempUri);
                } catch (IOException e) {
                e.printStackTrace();
            }
          }
        }
        else if(requestCode == CAPTURE_AUDIO_REQUEST && resultCode == Activity.RESULT_OK){
                if(data!=null){
                    add_audio(data.getData());
                }
        }
        else if(requestCode==REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode){
                case  Activity.RESULT_OK:
                      getMyLocation();
                    break;
                case  Activity.RESULT_CANCELED:
                     // checkPermissions();
                      popupAddress(new OrderAddress(),false);
                    break;
            }
        }

    }
    ImageView iv;
    public void add_img(Uri uri){
        String filePath = FilePath.getPath(ItemOrderPage.this,uri);
        filePaths.add(filePath);
        for (FrameLayout fl : attach_list) {
            if (fl.getVisibility() == View.GONE) {
                fl.setVisibility(View.VISIBLE);
                if(fl.getTag().toString().equals("one")) {
                    iv=fl.findViewById(R.id.first_attach_iv);
                }else if(fl.getTag().toString().equals("two")) {
                    iv=fl.findViewById(R.id.second_attach_iv);
                }
                else
                    iv=fl.findViewById(R.id.third_attach_iv);
                    Glide.with(getApplicationContext()).load(uri).into(iv);
                    attachments_num++;
                break;
            }
        }
    }

    public void add_audio(Uri uri){
        String filePath = FilePath.getPath(ItemOrderPage.this,uri);
        filePaths.add(filePath);
        record_layout.setVisibility(View.VISIBLE);
    }


    private static ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

    public void makeOrder(){
            showProgress();
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            // Multiple Images
            for (int i = 0; i < filePaths.size(); i++) {
                File file = new File(filePaths.get(i));
                RequestBody requestImage = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                builder.addFormDataPart("event_images[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            }
            builder.addFormDataPart("Mobile", myAddress.getMobile());
            if(myAddress.getCountry()!=null)
          //      builder.addFormDataPart("Country", myAddress.getCountry());
          //  builder.addFormDataPart("City", myAddress.getCity());
            builder.addFormDataPart("District", myAddress.getDistrict());
            if(myAddress.getBuilding()!=null)
                builder.addFormDataPart("Building", myAddress.getBuilding());
            builder.addFormDataPart("Latitude", myAddress.getLat());
            builder.addFormDataPart("Longitude", myAddress.getLng());
          /*  if(myAddress.getFloor()!=null)
                builder.addFormDataPart("Floor", myAddress.getFloor());*/
            if(myAddress.getApartment()!=null)
                builder.addFormDataPart("Apartment", myAddress.getApartment());
            if(!comment_et.getText().toString().matches(""))
                builder.addFormDataPart("Description",comment_et.getText().toString());
            builder.addFormDataPart("ItemID", "125");
          //  builder.addFormDataPart("Description", "Descrptionnnn");
            // String mobile="010000000",id="152";
            //RequestBody name = RequestBody.create(MediaType.parse("text/plain"),mobile );
            // RequestBody itemId = RequestBody.create(MediaType.parse("text/plain"), id);
            final MultipartBody requestBody = builder.build();
            Call<String> call = apiInterface.makeOrderApi(requestBody);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        try {
                            JSONObject object=new JSONObject(response.body());
                            if(object.getString("Status").equals("Error"))
                                Toast.makeText(ItemOrderPage.this, "Error happened", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(ItemOrderPage.this, "Order sent successfully", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                         hideProgress();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ItemOrderPage.this, "Order failed", Toast.LENGTH_SHORT).show();
                    hideProgress();
                }
            });
    }

    public void showPopup(View v) {
        android.support.v7.widget.PopupMenu popup = new android.support.v7.widget.PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_select:
                        pickImage();
                        return true;
                    case R.id.menu_camera:
                        captureImage();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);
            }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void openRecorder(){
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, CAPTURE_AUDIO_REQUEST);
    }

    private Bitmap compressJpgPhoto(Bitmap original) throws IOException {
       // Bitmap original = BitmapFactory.decodeStream(getAssets().open("imagg1.jpg"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 70, out);
        Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
        return decoded;
    }

   /* public void open_location_picker(View v){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            // for activty
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
            // for fragment
            //startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }*/

    private void popupAddress(final OrderAddress address,boolean allowGPS){

        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();

        final Dialog popup = new Dialog(ItemOrderPage.this);
        popup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup.setContentView(R.layout.popup_add_order_address);
        final EditText mobile_et=popup.findViewById(R.id.mobile_et);
        //final EditText city_et=popup.findViewById(R.id.city_et);
        final EditText district_et=popup.findViewById(R.id.district_et);
       // final EditText street_et=popup.findViewById(R.id.street_et);
        final EditText build_no_et=popup.findViewById(R.id.build_no_et);
        //final EditText floor_no_et=popup.findViewById(R.id.floor_no_et);
        final EditText apart_no_et=popup.findViewById(R.id.apart_no_et);
        //final TextView change_address_tv=popup.findViewById(R.id.change_address_popup_tv);
       // city_et.setText(address.getCity());
       // district_et.setText(address.getDistrict());
       // street_et.setText(address.getStreet());
       /* change_address_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
                open_location_picker(view);
            }
        });*/

        Button save_btn=popup.findViewById(R.id.save_address_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEmpty(mobile_et)){//||isEmpty(city_et)||isEmpty(district_et)){
                    return;
                }
                else {
                    String mobile = mobile_et.getText().toString();
                    //String city = city_et.getText().toString();
                    String district = district_et.getText().toString();
                   // String street = street_et.getText().toString();
                    String build_no = build_no_et.getText().toString();
                   // String floor_no = floor_no_et.getText().toString();
                    String apart_no = apart_no_et.getText().toString();

                    //address.setCity(city);
                    address.setMobile(mobile);
                    address.setDistrict(district);
                    //address.setStreet(street);
                    address.setBuilding(build_no);
                    //address.setFloor(floor_no);
                    address.setApartment(apart_no);
                    myAddress=address;
                    makeOrder();
                    Toast.makeText(ItemOrderPage.this,"Your Address saved.",Toast.LENGTH_LONG).show();
                   // add_address_btn.setText("Edit Address");
                    popup.dismiss();
                }
            }
        });

        Button cancel=popup.findViewById(R.id.cancel_address_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup.dismiss();
            }
        });

        final ImageView gps_indicator =popup.findViewById(R.id.gps_indicator_iv);
        if(allowGPS)
            ImageViewCompat.setImageTintList(gps_indicator, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.dimmed_green)));

        popup.setCancelable(false);
        popup.show();
    }

    OrderAddress myAddress;
    public boolean isEmpty(EditText text){
        CharSequence str= text.getText().toString();
        if(TextUtils.isEmpty(str)) text.setError("Required!");
        else text.setError(null);
        return TextUtils.isEmpty(str);
    }

    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }

    LocationManager locationManager;
    String latitude,longitude;

   /* public void getCurrentLocation() {
        showProgress();
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = String.valueOf(location.getLongitude());
                latitude = String.valueOf(location.getLatitude());

                try {
                    Geocoder geocoder = new Geocoder(ItemOrderPage.this, new Locale("ar"));
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    //      String []address= addresses.get(0).getAddressLine(0).split("،");
                    Toast.makeText(ItemOrderPage.this,addresses.get(0).getAdminArea(),Toast.LENGTH_LONG).show();

                    hideProgress();
                    Toast.makeText(ItemOrderPage.this,"تم الحصول على بيانات موقعك بنجاح.",Toast.LENGTH_LONG).show();

                }catch(Exception e){
                    Toast.makeText(ItemOrderPage.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
                hideProgress();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(ItemOrderPage.this, "الرجاء تفعيل خاصية ال GPS اولا.", Toast.LENGTH_SHORT).show();
                hideProgress();
            }
        };

        try {
            locationManager = (LocationManager)ItemOrderPage.this.getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null);//.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }

        catch(SecurityException e) {
            e.printStackTrace();
        }
    }
*/
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x1;

  /*  private boolean checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(ItemOrderPage.this,listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
            return false;
        } else {
            return true;
        }
    }*/

    private void getMyLocation(){

        showProgress();
        if(googleApiClient!=null) {
            if (googleApiClient.isConnected())
            {
                try{
                    if(ItemOrderPage.this!=null){
                        int permissionLocation = ContextCompat.checkSelfPermission(ItemOrderPage.this,
                                Manifest.permission.ACCESS_FINE_LOCATION);
                        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                            myLocation =     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                            LocationRequest locationRequest = new LocationRequest();
                            locationRequest.setInterval(3000);
                            locationRequest.setFastestInterval(3000);
                            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                            builder.setAlwaysShow(true);
                            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                            PendingResult<LocationSettingsResult> result =LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                                @Override
                                public void onResult(LocationSettingsResult result) {
                                    final Status status = result.getStatus();
                                    switch (status.getStatusCode()) {
                                        case LocationSettingsStatusCodes.SUCCESS:
                                            int permissionLocation = ContextCompat.checkSelfPermission(ItemOrderPage.this, Manifest.permission.ACCESS_FINE_LOCATION);
                                            if (permissionLocation == PackageManager.PERMISSION_GRANTED)
                                            {
                                                myLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                            try
                                            {
                                                status.startResolutionForResult(ItemOrderPage.this,REQUEST_CHECK_SETTINGS_GPS);
                                                hideProgress();

                                            }
                                            catch (IntentSender.SendIntentException e)
                                            {
                                            }
                                            break;
                                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                            hideProgress();
                                            break;
                                    }
                                }
                            });
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(getApplicationContext()!=null)
            checkPermissions();

    }


    private void checkPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(ItemOrderPage.this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ID_FINE_LOCATION_PERMISSIONS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            getMyLocation();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        hideProgress();
        myLocation = location;
        if (myLocation != null) {
                Double latitude = myLocation.getLatitude();
                Double longitude = myLocation.getLongitude();
                OrderAddress address=new OrderAddress();
                address.setLng(String.valueOf(longitude));
                address.setLat(String.valueOf(latitude));
                Toast.makeText(this,String.valueOf(latitude),Toast.LENGTH_SHORT).show();
                popupAddress(address,true);
               /* try {
                    String languageToLoad = "ar_EG";
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Geocoder geocoder = new Geocoder(ItemOrderPage.this, locale);
                    List<Address> listAddresses = null;
                    listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                    if (null != listAddresses && listAddresses.size() > 0) {
                        String country = listAddresses.get(0).getCountryName(); //country
                        String city = listAddresses.get(0).getAdminArea();//city
                        String region = listAddresses.get(0).getSubAdminArea();//region

                        address.setCity(city);
                        address.setDistrict(region);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }

    private synchronized void setUpGClient()
    {
        if(googleApiClient == null || !googleApiClient.isConnected()){
            try {
                googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                        .enableAutoManage(this, 0, this)
                        .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();

                    googleApiClient.connect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.d("GARG", "***** on Stop ***** ");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            Log.d("GARG", "***** on Stop mGoogleApiClient disconnect ***** ");

            googleApiClient.stopAutoManage(this);
            googleApiClient.disconnect();
        }
        super.onStop();
    }
}
