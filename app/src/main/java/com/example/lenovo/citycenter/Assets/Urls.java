package com.example.lenovo.citycenter.Assets;

import com.example.lenovo.citycenter.MainActivity;

/**
 * Created by lenovo on 23/01/2017.
 */

public class Urls {
      /*-------------------------------------------------------Sa3edny APIs--------------------------------------------------------*/
    /*public static final String URL_POST_FBID_GET_ACC_ID ="http://Sa3ednymallservice.azurewebsites.net/sodic/Account/CreateAccount/Create?FBID=";
    public static final String URL_GET_CATEGORIES_GOODS="http://sa3ednyMallservice.azurewebsites.net/sodic/Category/GetAllActiveCategoriesGoods/Get";
    public static final String URL_GET_CATEGORIES_SERVICES="http://sa3ednyMallservice.azurewebsites.net/sodic/Category/GetAllActiveCategoriesServices/Get";
    public static final String URL_GET_ALL_ITEMS="http://Sa3ednyMallservice.azurewebsites.net/sodic/Item/GetAllActiveItems/GetAll";
    public static final String URL_GET_NEW_ITEMS="http://Sa3ednyMallservice.azurewebsites.net/sodic/Item/GetAllNewItems/GetAllNew";
    public static final String URL_GET_SELECTED_CATEGORY_ITEMS="http://Sa3ednyMallservice.azurewebsites.net/sodic//Item/GetActiveItem/Get?categoryID=";
    public static final String URL_GET_LATEST_OFFERS_ITEMS="http://sa3ednymallservice.azurewebsites.net/sodic/Item/GetAllPromoItems/GetAllPromo";
    public  static  final String URL_GET_SELECTED_CATEGORY_SUBCATEGORIES="http://sa3ednyMallservice.azurewebsites.net/sodic/Category/GetActiveSubCategories/Get?categoryID=";
    public  static  final String URL_GET_SELECTED_SUBCATEGORY_ITEM="http://Sa3ednymallservice.azurewebsites.net/sodic/Item/GetActiveItemforsubcategory/Getitems?subcategoryID=";
    public  static  final String URL_ADD_TO_FAVORITES_ITEM= "http://sa3ednymallservice.azurewebsites.net/sodic/Favourite/AddToFavourite/Add?AccountID="+Variables.ACCOUNT_ID+"&itemID="; /*//***
    public  static final  String URL_GET_FAVOURITES_FOR_ID="http://sa3ednymallservice.azurewebsites.net/sodic/Favourite/GetFavourite/Get?AccountID="+Variables.ACCOUNT_ID;
    public  static final  String URL_DELETE_FROM_FAVORITES_ITEM="http://sa3ednymallservice.azurewebsites.net/sodic/Favourite/DeleteFromFavourite/Delete?AccountID="+Variables.ACCOUNT_ID+"&itemID=";/*//**
    public  static final  String URL_ADD_DEVICE_TOKEN="http://sa3ednyservice.azurewebsites.net/Sodic/Device/AddDevice/Add?DeviceType=S7&DeviceToken=";
    public  static  final String URL_IMG_PATH="https://sa3ednymalladmin.azurewebsites.net/IMG/";*/
    /*-------------------------------------------------------Sodic APIs--------------------------------------------------------*/
    public static final String URL_POST_FBID_GET_ACC_ID ="http://sodicservice.azurewebsites.net/sodic/Account/CreateAccount/Create?FBID=";
    public static final String URL_GET_CATEGORIES_GOODS="http://sodicservice.azurewebsites.net/sodic/Category/GetAllActiveCategoriesgoods/Getgoods";
    public static final String URL_GET_CATEGORIES_SERVICES="http://sodicservice.azurewebsites.net/sodic/Category/GetAllActiveCategoriesservices/Getservices";
    public  static  final String URL_GET_SELECTED_CATEGORY_SUBCATEGORIES="http://sodicservice.azurewebsites.net/sodic/Category//GetActiveSubCategories/Get?categoryID=";
    public static final String URL_GET_SELECTED_CATEGORY_ITEMS="http://sodicservice.azurewebsites.net/sodic/Item/GetActiveItem/Get?categoryID=";
    public static final String URL_GET_ALL_ITEMS="http://sodicservice.azurewebsites.net/sodic/Item/GetAllActiveItems/GetAll";
    public static final String URL_GET_NEW_ITEMS="http://sodicservice.azurewebsites.net/sodic/Item/GetAllNewItems/GetAllNew";
    public static final String URL_GET_LATEST_OFFERS_ITEMS="http://sodicservice.azurewebsites.net/sodic/Item/GetAllPromoItems/GetAllPromo";
    public  static  final String URL_GET_SELECTED_SUBCATEGORY_ITEM="http://sodicservice.azurewebsites.net/sodic/Item/GetActiveItemforsubcategory/Getitems?subcategoryID=";
    public  static  final String URL_ADD_TO_FAVORITES_ITEM= "http://sodicservice.azurewebsites.net/sodic/Favourite/AddToFavourite/Add?AccountID="+Variables.ACCOUNT_ID+"&itemID="; //***
    public  static final  String URL_GET_FAVOURITES_FOR_ID="http://sodicservice.azurewebsites.net/sodic/Favourite/GetFavourite/Get?AccountID="+Variables.ACCOUNT_ID;
    public  static final  String URL_DELETE_FROM_FAVORITES_ITEM="http://sodicservice.azurewebsites.net/sodic/Favourite/DeleteFromFavourite/Delete?AccountID="+Variables.ACCOUNT_ID+"&itemID=";//**
    public  static final  String URL_ADD_DEVICE_TOKEN="http://sa3ednyservice.azurewebsites.net/Sodic/Device/AddDevice/Add?DeviceType=2&DeviceToken=";
    public  static  final String URL_IMG_PATH="https://sodicadmin.azurewebsites.net/IMG/";

    public static String USER_RATE_ATTRS(String itemID,int star)
    {
        String URL_USER_RATE_FOR_ITEM= "http://sodicservice.azurewebsites.net/sodic/Rate/UpdateAccountRate/Updata?itemID="+itemID+"&star="+star+"&AccountID="+Variables.ACCOUNT_ID+"";
        return URL_USER_RATE_FOR_ITEM;
    }

}
