package com.av.m.sa3edny.ui.home.categories.addNewItem;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.av.m.sa3edny.R;
import com.av.m.sa3edny.networkUtilities.ApiClient;
import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.av.m.sa3edny.ui.home.categories.cats.Category;
import com.av.m.sa3edny.ui.home.categories.cats.Subcategory;
import com.bumptech.glide.Glide;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


public class AddNewItemFargment extends DialogFragment implements GetCallback.OnGetCatAndSubCatsFinish,GetCallback.OnCreateItemFinish{

    Spinner catSpinner,subCatSpinner;
    ImageView addPhoto,getLatlng;
    public static final int PICK_IMAGE = 1;
    ProgressBar progressBar;
  //  MaterialFancyButton
    Button createItemBtn,cancelBtn;
    EditText itemNameEt,itemDesEt,itemAddressEt,itemPhoneEt;

    String longitude,latitude;
    LocationManager locationManager;
    Vibrator vibrator;


    public static AddNewItemFargment newInstance(String title) {
        AddNewItemFargment frag = new AddNewItemFargment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Model.getCatsAndSubCatsApi(this);
        vibrator=(Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
       locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.fragment_add_new_item_fargment, container, false);

       // Methods.setPath(v,getContext());
        itemNameEt=v.findViewById(R.id.item_name_et);
        itemDesEt=v.findViewById(R.id.item_des_et);
        itemAddressEt=v.findViewById(R.id.item_address_et);
        itemPhoneEt=v.findViewById(R.id.item_phone_et);

        catSpinner=v.findViewById(R.id.cat_spinner);
        subCatSpinner=v.findViewById(R.id.subcat_spinner);
        addPhoto=v.findViewById(R.id.add_item_img);
        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPermissionsPhoto()){
                    pick_from_gallery();
                }
            }
        });

        getLatlng=v.findViewById(R.id.get_latlng_iv);
        getLatlng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(50);
                if(checkPermissions())
                   foo(getActivity());
                   // getLocation();
            }
        });

        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category categoryRes = (Category) adapterView.getAdapter().getItem(i);
                if(categoryRes!=null)
                setEntries(subCatSpinner,categoryRes.getSub_array());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        progressBar=v.findViewById(R.id.progress_bar);
        createItemBtn=v.findViewById(R.id.create_item_btn);
        cancelBtn=v.findViewById(R.id.cancel_btn);
        createItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(validateForms()) {
                        String data = validateData().toString();
                        showProgress();
                        Model.createNewItemApi(data, AddNewItemFargment.this);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewItemFargment.this.getDialog().dismiss();
            }
        });

        return v;
    }
    Uri filePath=null;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            if(data!=null){
                filePath = data.getData();
                Glide.with(getContext()).load(data.getData()).into(addPhoto);
            }
        }
    }

    @Override
    public void onGetCatsSuccess(ArrayList<Category> catList) {
        setEntries(catSpinner,catList);
        hideProgress();
    }

    @Override
    public void onGetCatsFailure(String error) {
        hideProgress();
        toastMsg(error);
        //
        // Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    public void setEntries(Spinner s, ArrayList entries) {
        if (getContext() != null) {
            ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, entries);
            s.setAdapter(spinnerArrayAdapter);
        }
    }

    public void pick_from_gallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public JSONObject validateData() throws JSONException {
        String itemName=itemNameEt.getText().toString();
        String itemDes=itemDesEt.getText().toString();
        String itemPhone=itemPhoneEt.getText().toString();
        String itemAddress=itemAddressEt.getText().toString();
        Category cat= (Category) catSpinner.getSelectedItem();
        String catID="";
        if(cat!=null)
        catID= String.valueOf(cat.getCategoryID());
        Subcategory subcat= (Subcategory) subCatSpinner.getSelectedItem();
        String subCatID="";
        if(subcat!=null)
             subCatID=subcat.getSubcat_id();
        JSONObject object=new JSONObject();
        object.put("Name_En",itemName);
        object.put("CategoryID",catID);
        object.put("SubCategoryID",subCatID);
        object.put("Description_En",itemDes);
        object.put("Phone1",itemPhone);
        object.put("Latitude",latitude);
        object.put("Longitude",longitude);

        return object;
    }

    @Override
    public void onCreateItemSuccess(String id) {
        if(filePath!=null&&id!=null)
            uploadImageToServer(id,filePath);
        hideProgress();

         final Snackbar snackbar = Snackbar
                .make(getView(), "Your Business added to Sa3edny, We will review your request and confirm back for Activation.",Snackbar.LENGTH_INDEFINITE);
               snackbar.setAction("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        //snackbar.getView().setLayoutParams(new ViewGroup.LayoutParams());
        snackbar.show();

        //Toast.makeText(getActivity(), "Your Company/Business has been added to Sa3edny,\nwe will review your request and confirm back for Activation", Toast.LENGTH_LONG).show();
        getActivity().onBackPressed();
    }

    @Override
    public void onCreateItemFailure(String error) {
        hideProgress();
    }


   /* public void getLocation() {
//        progressBar.setVisibility(View.VISIBLE);
        //LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        showProgress();
        locationManager = (LocationManager) getContext().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        try {
            if (locationManager != null) {
                //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,locationListener);//.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,this,null);//.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
            }
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }*/
    public void foo(final Context context) {
        // when you need location
        // if inside activity context = this;
        progressBar.setVisibility(View.VISIBLE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnabled){
            try{
        SingleShotLocationProvider.requestSingleUpdate(context,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(Location location) {
                        Log.d("Location", "my location is " + location.toString());
                        longitude= String.valueOf(location.getLongitude());
                        latitude= String.valueOf(location.getLatitude());
                        try {
                            Geocoder geocoder = new Geocoder(getContext(), new Locale("en"));
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                            String address= addresses.get(0).getAddressLine(0);
                            if(address!=null&&context!=null){
                                itemAddressEt.setText(address);
                                hideProgress();
                                toastMsg("We've got your location successfully.");
                            }
                    }
                    catch (Exception e){
                        hideProgress();
                        toastMsg(e.getMessage());
                        }
                    }
                });
            }catch (Exception e){
                hideProgress();
                toastMsg(e.getMessage());}
        }else{
            toastMsg("Please enable GPS first!");
            hideProgress();
        }
    }
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS_E=0x2;
    private boolean checkPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS);
            }
            return false;
        } else {
            return true;
        }
    }
    private boolean checkPermissionsPhoto() {
        int permissionLocation = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        REQUEST_ID_MULTIPLE_PERMISSIONS_E);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        locationManager=null;
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void uploadImageToServer(String id,Uri imageUri){
        //Uploading code
        try {
            Uri myUri = Uri.parse(imageUri.toString());
            String path = getPathForImage(myUri);
            String uploadId = UUID.randomUUID().toString();
            if(getActivity()!=null)
            new MultipartUploadRequest(getActivity(), uploadId, ApiClient.BASE_URL +"Item/Add_IMGs/Item_IMGs?ItemID="+id)
                    .addFileToUpload(path, "image") //Adding file
                    .addParameter("name", "test") //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(1)
                    .startUpload();
        } catch (Exception exc) {
            toastMsg(exc.getMessage());
            //Toast.makeText(getActivity(), exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getPathForImage(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    private void showProgress(){
        if(getActivity()!=null) {
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        progressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgress(){
        if(getActivity()!=null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    public boolean validateForms(){
        String itemName=itemNameEt.getText().toString();
        String itemDes=itemDesEt.getText().toString();
        String itemPhone=itemPhoneEt.getText().toString();

        if(itemName.isEmpty()||!itemName.matches("[a-zA-Z ]+")){
            itemNameEt.setError("Enter your Business name.(only English characters allowed)");
            return false;
        }
        else if(itemDes.isEmpty()||!itemDes.matches("[a-zA-Z ]+")){
            itemDesEt.setError("Enter your Business description.(only English characters allowed)");
            return false;
        }
        else if(itemPhone.isEmpty()){
            itemPhoneEt.setError("Please Enter your Business phone first.");
            return false;
        }
        else if(latitude==null||longitude==null){
            toastMsg("Click on the GPS icon to get  your business location first.");
            return false;
        }
        else return true;
    }
    public void toastMsg(String s){
        if(getActivity()!=null)
        Toast.makeText(getActivity(),s, Toast.LENGTH_SHORT).show();
    }


       /* @Override
        public void onLocationChanged(Location location) {
            longitude = String.valueOf(location.getLongitude());
            latitude = String.valueOf(location.getLatitude());
            try {
                Geocoder geocoder = new Geocoder(getContext(), new Locale("en"));
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String address= addresses.get(0).getAddressLine(0);
                if(address!=null){
                    itemAddressEt.setText(address);
                }

                hideProgress();
                toastMsg("We've got your location successfully.");
                //Toast.makeText(getActivity(),"We've got your location successfully.",Toast.LENGTH_LONG).show();

            }catch(Exception e){
                Log.d("geocoder",e.getMessage());
                //toastMsg(e.getMessage());
                //Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
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
            toastMsg("Please enable GPS first!");
            //Toast.makeText(getActivity(), "Please enable GPS first!", Toast.LENGTH_SHORT).show();
            hideProgress();
        }
*/

}
