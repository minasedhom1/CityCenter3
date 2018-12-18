package com.av.m.sa3edny.ui.items;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.av.m.sa3edny.R;
import com.av.m.sa3edny.classes.VolleySingleton;
import com.av.m.sa3edny.ui.login.LoginActivity;
import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.utils.Variables;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Mina on 7/3/2018.
 */

public class NewItemAdapter extends RecyclerView.Adapter<NewItemAdapter.MyViewHolder> {
    private ShareDialog shareDialog ;
    private ProgressBar progressBar;
    private Context context;
    private List<Item> itemsList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ViewPager viewPager;
            TextView name, description,rate,rates_num,promo_txt;
            ShineButton addToFavBtn;
            Spinner rateSpin;
            Button menu,share, call,comment, optional_btn, item_view_loyalty_btn,item_directions_btn;
            View promo_view;
            ImageView facebook,twitter,insta,youtube;
            public MyViewHolder(View view) {
                super(view);
                call =  view.findViewById(R.id.item_call_btn);
                share = view.findViewById(R.id.item_share_btn);
                comment = view.findViewById(R.id.item_comment_btn);
                menu = view.findViewById(R.id.item_view_menu_btn);
                name = view.findViewById(R.id.name2_tv);
                description =view.findViewById(R.id.item_description);

                //  holder.image = (ImageView) convertView.findViewById(R.id.item_icon);

                viewPager=view.findViewById(R.id.viewpager_list);
                addToFavBtn =  view.findViewById(R.id.like_btn);
                rate =view.findViewById(R.id.item_rate_value);
                rateSpin = view.findViewById(R.id.rate_spinner);
                optional_btn =  view.findViewById(R.id.option_btn);
                rates_num =view.findViewById(R.id.rates_num);
                promo_txt =view.findViewById(R.id.promo_tv);
                promo_view = view.findViewById(R.id.promo_layout);
                item_view_loyalty_btn=view.findViewById(R.id.item_view_loyalty_btn);

                item_directions_btn=view.findViewById(R.id.item_directions_btn);
                //Social Media buttons
                facebook=view.findViewById(R.id.iv_facebook);
                twitter=view.findViewById(R.id.iv_twitter);
                insta=view.findViewById(R.id.iv_instagram);
                youtube=view.findViewById(R.id.iv_youtube);
            }
        }


        public NewItemAdapter(List<Item> itemsList, Context context) {
            this.itemsList = itemsList;
            this.context= context;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_layout, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Item myItem = itemsList.get(position);
            holder.facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Methods.toast("Data not Added.", context);
                }
            });
            holder.twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Methods.toast("Data not Added.", context);
                }
            });
            holder.insta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Methods.toast("Data not Added.", context);
                }
            });
            holder.youtube.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Methods.toast("Data not Added.", context);
                }
            });

            if(myItem.getLatitude()==null || myItem.getLatitude().equals("null")) {
                holder.item_directions_btn.setVisibility(View.GONE);
            }
            holder.item_directions_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Double.valueOf(myItem.getLatitude()), Double.valueOf(myItem.getLongitude()));
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    context.startActivity(intent);
                }
            });

            holder.name.setText(Html.fromHtml(myItem.getName()));
            holder.name.setTextSize(18);
            String des = String.valueOf(Html.fromHtml(myItem.getDescription_En()));
            des = des.replace("\n\n", "\n");

            holder.description.setText(des);

            holder.rate.setText(String.valueOf(myItem.getRate()));

            if (myItem.getNoPersonRate() != null) {
                holder.rates_num.setVisibility(View.VISIBLE);
                holder.rates_num.setText("(" + myItem.getNoPersonRate() + ")");
            } else holder.rates_num.setVisibility(View.GONE);

            if (myItem.isPromo()) {
                holder.promo_view.setVisibility(View.VISIBLE);
                String promoText=Html.fromHtml(myItem.getPromoText_En()).toString();
                promoText=promoText.replace("\n","");
                holder.promo_txt.setText(promoText);
            } else {
                holder.promo_view.setVisibility(View.GONE);
            }


            final ArrayAdapter<String> phones_adapter =
                    new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, myItem.getPhones());

            if (!myItem.isRatyCategory()) holder.rateSpin.setVisibility(View.GONE);
            else holder.rateSpin.setVisibility(View.VISIBLE);

            if (myItem.isPromo()) {
                holder.optional_btn.setVisibility(View.VISIBLE);
                holder.optional_btn.setText(myItem.getPromoButtonText());

            } else {
                holder.optional_btn.setVisibility(View.GONE);
            }

            holder.optional_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {

                        Uri uri = Uri.parse(Urls.URL_PDF_PATH + myItem.getPDFPromo());
                        DownloadManager.Request r = new DownloadManager.Request(uri);
                        String s = Html.fromHtml(myItem.getName()).toString() + "Offers";
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, s);
                        r.allowScanningByMediaScanner();
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        try {
                            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                            dm.enqueue(r);
                        } catch (Exception e) {
                            Methods.toast(e.getMessage().toString(), context);
                        }
                    }

                }

            });

            /*----------------------------------------------------------call btn popup nums----------------------------------------------------------------------------------*/

            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlphaAnimation buttonClick = new AlphaAnimation(3F, 0.8F);
                    view.setAnimation(buttonClick);
                    if (myItem.getPhones().size() == 1) {
                        context.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + myItem.getPhone1())));

                    } else if (myItem.getPhones().size() > 1) {
                        final Dialog nagDialog = new Dialog(context);
                        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        nagDialog.setContentView(R.layout.popup_phone_numbers_for_item);
                        ListView listView1 = (ListView) nagDialog.findViewById(R.id.phones_list);
                        listView1.setAdapter(phones_adapter);
                        nagDialog.show();
                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                context.startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + phones_adapter.getItem(position))));
                            }
                        });
                    } else {
                        Methods.toast("No Phone numbers for this item!", context);
                    }
                }
            });

            /*-----------------------------------------------------------------------like btn-----------------------------------------------------------------------------------------*/
            holder.addToFavBtn.init((AppCompatActivity) context);
            if (Variables.ITEM_PATH.equals("Latest offers") || Variables.ITEM_PATH.equals("Whats new?!")) //if so
            {
                holder.addToFavBtn.setVisibility(View.GONE);
            }//invisible addToFav btn in latest offers & whats new?!
            else {
                holder.addToFavBtn.setVisibility(View.VISIBLE);
            }
            if (Variables.ITEM_PATH.equals("Favorite")) {
                myItem.setLike(true);
            } //if it's favourites set all with true
            holder.addToFavBtn.setChecked(myItem.isLike());
            holder.addToFavBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Variables.ACCOUNT_ID!=null) {
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
                                            Methods.toast("Added to Favorites list!", context);
                                        } else Methods.toast(status, context);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
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
                                            Methods.toast("Removed from Favorites list!", context);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
                            VolleySingleton.getInstance().addToRequestQueue(postReq);
                            if (Variables.ITEM_PATH.matches("Favorite")) {
                                itemsList.remove(myItem);
                                notifyDataSetChanged();
                            }

                        }
                    }
                    else{
                        goLoginAlert();
                        ((ShineButton)view).setChecked(false);
                    }
                }
            });

            holder.viewPager.setAdapter(new ViewPagerAdapter(context,myItem.getItem_media()));
/*---------------------------------------------------------------------Menu btn-----------------------------------------------------------------------*/

            if (myItem.getURLButtonText().matches("null")) {
                holder.menu.setVisibility(View.GONE);
            } else {
                holder.menu.setText(myItem.getURLButtonText());
                holder.menu.setVisibility(View.VISIBLE);
            }
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu(myItem.getPDF_URL());
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
                                    Methods.toast("Rating Success", context);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, null);

                    if (Variables.ACCOUNT_ID != null) {
                        if (position > 0) {
                            VolleySingleton.getInstance().addToRequestQueue(request);

                        }
                        // else {Methods.toast("please select a value",getContext());}
                    } else {
                        if(position>0)
                            //Methods.toast("You Must login First!", getContext());
                            goLoginAlert();
                        parent.setSelection(0);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Methods.toast("nothing", context);
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareItem(myItem);
                }
            });

            holder.comment.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
// launching Facebook comments activity
                    Intent intent = new Intent(context, FbCommentActivity.class);

// passing the article url
                    intent.putExtra("url", "https://www.androidhive.info/2016/06/android-firebase-integrate-analytics/");
                    context.startActivity(intent);
                    //popComment(myItem.getId());
                }
            });

        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }


    /*---------------------------------------------------------------Menu Btn-----------------------------------------------------------------------------*/
    private void popupMenu(String menuUrl) {
        final Dialog nagDialog = new Dialog(context);
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
/*--------------------------------------------------------------------------------------------------------------------------------------------------------*/


/*---------------------------------------------------------------Share Btn-----------------------------------------------------------------------------*/

    private void shareItem(Item myItem) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            shareDialog = new ShareDialog((Activity) context);
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(Html.fromHtml(myItem.getName()).toString())
                    .setContentDescription(Html.fromHtml(myItem.getDescription_En()).toString())
                    //     .setImageUrl(Uri.parse(myItem.getPhoto1()))
                    .setContentUrl(Uri.parse("https://sodicclient.azurewebsites.net/#/Itemid?id="+myItem.getId()))
                    .build();
            shareDialog.show(linkContent);
        }
    }
/*--------------------------------------------------------------------------------------------------------------------------------------------*/
/*--------------------------------------------------------------------------------------------------------------------------------------------*/
public void goLoginAlert(){
    AlertDialog.Builder builder=new AlertDialog.Builder(context);
    builder.setMessage("You need to Login first!")
            .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    context.startActivity(new Intent(context,LoginActivity.class));
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
    AlertDialog dialog=builder.create();
    dialog.show();
}
}

