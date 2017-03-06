package com.example.lenovo.citycenter.Assets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
    static int count=0;
    public static void signture(Context context){count++;if(count==10){Methods.toast("Apps-V@lley",context);count=0;}}
}
