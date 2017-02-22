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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
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
import com.example.lenovo.citycenter.Assets.Urls;
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

public class ItemsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
                                    item.setName(htmlRender(object.getString("Name_En")));
                                    item.setDescription(htmlRender(object.getString("Description_En")));
                                    item.setPhoto1("https://sa3ednymalladmin.azurewebsites.net/IMG/" + object.getString("Photo1"));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_items, container, false);
        ItemList= (ListView) view.findViewById(R.id.clickedItem_customList);
        //Second: get items for that category id (get from cat frag.), then compare with  fav IDs
            Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("ItemsList");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Item item=new Item();
                        item.setId(object.getString("ItemID"));
                        item.setName(htmlRender(object.getString("Name_En")));
                        item.setDescription(htmlRender(object.getString("Description_En")));
                        item.setPhone1(object.getString("Phone1"));
                        item.setPhoto1("https://sa3ednymalladmin.azurewebsites.net/IMG/"+object.getString("Photo1"));
                        item.setCategoryName(object.getString("CategoryName_En"));
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
       /* if(itemArrayList.size()!=0) //check the static arraylist, not to re-request.
        { if(itemArrayList.get(0).getCategoryID()==Urls.catID  )//check if the catId is identical.if so ->|use it to fill adapter|
            {  itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.id.name2_tv,itemArrayList);
                ItemList.setAdapter(itemAdapter);
                itemAdapter.setNotifyOnChange(true);
            }
            else {  //if not, request new items for that category
            itemArrayList.clear();
            GetDataRequest fetchRequest = new GetDataRequest(responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(fetchRequest);
        }
        }
else {    //if it's empty,request.*/
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
    }

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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder=new ViewHolder();

            try {

                if(convertView==null) {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.item_clicked, parent, false);

                  //  Item myItem = itemArrayList.get(position);

                    holder.name = (TextView) convertView.findViewById(R.id.name2_tv);
                    holder.description = (TextView) convertView.findViewById(R.id.promo2_tv);
                    holder.image = (ImageView) convertView.findViewById(R.id.item_icon);
                    //holder.call = (Button) convertView.findViewById(R.id.call_btn);
                   // holder.website = (Button) convertView.findViewById(R.id.website_btn);
                    holder.shineButton = (ShineButton) convertView.findViewById(R.id.like_btn);
                    convertView.setTag(holder);
                }
                else {holder = (ViewHolder) convertView.getTag();}

                final Item myItem = itemArrayList.get(position);

/*------------------------------------set values and action to views----------------------------------------*/

                holder.name.setText(Html.fromHtml(myItem.getName()));
                holder. description.setText(Html.fromHtml(myItem.getDescription()));

               //  holder.image.setMaxHeight(300);
                Picasso.with(getContext()).load(myItem.getPhoto1()).error(R.mipmap.ic_launcher).into(holder.image);  //             //new DownLoadImageTask(image).execute(imageUrl);

/*
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
                        *//*Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(myItem.getSite()));
                        startActivity(i);*//*
                        toast(myItem.getPhoto1());
                    }
                });*/



                /*-------------------like btn--------------------*/
                holder.shineButton.init(getActivity());
                holder.shineButton.setChecked(myItem.isLike());
             /*   if (Variables.ACCOUNT_ID==null)
                {
                    holder.shineButton.setVisibility(View.INVISIBLE);
                }*/

                holder.shineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //  MainActivity.fav_items.add(myItem);
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
                                        }
                                         else toast(status);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, null);
                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            queue.add(postReq);
                            myItem.setLike(true);
                        }
                    else {
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
                                        toast("Removed from Favorites list!");}
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, null);
                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            queue.add(postReq);
                            itemAdapter.setNotifyOnChange(true);
                        }
                        //toast("it's already added to favourites list!");
                }
}
                );


            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog nagDialog = new Dialog(getContext());
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setContentView(R.layout.pop_up_item_image);
                    ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.imageView3);
                    Picasso.with(getContext()).load(myItem.getPhoto1()).error(R.mipmap.ic_launcher).into(ivPreview);  //             //new DownLoadImageTask(image).execute(imageUrl);

                    nagDialog.show();


                }
            });
                return convertView;

            } catch (Exception e) {
                return null;
            }
        }




  /*    class ViewHolder

        {
            Button website, call;
            ImageView image;
            TextView name, description;
            ShineButton shineButton;

            public ViewHolder(View view)
            {
            name = (TextView) view.findViewById(R.id.name2_tv);
            description = (TextView) view.findViewById(R.id.promo2_tv);
            image = (ImageView) view.findViewById(R.id.item_icon);
            call = (Button) view.findViewById(R.id.call_btn);
            website = (Button) view.findViewById(R.id.website_btn);
            shineButton = (ShineButton) view.findViewById(R.id.like_btn);
            view.setTag(this);
            }
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
 //       outState.putSerializable("key",myItem);

    }

    public void toast(String msg){Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();}

        public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public String htmlRender(String ss)

    {
        ss=ss.replace("span","font");
        ss=ss.replace("style=\"color:","color=");
        ss=ss.replace(";\"","");
        ss=ss.replaceAll("<p>","");
        ss=ss.replaceAll("</p>",""); //********
        return ss;
    }


}
