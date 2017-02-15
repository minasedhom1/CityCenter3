package com.example.lenovo.citycenter.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Item;
import com.example.lenovo.citycenter.R;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public LatestOffers() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ListView ItemList;
    private ArrayList<Item> itemArrayList;
    private ArrayAdapter itemAdapter;
    Item myItem;


    JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
View view=inflater.inflate(R.layout.fragment_latest_offers, container, false);

        ItemList= (ListView) view.findViewById(R.id.clickedItem_customList);
        itemArrayList = new ArrayList<>();

        GetDataRequest.setUrl(Variables.URL_GET_LATEST_OFFERS_ITEMS );
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
                        item.setName(Methods.htmlRender(object.getString("Name_En")));
                        item.setDescription(Methods.htmlRender(object.getString("Description_En")));
                        item.setPhone1(object.getString("Phone1"));
                        item.setPhoto1("https://sa3ednymalladmin.azurewebsites.net/IMG/"+object.getString("Photo1"));
                        item.setCategoryName(object.getString("CategoryName_En"));
                        itemArrayList.add(item);

                    }

                    itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.id.name2_tv,itemArrayList);
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

    FloatingActionButton fab;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
  //      itemArrayList = new ArrayList<>();
        //itemArrayList.add(new Item("Carrefour","a7la klam","https://sa3ednymalladmin.azurewebsites.net/IMG/636177604271948352carrefour-logo-9D3FDB68F7-seeklogo.com.gif","01275791088","www.google.com",true));

      //  ItemList = (ListView) getActivity().findViewById(R.id.clickedItem_customList);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, ItemList, false);
        ItemList.addFooterView(footerView,null,false);

     /*   itemAdapter = new MyCustomListAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.shopNameTextView, itemArrayList);
        ItemList.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();*/
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

    }
    boolean like=false;

    public class MyCustomListAdapter extends ArrayAdapter<Item> {


        public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
            super(context, resource, textViewResourceId, objects);
        }
        class ViewHolder
        {
            Button website, call;
            ImageView image;
            TextView name, description;
            ShineButton shineButton;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyCustomListAdapter.ViewHolder holder=new MyCustomListAdapter.ViewHolder();
            try {

                if(convertView==null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_clicked, parent, false);

                    myItem = itemArrayList.get(position);

                    holder.name = (TextView) convertView.findViewById(R.id.name2_tv);
                    holder.description = (TextView) convertView.findViewById(R.id.promo2_tv);
                    holder.image = (ImageView) convertView.findViewById(R.id.item_icon);
                    holder.call = (Button) convertView.findViewById(R.id.call_btn);
                    holder.website = (Button) convertView.findViewById(R.id.website_btn);
                    holder.shineButton = (ShineButton) convertView.findViewById(R.id.like_btn);
                    convertView.setTag(holder);
                }
                else {holder = (ViewHolder) convertView.getTag();}
                final Item myItem = itemArrayList.get(position);

/*------------------------------------set values and action to views----------------------------------------*/

                holder.name.setText(Html.fromHtml(myItem.getName()));
                holder. description.setText(Html.fromHtml(myItem.getDescription()));


                Picasso.with(getContext()).load(myItem.getPhoto1()).error(R.mipmap.ic_launcher).into(holder.image);  //             //new DownLoadImageTask(image).execute(imageUrl);


                holder.call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:" + myItem.getPhone1()));
                        startActivity(phoneIntent);
                    }
                });



                holder.website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(myItem.getSite()));
                        startActivity(i);*/
                        toast(myItem.getPhoto1());
                    }
                });



                /*-------------------like btn--------------------*/

                holder.shineButton.init(getActivity());


                //holder.shineButton.setChecked(itemArrayList.get(position).isLike());


                holder.shineButton.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {

                                                          }
                                                      }
                );


                return convertView;

            } catch (Exception e) {
                return null;
            }
        }
        }

    public void toast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();}

}
