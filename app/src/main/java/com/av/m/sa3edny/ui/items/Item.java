package com.av.m.sa3edny.ui.items;

import android.text.Html;

import com.av.m.sa3edny.utils.Urls;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 02/01/2017.
 */

public class Item implements Serializable{

   private String ItemID, Name_En, Description_En, Phone1 ="0000", Phone2,phone3, Phone4, Phone5, CategoryName_En,
           SubcategoryName_En,Photo1,Photo2,Photo3,Photo4,Photo5,city,region
           ,site,categoryID, PDF_URL, PromoText_En, PromoButtonText, PDFPromo, URLButtonText, NoPersonRate, Latitude, Longitude;
   private float Rate;
    private boolean IsPromo, IsRatyCategory,HaveLoyalty,IsShare,IsComment,IsCall,IsDirection;

    private List<String> item_media=new ArrayList<>();


    public List<String> getItem_media() {
        return item_media;
    }

    public void setItem_media(List<String> item_media) {
        this.item_media = item_media;
    }

    public String getNoPersonRate() {
        return NoPersonRate;
    }

    public void setNoPersonRate(String noPersonRate) {
        this.NoPersonRate = noPersonRate;
    }

    public boolean isHaveLoyalty() {
        return HaveLoyalty;
    }

    public void setHaveLoyalty(boolean haveLoyalty) {
        HaveLoyalty = haveLoyalty;
    }

    public String getURLButtonText() {
        return URLButtonText;
    }

    public void setURLButtonText(String URLButtonText) {
        this.URLButtonText = URLButtonText;
    }

    public boolean isRatyCategory() {
        return IsRatyCategory;
    }

    public void setRatyCategory(boolean ratyCategory) {
        IsRatyCategory = ratyCategory;
    }

    public boolean isPromo() {
        return IsPromo;
    }

    public void setPromo(boolean promo) {
        IsPromo = promo;
    }

    public String getPromoText_En() {
        return Html.fromHtml(PromoText_En).toString();
    }

    public void setPromoText_En(String promoText_En) {
        this.PromoText_En = promoText_En;
    }

    public String getPromoButtonText() {
        return PromoButtonText;
    }

    public void setPromoButtonText(String promoButtonText) {
        this.PromoButtonText = promoButtonText;
    }

    public String getPDFPromo() {
        return PDFPromo;
    }

    public void setPDFPromo(String PDFPromo) {
        this.PDFPromo = PDFPromo;
    }

    public String getPDF_URL() {
        return PDF_URL;
    }

    public void setPDF_URL(String PDF_URL) {
        this.PDF_URL = PDF_URL;
    }

    ArrayList<String> phones=new ArrayList<>();

    public ArrayList<String> getPhones() {
        String phonesArr[]={getPhone1(),getPhone2(),getPhone3(),getPhone4(),getPhone5()};
        for(String ph : phonesArr){
            if(ph!=null&& !phones.contains(ph))
                phones.add(ph);
        }
        return phones;
    }

    public void setPhones(ArrayList<String> phones) {
        this.phones = phones;
    }


    public String getPhone1() {
        return Phone1;
    }

    public void setPhone1(String phone1) {
        this.Phone1 = phone1;
        if(!phone1.matches("null"))
            phones.add(phone1);
    }


    public String getPhone2() {
        return Phone2;
    }

    public void setPhone2(String phone2) {
         this.Phone2 = phone2;
        if(!phone2.matches("null"))
            phones.add(phone2);
    }

    public String getPhone3() {
        return phone3;
    }

    public void setPhone3(String phone3) {
        this.phone3 = phone3;
        if(!phone3.matches("null"))
          phones.add(phone3);

    }

    public String getPhone4() {
        return Phone4;
    }

    public void setPhone4(String phone4) {
        this.Phone4 = phone4;
        if(!phone4.matches("null"))
          phones.add(phone4);

    }

    public String getPhone5() {
        return Phone5;
    }

    public void setPhone5(String phone5) {

        this.Phone5 = phone5;
        if(!phone5.matches("null"))
            phones.add(phone5);

    }

    public String getSubcategoryName_En() {
        return SubcategoryName_En;
    }

    public void setSubcategoryName_En(String subcategoryName_En) {
        this.SubcategoryName_En = subcategoryName_En;
    }

    public float getRate() {
        return (float) (Math.round(Rate*10.0)/10.0);
    }

    public void setRate(float rate) {
        this.Rate = (float) (Math.round(rate*10.0)/10.0);
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    private boolean like;

    public Item() {
    }
    public Item(String name,String photo,String rate,String catName,String subCatName){
        this.Name_En=name;
        this.setPhoto1(photo);
        this.setCategoryName_En(catName);
        this.setRate(Float.valueOf(rate));
        this.setSubcategoryName_En(subCatName);

    }
    public String getId() {
        return ItemID;
    }

    public void setId(String id) {
        this.ItemID = id;
    }

    public String getName() {
        Name_En=Html.fromHtml(Name_En).toString();
        Name_En=Name_En.replaceAll("\n","");
        return Name_En;
    }

    public void setName(String name) {
        this.Name_En = name;
    }

    public String getDescription_En() {
        return Description_En;
    }

    public void setDescription_En(String description_En) {
        this.Description_En = description_En;
    }

/*    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        photo1=photo1.replaceAll(" ","%20");
        this.photo1 = photo1;
    }*/


    public String getCategoryName_En() {
        return CategoryName_En;
    }

    public void setCategoryName_En(String categoryName_En) {
        this.CategoryName_En = categoryName_En;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        this.Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
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

    public void setPhoto1(String photo1) {
        Photo1 = photo1;
    }

    public void setPhoto2(String photo2) {
        Photo2 = photo2;
    }

    public void setPhoto3(String photo3) {
        Photo3 = photo3;
    }

    public void setPhoto4(String photo4) {
        Photo4 = photo4;
    }

    public void setPhoto5(String photo5) {
        Photo5 = photo5;
    }

    public void setLongitude(String longitude) {
        this.Longitude = longitude;
    }
    public ArrayList getItemMedia(){
        String [] media={getPhoto1(),getPhoto2(),getPhoto3(),getPhoto4(),getPhoto5()};
        ArrayList<String>media_list=new ArrayList<>();
        for(String s : media)
        {
            if(s!=null)
                media_list.add(Urls.URL_IMG_PATH + s);
        }
        return media_list;
    }
}
