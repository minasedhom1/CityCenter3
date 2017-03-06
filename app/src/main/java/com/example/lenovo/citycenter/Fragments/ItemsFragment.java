package com.example.lenovo.citycenter.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.MainActivity;
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Item;
import com.example.lenovo.citycenter.R;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class ItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ShareDialog shareDialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ItemsFragment() {
        // Required empty public constructor
    }

    ListView ItemList;
   static ArrayList<Item> itemArrayList ;
    private ArrayAdapter itemAdapter;
    static ArrayList<Item> favouriteList ;
    ArrayList<String>fav_ids;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shareDialog=new ShareDialog(getActivity());
        queue = Volley.newRequestQueue(getActivity());
//First: get favourite IDs to compare
        itemArrayList = new ArrayList<>();
        favouriteList=new ArrayList<>();
        fav_ids=new ArrayList<>();
        if (favouriteList.size() == 0) {
            final StringRequest favrequest = new StringRequest(Request.Method.GET, Urls.URL_GET_FAVOURITES_FOR_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonElement root = new JsonParser().parse(response);
                            response = root.getAsString();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("allFav");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    object = object.getJSONObject("fav");
                                    Item item = new Item();
                                    item.setId(object.getString("ItemID"));
                                    item.setName(Methods.htmlRender(object.getString("Name_En")));
                                    item.setDescription(Methods.htmlRender(object.getString("Description_En")));
                                    item.setPhoto1(Urls.URL_IMG_PATH  + object.getString("Photo1"));
                                    favouriteList.add(item);
                                }
                                for (int i = 0; i < favouriteList.size(); i++) {
                                    fav_ids.add(favouriteList.get(i).getId());
                                }
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }, null);

          //  RequestQueue queue = Volley.newRequestQueue(getContext());
           if(Variables.ACCOUNT_ID!=null)
           {queue.add(favrequest);}
        }
    }
    JSONArray jsonArray;


    TextView path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_items, container, false);
         path= (TextView) view.findViewById(R.id.item_path_tv);
        ItemList= (ListView) view.findViewById(R.id.clickedItem_customList);
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();
                   JSONObject jsonObject=new JSONObject(response);
                    jsonArray=jsonObject.getJSONArray("ItemsList");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Item item=new Item();
                        item.setId(object.getString("ItemID"));
                        item.setName(Methods.htmlRender(object.getString("Name_En")));
                        item.setDescription(object.getString("Description_En"));
                        item.setPhone1(object.getString("Phone1"));
                        item.setPhone2(object.getString("Phone2"));
                        item.setPhone3(object.getString("Phone3"));
                        item.setPhone4(object.getString("Phone4"));
                        item.setPhone5(object.getString("Phone5"));
                        item.setMenu_url(object.getString("PDF_URL"));
                         if(object.getString("Rate")!="null")
                         {item.setRate(Float.valueOf(object.getString("Rate"))); //get rate and round it implicitly
                             Log.d("rate",Float.valueOf(object.getString("Rate")).toString());}

                        item.setPhoto1(Urls.URL_IMG_PATH +object.getString("Photo1"));
                        item.setCategoryName(object.getString("CategoryName_En"));
                        item.setSubcategoryName(object.getString("SubcategoryName_En"));
                        item.setCategoryID(Variables.catID);
                        itemArrayList.add(item);
                        if(fav_ids.size()!=0 && fav_ids.contains(item.getId()))
                        {
                            item.setLike(true);
                        }
                    }

                    itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.id.name2_tv,itemArrayList);
                    ItemList.setAdapter(itemAdapter);
                    itemAdapter.setNotifyOnChange(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
     //           GetDataRequest.setUrl(Variables.catID);
                GetDataRequest fetchRequest = new GetDataRequest(responseListener);
                queue.add(fetchRequest);
                 return view;
   }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    FloatingActionButton fab;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      //  ItemList = (ListView) getActivity().findViewById(R.id.clickedItem_customList);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, ItemList, false);
        ItemList.addFooterView(footerView,null,false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        ItemList.smoothScrollToPosition(0);

                // customListView.setSelection(0); //listView.smoothScrollToPosition(listView.getTop());

            }}
        );
        ItemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i==0)
                    fab.hide();
                else fab.show();
            }
        });

        path.setTextSize(16);
        path.setText(Html.fromHtml(Variables.ITEM_PATH));

    }
    public class MyCustomListAdapter extends ArrayAdapter<Item> {


        public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
            super(context, resource, textViewResourceId, objects);
        }
   class ViewHolder
   {
        Button share, call,comment,menu;
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
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_layout, parent, false);
                    holder.call = (Button) convertView.findViewById(R.id.item_call_btn);
                    holder.share = (Button) convertView.findViewById(R.id.item_share_btn);
                    holder.comment = (Button) convertView.findViewById(R.id.item_comment_btn);
                    holder.menu = (Button) convertView.findViewById(R.id.item_view_menu);
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

                final Item myItem = itemArrayList.get(position);

/*------------------------------------set values and action to views----------------------------------------*/
                holder.call.setTypeface(MainActivity.font);
                holder.share.setTypeface(MainActivity.font);
                holder.comment.setTypeface(MainActivity.font);
                holder.menu.setTypeface(MainActivity.font);


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

                        if (myItem.getPhones().size() == 1) {
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                            phoneIntent.setData(Uri.parse("tel:" + myItem.getPhone1()));
                            startActivity(phoneIntent);
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
                                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                                    phoneIntent.setData(Uri.parse("tel:" + phones_adapter.getItem(position)));
                                    startActivity(phoneIntent);
                                }
                            });
                        } else {
                            Methods.toast("No Phone numbers for this item!", getContext());
                        }
                    }
                });
/*-----------------------------------------------------------------------like btn-----------------------------------------------------------------------------------------*/
                holder.shineButton.init(getActivity());
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
                                            toast("Added to Favorites list!");
                                        } else toast(status);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            queue.add(postReq);
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
                                            toast("Removed from Favorites list!");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            queue.add(postReq);
                            itemAdapter.setNotifyOnChange(true);
                        }
                    }
                });
/*------------------------------------------------------------------pupup item image--------------------------------------------------------------------------*/

                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog nagDialog = new Dialog(getContext());
                        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        nagDialog.setContentView(R.layout.pop_up_item_image);
                        ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.imageView3);
                        Picasso.with(getContext()).load(myItem.getPhoto1()).error(R.mipmap.ic_launcher).into(ivPreview);  //             //new DownLoadImageTask(image).execute(imageUrl);
                        nagDialog.show();

                    }
                });
/*--------------------------------------------------------------------------------------------------------------------------------------------*/

 /*---------------------------------------------------------------------Menu btn-----------------------------------------------------------------------*/

                if (myItem.getMenu_url().matches("null")) {
                    holder.menu.setVisibility(View.GONE);
                } else {
                    holder.menu.setVisibility(View.VISIBLE);
                }
                holder.menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog nagDialog = new Dialog(getContext());
                        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        nagDialog.setContentView(R.layout.popup_item_load_menu_pdf_url);
                        WebView webView = (WebView) nagDialog.findViewById(R.id.item_menu_webview_);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.getSettings().setLoadWithOverviewMode(true);
                        webView.getSettings().setUseWideViewPort(true);
                        webView.getSettings().setBuiltInZoomControls(true);
                        webView.setWebViewClient(new WebViewClient());
                        webView.setWebChromeClient(new WebChromeClient());
                        String url = "https://docs.google.com/gview?url=https://sodicadmin.azurewebsites.net/PDF/" + myItem.getMenu_url() + "&embedded=true";
                        webView.loadUrl(url);
                        nagDialog.show();
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
                        }
                                , null);

                        if (Variables.ACCOUNT_ID != null) {
                            if (position > 0) {
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                queue.add(request);
                            }
                          //  else {Methods.toast("please select a value",getContext());}
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
                        if (ShareDialog.canShow(ShareLinkContent.class)) {
                            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                    .setContentTitle(Html.fromHtml(myItem.getName()).toString())
                                    .setContentDescription(Html.fromHtml(myItem.getDescription()).toString())
                                    .setImageUrl(Uri.parse(myItem.getPhoto1()))
                                    .setContentUrl(Uri.parse("https://sodicclient.azurewebsites.net/#/Itemid?id="+myItem.getId()))
                                    .build();

                            shareDialog.show(linkContent);
                        }
                    }
                });



                return convertView;

            } catch (Exception e) {
                return null;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void toast(String msg){Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();}

        public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
