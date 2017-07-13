package com.av.lenovo.sa3edny.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.LoyaltyClass;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoucherFragment extends Fragment {


    public VoucherFragment() {
        // Required empty public constructor
    }

Button loyal_add_points_btn,loyalty_claim_points,loyalty_add_visit,loyal_claim_visit,loyalty_claim_promocode;
    TextView FB_name_voucher_page,shop_name,total_points,total_visits,promoCode;
    Bundle bundle;

LoyaltyClass loyaltyObject;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=getArguments();
        String id=bundle.getString("ItemID","not found");



        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                Urls.URL_GET_LOYALTY_DATA_FOR_ITEM + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root=new JsonParser().parse(response);
                response = root.getAsString();
                Gson gson=new Gson();
                loyaltyObject= gson.fromJson(response,LoyaltyClass.class);
                loyaltyObject.isVisite();

                total_points.setText(loyaltyObject.getTotal_point());
                total_visits.setText(loyaltyObject.getTotal_Visite());
                if(!loyaltyObject.getPromo_Code().equals("null"))
                {
                    promoCode.setText(loyaltyObject.getPromo_Code());
                }
            }
        },null);
        VolleySingleton.getInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_voucher, container, false);



            // ImageView prof_voucher_page= (ImageView) v.findViewById(R.id.prof_voucher_page);
           //   FB_name_voucher_page= (TextView) v.findViewById(R.id.FB_name_voucher_page);
          //  FB_name_voucher_page.setText(MainActivity.profile.getName());
         // Picasso.with(getContext()).load(MainActivity.profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(prof_voucher_page);

        shop_name=(TextView) v.findViewById(R.id.loyalty_shop_name);
        total_points=(TextView) v.findViewById(R.id.total_points_tv);
        total_visits=(TextView) v.findViewById(R.id.total_visits_tv);
        promoCode=(TextView) v.findViewById(R.id.promoCode_tv);

        loyal_add_points_btn= (Button) v.findViewById(R.id.loyal_add_points_btn);
        loyal_add_points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp();
            }
        });
        loyalty_claim_points= (Button) v.findViewById(R.id.loyalty_claim_points);
        loyal_add_points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp();
            }
        });



        loyalty_add_visit= (Button) v.findViewById(R.id.loyalty_add_visit);
        loyal_claim_visit= (Button) v.findViewById(R.id.loyal_claim_visit);
        loyalty_claim_promocode=(Button) v.findViewById(R.id.loyalty_claim_promocode);



        bundle=getArguments();
        shop_name.setText(bundle.getString("ItemName"));
        String s=bundle.getString("ItemID","not found");
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
        return v;

    }

    private void popUp() {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.popup_loyalty);
        Button loyal_verifiy_btn= (Button) nagDialog.findViewById(R.id.loyal_verifiy_btn);
        loyal_verifiy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Passcode is Wrong!")
                        .setIcon(R.mipmap.staron)
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });
        Button loyal_cancel_btn= (Button) nagDialog.findViewById(R.id.loyal_cancel_btn);
        loyal_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
            }
        });
        nagDialog.setCancelable(false);
        nagDialog.show();
    }
}
