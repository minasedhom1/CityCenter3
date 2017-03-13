package com.example.lenovo.citycenter.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
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
import com.example.lenovo.citycenter.classes.CacheRequest;
import com.example.lenovo.citycenter.classes.Category;
import com.example.lenovo.citycenter.classes.ExpandListAdpter;
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Subcategory;
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.VolleySingleton;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class Categories extends Fragment {

    private ExpandableListView expand_listView;
    private ArrayList<Category> categoryArrayList;
    ExpandListAdpter expandListAdpter;
    RequestQueue queue ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
        JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       // getFavourtieItems();
        categoryArrayList = new ArrayList<>();
        /*----------------------------------------------------------------------------------------------------------------------------------------------------*/
        //GetDataRequest.setUrl(Urls.URL_GET_CATEGORIES_GOODS);
        GetDataRequest.setUrl(Urls.URL_GET_CATEGORIES_GOODS);
         queue = Volley.newRequestQueue(getContext());

      /*  Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {*/
               // categoryArrayList.clear();
            CacheRequest cacheRequest = new CacheRequest(Request.Method.GET, Urls.URL_GET_CATEGORIES_GOODS, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse allresponse) {
                try {
                    String response = new String(allresponse.data);
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();  //not .toString
                    jsonArray = new JSONArray(response) ;
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Category myCategory=new Category();
                        myCategory.set_id(object.getInt("CategoryID"));
                        myCategory.set_name(object.getString("Name_En")); // X
                        myCategory.set_details(object.getString("Description_En")); // X
                        myCategory.set_icon(Urls.URL_IMG_PATH +object.getString("Logo")); //filter here
                        myCategory.setHas_sub(object.getBoolean("AllowSubcategory"));//filter here
                        if(myCategory.isHas_sub())
                        {myCategory.setSub_array(getSubs(object.getInt("CategoryID")));}
                        myCategory.setRaty(object.getBoolean("IsRaty"));
                        categoryArrayList.add(myCategory);
                    }
                    expandListAdpter=new ExpandListAdpter((AppCompatActivity) getActivity(),categoryArrayList);
                    expand_listView.setAdapter(expandListAdpter);
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "onErrorResponse:\n\n" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        //if(categoryArrayList.size()==0)
   //     GetDataRequest fetchRequest = new GetDataRequest(responseListener);
     /*   Cache.Entry entry=queue.getCache().get(Urls.URL_GET_CATEGORIES_GOODS);
        if(queue.getCache().get(Urls.URL_GET_CATEGORIES_GOODS)!=null){
            //response exists
          String cachedResponse = new String(queue.getCache().get(Urls.URL_GET_CATEGORIES_GOODS).data);

        }else {
        queue.add(fetchRequest);}*/
       // queue.start();
         queue.getCache().clear();
         queue.add(cacheRequest);
/*------------------------------------------------------------------------------------------------------------------------------------------------*/

    //    Methods.toast("hello",getContext());

        return inflater.inflate(R.layout.fragment_categories, container, false);

    }

    FloatingActionButton fab;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // customListView = (ListView) view.findViewById(R.id.listview_category);
        expand_listView = (ExpandableListView) view.findViewById(R.id.expandded_list_category);
        final View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, expand_listView, false);
/*        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://apps-valley.net/")));
            }
        });*/
        expand_listView.addFooterView(footerView,null,false);
        expand_listView.setGroupIndicator(null);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // customListView.smoothScrollToPositionFromTop(0,0);
                expand_listView.smoothScrollToPositionFromTop(0,0);
            }}
        );


        expand_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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



        expand_listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Button explore = (Button) v.findViewById(R.id.explore_btn);
                explore.setTypeface(MainActivity.font);
                if(expand_listView.isGroupExpanded(groupPosition))
                {
                    explore.setText(getString(R.string.arrow_left));
                }
                 else
                explore.setText(getString(R.string.arrow_bottom));
                Category cat = (Category) expandListAdpter.getGroup(groupPosition);
                Variables.IS_RATY_CATEGORY=cat.isRaty();//****new*****
                if(!cat.isHas_sub())
                {
                Variables.ITEM_PATH=String.valueOf(Html.fromHtml(cat.get_name()));
                Variables.catID = String.valueOf(cat.get_id());
                Fragment fragment = new ItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
                GetDataRequest.setUrl(Urls.URL_GET_SELECTED_CATEGORY_ITEMS+ Variables.catID ); //set the clicked cat id to fetch it's items
                }
                return false;
            }
        });


          expand_listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
       @Override
       public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
           Category cat = (Category) expandListAdpter.getGroup(groupPosition);
           Variables.IS_RATY_CATEGORY=cat.isRaty(); //****new*****
           String subcatID=cat.getSub_array().get(childPosition).getSubcat_id();
           String subcatName=cat.getSub_array().get(childPosition).getSubCat_name();
          //  subcatName=subcatName.replace("\n","");
            Variables.ITEM_PATH= String.valueOf(Html.fromHtml(cat.get_name())+"> "+String.valueOf(Html.fromHtml(subcatName)));
            Fragment fragment = new ItemsFragment();
           getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
           String url = Urls.URL_GET_SELECTED_SUBCATEGORY_ITEM + subcatID;
            GetDataRequest.setUrl(url );
           return false;
       }
   });
}



    public  ArrayList<Subcategory> getSubs(int catID) {


        final ArrayList<Subcategory> subCat_array = new ArrayList();
        StringRequest subcatRequest= new StringRequest(Request.Method.GET, Urls.URL_GET_SELECTED_CATEGORY_SUBCATEGORIES + catID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JsonElement root = new JsonParser().parse(response);
                            response = root.getAsString();  //not .toString
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Subcategory mySub = new Subcategory();
                                mySub.setSubcat_id((object.getString("SubCategoryID")));
                                mySub.setSubCat_name(object.getString("Name_En")); // X
                                mySub.setSubCat_describtion(object.getString("Description_En")); // X
                                mySub.setSubCat_icon_url(Urls.URL_IMG_PATH + object.getString("Photo1"));
                                subCat_array.add(mySub);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, null) ;

        VolleySingleton.getInstance().addToRequestQueue(subcatRequest);
        return subCat_array;
    }

}