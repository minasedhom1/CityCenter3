package com.av.lenovo.sa3edny.Assets;

/**
 * Created by lenovo on 23/01/2017.
 */

public class Urls {
    public static final String TYGER_BASE="http://tygerservices.azurewebsites.net/Tyger/";
    public static final String SODIC_BASE="http://sodicservice.azurewebsites.net/sodic/";

/*-------------------------------------------------------Sodic APIs--------------------------------------------------------*/
    public static final String URL_POST_FBID_GET_ACC_ID ="http://sodicservice.azurewebsites.net/sodic/Account/CreateAccount/Create?FBID=";
    public static final String URL_GET_CATEGORIES_GOODS= SODIC_BASE+"Category/GetAllActiveCategoriesgoods/Getgoods";
    public static final String URL_GET_CATEGORIES_SERVICES= SODIC_BASE+"Category/GetAllActiveCategoriesservices/Getservices";
    public  static  final String URL_GET_SELECTED_CATEGORY_SUBCATEGORIES= SODIC_BASE+"Category/GetActiveSubCategories/Get?categoryID=";
    public static final String URL_GET_SELECTED_CATEGORY_ITEMS= SODIC_BASE+"Item/GetActiveItem/Get?categoryID=";
    public static final String URL_GET_ALL_ITEMS=SODIC_BASE+"Item/GetAllActiveItems/GetAll";
    public static final String URL_GET_NEW_ITEMS= SODIC_BASE+"Item/GetAllNewItems/GetAllNew";
    public static final String URL_GET_LATEST_OFFERS_ITEMS= SODIC_BASE+"Item/GetAllPromoItems/GetAllPromo";
    public  static  final String URL_GET_SELECTED_SUBCATEGORY_ITEM= SODIC_BASE+"+";
    public  static  final String URL_ADD_TO_FAVORITES_ITEM= SODIC_BASE+"Favourite/AddToFavourite/Add?AccountID="+Variables.ACCOUNT_ID+"&itemID="; //***
    public  static final  String URL_GET_FAVOURITES_FOR_ID= SODIC_BASE+"Favourite/GetFavourite/Get?AccountID="+Variables.ACCOUNT_ID;
    public  static final  String URL_DELETE_FROM_FAVORITES_ITEM=  SODIC_BASE+"Favourite/DeleteFromFavourite/Delete?AccountID="+Variables.ACCOUNT_ID+"&itemID=";//**
    public  static final  String URL_ADD_DEVICE_TOKEN=SODIC_BASE+"Device/AddDevice/Add?DeviceType=2&DeviceToken=";
    public  static  final String URL_IMG_PATH="http://sodicadmin.azurewebsites.net/IMG/";
    public  static  final String URL_PDF_PATH="https://sodicadmin.azurewebsites.net/PDF/";
    public  static  final String URL_CONTACT_US_MAP = "https://www.google.com/maps/d/viewer?mid=1Xe4NoLjHr3ftUvinoY8v8UhyxjA&ll=30.072141670076693%2C31.144612250000023&z=9";
    public static final String URL_GET_NOTIFICATIONS=SODIC_BASE+"Device/GetNotifications/notification";

    // http://maps.google.com/maps?q=47.404376,8.601478
    public static String USER_RATE_ATTRS(String itemID,int star)
    {
        String URL_USER_RATE_FOR_ITEM= SODIC_BASE+"Rate/UpdateAccountRate/Updata?itemID="+itemID+"&star="+star+"&AccountID="+Variables.ACCOUNT_ID+"";
        return URL_USER_RATE_FOR_ITEM;
    }
    public static String URL_ALL_ITEM_SEARCH=SODIC_BASE+"Item/GetAllSearchItems/search";

    public static String URL_GET_LOYALTY_DATA_FOR_ITEM=  SODIC_BASE+"loyalty/GetloyaltyData/Get?AccountID="+Variables.ACCOUNT_ID+"&ItemID=";

    public static String URL_POST_ADD_POINTS(String acc_id,String item_id,String passcode,String amount,String order_num)
    {
        String  URL_ADD_POINTS=SODIC_BASE+"loyalty/Point/AddPoint?AccountID="+acc_id+"&ItemID="+item_id+"&Password="+passcode+"&Amount="+amount+"&OrderNo="+order_num;
        return  URL_ADD_POINTS;
    }
    public static String URL_POST_ADD_VISITS(String acc_id,String item_id,String passcode)
    {

        String  URL_ADD_Visits=SODIC_BASE+"loyalty/Visite/AddVisite?AccountID="+acc_id+"&ItemID="+item_id+"&Password="+passcode;
        return URL_ADD_Visits;
    }

    public static String URL_POST_CLAIM_PROMO(String acc_id,String item_id,String passcode)
    {

        String  URL_CLAIM_PROMO=SODIC_BASE+"loyalty/PromoCode/UsePromo?AccountID="+acc_id+"&ItemID="+item_id+"&Password="+passcode;
        return URL_CLAIM_PROMO;
    }




    public static String URL_POST_CLAIM_POINTS(String acc_id,String item_id,String passcode,String level) {

        return SODIC_BASE + "loyalty/Point/AddPoint?AccountID="+acc_id+"&ItemID="+item_id+"&Password="+passcode+"&LevelNo="+level;
    }
    public static String URL_POST_CLAIM_VISITS(String acc_id,String item_id,String passcode,String level) {

        return SODIC_BASE + "loyalty/Point/UseVisite?AccountID="+acc_id+"&ItemID="+item_id+"&Password="+passcode+"&LevelNo="+level;
    }
//http://sa3ednyservice.azurewebsites.net/Sodic/loyalty/Point/UseVisite?AccountID=1&itemID=292&Password=123456&LevelNo=1
//"https://www.google.com/maps/d/viewer?mid=1Xe4NoLjHr3ftUvinoY8v8UhyxjA&ll=30.072141670076693%2C31.144612250000023&z=11"
}
