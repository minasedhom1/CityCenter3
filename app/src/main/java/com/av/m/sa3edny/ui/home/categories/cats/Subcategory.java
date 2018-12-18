package com.av.m.sa3edny.ui.home.categories.cats;

import android.text.Html;

import com.av.m.sa3edny.utils.Urls;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mido on 2/5/2017.
 */

public class Subcategory {
    @SerializedName("SubCategoryID")
    private String subcat_id;
    @SerializedName("Name_En")
    String subCat_name;

    @SerializedName("Photo1")
    private String subCat_icon_url;

   String subCat_describtion;
    int Cat_ID;

    public String getSubcat_id() {
        return subcat_id;
    }

    public void setSubcat_id(String subcat_id) {
        this.subcat_id = subcat_id;
    }

    public int getCat_ID() {
        return Cat_ID;
    }

    public void setCat_ID(int cat_ID) {
        Cat_ID = cat_ID;
    }

    public String getSubCat_icon_url() {
        return Urls.URL_IMG_PATH+subCat_icon_url;
    }

    public void setSubCat_icon_url(String subCat_icon_url) {
        subCat_icon_url=subCat_icon_url.replace(" ","%20");
        this.subCat_icon_url = subCat_icon_url;
    }

    public String getSubCat_describtion() {
        return  subCat_describtion;
    }

    public void setSubCat_describtion(String subCat_describtion) {
        this.subCat_describtion = subCat_describtion;
    }

    public String getSubCat_name() {
        return subCat_name;
    }

    public void setSubCat_name(String subCat_name) {
        this.subCat_name = subCat_name;
    }

    @Override
    public String toString() {
        return String.valueOf(Html.fromHtml(subCat_name));
    }
}
