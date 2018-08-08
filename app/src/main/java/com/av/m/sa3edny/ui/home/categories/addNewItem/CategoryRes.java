package com.av.m.sa3edny.ui.home.categories.addNewItem;

import android.text.Html;

import com.av.m.sa3edny.ui.home.categories.cats.Subcategory;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mina on 6/19/2018.
 */

public class CategoryRes {
    String CategoryID,Name_En;

    @SerializedName("Active")
    boolean isActive;

    @SerializedName("AllowSubcategory")
    boolean isAllowSubcategory;

    @SerializedName("SubCategories")
    ArrayList<Subcategory> subCatList;

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getName_En() {
        return Name_En;
    }

    public void setName_En(String name_En) {
        Name_En = name_En;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isAllowSubcategory() {
        return isAllowSubcategory;
    }

    public void setAllowSubcategory(boolean allowSubcategory) {
        isAllowSubcategory = allowSubcategory;
    }

    public ArrayList<Subcategory> getSubCatList() {
        return subCatList;
    }

    public void setSubCatList(ArrayList<Subcategory> subCatList) {
        this.subCatList = subCatList;
    }

    @Override
    public String toString() {
        return String.valueOf(Html.fromHtml(Name_En));
    }
}
