package com.av.lenovo.sa3edny.classes;

import java.io.Serializable;

/**
 * Created by Mina on 5/22/2017.
 */

public class ThemeApp implements Serializable {

    private String AppLogo,AppBanner,AppBackGround;

    public ThemeApp(){

    }

    public String getAppLogo() {
        return AppLogo;
    }

    public void setAppLogo(String appLogo) {
        AppLogo = appLogo;
    }

    public String getAppBanner() {
        return AppBanner;
    }

    public void setAppBanner(String appBanner) {
        AppBanner = appBanner;
    }

    public String getAppBackGround() {
        return AppBackGround;
    }

    public void setAppBackGround(String appBackGround) {
        AppBackGround = appBackGround;
    }
}
