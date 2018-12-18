package com.av.m.sa3edny.ui.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.m.sa3edny.R;
import com.av.m.sa3edny.classes.VolleySingleton;
import com.av.m.sa3edny.databinding.ActivityLoginBinding;
import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.av.m.sa3edny.ui.MainActivity;
import com.av.m.sa3edny.ui.login.signup.SignupActivity;
import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Arrays;

import static com.av.m.sa3edny.utils.Variables.USER_DATA;


public class LoginActivity extends AppCompatActivity implements LoginContract.LoginView, GetCallback.OnGetAccIDForGoogleFinish {
    
    ActivityLoginBinding binding;
    LoginPresenterImpl presenter;
    private CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient ;
    private final static int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // setTheme(android.R.style.Theme_Dialog);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor (getResources().getColor(R.color.colorPrimaryDark));
        }
        presenter=new LoginPresenterImpl(this);

        //MaterialFancyButton button=findViewById(R.id.btn_server_login);
        binding.btnServerLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.etEmail.getText().toString();
                String password=binding.etPassword.getText().toString();
                presenter.onLoginClicked(email,password);
            }
        });
        TextView textView=findViewById(R.id.tv_go_signup);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.goToSignupClicked();
            }
        });

        callbackManager = CallbackManager.Factory.create();

        binding.facebookLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFB_request();
            }
        });

        binding.googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

    }

    @Override
    public void showProgress() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToHomeScreen(User user) {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));//.putExtra(USER_DATA,user));
        LoginActivity.this.finish();
    }

    @Override
    public void moveToSignupScreen() {
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
    }

    @Override
    public void setUsernameError(String e) {

    }

    @Override
    public void setPasswordError(String e) {

    }

    @Override
    public void saveUserData(User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(USER_DATA, json).apply();
    }

    @Override
    public void loadCredentials() {

    }

    @Override
    public void dismissForgetPopup() {

    }

    @Override
    public void showForgetPopup(String phone) {

    }

    public void loginFB_request()
    {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("UserID", AccessToken.getCurrentAccessToken().getUserId());
                Methods.toast("Success",getApplicationContext());
                Profile profile=Profile.getCurrentProfile();
                User user=new User();
                user.setName(profile.getFirstName()+" "+profile.getLastName());
                user.setImg(profile.getProfilePictureUri(300,300).toString());
                user.setFacebookID(AccessToken.getCurrentAccessToken().getUserId());
                getAccID(user);
            }

            @Override
            public void onCancel() {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
                alertDialog.setMessage("Login Cancelled!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
                Methods.toast("Login Cancelled", LoginActivity.this);
                /*likebtn.setVisibility(View.GONE);
                drawer_profile.setVisibility(View.GONE);*/
            }

            @Override
            public void onError(FacebookException error) {
                Methods.toast("Error happend", LoginActivity.this);
            }
        });
        loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            // user select his gmail from dialog box
            if (task.isSuccessful()) {
                Toast.makeText(this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                String name=task.getResult().getGivenName();
                String email=task.getResult().getEmail();
                String id=task.getResult().getId();
                User user=new User();
                user.setName(name);
                user.setEmail(email);
                user.setFacebookID(id);
                LoginModelImpl.getAccIDForGoogle(user,this);
                //getAccID(user);
            } else {
                // user igone to select his gmail from dialog box by hitten back button
                Toast.makeText(this, "Failed to sign in ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void getAccID(final User user)
    {
        StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_POST_FBID_GET_ACC_ID + user.getFacebookID(),
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root = new JsonParser().parse(response);
                root = new JsonParser().parse(root.getAsString());   //double parse
                response = root.getAsString();
                try {
                    JSONObject obj = new JSONObject(response);
                    String id = obj.getString("ID");
                    user.setAccountID(id);
                    saveUserData(user);
                  //  Variables.ACCOUNT_ID=id;
                   // PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("AccountID", Variables.ACCOUNT_ID).apply();
                    // mainFrag();
                  //  Log.d("ACCOUNTID", Variables.ACCOUNT_ID);
                    moveToHomeScreen(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(Methods.onErrorVolley(error), getApplicationContext());
            }
        });
        VolleySingleton.getInstance().addToRequestQueue(postReq);
    }

    private void googleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onSuccess(User user) {
        saveUserData(user);
        moveToHomeScreen(user);
    }

    @Override
    public void onFailure(String s) {
        showError(s);
    }
}
