package com.example.lenovo.citycenter.Fragments;

import android.content.Context;
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
import android.widget.AbsListView;
import android.widget.ListView;
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
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Item;
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.MyItemAdapter;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
   // private ArrayAdapter itemAdapter;
    static ArrayList<Item> favouriteList ;
    ArrayList<String>fav_ids;
    RequestQueue queue;
    MyItemAdapter itemAdapter1;
    JSONArray jsonArray;
    TextView path;
    FloatingActionButton fab;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_items, container, false);
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

                        if(fav_ids.size()!=0 && fav_ids.contains(item.getId()))
                        {
                            item.setLike(true);
                        }
                        itemArrayList.add(item);
                    }

                  //  itemAdapter=new MyCustomListAdapter(getContext(),android.R.layout.simple_list_item_1,R.id.name2_tv,itemArrayList);
                    itemAdapter1=new MyItemAdapter(getContext(),android.R.layout.simple_list_item_1,itemArrayList);
                    ItemList.setAdapter(itemAdapter1);
                    itemAdapter1.setNotifyOnChange(true);
                }   catch (JSONException e) {
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

       Methods.setPath(view);

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
