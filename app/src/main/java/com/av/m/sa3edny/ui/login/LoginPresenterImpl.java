package com.av.m.sa3edny.ui.login;


import android.content.Context;

import com.av.m.sa3edny.networkUtilities.GetCallback;


/**
 * Created by lenovo on 20/02/2018.
 */


public class LoginPresenterImpl implements LoginContract.LoginPresenter,GetCallback.OnLoginFinish,GetCallback.OnResetPasswordFinish {

    private LoginContract.LoginView loginView;
    private LoginModelImpl loginModel;

 //   public boolean isSavePassChecked;

    public LoginPresenterImpl(LoginContract.LoginView loginView){ //take an object of a class implements the model view
        this.loginView=loginView;
        this.loginModel=new LoginModelImpl();
    }

    @Override
    public void onLoginClicked(String userName, String password){
        loginView.showProgress();
        if(validatePhone(userName)&& validatePass(password)){
            requestLoginFromModel(userName,password);
        }else{
            loginView.hideProgress();
        }
    }

    @Override
     public boolean isValidFormat(String phone, String password){

        return true;
    }

    @Override
    public boolean validatePhone(String phone) {

            return true;
     }

    @Override
    public boolean validatePass(String pass) {
            return true;
    }

    @Override
     public void requestLoginFromModel(String userName, String password){
        loginModel.requestLogin(userName,password,this);
    }

    @Override
    public void goToSignupClicked() {
        loginView.moveToSignupScreen();
    }

    @Override
    public void onAttach(Context context) {
        loginView.loadCredentials();
    }

    @Override
    public void onDetach() {
        loginView=null;
    }

    @Override
    public void requestForgetPasswordFromModel(String phone, String email) {
      //  if(CommonUtils.isEmailValid(email)){
           // loginView.showProgress();
            loginModel.requestForgetPassword(phone,email,this);
   //     }
      //  else
         //   loginView.showError(email+" is not a valid email.");

    }

    @Override
    public void onSuccess(User user) {
        loginView.hideProgress();
        loginView.showError("Login Successfully.");
        loginView.saveUserData(user);
        loginView.moveToHomeScreen(user);
    }

    @Override
    public void onFailure(String s) {
        loginView.hideProgress();
        loginView.showError(s);
    }


    @Override
    public void onResetPasswordSuccess(String status) {
        loginView.showError(status);
        loginView.dismissForgetPopup();
       // loginView.hideProgress();
    }

    @Override
    public void onResetPasswordFailure(String status) {
        loginView.showError("خطأ: "+status);
        loginView.dismissForgetPopup();
       // loginView.hideProgress();
    }
}
