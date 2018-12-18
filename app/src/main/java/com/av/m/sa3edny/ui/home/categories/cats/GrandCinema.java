package com.av.m.sa3edny.ui.home.categories.cats;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.av.m.sa3edny.networkUtilities.GetCallback;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.utils.Variables;
import com.av.m.sa3edny.ui.MainActivity;
import com.av.m.sa3edny.classes.GetDataRequest;
import com.av.m.sa3edny.R;
import com.av.m.sa3edny.ui.items.ItemsFragment;

import org.json.JSONArray;

import java.util.ArrayList;



public class GrandCinema extends Fragment implements GetCallback.OnGetCatAndSubCatsFinish {
    private ExpandableListView expand_listView;
    private ArrayList<Category> categoryArrayList;
    private ExpandListAdpter expandListAdpter;
    JSONArray jsonArray;

    ProgressBar progressBar;
    //FloatingActionButton fab;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_grandcinema, container, false);

        progressBar=view.findViewById(R.id.progress_bar);
        expand_listView = view.findViewById(R.id.expandded_list_category);
        categoryArrayList = new ArrayList<>();
        /*getCats();*/

        Model.getServicesCached(this);

        return view;
    }

/*-----------------------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expand_listView.setGroupIndicator(null);

      //   fab = (FloatingActionButton) getActivity().findViewById(R.shop_id.fab);
        MainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // customListView.setSmoothScrollbarEnabled(true);
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
                    MainActivity.fab.hide();
                else  MainActivity.fab.show();
            }
        });

        expand_listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Button explore = v.findViewById(R.id.explore_btn);
                explore.setTypeface(MainActivity.font);
                if(expand_listView.isGroupExpanded(groupPosition))
                {explore.setText(getString(R.string.arrow_left));}
                else explore.setText(getString(R.string.arrow_bottom));

                Category cat = (Category) expandListAdpter.getGroup(groupPosition);
                Variables.ITEM_PATH=String.valueOf(Html.fromHtml(cat.getName_En()));
                if(!cat.isAllowSubcategory())
                {
                    Variables.catID = String.valueOf(cat.getCategoryID());
                    Fragment fragment = new ItemsFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
                    GetDataRequest.setUrl(Urls.URL_GET_SELECTED_CATEGORY_ITEMS+ Variables.catID ); //set the clicked cat shop_id to fetch it's items
                }
                return false;
            }
        });

        expand_listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category cat = (Category) expandListAdpter.getGroup(groupPosition);
                String subcatID=cat.getSub_array().get(childPosition).getSubcat_id();
                String subcatName=cat.getSub_array().get(childPosition).getSubCat_name();
                Variables.ITEM_PATH= String.valueOf(Html.fromHtml(cat.getName_En())+"> "+String.valueOf(Html.fromHtml(subcatName)));
                Fragment fragment = new ItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
                String url = Urls.URL_GET_SELECTED_SUBCATEGORY_ITEM + subcatID;
                GetDataRequest.setUrl(url);
                return false;
            }
        });

    }

/*
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
                                mySub.setSubCat_icon_url(object.getString("Photo1"));
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

   public void  getCats(){
        showProgress();
        CacheRequest cacheRequest = new CacheRequest(Request.Method.GET,Urls.URL_GET_CATEGORIES_SERVICES, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse allresponse) {
                try {
                    String response = new String(allresponse.data);
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();  //not .toString
                    jsonArray = new JSONArray(response) ;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Category myCategory=new Category();
                        myCategory.setCategoryID(object.getInt("CategoryID"));
                        myCategory.setName(Methods.htmlRender(object.getString("Name_En"))); // X
                        myCategory.setDescription_En(Methods.htmlRender(object.getString("Description_En"))); // X
                        myCategory.setLogo(object.getString("Logo"));
                        myCategory.setAllowSubcategory(object.getBoolean("AllowSubcategory"));//filter here
                        if(myCategory.isAllowSubcategory())
                        {myCategory.setSub_array(getSubs(object.getInt("CategoryID")));}//filter here
                        myCategory.setRatyCategory(object.getBoolean("IsRaty"));
                        categoryArrayList.add(myCategory);
                    }

                    expandListAdpter=new ExpandListAdpter((AppCompatActivity) getActivity(),categoryArrayList);
                    expand_listView.setAdapter(expandListAdpter);
                    hideProgress();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(Methods.onErrorVolley(error), getContext());
                hideProgress();
            }

        });

        VolleySingleton.getInstance().addToRequestQueue(cacheRequest);
    }*/



    public void showProgress(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onGetCatsSuccess(ArrayList<Category> catList) {
        expandListAdpter=new ExpandListAdpter((AppCompatActivity) getActivity(),catList);
        expand_listView.setAdapter(expandListAdpter);
        hideProgress();
    }

    @Override
    public void onGetCatsFailure(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        hideProgress();
    }
}
