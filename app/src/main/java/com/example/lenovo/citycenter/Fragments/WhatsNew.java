package com.example.lenovo.citycenter.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class WhatsNew extends Fragment {
    public WhatsNew() {
        // Required empty public constructor
    }
JSONArray jsonArray;
MyItemAdapter itemAdapter;
    ListView ItemList;
    private ArrayList<Item> itemArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_whats_new, container, false);

        ItemList= (ListView) view.findViewById(R.id.clickedItem_customList);
        itemArrayList = new ArrayList<>();

        StringRequest request=new StringRequest(Request.Method.GET,Urls.URL_GET_NEW_ITEMS,
                new Response.Listener<String>() {

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
                  //  itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.id.name2_tv,itemArrayList);
                     itemAdapter=new MyItemAdapter(getContext(),android.R.layout.simple_list_item_1,itemArrayList);
                     ItemList.setAdapter(itemAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, ItemList, false);
        ItemList.addFooterView(footerView,null,false);
        Methods.setPath(view);
    }

































  /*  public class MyCustomListAdapter extends ArrayAdapter<Item> {

class ViewHolder
        {
            Button website, call;
            ImageView image;
            TextView name, description;
            ShineButton shineButton;
        }

        public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder=new ViewHolder();
            try {
                if(convertView==null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_layout, parent, false);

                    myItem = itemArrayList.get(position);
                    holder.name = (TextView) convertView.findViewById(R.id.name2_tv);
                    holder.description = (TextView) convertView.findViewById(R.id.item_description);
                    holder.image = (ImageView) convertView.findViewById(R.id.item_icon);
                    //holder.call = (Button) convertView.findViewById(R.id.call_btn);
                   // holder.website = (Button) convertView.findViewById(R.id.website_btn);
                    holder.shineButton = (ShineButton) convertView.findViewById(R.id.like_btn);
                    convertView.setTag(holder);

                }
                else {holder = (ViewHolder) convertView.getTag();}
                final Item myItem = itemArrayList.get(position);

*//*------------------------------------set values and action to views----------------------------------------*//*
                holder.name.setText(Html.fromHtml(myItem.getName()));
                holder. description.setText(Html.fromHtml(myItem.getDescription()));
                 Picasso.with(getContext()).load(myItem.getPhoto1()).error(R.mipmap.ic_launcher).into(holder.image);  //             //new DownLoadImageTask(image).execute(imageUrl);
                //new DownLoadImageTask(image).execute(imageUrl);

              *//*  holder.call.setOnClickListener(new View.OnClickListener() {
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
                        *//**//*Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(myItem.getSite()));
                        startActivity(i);*//**//*
                        toast(myItem.getPhoto1());
                    }
                });*//*


               *//*-------------------like btn--------------------*//*

                holder. shineButton.init(getActivity());
               // holder.shineButton.setClickable(myItem.isLike());
                *//*
                holder.shineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
*//*
                return convertView;

            } catch (Exception e) {
                return null;
            }

        }
    }*/
   /* public void toast(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }
*/
}
