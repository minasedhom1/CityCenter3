package com.example.lenovo.citycenter.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lenovo.citycenter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUs extends Fragment {


    public ContactUs() {
        // Required empty public constructor
    }

WebView  webViewl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view =inflater.inflate(R.layout.fragment_contact_us, container, false);

       webViewl= (WebView) view.findViewById(R.id.contact_us_web_view);
        webViewl.setWebViewClient(new WebViewClient());
        WebSettings settings=webViewl.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);

        webViewl.loadUrl("https://www.google.com/maps/d/viewer?mid=1Xe4NoLjHr3ftUvinoY8v8UhyxjA&ll=30.072141670076693%2C31.144612250000023&z=11");

        return  view;
    }

}
