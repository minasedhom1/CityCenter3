package com.example.lenovo.citycenter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Item;
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.MyItemAdapter;
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


public class LatestOffers extends Fragment {



    public LatestOffers() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ListView ItemList;
    private ArrayList<Item> itemArrayList;
    private MyItemAdapter  itemAdapter;
    JSONArray jsonArray;
    FloatingActionButton fab;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_latest_offers, container, false);
        ItemList= (ListView) view.findViewById(R.id.clickedItem_customList);
        itemArrayList = new ArrayList<>();

        GetDataRequest.setUrl(Urls.URL_GET_LATEST_OFFERS_ITEMS );
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

                   /*     if(fav_ids.size()!=0 && fav_ids.contains(item.getId()))
                        {
                            item.setLike(true);
                        }*/
                        itemArrayList.add(item);
                    }
                    itemAdapter=new MyItemAdapter(getContext(),android.R.layout.simple_list_item_1,itemArrayList);
                    ItemList.setAdapter(itemAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetDataRequest fetchRequest = new GetDataRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(fetchRequest);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, ItemList, false);
        ItemList.addFooterView(footerView,null,false);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
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
                    fab.hide();
                else fab.show();
            }
        });
        Methods.setPath(view);
    }

}
