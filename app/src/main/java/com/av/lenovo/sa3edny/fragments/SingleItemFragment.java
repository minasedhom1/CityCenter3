package com.av.lenovo.sa3edny.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.Item;
import com.av.lenovo.sa3edny.adapters.MyItemAdapter;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
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
                    "http://sodicservice.azurewebsites.net/Sodic/Item?itemID=" + Variables.SINGLE_ITEM_ID,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JsonElement root = new JsonParser().parse(response);
                                response = root.getAsString();
                                JSONObject object = new JSONObject(response);

                                Item item = new Item();
                                item.setId(object.getString("ItemID"));
                                item.setName(Methods.htmlRender(object.getString("Name_En")));
                                item.setDescription(object.getString("Description_En"));
                                item.setPhone1(object.getString("Phone1"));
                                item.setPhone2(object.getString("Phone2"));
                                item.setPhone3(object.getString("Phone3"));
                                item.setPhone4(object.getString("Phone4"));
                                item.setPhone5(object.getString("Phone5"));
                                item.setMenu_url(object.getString("PDF_URL"));
                                item.setPromo(object.getBoolean("IsPromo"));
                                item.setPromoText(object.getString("PromoText_En"));
                                item.setPromoButton(object.getString("PromoButtonText"));
                                item.setPromo_pdf(object.getString("PDFPromo"));
                                item.setRaty(object.getBoolean("IsRatyCategory"));
                                item.setUrl_btn_text(object.getString("URLButtonText"));
                                if (object.getString("Rate") != "null") {
                                    item.setRate(Float.valueOf(object.getString("Rate"))); //get rate and round it implicitly
                                    Log.d("rate", Float.valueOf(object.getString("Rate")).toString());
                                }
                                item.setPhoto1(Urls.URL_IMG_PATH + object.getString("Photo1"));
                                item.setCategoryName(object.getString("CategoryName_En"));
                                item.setSubcategoryName(object.getString("SubcategoryName_En"));
                                item.setCategoryID(Variables.catID);

                                if (Variables.fav_ids.size() != 0 && Variables.fav_ids.contains(item.getId())) {
                                    item.setLike(true);
                                }
                                singleitemArrayList.add(item);
                                itemAdapter = new MyItemAdapter(getContext(), android.R.layout.simple_list_item_1, singleitemArrayList);
                                listView1.setAdapter(itemAdapter);
                                progressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
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
        listView1 = (ListView) v.findViewById(R.id.list_single_item);
        progressBar= (ProgressBar) v.findViewById(R.id.progress_bar);
     //  0 Variables.ITEM_PATH=item.getName();
        Methods.setPath(v,getContext());
        return v;
    }
}
