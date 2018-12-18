package com.av.m.sa3edny.networkUtilities;

import com.av.m.sa3edny.utils.Urls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mina on 1/10/2018.
 */

public class ApiClient {
    public static final String BASE_URL= Urls.SODIC_BASE;
 /*   public static final String IMG_URL         = "https://ibnammy.net/admin/IMG/";
    public static final String WORK_MEDIA_URL  = "https://ibnammy.net/admin/Work_Media/";*/
  //  public static final String BASE_URL = "http://197.45.12.35/ibn-ammey/api/";//"https://reqres.in/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit==null)
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(25, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS).build();

            retrofit=new Retrofit.Builder()
                    .client(client)
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
