package com.av.lenovo.sa3edny.fragments;


import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.LoyaltyClass;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.av.lenovo.sa3edny.services.LoyaltyService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoucherFragment extends Fragment {


    public VoucherFragment() {
        // Required empty public constructor
    }

Button loyal_add_points_btn,loyalty_claim_points,loyalty_add_visit,loyal_claim_visit,loyalty_claim_promocode;
    TextView FB_name_voucher_page, shop_name_tv,total_points,total_visits,promoCode;
    Bundle bundle;

    LoyaltyClass loyaltyObject;
    String shop_id,shopname="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle=getArguments();
        shop_id =bundle.getString("ItemID","not found");
        getActivity().startService(new Intent(getContext(), LoyaltyService.class).putExtra("ItemID", shop_id));
        BroadcastReceiver receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loyaltyObject= (LoyaltyClass) intent.getSerializableExtra("loyaltyObject");
                total_points.setText(loyaltyObject.getTotal_point());
                total_visits.setText(loyaltyObject.getTotal_Visite());

                promoCode.setText(loyaltyObject.getPromo_Code());
            }
        };
        IntentFilter filter=new IntentFilter("loyaltyService");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver,filter);

/*        StringRequest stringRequest=new StringRequest(Request.Method.GET,
                Urls.URL_GET_LOYALTY_DATA_FOR_ITEM + shop_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root=new JsonParser().parse(response);
                response = root.getAsString();
                Gson gson=new Gson();
                loyaltyObject= gson.fromJson(response,LoyaltyClass.class);
                loyaltyObject.isVisite();

                total_points.setText(loyaltyObject.getTotal_point());
                total_visits.setText(loyaltyObject.getTotal_Visite());

                    promoCode.setText(loyaltyObject.getPromo_Code());
                   }
        },null);

        VolleySingleton.getInstance().addToRequestQueue(stringRequest);*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_voucher, container, false);



            // ImageView prof_voucher_page= (ImageView) v.findViewById(R.shop_id.prof_voucher_page);
           //   FB_name_voucher_page= (TextView) v.findViewById(R.shop_id.FB_name_voucher_page);
          //  FB_name_voucher_page.setText(MainActivity.profile.getName());
         // Picasso.with(getContext()).load(MainActivity.profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(prof_voucher_page);

        shop_name_tv =(TextView) v.findViewById(R.id.loyalty_shop_name);
        total_points=(TextView) v.findViewById(R.id.total_points_tv);
        total_visits=(TextView) v.findViewById(R.id.total_visits_tv);
        promoCode=(TextView) v.findViewById(R.id.promoCode_tv);

        loyal_add_points_btn= (Button) v.findViewById(R.id.loyal_add_points_btn);
        loyal_add_points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_add_points();
            }
        });
        loyalty_claim_points= (Button) v.findViewById(R.id.loyalty_claim_points);

        loyalty_claim_points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_claim_points();
            }
        });


        loyalty_add_visit= (Button) v.findViewById(R.id.loyalty_add_visit);
        loyalty_add_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loyaltyObject.isVisite())
                    popup_add_visit();
                else Toast.makeText(getContext(),"This item not providing visits claiming",Toast.LENGTH_SHORT).show();
            }


        });

        loyal_claim_visit= (Button) v.findViewById(R.id.loyal_claim_visit);
        loyal_claim_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup_claim_visits();
            }
        });
        loyalty_claim_promocode=(Button) v.findViewById(R.id.loyalty_claim_promocode);
         loyalty_claim_promocode.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 popup_claim_promo();
             }
         });


        bundle=getArguments();
        shopname=bundle.getString("ItemName");
        shop_name_tv.setText(shopname);
        String s=bundle.getString("ItemID","not found");
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
        return v;
    }

    private void popup_add_points() {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.popup_loyalty_add_points);
        TextView shop_name_add_points= (TextView) nagDialog.findViewById(R.id.shop_name_add_points);
        shop_name_add_points.setText(shopname);
        final EditText shop_passcode= (EditText) nagDialog.findViewById(R.id.shop_passcode_add_points);
        final EditText order_num= (EditText) nagDialog.findViewById(R.id.order_num_add_points);
        final EditText bill_amount= (EditText) nagDialog.findViewById(R.id.bill_amount_add_points);
        Button loyal_verifiy_btn= (Button) nagDialog.findViewById(R.id.loyal_verifiy_btn);
        loyal_verifiy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=shop_passcode.getText().toString() , order=order_num.getText().toString(), amount=bill_amount.getText().toString();
              if(!pass.matches("") && !order.matches("") && !amount.matches("")  )

              { add_points(pass,order,amount);
                  shop_passcode.setText("");
                  order_num.setText("");
                  bill_amount.setText("");
              }
                else {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Passcode is Wrong!")
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
                     }
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

    public void add_points(String passcode,String order_num,String bill_amount)
    {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_POST_ADD_POINTS(Variables.ACCOUNT_ID,
                bundle.getString("ItemID","not found"),passcode,bill_amount,order_num), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
          //      root = new JsonParser().parse(root.getAsString());   //double parse
                response = root.getAsString();
                response.matches("");
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("Status");

                        Toast.makeText(getContext(),status,Toast.LENGTH_SHORT).show();
                        getActivity().startService(new Intent(getContext(), LoyaltyService.class).putExtra("ItemID", shop_id));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        VolleySingleton.getInstance().addToRequestQueue(postReq);
    }



    private void popup_add_visit() {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.popup_loyalty_add_visit);
        TextView shopname_add_visit= (TextView) nagDialog.findViewById(R.id.shop_name_add_visit);
        shopname_add_visit.setText(shopname);
        final EditText shop_passcode= (EditText) nagDialog.findViewById(R.id.shop_passcode_add_visit);
        Button loyal_verifiy_btn= (Button) nagDialog.findViewById(R.id.verifiy_btn_add_visit);
        loyal_verifiy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=shop_passcode.getText().toString();
                if(!pass.matches("") )

                {
                    add_visits(pass);
                    shop_passcode.setText("");
                }

                else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setMessage("Passcode is Wrong!")
                            .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    alertDialog.show();
                }
            }
        });
        Button loyal_cancel_btn= (Button) nagDialog.findViewById(R.id.cancel_btn_add_visit);
        loyal_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
            }
        });
        nagDialog.setCancelable(false);
        nagDialog.show();
    }



    public void add_visits(String passcode)
    {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_POST_ADD_VISITS(Variables.ACCOUNT_ID,
                shop_id,passcode), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("Status");
                    Toast.makeText(getContext(),status,Toast.LENGTH_SHORT).show();
                    refreshData();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        VolleySingleton.getInstance().addToRequestQueue(postReq);
    }



    private void popup_claim_promo() {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.popup_loyalty_add_visit);
        TextView shopname_claim_promo= (TextView) nagDialog.findViewById(R.id.shop_name_add_visit);
        final EditText shop_passcode= (EditText) nagDialog.findViewById(R.id.shop_passcode_add_visit);
        Button loyal_verifiy_btn= (Button) nagDialog.findViewById(R.id.verifiy_btn_add_visit);
        shopname_claim_promo.setText(shopname);
        loyal_verifiy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass=shop_passcode.getText().toString();
                if(!pass.matches("") )

                { claim_promo(pass);
                  shop_passcode.setText("");
                }
                else {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                    alertDialog.setMessage("Passcode is Wrong!")
                            .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    alertDialog.show();
                }
            }
        });
        Button loyal_cancel_btn= (Button) nagDialog.findViewById(R.id.cancel_btn_add_visit);
        loyal_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
            }
        });
        nagDialog.setCancelable(false);
        nagDialog.show();
    }


    public void claim_promo(String passcode)
    {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_POST_CLAIM_PROMO(Variables.ACCOUNT_ID,
                bundle.getString("ItemID","not found"),passcode), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("Status");
                    Toast.makeText(getContext(),status,Toast.LENGTH_SHORT).show();
                    refreshData();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        VolleySingleton.getInstance().addToRequestQueue(postReq);
    }


    private void popup_claim_points() {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.pop_up_claim_points);
        Button level1= (Button) nagDialog.findViewById(R.id.level1_btn_claim_points);
        Button level2= (Button) nagDialog.findViewById(R.id.level2_btn_claim_points);
        Button level3= (Button) nagDialog.findViewById(R.id.level3_btn_claim_points);
        TextView details= (TextView) nagDialog.findViewById(R.id.description_tv_popup_claim_loyalty);
        TextView cancel= (TextView) nagDialog.findViewById(R.id.cancel_tv_popup_claim_loyalty);
        final EditText shop_passcode_et = (EditText) nagDialog.findViewById(R.id.shop_passcode_claim_points);
        final ArrayList<LoyaltyClass.PointLevel> pointLevels=loyaltyObject.getPointsLevel();
        level1.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(pointLevels.get(0).isHaveLevel())
                 {
                     if(!shop_passcode_et.getText().toString().equals(""))
                     claim_points(shop_passcode_et.getText().toString(),pointLevels.get(0).getLevelNumber());
                     else
                         Toast.makeText(getContext(),"please Enter Passcode",Toast.LENGTH_SHORT).show();

                 }
                 else
                     {
                     Toast.makeText(getContext(),"You don't have enough Points for using this level!",Toast.LENGTH_SHORT).show();
                     }
             }
         });
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointLevels.get(1).isHaveLevel())
                {
                    if(!shop_passcode_et.getText().toString().equals(""))
                        claim_points(shop_passcode_et.getText().toString(),pointLevels.get(1).getLevelNumber());
                    else
                        Toast.makeText(getContext(),"please Enter Passcode",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(),"You don't have enough Points for using this level!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pointLevels.get(2).isHaveLevel())
                {
                    if(!shop_passcode_et.getText().toString().equals(""))
                        claim_points(shop_passcode_et.getText().toString(),pointLevels.get(2).getLevelNumber());
                    else
                        Toast.makeText(getContext(),"please Enter Passcode",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(),"You don't have enough Points for using this level!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog description_dialog = new Dialog(getContext());
                description_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                description_dialog.setContentView(R.layout.popup_claim_levels_description);
                TextView level1_des= (TextView) description_dialog.findViewById(R.id.level1_tv_description);
                TextView level2_des= (TextView) description_dialog.findViewById(R.id.level2_tv_description);
                TextView level3_des= (TextView) description_dialog.findViewById(R.id.level3_tv_description);
                level1_des.setText("Level One:  " + pointLevels.get(0).getDescription());
                level2_des.setText("Level Two:  " + pointLevels.get(1).getDescription());
                level3_des.setText("Level Three:  " + pointLevels.get(2).getDescription());
                description_dialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
            }
        });

        nagDialog.show();
    }


    public void claim_points(String passcode,String level)
    {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_POST_CLAIM_POINTS(Variables.ACCOUNT_ID,
                shop_id,passcode,level), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("Status");
                    Toast.makeText(getContext(),status,Toast.LENGTH_SHORT).show();
                    refreshData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        VolleySingleton.getInstance().addToRequestQueue(postReq);
    }

    private void popup_claim_visits() {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.pop_up_claim_points);
        final Button level1= (Button) nagDialog.findViewById(R.id.level1_btn_claim_points);
        Button level2= (Button) nagDialog.findViewById(R.id.level2_btn_claim_points);
        Button level3= (Button) nagDialog.findViewById(R.id.level3_btn_claim_points);
        TextView details= (TextView) nagDialog.findViewById(R.id.description_tv_popup_claim_loyalty);
        TextView cancel= (TextView) nagDialog.findViewById(R.id.cancel_tv_popup_claim_loyalty);
        final EditText shop_passcode_et = (EditText) nagDialog.findViewById(R.id.shop_passcode_claim_points);
        final ArrayList<LoyaltyClass.VisiteLevel> visiteLevels=loyaltyObject.getVisiteLevel();
        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(visiteLevels.get(0).isHaveLevel())
                {
                    if(!shop_passcode_et.getText().toString().equals(""))
                        claim_visits(shop_passcode_et.getText().toString(),visiteLevels.get(0).getLevelNumber());
                    else
                        Toast.makeText(getContext(),"please Enter Passcode",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(),"You don't have enough Visits to use this level!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visiteLevels.get(1).isHaveLevel())
                {
                    if(!shop_passcode_et.getText().toString().equals(""))
                        claim_visits(shop_passcode_et.getText().toString(),visiteLevels.get(1).getLevelNumber());
                    else
                        Toast.makeText(getContext(),"please Enter Passcode",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(),"You don't have enough Visits to use this level!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(visiteLevels.get(2).isHaveLevel())
                {
                    if(!shop_passcode_et.getText().toString().equals(""))
                        claim_visits(shop_passcode_et.getText().toString(),visiteLevels.get(2).getLevelNumber());
                    else
                        Toast.makeText(getContext(),"please Enter Passcode",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(getContext(),"You don't have enough Visits to use this level!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog description_dialog = new Dialog(getContext());
                description_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                description_dialog.setContentView(R.layout.popup_claim_levels_description);
                TextView level1_des= (TextView) description_dialog.findViewById(R.id.level1_tv_description);
                TextView level2_des= (TextView) description_dialog.findViewById(R.id.level2_tv_description);
                TextView level3_des= (TextView) description_dialog.findViewById(R.id.level3_tv_description);
                level1_des.setText("Level One:  " + visiteLevels.get(0).getDescription());
                level2_des.setText("Level Two:  " + visiteLevels.get(1).getDescription());
                level3_des.setText("Level Three:  " + visiteLevels.get(2).getDescription());
                description_dialog.show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
            }
        });


        nagDialog.show();
    }

    public void claim_visits(String passcode,String level)
    {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_POST_CLAIM_VISITS(Variables.ACCOUNT_ID,
                shop_id,passcode,level), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.getString("Status");
                    Toast.makeText(getContext(),status,Toast.LENGTH_SHORT).show();
                    refreshData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, null);
        VolleySingleton.getInstance().addToRequestQueue(postReq);
    }



















    void refreshData()
    {
        getActivity().startService(new Intent(getContext(), LoyaltyService.class).putExtra("ItemID", shop_id));

    }

}
