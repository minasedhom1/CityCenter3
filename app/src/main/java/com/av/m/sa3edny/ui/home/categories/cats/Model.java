package com.av.m.sa3edny.ui.home.categories.cats;



import android.support.v7.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.av.m.sa3edny.classes.CacheRequest;
import com.av.m.sa3edny.classes.VolleySingleton;
import com.av.m.sa3edny.networkUtilities.ApiClient;
import com.av.m.sa3edny.networkUtilities.ApiInterface;
import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mina on 6/19/2018.
 */

public class Model {

    private static ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

    public static void getGoods(final GetCallback.OnGetCatAndSubCatsFinish listener){
        Call<String> call = apiInterface.getGoods();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    ArrayList<Category> catList = new ArrayList<>();
                    JSONArray jsonArray=new JSONArray(response.body());
                    Gson gson=new Gson();
                    for(int i=0;i<jsonArray.length();i++){
                        Category cat=gson.fromJson(jsonArray.get(i).toString(),Category.class);
                        catList.add(cat);
                    }
                    listener.onGetCatsSuccess(catList);
                } catch (JSONException e) {
                    listener.onGetCatsFailure(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onGetCatsFailure(t.getMessage());
            }
        });
    }
    public static void getServices(final GetCallback.OnGetCatAndSubCatsFinish listener){
        Call<String> call = apiInterface.getServices();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    ArrayList<Category> catList = new ArrayList<>();
                    JSONArray jsonArray=new JSONArray(response.body());
                    Gson gson=new Gson();
                    for(int i=0;i<jsonArray.length();i++){
                        Category cat=gson.fromJson(jsonArray.get(i).toString(),Category.class);
                        catList.add(cat);
                    }
                    listener.onGetCatsSuccess(catList);
                } catch (JSONException e) {
                    listener.onGetCatsFailure(e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onGetCatsFailure(t.getMessage());
            }
        });
    }

    public static void getGoodsCached(final GetCallback.OnGetCatAndSubCatsFinish listener){

        CacheRequest cacheRequest = new CacheRequest(Request.Method.GET, Urls.URL_GET_CATEGORIES_GOODS, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse allresponse) {
                try {
                    ArrayList<Category> catList = new ArrayList<>();
                    Gson gson=new Gson();
                    String response = new String(allresponse.data);
                    JsonElement root = new JsonParser().parse(response);
                    response = root.getAsString();  //not .toString
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Category cat=gson.fromJson(jsonArray.get(i).toString(),Category.class);
                        catList.add(cat);
                    }
                    listener.onGetCatsSuccess(catList);
                } catch (JSONException e) {
                    listener.onGetCatsFailure(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onGetCatsFailure(error.getMessage());
            }
        });

        VolleySingleton.getInstance().addToRequestQueue(cacheRequest);
    }
    public static void getServicesCached(final GetCallback.OnGetCatAndSubCatsFinish listener){

        CacheRequest cacheRequest = new CacheRequest(Request.Method.GET, Urls.URL_GET_CATEGORIES_SERVICES, new com.android.volley.Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse allresponse) {
                try {
                    ArrayList<Category> catList = new ArrayList<>();
                    Gson gson=new Gson();
                    String response = new String(allresponse.data);
                    JsonElement root = new JsonParser().parse(response);
                    response = root.getAsString();  //not .toString
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Category cat=gson.fromJson(jsonArray.get(i).toString(),Category.class);
                        catList.add(cat);
                    }
                    listener.onGetCatsSuccess(catList);
                } catch (JSONException e) {
                    listener.onGetCatsFailure(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onGetCatsFailure(error.getMessage());
            }
        });

        VolleySingleton.getInstance().addToRequestQueue(cacheRequest);
    }
    public static void createNewItemApi(String data, final GetCallback.OnCreateItemFinish listener){

        Call<String> call=apiInterface.createItemApi(data);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject object=new JSONObject(response.body());
                    if(object.has("ItemID")){
                        listener.onCreateItemSuccess(object.getString("ItemID"));
                    }
                    else listener.onCreateItemFailure("Error happened");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onCreateItemFailure(t.getMessage());
            }
        });


    }
}
