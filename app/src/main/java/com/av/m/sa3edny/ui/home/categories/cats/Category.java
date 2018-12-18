package com.av.m.sa3edny.ui.home.categories.cats;

import android.text.Html;

import com.av.m.sa3edny.utils.Urls;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by lenovo on 09/09/2015.
 */
public class Category {
 private int CategoryID;
 private boolean isRaty;
   private String Name_En, Description_En, Logo;

   @SerializedName("SubCategories")
   private ArrayList<Subcategory> sub_array;

    public boolean isRaty() {
        return isRaty;
    }

    public void setRaty(boolean raty) {
        isRaty = raty;
    }

    public ArrayList<Subcategory> getSub_array() {
        return sub_array;
    }

    public void setSub_array(ArrayList<Subcategory> sub_array) {
        this.sub_array = sub_array;
    }

    public boolean isAllowSubcategory() {
        return AllowSubcategory;
    }

    private  boolean AllowSubcategory;
    public void setAllowSubcategory(boolean allowSubcategory) {
        this.AllowSubcategory = allowSubcategory;
    }


public Category()
{}


    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        this.CategoryID = categoryID;
    }

    public String getName_En() {
        return Name_En;
    }

    public void setName_En(String name_En) {
        this.Name_En = name_En;
    }

    public String getDescription_En() {
        return  Html.fromHtml(Description_En).toString();
    }

    public void setDescription_En(String description_En) {
        this.Description_En = description_En;
    }

    public String getLogo() {
        return Urls.URL_IMG_PATH +Logo;
    }

    public void setLogo(String logo) {
        logo = logo.replace(" ","%20");
        this.Logo = logo;
    }

    @Override
    public String toString() {
        return String.valueOf(Html.fromHtml(Name_En));
    }
}