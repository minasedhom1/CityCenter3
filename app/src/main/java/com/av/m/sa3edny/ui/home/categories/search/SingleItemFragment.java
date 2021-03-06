package com.av.m.sa3edny.ui.home.categories.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.utils.Variables;
import com.av.m.sa3edny.R;
import com.av.m.sa3edny.ui.items.Item;
import com.av.m.sa3edny.ui.items.MyItemAdapter;
import com.av.m.sa3edny.classes.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SingleItemFragment extends Fragment {
    ArrayList<Item> singleitemArrayList;
    ListView listView1;
    MyItemAdapter itemAdapter;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            singleitemArrayList = new ArrayList<Item>();
            StringRequest request1 = new StringRequest(Request.Method.POST,
                    Urls.SODIC_BASE+"Item/GetSelectedItem/GetSelectedItem?itemID=" + Variables.SINGLE_ITEM_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           /* try {
                                JsonElement root = new JsonParser().parse(response);
                                response = root.getAsString();
                                JSONObject object = new JSONObject(response);
                                Item item = new Item();
                                item.setId(object.getString("ItemID"));
                                item.setName(Methods.htmlRender(object.getString("Name_En")));
                                item.setDescription_En(object.getString("Description_En"));
                                item.setPhone1(object.getString("Phone1"));
                                item.setPhone2(object.getString("Phone2"));
                                item.setPhone3(object.getString("Phone3"));
                                item.setPhone4(object.getString("Phone4"));
                                item.setPhone5(object.getString("Phone5"));
                                item.setPDF_URL(object.getString("PDF_URL"));
                                item.setPromo(object.getBoolean("IsPromo"));
                                item.setPromoText_En(object.getString("PromoText_En"));
                                item.setPromoButtonText(object.getString("PromoButtonText"));
                                item.setPDFPromo(object.getString("PDFPromo"));
                                item.setRatyCategory(object.getBoolean("IsRatyCategory"));
                                item.setURLButtonText(object.getString("URLButtonText"));
                                if (object.getString("Rate") != "null") {
                                    item.setRate(Float.valueOf(object.getString("Rate"))); //get rate and round it implicitly
                           }
                                String [] media={object.getString("Photo1"),
                                        object.getString("Photo2"),
                                        object.getString("Photo3"),
                                        object.getString("Photo4"),
                                        object.getString("Photo5")};
                                ArrayList<String>media_list= (ArrayList<String>) item.getItem_media();
                                for(String s : media)
                                {
                                    if(!s.equals("null"))
                                        media_list.add(Urls.URL_IMG_PATH + s);
                                }
                              //  item.setPhoto1(Urls.URL_IMG_PATH + object.getString("Photo1"));
                                item.setCategoryName_En(object.getString("CategoryName_En"));
                                item.setSubcategoryName_En(object.getString("SubcategoryName_En"));
                                item.setCategoryID(Variables.catID);

                                if (Variables.fav_ids.size() != 0 && Variables.fav_ids.contains(item.getId())) {
                                    item.setLike(true);
                                }
                                item.setHaveLoyalty(object.getBoolean("HaveLoyalty"));
                                singleitemArrayList.add(item);
                                if(getContext()!=null)
                                {
                                itemAdapter = new MyItemAdapter(getContext(), android.R.layout.simple_list_item_1, singleitemArrayList);
                                listView1.setAdapter(itemAdapter);
                                progressBar.setVisibility(View.GONE);}
                            }*/
                            try {
                                JsonElement root = new JsonParser().parse(response);
                                response = root.getAsString();
                                JSONObject jsonObject = new JSONObject(response);
                                Gson gson=new Gson();
                                Item item=gson.fromJson(jsonObject.toString(),Item.class);
                                singleitemArrayList.add(item);
                                itemAdapter = new MyItemAdapter(getContext(), android.R.layout.simple_list_item_1, singleitemArrayList);
                                listView1.setAdapter(itemAdapter);
                                progressBar.setVisibility(View.GONE);}
                            catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Methods.toast(error.toString(), getContext());
                }
            });

            VolleySingleton.getInstance().addToRequestQueue(request1);
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View v=inflater.inflate(R.layout.fragment_single_item, container, false);
        listView1 = v.findViewById(R.id.list_single_item);
        progressBar= v.findViewById(R.id.progress_bar);
     //  0 Variables.ITEM_PATH=item.getName();
        //Methods.setPath(v,getContext());
        return v;
    }
}
