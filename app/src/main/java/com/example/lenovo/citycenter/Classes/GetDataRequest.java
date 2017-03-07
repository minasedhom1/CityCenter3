package com.example.lenovo.citycenter.classes;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by lenovo on 01/11/2016.
 */

public class GetDataRequest extends StringRequest {
    private static  String REQUEST_URL;
   // "http://ne3o.freevar.com/fetchData.php" ; /

    public static void setUrl(String RequestUrl) {
        REQUEST_URL = RequestUrl;
    }

    public GetDataRequest(Response.Listener<String> listener) {
        super(Method.GET, REQUEST_URL, listener, null);
    }}

