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
    public  static  final String URL_GET_SELECTED_SUBCATEGORY_ITEM= SODIC_BASE+"Item/GetActiveItemforsubcategory/Getitems?subcategoryID=";
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
//"https://www.google.com/maps/d/viewer?mid=1Xe4NoLjHr3ftUvinoY8v8UhyxjA&ll=30.072141670076693%2C31.144612250000023&z=11"
}
