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
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.m.sa3edny.ui.login.LoginActivity;
import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.utils.Variables;
import com.av.m.sa3edny.R;
import com.av.m.sa3edny.classes.VolleySingleton;
import com.av.m.sa3edny.ui.home.categories.loyality.VoucherFragment;
import com.bumptech.glide.Glide;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sackcentury.shinebuttonlib.ShineButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by mina on 12/01/2017.
 */

public class MyItemAdapter extends ArrayAdapter<Item> {
    private ShareDialog shareDialog ;
    private Context context;
    private List<Item> itemsList;
    private ProgressBar progressBar;
    public Fragment fragment = null;
    private Class fragmentClass = null;

    public MyItemAdapter(Context context, int resource, List<Item> itemsList) {
        super(context, resource,itemsList);
        this.context= context;
        this.itemsList=itemsList;
    }

    private class ViewHolder
        {
           // ImageButton menu,share, call,comment;
          //  ImageView image;
            ViewPager viewPager;
            TextView name, description,rate,rates_num,promo_txt;
            ShineButton addToFavBtn;
            Spinner rateSpin;
            Button menu,share, call,comment, optional_btn, item_view_loyalty_btn,item_directions_btn;
            View promo_view;
            ImageView facebook,twitter,insta,youtube;

            TabLayout tabLayout;

        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {

            try {
                ViewHolder holder = new ViewHolder();
                  if(context!=null) {

                      if (convertView == null) {
                          LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                          convertView = inflater.inflate(R.layout.item_layout, parent, false);
                          holder.call = (Button) convertView.findViewById(R.id.item_call_btn);
                          holder.share = (Button) convertView.findViewById(R.id.item_share_btn);
                          holder.comment = (Button) convertView.findViewById(R.id.item_comment_btn);
                          holder.menu = (Button) convertView.findViewById(R.id.item_view_menu_btn);
                          holder.name = (TextView) convertView.findViewById(R.id.name2_tv);
                          holder.description = (TextView) convertView.findViewById(R.id.item_description);

                          //  holder.image = (ImageView) convertView.findViewById(R.id.item_icon);

                          holder.viewPager= (ViewPager) convertView.findViewById(R.id.viewpager_list);
                          holder.addToFavBtn = (ShineButton) convertView.findViewById(R.id.like_btn);
                          holder.rate = (TextView) convertView.findViewById(R.id.item_rate_value);
                          holder.rateSpin = (Spinner) convertView.findViewById(R.id.rate_spinner);
                          holder.optional_btn = (Button) convertView.findViewById(R.id.option_btn);
                          holder.rates_num = (TextView) convertView.findViewById(R.id.rates_num);
                          holder.promo_txt = (TextView) convertView.findViewById(R.id.promo_tv);
                          holder.promo_view = convertView.findViewById(R.id.promo_layout);
                          holder.item_view_loyalty_btn= (Button) convertView.findViewById(R.id.item_view_loyalty_btn);

                          holder.item_directions_btn=convertView.findViewById(R.id.item_directions_btn);


                          //Social Media buttons
                          holder.facebook=convertView.findViewById(R.id.iv_facebook);
                          holder.twitter=convertView.findViewById(R.id.iv_twitter);
                          holder.insta=convertView.findViewById(R.id.iv_instagram);
                          holder.youtube=convertView.findViewById(R.id.iv_youtube);

                          holder.facebook.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Methods.toast("Data not Added.", getContext());
                              }
                          });
                          holder.twitter.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Methods.toast("Data not Added.", getContext());
                              }
                          });
                          holder.insta.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Methods.toast("Data not Added.", getContext());
                              }
                          });
                          holder.youtube.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                  Methods.toast("Data not Added.", getContext());
                              }
                          });

                          holder.tabLayout = (TabLayout) convertView.findViewById(R.id.tabDots);
                          holder.tabLayout.setupWithViewPager(holder.viewPager, true);


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

                        if(myItem.getLatitude()==null || myItem.getLatitude().equals("")) {
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

                      holder.name.setText(myItem.getName());
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
                              new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, myItem.getPhones());

                      if (!myItem.isRatyCategory()) holder.rateSpin.setVisibility(View.GONE);
                      else holder.rateSpin.setVisibility(View.VISIBLE);
   /*--------------------------------------------------------------------------------------------------------------------------------------------*/
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
                                      Methods.toast(e.getMessage().toString(), getContext());
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
                                  final Dialog nagDialog = new Dialog(getContext());
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
                                  Methods.toast("No Phone numbers for this item!", getContext());
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
                      if (Variables.ITEM_PATH.equals("Favorites")) {
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
                                      }, new Response.ErrorListener() {
                                          @Override
                                          public void onErrorResponse(VolleyError error) {
                                              Methods.toast(error.getMessage(), context);
                                          }
                                      });
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
                                      }, new Response.ErrorListener() {
                                          @Override
                                          public void onErrorResponse(VolleyError error) {
                                              Methods.toast(error.getMessage(), context);
                                          }
                                      });

                                      VolleySingleton.getInstance().addToRequestQueue(postReq);
                                      if (Variables.ITEM_PATH.matches("Favorites")) {
                                          itemsList.remove(myItem);
                                          setNotifyOnChange(true);
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
/*------------------------------------------------------------------pupup item image--------------------------------------------------------------------------*/

               /*       holder.image.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                     //         popUpPhoto(myItem.getPhoto1());

                          }
                      });*/
               //Viewpager part replaced with the ordinary image view ******new*******
                      ArrayList<String> media=myItem.getItemMedia();
                      holder.viewPager.setAdapter(new ViewPagerAdapter(context,media));
/*------------------------------------------------------------------------------------------------------------------------------------------------*/

/*---------------------------------------------------------------------Menu btn-----------------------------------------------------------------------*/

                      if (myItem.getURLButtonText()==null || myItem.getURLButtonText().matches("null") ) {
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
                                              Methods.toast("Rating Success", getContext());
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
// launching Facebook comments activity
                              Intent intent = new Intent(context, FbCommentActivity.class);

// passing the article url
                              intent.putExtra("url", "https://www.androidhive.info/2016/06/android-firebase-integrate-analytics/");
                              getContext().startActivity(intent);
                              //popComment(myItem.getId());
                          }
                      });
                       if(myItem.isHaveLoyalty())
                           holder.item_view_loyalty_btn.setVisibility(View.VISIBLE);
                      else
                           holder.item_view_loyalty_btn.setVisibility(View.GONE);

                      holder.item_view_loyalty_btn.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View v) {

                              fragmentClass = VoucherFragment.class;
                              try {
                                   fragment = (Fragment) fragmentClass.newInstance();
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                              Bundle bundle=new Bundle();
                              bundle.putString("ItemID",myItem.getId());
                              bundle.putString("ItemName",Html.fromHtml(myItem.getName()).toString());
                              fragment.setArguments(bundle);
                              ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).commit();


                          }
                      });


                      if(myItem.getItemMedia().size()>1) {
                          holder.tabLayout.setVisibility(View.VISIBLE);
                      }
                      else {
                          holder.tabLayout.setVisibility(View.GONE);
                      }
                      return convertView;

                  }else{return null;}
                 } catch (Exception e) {
                   return null;
               }

        }
/*--------------------------------------------------------------------------------------------------------------------------------------------*/
    private void popUpPhoto(String photoUrl) {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.pop_up_item_image);
        ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.imageView3);
        Glide.with(getContext()).load(photoUrl).into(ivPreview);  //             //new DownLoadImageTask(image).execute(imageUrl);
        nagDialog.show();
    }
/*-----------------------------------------------------------------------------------------------------------------------------------------------------*/


/*---------------------------------------------------------------Menu Btn-----------------------------------------------------------------------------*/
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

/*---------------------------------------------------------------Comment Btn-----------------------------------------------------------------------------*/
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
    String html = "<!doctype html> <html lang=\"en\"> <head></head> <body> " +
            "<div id=\"fb-root\"></div> <script>(function(d, s, id) { var js, fjs = d.getElementsByTagName(s)[0]; if (d.getElementById(id)) return; js = d.createElement(s); js.id = id; js.src = \"//connect.facebook.net/en_US/sdk.js#xfbml=1&version=v2.8&appId=1251459601575440\"; fjs.parentNode.insertBefore(js, fjs); }(document, 'script', 'facebook-jssdk'));</script> " +
            "<div class=\"fb-comments\" data-href=\"" + "https://sodicclient.azurewebsites.net/#/Itemid?id="+item_id+ "\" " +
            "data-numposts=\"" +"3"+ "\" data-order-by=\"reverse_time\">" +
            "</div> </body> </html>";
    mWebViewComments.loadDataWithBaseURL("https://sodicclient.azurewebsites.net/", html, "text/html", "UTF-8", null);
    nagDialog.setCanceledOnTouchOutside(true);
    nagDialog.show();
}
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

