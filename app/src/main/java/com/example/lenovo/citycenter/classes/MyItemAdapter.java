package com.example.lenovo.citycenter.classes;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.MainActivity;
import com.example.lenovo.citycenter.R;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by lenovo on 12/01/2017.
 */

public class MyItemAdapter extends ArrayAdapter<Item> {
    ShareDialog shareDialog ;


    Context context;
    List<Item> itemsList;

    ProgressBar progressBar;
    public MyItemAdapter(Context context, int resource, List<Item> itemsList) {
        super(context, resource,itemsList);
        this.context= context;
        this.itemsList=itemsList;
    }

    class ViewHolder
        {

            ImageButton menu,share, call,comment;
            ImageView image;
            TextView name, description,rate;
            ShineButton shineButton;
            Spinner rateSpin;

        }
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            try {

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_layout, parent, false);
                    holder.call = (ImageButton) convertView.findViewById(R.id.item_call_btn);
                    holder.share = (ImageButton) convertView.findViewById(R.id.item_share_btn);
                    holder.comment = (ImageButton) convertView.findViewById(R.id.item_comment_btn);
                    holder.menu = (ImageButton) convertView.findViewById(R.id.item_view_menu_btn);
                    holder.name = (TextView) convertView.findViewById(R.id.name2_tv);
                    holder.description = (TextView) convertView.findViewById(R.id.item_description);
                    holder.image = (ImageView) convertView.findViewById(R.id.item_icon);
                    holder.shineButton = (ShineButton) convertView.findViewById(R.id.like_btn);
                    holder.rate = (TextView) convertView.findViewById(R.id.item_rate_value);
                    holder.rateSpin = (Spinner) convertView.findViewById(R.id.rate_spinner);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final Item myItem = itemsList.get(position);

/*------------------------------------set values and action to views----------------------------------------*/
           /*     holder.call.setTypeface(MainActivity.font);
                holder.share.setTypeface(MainActivity.font);
                holder.comment.setTypeface(MainActivity.font);
                holder.menu.setTypeface(MainActivity.font);*/


                holder.name.setText(Html.fromHtml(myItem.getName()));
                String des = String.valueOf(Html.fromHtml(myItem.getDescription()));
                des = des.replace("\n\n", "\n");
                holder.description.setText(des);

                holder.rate.setText(String.valueOf(myItem.getRate()));
                //  holder.image.setMaxHeight(300);
                Picasso.with(getContext()).load(myItem.getPhoto1()).error(R.mipmap.ic_launcher).into(holder.image);  //             //new DownLoadImageTask(image).execute(imageUrl)

                final ArrayAdapter<String> phones_adapter =
                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myItem.getPhones());

                if (!Variables.IS_RATY_CATEGORY) holder.rateSpin.setVisibility(View.GONE);
   /*--------------------------------------------------------------------------------------------------------------------------------------------*/



   /*----------------------------------------------------------call btn popup nums----------------------------------------------------------------------------------*/

                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlphaAnimation buttonClick = new AlphaAnimation(3F, 0.8F);
                        view.setAnimation(buttonClick);
                        if (myItem.getPhones().size() == 1) {
                            context.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + myItem.getPhone1())));

                        } else if (myItem.getPhones().size() > 1) {
                            final Dialog nagDialog = new Dialog(getContext());
                            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            nagDialog.setContentView(R.layout.popup_phone_numbers_for_item);
                            ListView listView1 = (ListView) nagDialog.findViewById(R.id.phones_list);
                            listView1.setAdapter(phones_adapter);
                            nagDialog.show();
                            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                              /*      Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                                    phoneIntent.setData(Uri.parse("tel:" + phones_adapter.getItem(position)));
                                    context.startActivity(phoneIntent);*/
                               context.startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + phones_adapter.getItem(position))));
                                }
                            });
                        } else {
                            Methods.toast("No Phone numbers for this item!", getContext());
                        }
                    }
                });

/*-----------------------------------------------------------------------like btn-----------------------------------------------------------------------------------------*/
                holder.shineButton.init((AppCompatActivity) context);
                holder.shineButton.setChecked(myItem.isLike());
                holder.shineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!myItem.isLike()) {
                            StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_ADD_TO_FAVORITES_ITEM + myItem.getId(), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonElement root = new JsonParser().parse(response);
                                    root = new JsonParser().parse(root.getAsString());   //double parse
                                    response = root.getAsString();
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        String status = obj.getString("Status");
                                        if (status.matches("Success") || status.matches("Already Exists")) {
                                            // myItem.setLike(true);
                                            Methods.toast("Added to Favorites list!",context);
                                        } else Methods.toast(status,context);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
                           // RequestQueue queue = Volley.newRequestQueue(getContext());
                           // queue.add(postReq);
                            VolleySingleton.getInstance().addToRequestQueue(postReq);

                            myItem.setLike(true);
                        } else {
                            StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_DELETE_FROM_FAVORITES_ITEM + myItem.getId(), new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonElement root = new JsonParser().parse(response);
                                    root = new JsonParser().parse(root.getAsString());   //double parse
                                    response = root.getAsString();
                                    try {
                                        JSONObject obj = new JSONObject(response);
                                        String status = obj.getString("Status");
                                        if (status.matches("Success")) {
                                            Methods.toast("Removed from Favorites list!",context);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
                           /* RequestQueue queue = Volley.newRequestQueue(getContext());
                            queue.add(postReq);*/
                            VolleySingleton.getInstance().addToRequestQueue(postReq);
                            if(Variables.ITEM_PATH.matches("Favorite")){
                            itemsList.remove(myItem);
                             setNotifyOnChange(true);
                             notifyDataSetChanged();
                            }

                        }
                    }
                });
/*------------------------------------------------------------------pupup item image--------------------------------------------------------------------------*/

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popUpPhoto(myItem.getPhoto1());

                    }
                });
/*------------------------------------------------------------------------------------------------------------------------------------------------*/

/*---------------------------------------------------------------------Menu btn-----------------------------------------------------------------------*/

                if (myItem.getMenu_url().matches("null")) {
                    holder.menu.setVisibility(View.GONE);
                } else {
                    holder.menu.setVisibility(View.VISIBLE);
                }
                holder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                 popupMenu(myItem.getMenu_url());
                    }
                });
   /*--------------------------------------------------------------------------------------------------------------------------------------------*/

                holder.rateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Methods.toast(String.valueOf(position), getContext());

                        String url = Urls.USER_RATE_ATTRS(myItem.getId(), position);
                        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                JsonElement root = new JsonParser().parse(response);
                                root = new JsonParser().parse(root.getAsString());   //double parse
                                response = root.getAsString();
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String status = obj.getString("Status");
                                    if (status.matches("Success")) {
                                        Methods.toast("Rating Success", getContext());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } , null);

                        if (Variables.ACCOUNT_ID != null) {
                            if (position > 0) {
                            //    RequestQueue queue = Volley.newRequestQueue(getContext());
                            //    queue.add(request);
                                VolleySingleton.getInstance().addToRequestQueue(request);

                            }
                            // else {Methods.toast("please select a value",getContext());}
                        } else {
                            Methods.toast("You Must login First!", getContext());
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Methods.toast("nothing", getContext());
                    }
                });


                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                 shareItem(myItem);
                    }
                });


/*--------------------------------------------------------------------------------------------------------------------------------------------*/
                holder.comment.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        popComment(myItem.getId());
                    }});

                return convertView;

            } catch (Exception e) {
                return null;
            }
/*--------------------------------------------------------------------------------------------------------------------------------------------*/


        }




    private void popUpPhoto(String photoUrl) {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.pop_up_item_image);
        ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.imageView3);
        Picasso.with(getContext()).load(photoUrl).error(R.mipmap.ic_launcher).into(ivPreview);  //             //new DownLoadImageTask(image).execute(imageUrl);
        nagDialog.show();
    }


    private void popupMenu(String menuUrl) {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.popup_item_load_menu_pdf_url);
        WebView webView = (WebView) nagDialog.findViewById(R.id.item_menu_webview_);
        progressBar= (ProgressBar) nagDialog.findViewById(R.id.progressBar);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setFitsSystemWindows(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        String url = "https://docs.google.com/gview?url=" + Urls.URL_PDF_PATH +menuUrl  + "&embedded=true";
        webView.loadUrl(url);
        nagDialog.show();
    }



    private void shareItem(Item myItem) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            shareDialog = new ShareDialog((Activity) context);
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(Html.fromHtml(myItem.getName()).toString())
                    .setContentDescription(Html.fromHtml(myItem.getDescription()).toString())
                    .setImageUrl(Uri.parse(myItem.getPhoto1()))
                    .setContentUrl(Uri.parse("https://sodicclient.azurewebsites.net/#/Itemid?id="+myItem.getId()))
                    .build();
            shareDialog.show(linkContent);
        }
    }


    private void popComment(String item_id)
{
    final Dialog nagDialog = new Dialog(getContext());
    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    nagDialog.setContentView(R.layout.popup_comment_item);
    progressBar= (ProgressBar) nagDialog.findViewById(R.id.progressBarrr);
    WebView mWebViewComments = (WebView) nagDialog.findViewById(R.id.item_comment_webview_);
    mWebViewComments.setWebViewClient(new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    });

    mWebViewComments.setWebChromeClient(new WebChromeClient());
    mWebViewComments.getSettings().setJavaScriptEnabled(true);
    mWebViewComments.getSettings().setAppCacheEnabled(true);
    mWebViewComments.getSettings().setDomStorageEnabled(true);
    mWebViewComments.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    mWebViewComments.getSettings().setSupportMultipleWindows(true);
    mWebViewComments.getSettings().setSupportZoom(false);
    mWebViewComments.getSettings().setBuiltInZoomControls(false);
    CookieManager.getInstance().setAcceptCookie(true);
    if (Build.VERSION.SDK_INT >= 21) {
        mWebViewComments.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        CookieManager.getInstance().setAcceptThirdPartyCookies(mWebViewComments, true);
    }
    // facebook comment widget including the article url
    String html = "<!doctype html> <html lang=\"en\"> <head></head> <body> " +
            "<div id=\"fb-root\"></div> <script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; if (d.getElementById(id)) return; js = d.createElement(s); js.id = id; js.src = \"//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.8&appId=245162305681302\"; fjs.parentNode.insertBefore(js, fjs); }(document, 'script', 'facebook-jssdk'));</script> " +
            "<div class=\"fb-comments\" data-href=\"" + "https://sodicclient.azurewebsites.net/#/Itemid?id="+item_id+ "\" " +
            "data-numposts=\"" +"3"+ "\" data-order-by=\"reverse_time\">" +
            "</div> </body> </html>";
    mWebViewComments.loadDataWithBaseURL("http://www.nothing.com", html, "text/html", "UTF-8", null);
    nagDialog.setCanceledOnTouchOutside(true);
    nagDialog.show();
}
    }

