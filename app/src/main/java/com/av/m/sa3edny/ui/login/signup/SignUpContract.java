package com.av.m.sa3edny.ui.login.signup;

import com.av.m.sa3edny.networkUtilities.GetCallback;

/**
 * Created by Mina on 3/15/2018.
 */

public interface SignUpContract {
     interface SignUpModel {
        void requestSignUp(String username,String phone, String email, String password,String gender, GetCallback.OnSignUpFinish listener);

    }

     interface SignUpView {
        void showProgress();
        void hideProgress();
        void showMessage(String error);
        void moveToLoginScreen(String phone);
        void setUserNameError(String e);
        void setPhoneError(String e);
        void setPasswordError(String e);
        void setEmailError(String e);
        //void setRepeatPasswordError(String e);
    }

     interface SignUpPresenter {
        boolean validateItemName(String name);
        boolean validatePhone(String phone);
        boolean validatePass(String pass);
        boolean validateEmail(String email);
        void onSignUpClicked(String username, String phone, String email,String password,String gender);
        void requestSignUpFromModel(String username, String phone, String email,String password,String gender);

    }
}
