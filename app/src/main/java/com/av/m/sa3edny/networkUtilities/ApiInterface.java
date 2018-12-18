package com.av.m.sa3edny.networkUtilities;


import com.av.m.sa3edny.utils.Urls;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Mina on 1/10/2018.
 */

public interface ApiInterface {
    //@GET("/api/users?")
    @POST("Account/Signin_Account/LoginAccount")
    Call<String> loginApi(@Body String data);

    @POST("Account/CreateAccount/CreateAccount") //"{'Name':'Mina','Email':'mm@mm.com','Gender':'male','Password':'12345'}"
    Call<String> signUpApi(@Body String data);

    @GET(Urls.URL_GET_ALL_CATS_AND_SUBCATS)
    Call<String> getAllCatsApi();

    @POST("Account/CreateAccountGoogleID/CreateGoogleID")
    Call<String> gmailLoginApi(@Body String data);

    //http://sa3edney.com/service/Sodic/Item/CreateItem/CreateItem

    @POST("Item/CreateItem/CreateItem")
    Call<String> createItemApi(@Body String data);

    @GET("Category/GetAllActiveCategoriesgoods/Getgoods")
    Call<String> getGoods();

    @GET("Category/GetAllActiveCategoriesservices/Getservices")
    Call<String> getServices();

    @GET("Item/GetAllSearchItems/search")
    Call<String>getAllItems();

    @GET ("Item/GetAllActiveItemsLoyalty/GetLoyalty")
    Call<String>getLoyalItems();

    @POST("Deliver_Order/Order/AddOrder")
    Call<String> requestOrderApi(@Body String body);
    @POST("Deliver_Order/OrderWithMedia/AddOrder_Media")
    Call<String> makeOrderApi(@Body RequestBody data);//,@Part("Mobile") RequestBody mobile, @Part("ItemID") RequestBody itemId);
}
