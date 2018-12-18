package com.av.m.sa3edny.ui.items;

import com.av.m.sa3edny.utils.Urls;

import java.util.ArrayList;

/**
 * Created by Mina on 7/3/2018.
 */

public class NewItem {
    String ItemID,Name_En,Description_En,Phone1,Phone2,Phone3,Phone4,Phone5,PDF_URL,Photo1,Photo2,Photo3,Photo4,Photo5;
    String PromoText_En,PromoButtonText,PDFPromo,URLButtonText,Rate,CategoryName_En,SubcategoryName_En,NoPersonRate;
    boolean IsPromo,IsRatyCategory,HaveLoyalty,isLike;

    public NewItem() {
    }


    public String getItemID() {
        return ItemID;
    }

    public String getName_En() {
        return Name_En;
    }

    public String getDescription_En() {
        return Description_En;
    }

    public String getPhone1() {
        return Phone1;
    }

    public String getPhone2() {
        return Phone2;
    }

    public String getPhone3() {
        return Phone3;
    }

    public String getPhone4() {
        return Phone4;
    }

    public String getPhone5() {
        return Phone5;
    }

    public String getPDF_URL() {
        return PDF_URL;
    }

    public String getPromoText_En() {
        return PromoText_En;
    }

    public String getPromoButtonText() {
        return PromoButtonText;
    }

    public String getPDFPromo() {
        return PDFPromo;
    }

    public String getURLButtonText() {
        return URLButtonText;
    }

    public String getRate() {
        return Rate;
    }

    public String getCategoryName_En() {
        return CategoryName_En;
    }

    public String getSubcategoryName_En() {
        return SubcategoryName_En;
    }

    public String getNoPersonRate() {
        return NoPersonRate;
    }

    public boolean isPromo() {
        return IsPromo;
    }

    public boolean isRatyCategory() {
        return IsRatyCategory;
    }

    public boolean isHaveLoyalty() {
        return HaveLoyalty;
    }

    public String getPhoto1() {
        return Photo1;
    }

    public String getPhoto2() {
        return Photo2;
    }

    public String getPhoto3() {
        return Photo3;
    }

    public String getPhoto4() {
        return Photo4;
    }

    public String getPhoto5() {
        return Photo5;
    }

    public ArrayList getITemMedia(){
        String [] media={getPhoto1(),getPhoto2(),getPhoto3(),getPhoto4(),getPhoto5()};
        ArrayList<String>media_list=new ArrayList<>();
        for(String s : media)
        {
            if(!s.equals("null"))
                media_list.add(Urls.URL_IMG_PATH + s);
        }
        return media_list;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
