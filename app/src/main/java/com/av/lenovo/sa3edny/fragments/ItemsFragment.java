package com.av.lenovo.sa3edny.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.classes.ExceptionHandler;
import com.av.lenovo.sa3edny.classes.GetDataRequest;
import com.av.lenovo.sa3edny.classes.Item;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.adapters.MyItemAdapter;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsFragment extends Fragment {

    ShareDialog shareDialog;
    ListView ItemList;
   static ArrayList<Item> itemArrayList ;
    ArrayList<String>fav_ids;
    MyItemAdapter itemAdapter;
    JSONArray jsonArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getContext()));

        shareDialog=new ShareDialog(getActivity());

//First: get favourite IDs to compare
        itemArrayList = new ArrayList<>();
        fav_ids=new ArrayList<>();

      //   getFavIds();

        }

   ProgressBar progressBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_items, container, false);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            ItemList = (ListView) view.findViewById(R.id.clickedItem_customList);
            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JsonElement root = new JsonParser().parse(response);
                        response = root.getAsString();
                        JSONObject jsonObject = new JSONObject(response);
                        jsonArray = jsonObject.getJSONArray("ItemsList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            Item item = new Item();
                            item.setId(object.getString("ItemID"));
                            item.setName(Methods.htmlRender(object.getString("Name_En")));
                            item.setDescription(object.getString("Description_En"));
                            item.setPhone1(object.getString("Phone1"));
                            item.setPhone2(object.getString("Phone2"));
                            item.setPhone3(object.getString("Phone3"));
                            item.setPhone4(object.getString("Phone4"));
                            item.setPhone5(object.getString("Phone5"));
                            item.setMenu_url(object.getString("PDF_URL"));
                            item.setPromo(object.getBoolean("IsPromo"));
                            item.setPromoText(Html.fromHtml(object.getString("PromoText_En")).toString());
                            item.setPromoButton(object.getString("PromoButtonText"));
                            item.setPromo_pdf(object.getString("PDFPromo"));
                            item.setRaty(object.getBoolean("IsRatyCategory"));
                            item.setUrl_btn_text(object.getString("URLButtonText"));
                            if (object.getString("Rate") != "null") {
                                item.setRate(Float.valueOf(object.getString("Rate"))); //get rate and round it implicitly
                               // Log.d("rate", Float.valueOf(object.getString("Rate")).toString());
                            }

                           // item.setPhoto1(Urls.URL_IMG_PATH + object.getString("Photo1"));

                            String [] media={object.getString("Photo1"),
                                    object.getString("Photo2"),
                                    object.getString("Photo3"),
                                    object.getString("Photo4"),
                                    object.getString("Photo5")};
                            ArrayList<String>media_list= (ArrayList<String>) item.getItem_media();
                            for(String s : media)
                            {
                                if(!s.equals("null"))
                                 media_list.add(Urls.URL_IMG_PATH + s);
                            }
                            media_list.add("https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4");
                           /* item.getItem_media().add(Urls.URL_IMG_PATH + object.getString("Photo1"));
                            item.getItem_media().add(Urls.URL_IMG_PATH + object.getString("Photo2"));
                            item.getItem_media().add(Urls.URL_IMG_PATH + object.getString("Photo3"));
                            item.getItem_media().add(Urls.URL_IMG_PATH + object.getString("Photo4"));
                            item.getItem_media().add(Urls.URL_IMG_PATH + object.getString("Photo5"));
                            item.getItem_media().add(Urls.URL_IMG_PATH + object.getString("Video"));*/


                            item.setCategoryName(object.getString("CategoryName_En"));
                            item.setSubcategoryName(object.getString("SubcategoryName_En"));
                            item.setCategoryID(Variables.catID);

                            if (Variables.fav_ids.size() != 0 && Variables.fav_ids.contains(item.getId())) {
                                item.setLike(true);
                            }
                            if(!object.getString("NoPersonRate").equals("null"))
                            {item.setNumOfPersonsRate(object.getString("NoPersonRate"));}

                            item.setHaveLoyalty(object.getBoolean("HaveLoyalty"));

                            itemArrayList.add(item);
                        }

                        //  itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.shop_id.name2_tv,itemArrayList);
                        if(getContext()!=null)
                        {itemAdapter = new MyItemAdapter(getContext(), android.R.layout.simple_list_item_1, itemArrayList);
                        ItemList.setAdapter(itemAdapter);
                        // progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);}
                        //  itemAdapter.setNotifyOnChange(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Methods.toast(Methods.onErrorVolley(error), getContext());
                }
            };
            //           GetDataRequest.setUrl(Variables.catID);
            GetDataRequest fetchRequest = new GetDataRequest(responseListener, errorListener);
            // queue.add(fetchRequest);
            fetchRequest.setRetryPolicy(new DefaultRetryPolicy(
                    15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance().addToRequestQueue(fetchRequest);
            Methods.setPath(view,getContext());
            return view;
        }
        catch (Exception e){ e.printStackTrace();
        return null;
        }
   }



    @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, ItemList, false);
        ItemList.addFooterView(footerView,null,false);
        MainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        ItemList.smoothScrollToPosition(0);
            }}
        );


        ItemList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(i==0)
                    MainActivity.fab.hide();
                else  MainActivity.fab.show();
            }
        });


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public boolean notNull(String s)
    {
        if(s.equals("null")) return false;
        else return true;
    }

}
