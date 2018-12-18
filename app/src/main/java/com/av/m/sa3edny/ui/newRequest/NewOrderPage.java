package com.av.m.sa3edny.ui.newRequest;


import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.av.m.sa3edny.R;
import com.av.m.sa3edny.networkUtilities.ApiClient;
import com.av.m.sa3edny.networkUtilities.ApiInterface;
import com.av.m.sa3edny.ui.items.Item;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrderPage extends DialogFragment   {

    RecyclerView recyclerView;
    Button request_btn,cancel_btn;
    LocationManager locationManager;
    double longitude, latitude;
    EditText comment_et;
    ProgressBar progressBar;

    ArrayList<Item> loyaItems;
    MyAdapter myAdapter;

    TextView come_soon_tv;
    LinearLayout content_layout;
    public NewOrderPage() {
        // Required empty public constructor
    }

    public static NewOrderPage newInstance(String title) {
        NewOrderPage frag = new NewOrderPage();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_new_request_dialog, container);
        progressBar=v.findViewById(R.id.progressBar);
      //  comment_et=v.findViewById(R.id.comment_et);
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setHasFixedSize(true);
        loyaItems = new ArrayList<>();
        getSearchItems();

        come_soon_tv=v.findViewById(R.id.come_soon_tv);
        content_layout=v.findViewById(R.id.content_layout);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }




    public void getSearchItems() {
        myAdapter =new MyAdapter(loyaItems,getContext());
        recyclerView.setAdapter(myAdapter);
        hideProgress();
    }




    public void getLocation() {
        showProgress();
        LocationListener locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                //requestOrder();
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(getActivity(), "Please Enable GPS first!", Toast.LENGTH_SHORT).show();

            }
        };
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null);//.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean validateDate(){
        String comment=comment_et.getText().toString();
        /*if(longitude==0){
            Toast.makeText(getContext(), "We have not get your location please try again!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else*/ if(myAdapter.getSelectedItems().size()==0){
            Toast.makeText(getContext(), "Please select some items first!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(comment.equals("")){
            Toast.makeText(getContext(), "Please Enter your comment first!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    public HashMap<String,String> gatheringDate(){
        String comment=comment_et.getText().toString();
        Map<String,String> map=new HashMap<>();
        if(longitude!=0){
            map.put("Longitude", String.valueOf(longitude));
            map.put("Latitude",String.valueOf(latitude));
        }

        if(comment!=null && !comment.equals("")){
            map.put("Comment",comment);
        }
        String desc= myAdapter.getSelectedItems().toString();
        if(desc!=null){
            map.put("Description",desc);
        }
        return (HashMap<String, String>) map;
    }

    public void requestOrder() {
        if (validateDate()) {
            Map <String,String> map=gatheringDate();

            String  body = "{'Description':'"+map.get("Description")+"',' Comment ':'"+map.get("Comment")+"','Longitude':'"+map.get("Longitude")+"','Latitude':'"+map.get("Latitude")+"'}";

            Toast.makeText(getContext(), body, Toast.LENGTH_SHORT).show();

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<String> call = apiInterface.requestOrderApi(body);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        JSONObject object=new JSONObject(response.body());
                        hideProgress();
                        if(object.has("Status")){
                            String status =object.getString("Status");
                            if(status.equals("Success")) {
                               //Toast.makeText(getContext(), "Request sent successfully.", Toast.LENGTH_SHORT).show();
                                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                                alertDialog.setMessage("Request sent successfully.");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                NewOrderPage.this.getDialog().dismiss();
                                            }
                                        });
                                alertDialog.show();
                        }
                            else
                                Toast.makeText(getContext(), status, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    hideProgress();
                }
            });
        }
        else hideProgress();
    }

    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        progressBar.setVisibility(View.INVISIBLE);
    }
}
