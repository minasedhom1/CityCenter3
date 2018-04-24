package com.av.lenovo.sa3edny.ui.categories;

/**
 * Created by mido on 2/5/2017.
 */

public class Subcategory {
   private String subcat_id, subCat_name,subCat_describtion,subCat_icon_url;
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
        return subCat_icon_url;
    }

    public void setSubCat_icon_url(String subCat_icon_url) {
        subCat_icon_url=subCat_icon_url.replace(" ","%20");
        this.subCat_icon_url = subCat_icon_url;
    }

    public String getSubCat_describtion() {
        return subCat_describtion;
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
}
