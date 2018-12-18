package com.av.m.sa3edny.ui.home.categories.addNewItem;
import com.av.m.sa3edny.networkUtilities.ApiClient;
import com.av.m.sa3edny.networkUtilities.ApiInterface;
import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.av.m.sa3edny.ui.home.categories.cats.Category;
import com.google.gson.Gson;

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

    public static void getCatsAndSubCatsApi(final GetCallback.OnGetCatAndSubCatsFinish listener){
        Call<String> call = apiInterface.getAllCatsApi();
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
