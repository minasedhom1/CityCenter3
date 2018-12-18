package com.av.m.sa3edny.ui.login;

import java.io.Serializable;

/**
 * Created by Mina on 5/24/2018.
 */

public class User implements Serializable{

    String AccountID,Email,Name,FacebookID,Gender,Phone,Img;

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public String getAccountID() {
        return AccountID;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    public String getFacebookID() {
        return FacebookID;
    }

    public String getGender() {
        return Gender;
    }

    public String getPhone() {
        return Phone;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setFacebookID(String facebookID) {
        FacebookID = facebookID;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }
}
