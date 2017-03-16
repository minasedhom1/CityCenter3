package com.example.lenovo.citycenter.Fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUs extends Fragment {


    public ContactUs() {
        // Required empty public constructor
    }

WebView  webViewl;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view =inflater.inflate(R.layout.fragment_contact_us, container, false);
        Methods.setPath(view);
        progressBar= (ProgressBar) view.findViewById(R.id.progress_bar);
        webViewl= (WebView) view.findViewById(R.id.contact_us_web_view);
        webViewl.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        WebSettings settings=webViewl.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);

        webViewl.loadUrl(Urls.URL_CONTACT_US_MAP);

        return  view;
    }

}
