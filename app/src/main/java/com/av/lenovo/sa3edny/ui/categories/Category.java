package com.av.lenovo.sa3edny.ui.categories;

import java.util.ArrayList;

/**
 * Created by lenovo on 09/09/2015.
 */
public class Category {
 private int CategoryID;
 private boolean isRaty;
   private String Name_En, Description_En, Logo;
   private ArrayList<Subcategory> sub_array= new ArrayList<>();

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
        return Description_En;
    }

    public void setDescription_En(String description_En) {
        this.Description_En = description_En;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        logo = logo.replace(" ","%20");
        this.Logo = logo;
    }


}