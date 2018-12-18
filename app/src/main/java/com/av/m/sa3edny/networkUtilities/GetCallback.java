package com.av.m.sa3edny.networkUtilities;


import com.av.m.sa3edny.ui.home.categories.cats.Category;
import com.av.m.sa3edny.ui.items.Item;
import com.av.m.sa3edny.ui.login.User;

import java.util.ArrayList;

/**
 * Created by Mina on 2/20/2018.
 */

public abstract class GetCallback {
    public interface OnLoginFinish {
        void onSuccess(User userData);
        void onFailure(String s);
    }

    public interface OnSignUpFinish {
        void onSuccess(String status, String phone);
        void onFailure(String s);
    }
    public interface OnResetPasswordFinish {
        void onResetPasswordSuccess(String status);
        void onResetPasswordFailure(String status);
    }
    public interface OnGetAccIDForGoogleFinish {
        void onSuccess(User userData);
        void onFailure(String s);
    }

    public  interface OnGetCatAndSubCatsFinish{
        void onGetCatsSuccess(ArrayList<Category> catList);
        void onGetCatsFailure(String error);
    }
    public interface OnCreateItemFinish{
        void onCreateItemSuccess(String id);
        void onCreateItemFailure(String error);
    }
    public interface OnGetFavIdsList{
        void onGetFavListSuccess(ArrayList<String> fav_list);
        void onGetFavListFailure(String s);
    }
    public  interface OnGetItemsList{
        void onGetItemsSuccess(ArrayList<Item> items);
        void onGetItemsFailure(String s);
    }
}
