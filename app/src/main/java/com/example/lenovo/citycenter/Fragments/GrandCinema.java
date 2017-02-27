package com.example.lenovo.citycenter.Fragments;


import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
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
import com.example.lenovo.citycenter.MainActivity;
import com.example.lenovo.citycenter.classes.ExpandListAdpter;
import com.example.lenovo.citycenter.classes.GetDataRequest;
import com.example.lenovo.citycenter.classes.Category;
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.Subcategory;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class GrandCinema extends Fragment {
    private ExpandableListView customListView;
    private ArrayList<Category> categoryArrayList;
    private ExpandListAdpter myAdapter;

    FloatingActionButton fab;

    public GrandCinema() {
        // Required empty public constructor
    }

    JSONArray jsonArray;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_grandcinema, container, false);

       customListView = (ExpandableListView)view.findViewById(R.id.expandded_list_category);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, customListView, false);
        customListView.addFooterView(footerView,null,false);
        categoryArrayList = new ArrayList<>();
        /*----------------------------------------------------------------------------------------------------------------------------------------------------*/
        GetDataRequest.setUrl(Urls.URL_GET_CATEGORIES_SERVICES);
        RequestQueue queue = Volley.newRequestQueue(getContext());

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
                        myCategory.set_details(htmlRender(object.getString("Description_En"))); // X
                        myCategory.set_icon(Urls.URL_IMG_PATH + object.getString("Logo"));
                        myCategory.setHas_sub(object.getBoolean("AllowSubcategory"));//filter here
                        if(myCategory.isHas_sub())
                        {myCategory.setSub_array(getSubs(object.getInt("CategoryID")));}//filter here
                        categoryArrayList.add(myCategory);
                    }

                   myAdapter=new ExpandListAdpter((AppCompatActivity) getActivity(),categoryArrayList);
                   customListView.setAdapter(myAdapter);
               //     myAdapter.setNotifyOnChange(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        GetDataRequest fetchRequest = new GetDataRequest(responseListener);
        queue.add(fetchRequest);
        return view;
    }

/*-----------------------------------------------------------------------------------------------------------------------------------*/



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        customListView.setGroupIndicator(null);

         fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // customListView.setSmoothScrollbarEnabled(true);
                customListView.smoothScrollToPositionFromTop(0,0);
            }}
        );
        customListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

 /*customListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
     @Override
     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         Category cat= (Category) myAdapter.getItem(i);
         Variables.catID = String.valueOf(cat.get_id());
         GetDataRequest.setUrl(Urls.URL_GET_SELECTED_CATEGORY_ITEMS+ Variables.catID );
         Fragment  fragment = new ItemsFragment();
         getFragmentManager().beginTransaction().replace(R.id.frag_holder,fragment ).commit();
     }
 });*/
        customListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Button explore = (Button) v.findViewById(R.id.explore_btn);
                explore.setTypeface(MainActivity.font);
                if(customListView.isGroupExpanded(groupPosition))
                {
                    explore.setText(getString(R.string.arrow_left));

                }
                else
                    explore.setText(getString(R.string.arrow_bottom));

                Category cat = (Category) myAdapter.getGroup(groupPosition);

                if(!cat.isHas_sub())
                {
                    Variables.catID = String.valueOf(cat.get_id());
                    Fragment fragment = new ItemsFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
                    GetDataRequest.setUrl(Urls.URL_GET_SELECTED_CATEGORY_ITEMS+ Variables.catID ); //set the clicked cat id to fetch it's items
                }
                return false;
            }
        });

        customListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Category cat = (Category) myAdapter.getGroup(groupPosition);
                String subcatID=cat.getSub_array().get(childPosition).getSubcat_id();
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
        RequestQueue queue=Volley.newRequestQueue(getContext());

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
        queue.add(subcatRequest);
        return subCat_array;

    }

  /*   public class MyCustomListAdapter extends ArrayAdapter<Category> {

        class ViewHolder {
            Button explore;
            TextView shopName, shopDetails;
            ImageView categoryIcon;
        }

       public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<Category> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

           ViewHolder holder = new ViewHolder();
            try {



                if(convertView==null)
                {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.category_group, parent, false);

                    holder.shopName= (TextView) convertView.findViewById(R.id.shopNameTextView); /*//*******kont nasi el view.
                    holder.shopDetails = (TextView) convertView.findViewById(R.id.shopDetailsTextview);
                    holder. categoryIcon = (ImageView) convertView.findViewById(R.id.shopPic);
                    holder.explore = (Button) convertView.findViewById(R.id.explore_btn);
                    convertView.setTag(holder);

                }
                else {holder = (ViewHolder) convertView.getTag();}
                final Category myCat=categoryArrayList.get(position); //final for each element
                holder.shopName.setText(Html.fromHtml(myCat.get_name()), TextView.BufferType.SPANNABLE);
                holder.shopDetails.setText(Html.fromHtml(myCat.get_details()));
               // String logoURL="https://sa3ednymalladmin.azurewebsites.net/IMG/"+ myCat.get_icon();

                Picasso.with(getContext()).load(myCat.get_icon()).transform(new RoundedCornersTransformation(20,0)).fit().into(holder.categoryIcon);
*//*                holder.categoryIcon.setMaxHeight(200);
                holder.categoryIcon.setScaleType(ImageView.ScaleType.FIT_XY);*//*
                holder.shopName.setTextSize(16f);
                holder.explore.setTextSize(13f);
                holder.explore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                return convertView;

            } catch (Exception e) {
                return null;
            }


          *//*  Picasso.with(getContext()).load(imageUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                        Bitmap imageRounded=   Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
                        Canvas canvas = new Canvas(imageRounded);
                        Paint mpaint = new Paint();
                        mpaint.setAntiAlias(true);
                        mpaint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

                        canvas.drawRoundRect((new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight())), 20, 20, mpaint);
                        categoryIcon.setImageBitmap(imageRounded);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                        categoryIcon.setImageBitmap(bm);
                    }
                });*//*
            //new DownLoadImageTask(image).execute(imageUrl);


            //  categoryIcon.setImageResource(myCategory.get_icon());
            *//*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*//*

*//*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*//*
       *//*         Bitmap mbitmap = ((BitmapDrawable) getResources().getDrawable(myCategory.get_icon())).getBitmap();
                Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                Canvas canvas = new Canvas(imageRounded);
                Paint mpaint = new Paint();
                mpaint.setAntiAlias(true);
                mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 20, 20, mpaint);// Round Image Corner 100 100 100 100
                categoryIcon.setImageBitmap(imageRounded);
                categoryIcon.setPadding(10,10,10,10);*//*
*//*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*//*


*//*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*//*
        }


    }*/
    public String htmlRender(String ss)
    {
        ss=ss.replace("span","font");
        ss=ss.replace("style=\"color: ","color=");
        ss=ss.replace(";\"","");
        ss=ss.replaceAll("<p>","");
        ss=ss.replaceAll("</p>",""); //********
        return ss;
    }

    public void toast(String s)
    {
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }

}
