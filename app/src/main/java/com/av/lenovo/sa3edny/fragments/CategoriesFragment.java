package com.av.lenovo.sa3edny.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.classes.CacheRequest;
import com.av.lenovo.sa3edny.classes.Category;
import com.av.lenovo.sa3edny.adapters.ExpandListAdpter;
import com.av.lenovo.sa3edny.classes.GetDataRequest;
import com.av.lenovo.sa3edny.classes.Subcategory;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.av.lenovo.sa3edny.services.CheckNotificationService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class CategoriesFragment extends Fragment {

    private ExpandableListView expand_listView;
    private ArrayList<Category> categoryArrayList;
    ExpandListAdpter expandListAdpter;
    JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      categoryArrayList = new ArrayList<>();

        CacheRequest cacheRequest = new CacheRequest(Request.Method.GET,Urls.URL_GET_CATEGORIES_GOODS, new Response.Listener<NetworkResponse>() {
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
                        myCategory.setCategoryID(object.getInt("CategoryID"));
                        myCategory.setName_En(Methods.htmlRender(object.getString("Name_En"))); // X
                        myCategory.setDescription_En(Methods.htmlRender(object.getString("Description_En"))); // X
                        myCategory.setLogo(Urls.URL_IMG_PATH + object.getString("Logo"));
                        myCategory.setAllowSubcategory(object.getBoolean("AllowSubcategory"));//filter here
                        if(myCategory.isAllowSubcategory())
                        {myCategory.setSub_array(getSubs(object.getInt("CategoryID")));}//filter here
                        myCategory.setRaty(object.getBoolean("IsRaty"));

                        categoryArrayList.add(myCategory);
                    }
                    expandListAdpter=new ExpandListAdpter((AppCompatActivity) getActivity(),categoryArrayList);
                    expand_listView.setAdapter(expandListAdpter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(Methods.onErrorVolley(error), getContext());            }
        });

        VolleySingleton.getInstance().addToRequestQueue(cacheRequest);

        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

/*------------------------------------------------------------------------------------------------------------------------------------------------*/
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expand_listView = (ExpandableListView) view.findViewById(R.id.expandded_list_category);
        expand_listView.setGroupIndicator(null);

        FabToList(); //float button

        expand_listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
              Button explore = (Button) v.findViewById(R.id.explore_btn);
                explore.setTextSize(13f);
                explore.setTypeface(Typeface.DEFAULT_BOLD);
              //  explore.setTypeface(MainActivity.font);
                if(expand_listView.isGroupExpanded(groupPosition))
                {
                    explore.setText(getString(R.string.arrow_left));

                   //
                }
                 else
                explore.setText(getString(R.string.arrow_bottom));

                Category cat = (Category) expandListAdpter.getGroup(groupPosition);
                Variables.IS_RATY_CATEGORY=cat.isRaty();//****new*****
                if(!cat.isAllowSubcategory())
                {
                Variables.ITEM_PATH=String.valueOf(Html.fromHtml(cat.getName_En()));
                Variables.catID = String.valueOf(cat.getCategoryID());
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
            Variables.ITEM_PATH= String.valueOf(Html.fromHtml(cat.getName_En())+"> "+String.valueOf(Html.fromHtml(subcatName)));
            Fragment fragment = new ItemsFragment();
           getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
           String url = Urls.URL_GET_SELECTED_SUBCATEGORY_ITEM + subcatID;
            GetDataRequest.setUrl(url);
           return false;
       }
   });
}





/*--------------------------------------------------------------get subcategories----------------------------------------------------------------------------------*/

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


   void FabToList(){
       MainActivity.fab.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View view) {
      expand_listView.smoothScrollToPositionFromTop(0,0);}
                                           }
       );
       expand_listView.setOnScrollListener(new AbsListView.OnScrollListener() {
           @Override
           public void onScrollStateChanged(AbsListView absListView, int i) {
           }
           @Override
           public void onScroll(AbsListView absListView, int i, int i1, int i2) {
               if(i==0)  MainActivity.fab.hide();
               else  MainActivity.fab.show();
           }
       });
   }

}