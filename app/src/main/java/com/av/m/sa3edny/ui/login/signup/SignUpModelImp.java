package com.av.m.sa3edny.ui.login.signup;

import com.av.m.sa3edny.networkUtilities.ApiClient;
import com.av.m.sa3edny.networkUtilities.ApiInterface;
import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mina on 3/1/2018.
 */

public class SignUpModelImp implements SignUpContract.SignUpModel {
    ApiInterface apiInterface= ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void requestSignUp(String username, String phone, final String email, String password, String gender, final GetCallback.OnSignUpFinish listener) {
        String data="{'Name':'"+username+"','phone':'"+phone+"','Email':'"+email+"','password':'"+ password +"','gender':'"+ gender +"'}";

        Call<String> call = apiInterface.signUpApi(data);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //  success=true;
                String s = response.body();
                try {
                    JsonElement root = new JsonParser().parse(s);
                    s = root.getAsString();
                    JSONObject object = new JSONObject(s);
                    String status=object.getString("Status");
                    if(!(status.equals("Success"))){
                        listener.onFailure(status);
                    }
                    else {
                        listener.onSuccess(status,email);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //  success=false;
                listener.onFailure(t.getMessage());
            }
        });
    }
}
