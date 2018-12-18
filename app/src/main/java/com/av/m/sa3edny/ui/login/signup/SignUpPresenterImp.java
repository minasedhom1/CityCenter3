package com.av.m.sa3edny.ui.login.signup;

import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.av.m.sa3edny.utils.CommonUtils;

/**
 * Created by Mina on 3/1/2018.
 */

public class SignUpPresenterImp implements SignUpContract.SignUpPresenter,GetCallback.OnSignUpFinish {

    SignUpModelImp signUpModelImp;
    SignUpContract.SignUpView signUpView;

    public SignUpPresenterImp(SignUpContract.SignUpView signUpView){
        signUpModelImp= new SignUpModelImp();
        this.signUpView=signUpView;
    }

    @Override
    public boolean validateItemName(String name) {
        if(name.startsWith(" ")|| name.isEmpty() || name.equals("")){
            // signUpView.setUserNameError("رقم الهاتف غبر صحيح..يجب ان يتكون من 11 رقم.");
            signUpView.setUserNameError("Name can't be empty or starts with spaces");
            signUpView.setEmailError(null);
            signUpView.setPasswordError(null);
            signUpView.setPhoneError(null);
            return false;
        }
        else{
            signUpView.setUserNameError(null);
            return true;
        }
    }

    @Override
    public boolean validatePhone(String phone) {
        if(phone.length()!=11|| !phone.startsWith("0")){
            // signUpView.setUserNameError("رقم الهاتف غبر صحيح..يجب ان يتكون من 11 رقم.");
            signUpView.setPhoneError("Phone is not valid");
            signUpView.setEmailError(null);
            signUpView.setPasswordError(null);
            signUpView.setUserNameError(null);
            return false;
        }
        else{
            signUpView.setUserNameError(null);
            return true;
        }
    }

    @Override
    public boolean validateEmail(String email) {
        if(!CommonUtils.isEmailValid(email)){
            signUpView.setEmailError("Email is not valid.");
            signUpView.setUserNameError(null);
            signUpView.setPasswordError(null);
            signUpView.setPhoneError(null);
        }
       return CommonUtils.isEmailValid(email);
    }

    @Override
    public boolean validatePass(String pass) {
        if(pass.length()<4 || pass.contains(" ")){
            signUpView.setPasswordError("Password must be at least 4 characters with no spaces");
            return false;
        }
        else{
            signUpView.setPasswordError(null);
            return true;
        }
    }

    @Override
    public void onSignUpClicked(String username, String phone, String email, String password,String gender) {
        signUpView.showProgress();
        if(validateItemName(username)&& validateEmail(email) &&validatePass(password)) {
            requestSignUpFromModel(username,phone,email,password,gender);
        }
        else
           signUpView.hideProgress();
    }


    @Override
    public void requestSignUpFromModel(String username, String phone, String email, String password, String gender) {
        signUpModelImp.requestSignUp(username,phone,email,password,gender,this);
    }


    @Override
    public void onSuccess(String status, String phone) {
        signUpView.hideProgress();
        signUpView.showMessage(status);
        signUpView.moveToLoginScreen(phone);
    }

    @Override
    public void onFailure(String s) {
        signUpView.hideProgress();
        signUpView.showMessage(s);
    }
}
