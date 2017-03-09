package com.example.lenovo.citycenter.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.MainActivity;
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
import static com.example.lenovo.citycenter.Assets.Methods.htmlRender;
import static com.example.lenovo.citycenter.Assets.Methods.toast;

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

     //  itemArrayList = MainActivity.fav_items;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_favourite, container, false);
        itemList= (ListView) view.findViewById(R.id.favourite_list);

                StringRequest request=new StringRequest(Request.Method.GET, Urls.URL_GET_FAVOURITES_FOR_ID, new Response.Listener<String>() {
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
                        item.setId(object.getString("ItemID"));
                        item.setName(htmlRender(object.getString("ItemNameEn")));
                        item.setDescription(htmlRender(object.getString("Description_En")));
                        item.setPhone1(object.getString("Phone1"));
                        item.setPhoto1(Urls.URL_IMG_PATH+object.getString("Photo1"));
                        if(object.getString("Rate")!="null")
                        {item.setRate(Float.valueOf(object.getString("Rate"))); //get rate and round it implicitly
                            Log.d("rate",Float.valueOf(object.getString("Rate")).toString());}
                        item.setMenu_url(object.getString("PDF_URL"));
                        item.setCategoryID(object.getString("CategoryID"));
                        item.setCategoryName(object.getString("CategoryName"));


                       // item.setCategoryName(object.getString("CategoryName_En"));
                        itemArrayList.add(item);
                    }
                    itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.id.name2_tv,itemArrayList);
                    itemList.setAdapter(itemAdapter);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Methods.toast(error.toString(),getContext());
                            }
                        });

          if(Variables.ACCOUNT_ID!=null)
            { RequestQueue queue = Volley.newRequestQueue(getActivity());
              queue.add(request);
            }
        else
        {
          toast("Login to display your Favourites",getContext());
}
          return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*----------------------------------------------APi favoutrite handle----------------------------------------------------*/

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
                    convertView = inflater.inflate(R.layout.item_layout, parent, false);
                    myItem = itemArrayList.get(position);
                    holder.name = (TextView) convertView.findViewById(R.id.name2_tv);
                    holder.description = (TextView) convertView.findViewById(R.id.item_description);
                    holder.image = (ImageView) convertView.findViewById(R.id.item_icon);

                   // holder.call = (Button) convertView.findViewById(R.id.call_btn);
                  // holder.website = (Button) convertView.findViewById(R.id.website_btn);
                    holder.shineButton = (ShineButton) convertView.findViewById(R.id.like_btn);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final Item myItem = itemArrayList.get(position);
/*------------------------------------set values and action to views----------------------------------------*/

                holder.name.setText(Html.fromHtml(myItem.getName()));
                holder.description.setText(Html.fromHtml(myItem.getDescription()));
                Picasso.with(getContext()).load(myItem.getPhoto1()).error(R.mipmap.ic_launcher).into(holder.image);
                //             //new DownLoadImageTask(image).execute(imageUrl);
                /*-------------------like btn--------------------*/
                holder.shineButton.init(getActivity());
                holder.shineButton.setChecked(true);

                holder.shineButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                    public void onClick(View v) {
                       StringRequest postReq = new StringRequest(Request.Method.POST, Urls.URL_DELETE_FROM_FAVORITES_ITEM + myItem.getId(), new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                               JsonElement root = new JsonParser().parse(response);
                               root = new JsonParser().parse(root.getAsString());   //double parse
                               response = root.getAsString();
                               try {
                                   JSONObject obj = new JSONObject(response);
                                   String status = obj.getString("Status");
                                   if(status.matches("Success"))
                                   {
                                       Methods.toast("Removed from Favorites list!",getContext());}
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       }, new Response.ErrorListener() {
                           @Override
                           public void onErrorResponse(VolleyError error) {
                               Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_LONG).show();
                           }
                       });
                       RequestQueue queue = Volley.newRequestQueue(getContext());
                       queue.add(postReq);
                       itemAdapter.remove(myItem);
                       itemAdapter.setNotifyOnChange(true);
                    }});

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
    }
}