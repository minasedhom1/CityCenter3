package com.example.lenovo.citycenter.Assets;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.classes.Item;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 26/01/2017.
 */

public class Methods {

    public static void toast(String s, Context context)
    {
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
    public static String htmlRender(String ss)

    {
        ss=ss.replace("span","font");
        ss=ss.replace("style=\"color:","color=");
        ss=ss.replace(";\"","");
        ss=ss.replaceAll("<p>","");
        ss=ss.replaceAll("</p>",""); //********
        return ss;
    }

   public static ArrayList<Item> getFavourites( AppCompatActivity activity) {
        final ArrayList<Item> subCat_array = new ArrayList();

        Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.GET, Urls.URL_GET_FAVOURITES_FOR_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root=new JsonParser().parse(response);
                response = root.getAsString();
                JSONArray jsonArray;
                try {
                    JSONObject jsonObject= new JSONObject(response);

                    jsonArray=jsonObject.getJSONArray("allFav");

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        object= object.getJSONObject("fav");
                        Item item=new Item();
                        item.setName(htmlRender(object.getString("Name_En")));
                        item.setDescription(htmlRender(object.getString("Description_En")));
                       // item.setPhone1(object.getString("Phone1"));
                        item.setPhoto1("https://sa3ednymalladmin.azurewebsites.net/IMG/"+object.getString("Photo1"));
                       // item.setCategoryName(object.getString("CategoryName_En"));
                        subCat_array.add(item);
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }

        },null)
        );
        return subCat_array;

    }
}
