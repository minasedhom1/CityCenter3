package com.example.lenovo.citycenter.Assets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.MainActivity;
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Item;
import com.example.lenovo.citycenter.classes.MyItemAdapter;
import com.example.lenovo.citycenter.classes.VolleySingleton;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 26/01/2017.
 */

public class Methods {

    public static void toast(String s, Context context) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
    public static String htmlRender(String ss) {
        ss = ss.replace("span", "font");
        ss = ss.replace("style=\"color:", "color=");
        ss = ss.replace(";\"", "");
        ss = ss.replaceAll("<p>", "");
        ss = ss.replaceAll("</p>", ""); //********
      //  if(ss.contains("<p style=\"text-align: left>"))
        ss=ss.replace("<p style=\"text-align: left>","");
        if(ss.startsWith("<strong"))
        {ss=ss.replace("strong","font");}
        return ss;
    }

    static int count = 0;

    public static void signture(Context context) {
        count++;
        if (count == 10) {
            Methods.toast("Apps-V@lley", context);
            count = 0;
        }
    }

    public static void setPath(View v) {
        TextView path = (TextView) v.findViewById(R.id.item_path_tv);
        path.setTextSize(16);
        path.setText(Html.fromHtml(Variables.ITEM_PATH));
    }

   public static void getFavIds(final Context context) {
        Variables.fav_ids=new ArrayList<>();
        final StringRequest favrequest = new StringRequest(Request.Method.GET, Urls.URL_GET_FAVOURITES_FOR_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JsonElement root = new JsonParser().parse(response);
                            response = root.getAsString();
                            JSONObject jsonObject=new JSONObject(response);
                            JSONArray jsonArray=jsonObject.getJSONArray("ItemsList");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                jsonObject = jsonArray.getJSONObject(i);
                                Variables.fav_ids.add(jsonObject.getString("ItemID"));
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
       if(Variables.ACCOUNT_ID!=null)
       { VolleySingleton.getInstance().addToRequestQueue(favrequest);}

   }
}
/*
    public ArrayList<Item> get_items(String url,Context context) {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JsonElement root = new JsonParser().parse(response);
                    response = root.getAsString();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("ItemsList");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Item item = new Item();
                        item.setId(object.getString("ItemID"));
                        item.setName(Methods.htmlRender(object.getString("Name_En")));
                        item.setDescription(object.getString("Description_En"));
                        item.setPhone1(object.getString("Phone1"));
                        item.setPhone2(object.getString("Phone2"));
                        item.setPhone3(object.getString("Phone3"));
                        item.setPhone4(object.getString("Phone4"));
                        item.setPhone5(object.getString("Phone5"));
                        item.setMenu_url(object.getString("PDF_URL"));
                        if (object.getString("Rate") != "null") {
                            item.setRate(Float.valueOf(object.getString("Rate"))); //get rate and round it implicitly
                            Log.d("rate", Float.valueOf(object.getString("Rate")).toString());
                        }

                        item.setPhoto1(Urls.URL_IMG_PATH + object.getString("Photo1"));
                        item.setCategoryName(object.getString("CategoryName_En"));
                        item.setSubcategoryName(object.getString("SubcategoryName_En"));
                        item.setCategoryID(Variables.catID);

                      *//*  if(fav_ids.size()!=0 && fav_ids.contains(item.getId()))
                        {
                            item.setLike(true);
                        }*//*

                        itemArrayList.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }

                , null);
        RequestQueue queue=Volley.newRequestQueue(context);
        queue.add(request);

    }}*/
