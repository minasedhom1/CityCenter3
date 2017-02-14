package com.example.lenovo.citycenter.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.Classes.Category;
import com.example.lenovo.citycenter.Classes.ExpandListAdpter;
import com.example.lenovo.citycenter.Classes.GetDataRequest;
import com.example.lenovo.citycenter.Classes.Subcategory;
import com.example.lenovo.citycenter.R;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Categories extends Fragment {

    private ExpandableListView expand_listView;

    //private ListView customListView;
    private ArrayList<Category> categoryArrayList;
    private ArrayList<Subcategory> subcategoryAraayList;
   ExpandListAdpter expandListAdpter;
    RequestQueue queue ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  fav_ids=new ArrayList<>();

    }
        JSONArray jsonArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // getFavourtieItems();
        categoryArrayList = new ArrayList<>();
        /*----------------------------------------------------------------------------------------------------------------------------------------------------*/
        GetDataRequest.setUrl(Variables.URL_GET_CATEGORIES_GOODS);
         queue = Volley.newRequestQueue(getContext());

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();  //not .toString
                    jsonArray = new JSONArray(response) ;
                    for (int i = 0; i < jsonArray.length(); i++)

                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Category myCategory=new Category();
                        myCategory.set_id(object.getInt("CategoryID"));
                        myCategory.set_name(htmlRender(object.getString("Name_En"))); // X
                        myCategory.set_details(object.getString("Description_En")); // X
                        myCategory.set_icon(object.getString("Logo")); //filter here
                        myCategory.setHas_sub(object.getBoolean("AllowSubcategory"));//filter here
                        if(myCategory.isHas_sub())
                        {myCategory.setSub_array(getSubs(object.getInt("CategoryID")));}
                        categoryArrayList.add(myCategory);
                    }

                    expandListAdpter=new ExpandListAdpter((AppCompatActivity) getActivity(),categoryArrayList);
                    expand_listView.setAdapter(expandListAdpter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetDataRequest fetchRequest = new GetDataRequest(responseListener);
        queue.add(fetchRequest);
        queue.start();
/*-----------------------------------------------------------------------------------------------------------------------------------*/

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
                Category cat = (Category) expandListAdpter.getGroup(groupPosition);

                if(!cat.isHas_sub())
                {Variables.catID = String.valueOf(cat.get_id());
                Fragment fragment = new ItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
                GetDataRequest.setUrl(Variables.URL_GET_SELECTED_CATEGORY_ITEMS+Variables.catID ); //set the clicked cat id to fetch it's items
                }
                return false;
            }
        });

          expand_listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
       @Override
       public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
           Category cat = (Category) expandListAdpter.getGroup(groupPosition);
             String subcatID=cat.getSub_array().get(childPosition).getSubcat_id();

           Fragment fragment = new ItemsFragment();
           getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
           String url = Variables.URL_GET_SELECTED_SUBCATEGORY_ITEM + subcatID;
            GetDataRequest.setUrl(url );

           return false;
       }
   });

    }




    public String htmlRender(String ss)
    {
        ss=ss.replace("span","font");
        ss=ss.replace("style=\"color: ","color=");
        ss=ss.replace(";\"","");
        ss=ss.replaceAll("<p>","");
        ss=ss.replaceAll("</p>",""); //********
        return ss;
    }



    public  ArrayList<Subcategory> getSubs(int catID) {
        final ArrayList<Subcategory> subCat_array = new ArrayList();
        Volley.newRequestQueue(getContext()).add(new StringRequest(Request.Method.GET, Variables.URL_GET_SELECTED_CATEGORY_SUBCATEGORIES + catID,
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
                                mySub.setSubCat_icon_url("https://sa3ednymalladmin.azurewebsites.net/IMG/" + object.getString("Photo1"));
                                subCat_array.add(mySub);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, null)
        );
        return subCat_array;

    }




   /* public void getFavourtieItems()

    {
        if(favouriteList.size()==0) {
            final StringRequest favrequest = new StringRequest(Request.Method.GET, Variables.URL_GET_FAVOURITES_FOR_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonElement root = new JsonParser().parse(response);
                            response = root.getAsString();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("allFav");
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
            RequestQueue queue= Volley.newRequestQueue(getContext());
            queue.add(favrequest);
        }
    }*/






















































        //onItem click
       // customListView.setDescendantFocusability(customListView.getBottom());
       /* customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           *//*     if(i != adapterView.getLastVisiblePosition()) //not footer
                {*//*
                    Category cat = (Category) myAdapter.getItem(i);
                    Variables.catID = String.valueOf(cat.get_id());
                    Fragment fragment = new ItemsFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
           *//*     }
                else {}
*//*
// Commit the transaction
            }
        });*/



        //ExpandListAdpter expandListAdpter=new ExpandListAdpter(getActivity(),categoryArrayList,subcategoryAraayList);





   /* public class MyCustomListAdapter extends ArrayAdapter<Category> {

        class ViewHolder {
            Button explore;
            TextView shopName, shopDetails;
            ImageView categoryIcon;
            ListView listView;
        }
        public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<Category> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            try {



                if(convertView==null)
                   {LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.category_group, parent, false);




                holder.shopName= (TextView) convertView.findViewById(R.id.shopNameTextView); /*//*******kont nasi el view.
                holder.shopDetails = (TextView) convertView.findViewById(R.id.shopDetailsTextview);
                holder.categoryIcon = (ImageView) convertView.findViewById(R.id.shopPic);
                holder.explore = (Button) convertView.findViewById(R.id.explore_btn);
                       convertView.setTag(holder);
            }
                else {holder = (ViewHolder) convertView.getTag();}
                final Category myCat=categoryArrayList.get(position); //final for each element
                holder.shopName.setText(Html.fromHtml(myCat.get_name()), TextView.BufferType.SPANNABLE);
                holder.shopDetails.setText(Html.fromHtml(myCat.get_details()));
                String logoURL="https://sa3ednymalladmin.azurewebsites.net/IMG/"+ myCat.get_icon();
                Picasso.with(getContext()).load(logoURL).transform(new RoundedCornersTransformation(20,0)).fit().into(holder.categoryIcon);
                holder.shopName.setTextSize(16f);
                holder.explore.setTextSize(13f);
*//*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*//*

                // explore.setTextSize(13f);
                holder.explore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toast(myCat.get_name());

                    }
                });

                return convertView;

            } catch (Exception e) {
                return null;
            }
        }
    }*/


}