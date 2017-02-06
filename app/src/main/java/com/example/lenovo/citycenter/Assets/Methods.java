package com.example.lenovo.citycenter.Assets;

/**
 * Created by lenovo on 26/01/2017.
 */

public class Methods {


    public static String htmlRender(String ss)

    {
        ss=ss.replace("span","font");
        ss=ss.replace("style=\"color:","color=");
        ss=ss.replace(";\"","");
        ss=ss.replaceAll("<p>","");
        ss=ss.replaceAll("</p>",""); //********
        return ss;
    }
}
