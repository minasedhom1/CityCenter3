package com.av.m.sa3edny.ui.login;

import com.av.m.sa3edny.networkUtilities.ApiClient;
import com.av.m.sa3edny.networkUtilities.ApiInterface;
import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lenovo on 20/02/2018.
 */


public class LoginModelImpl implements LoginContract.LoginModel {
   private static ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void requestLogin(String userName, String password, final GetCallback.OnLoginFinish listener) {
          String data="{'Email':'"+userName+"','Password':'"+password+"'}";
            Call<String> call = apiInterface.loginApi(data);
            call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String s= response.body();

                try {
                    JSONObject object=new JSONObject(s);

                if(object.has("Status")&& object.getString("Status").equals("Success")){
                    Gson gson=new Gson();
                    User user=gson.fromJson(s,User.class);
                         listener.onSuccess(user);
                    }
                    else {
                         listener.onFailure(object.getString("Status"));
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // "{"AccountID":"3","UpdateDate":"False"}"
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

    @Override
    public void requestForgetPassword(String phone, String email, GetCallback.OnResetPasswordFinish listener) {

    }

    public static void getAccIDForGoogle(final User user , final GetCallback.OnGetAccIDForGoogleFinish listener){
        String data="{'GoogleID':'"+user.getFacebookID()+"'}";
        Call<String> call = apiInterface.gmailLoginApi(data);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.body().contains("AccountID")){
                    try {
                        JSONObject object=new JSONObject(response.body());
                        user.setAccountID(object.getString("AccountID"));
                        listener.onSuccess(user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.onFailure(e.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                listener.onFailure(t.getMessage());
            }
        });
    }

}
