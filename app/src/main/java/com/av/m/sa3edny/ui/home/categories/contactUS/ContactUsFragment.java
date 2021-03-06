package com.av.m.sa3edny.ui.home.categories.contactUS;


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

import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {


    WebView  webViewl;
    ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_contact_us, container, false);
        Methods.setPath(view,getContext());
        progressBar= view.findViewById(R.id.progress_bar);
        webViewl= view.findViewById(R.id.contact_us_web_view);
        webViewl.setWebViewClient(new WebViewClient(){
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
        return view;
    }
}
