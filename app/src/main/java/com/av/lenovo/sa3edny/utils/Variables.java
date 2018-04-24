package com.av.lenovo.sa3edny.utils;

import com.av.lenovo.sa3edny.ui.categories.Category;
import com.av.lenovo.sa3edny.ui.items.Item;
import com.av.lenovo.sa3edny.ui.notification.Notification;

import java.util.ArrayList;

/**
 * Created by mido on 2/19/2017.
 */

public class Variables {

    public  static  int badgeCount;
    public  static String catID;
    public  static String DEVICE_TOKEN;
    public static String ACCOUNT_ID;
    public  static  String ITEM_PATH="";
    public  static boolean IS_RATY_CATEGORY;
    public  static ArrayList<String> fav_ids=new ArrayList<>();;
    public static String SINGLE_ITEM_ID;
    public static ArrayList<Item> searchList=new ArrayList<>();
    public static ArrayList<Notification> notificationList=new ArrayList<>();
    public static ArrayList<Category> categoryArrayList=new ArrayList<>();

}

