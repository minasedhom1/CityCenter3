package com.av.m.sa3edny.ui.login.signup;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.av.m.sa3edny.R;
import com.av.m.sa3edny.databinding.ActivitySignupBinding;






public class SignupActivity extends AppCompatActivity implements SignUpContract.SignUpView {

    ActivitySignupBinding binding;
    SignUpPresenterImp presenterImp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_signup);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_signup);
        presenterImp=new SignUpPresenterImp(this);
        binding.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=binding.etEmail.getText().toString();
                String name=binding.etUsername.getText().toString();
                String phone =binding.etPhone.getText().toString();
                String password=binding.etPassword.getText().toString();
                RadioButton checkedRadioButton =binding.radioGroupGender.findViewById(binding.radioGroupGender.getCheckedRadioButtonId());
                String gender = null;
                if(checkedRadioButton!=null)
                gender= (String) checkedRadioButton.getText();
                presenterImp.onSignUpClicked(name,phone,email,password,gender);
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupActivity.this.finish();
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
    public void showMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToLoginScreen(String phone) {
        onBackPressed();
    }

    @Override
    public void setUserNameError(String e) {
        binding.etUsername.setError(e);
    }

    @Override
    public void setPhoneError(String e) {
        binding.etPhone.setError(e);
    }

    @Override
    public void setPasswordError(String e) {
        binding.etPassword.setError(e);
    }

    @Override
    public void setEmailError(String e) {
        binding.etEmail.setError(e);
    }
}
