package com.example.lenovo.citycenter.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.sax.RootElement;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.Classes.Item;
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
import static com.example.lenovo.citycenter.Assets.Methods.htmlRender;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {


    ListView itemList;
    ArrayList<Item> itemArrayList = new ArrayList<>();
    private ArrayAdapter itemAdapter;
    Item myItem;
    JSONArray jsonArray;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // itemArrayList=Methods.getFavourites((AppCompatActivity) getActivity());
       StringRequest request=new StringRequest(Request.Method.GET, Variables.URL_GET_FAVOURITES_FOR_ID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonElement root=new JsonParser().parse(response);
                response = root.getAsString();

                try {
                    JSONObject jsonObject= new JSONObject(response);

                    jsonArray=jsonObject.getJSONArray("allFav");

                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        object= object.getJSONObject("fav");
                        Item item=new Item();
                        item.setName(htmlRender(object.getString("Name_En")));
                        item.setDescription(htmlRender(object.getString("Description_En")));
                       // item.setPhone1(object.getString("Phone1"));
                        item.setPhoto1("https://sa3ednymalladmin.azurewebsites.net/IMG/"+object.getString("Photo1"));
                       // item.setCategoryName(object.getString("CategoryName_En"));
                        itemArrayList.add(item);
                    }
                    itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.id.name2_tv,itemArrayList);
                    itemList.setAdapter(itemAdapter);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }

        },null);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return inflater.inflate(R.layout.fragment_favourite, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

     //   View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, itemList, false);
    //    itemList.addFooterView(footerView,null,false);

        // toast(myItem.name);
   /*     this.itemArrayList = ItemsFragment.favouriteList;

        if (itemArrayList.size() != 0) {
            ItemList = (ListView) getActivity().findViewById(R.id.clickedItem_customList);
            View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, ItemList, false);
            ItemList.addFooterView(footerView);
            itemAdapter = new MyCustomListAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.shopNameTextView, itemArrayList);
            ItemList.setAdapter(itemAdapter);
            itemAdapter.notifyDataSetChanged();

        }*/
        //**********
        //this.myItem = ItemsFragment.myItem;
        //    int y=ItemsFragment.itemArrayList.size();
        // ArrayList<Item> itemArrayList2=ItemsFragment.itemArrayList;
        //  itemArrayList.add(new Item("Carrefour","a7la klam","https://sa3ednymalladmin.azurewebsites.net/IMG/636177604271948352carrefour-logo-9D3FDB68F7-seeklogo.com.gif","01275791088","www.google.com",true));
       /*if(ItemsFragment.itemArrayList.size()!=0) {
           for(int i=0; i<itemArrayList.size();i++)
           {
               myItem = ItemsFragment.itemArrayList.get(i);

           if(myItem.isLike())
           {
              //itemArrayList.add(myItem);
           }
               else{
               itemArrayList.remove(myItem);
           }
       }
       }*/

        /*----------------------------------------------APi favoutrite handle----------------------------------------------------*/

     itemList= (ListView) view.findViewById(R.id.favourite_list);


    }

    public class MyCustomListAdapter extends ArrayAdapter<Item> {


        public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        class ViewHolder {
            Button website, call;
            ImageView image;
            TextView name, description;
            ShineButton shineButton;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();

            try {

                if (convertView == null) {
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
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                final Item myItem = itemArrayList.get(position);

/*------------------------------------set values and action to views----------------------------------------*/

                holder.name.setText(Html.fromHtml(myItem.getName()));
                holder.description.setText(Html.fromHtml(myItem.getDescription()));
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

                holder.shineButton.setOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {

                                                          }
                                                      }
                );

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
                return convertView;

            } catch (Exception e) {
                return null;
            }
        }

        public void toast(String msg) {
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}