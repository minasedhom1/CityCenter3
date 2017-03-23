package com.example.lenovo.citycenter.Fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.Item;
import com.example.lenovo.citycenter.classes.MyItemAdapter;
import com.example.lenovo.citycenter.classes.SingleItemFragment;
import com.example.lenovo.citycenter.classes.VolleySingleton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
        // Required empty public constructor
    }

   ListView listView;
    JSONArray jsonArray;
    JSONObject jsonObject;
    ArrayList<Item> itemArrayList ;
    ArrayList<Item> singleitemArrayList ;

    MyItemSearchAdapter myItemSearchAdapter;
    EditText editText;
    ListView listView1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_search, container, false);
         listView= (ListView) v.findViewById(R.id.items_name_list);
        editText= (EditText) v.findViewById(R.id.search_et);
        editText.setTypeface(MainActivity.font);
        itemArrayList=new ArrayList<>();
        String url="http://sodicservice.azurewebsites.net/Sodic/Item/GetAllSearchItems/search";
        final StringRequest request=new StringRequest(Request.Method.GET,url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JsonElement root=new JsonParser().parse(response);
                    response = root.getAsString();
                    jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        Item item=new Item();
                        item.setId(jsonObject.getString("ItemID"));
                        item.setName(Html.fromHtml(jsonObject.getString("Name_En")).toString());
                        item.setDescription(Html.fromHtml(jsonObject.getString("Description_En")).toString());
                        itemArrayList.add(item);
                    }
                    myItemSearchAdapter=new MyItemSearchAdapter(getContext(),android.R.layout.simple_list_item_1,itemArrayList);
                    listView.setAdapter(myItemSearchAdapter);

                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                  }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item=  myItemSearchAdapter.getItem(i);
                Variables.SINGLE_ITEM_ID = String.valueOf(item.getId());
                Fragment fragment = new SingleItemFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();
               // Methods.toast(item.getDescription(),getContext());
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                myItemSearchAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
}

    class MyItemSearchAdapter extends ArrayAdapter<Item> implements Filterable {


        Context context;
        ArrayList<Item> itemsList;
        ArrayList<Item> filterdList;

        public MyItemSearchAdapter(Context context, int resource, ArrayList<Item> itemsList) {
            super(context, resource,itemsList);
            this.context= context;
            this.itemsList=itemsList;
            this.filterdList=itemsList;
        }

        class ViewHolder
        {  TextView name;
        Button call;}
        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            try {

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.listview_association, parent, false);
                    holder.name = (TextView) convertView.findViewById(R.id.item_name_tv);
                    holder.call= (Button) convertView.findViewById(R.id.search_call_btn);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                final Item myItem = filterdList.get(position);
                holder.call.setTypeface(MainActivity.font);
                holder.name.setText(Html.fromHtml(myItem.getName()));

                return  convertView;
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        public int getCount() {return filterdList.size();}
        @Nullable
        @Override
        public Item getItem(int position) {return filterdList.get(position);}

        @NonNull
        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    // FilterResults filterResults = new FilterResults();
                    FilterResults results = new FilterResults();

                    //If there's nothing to filter on, return the original data for your list
                    if(charSequence == null || charSequence.length() == 0)
                    {
                        results.values = itemsList;
                        results.count = itemsList.size();
                    }
                    else
                    {
                        ArrayList<Item> filterResultsData = new ArrayList<Item>();
                        for(Item data : itemsList)
                        {
                            //In this loop, you'll filter through originalData and compare each item to charSequence.
                            //If you find a match, add it to your new ArrayList
                            //I'm not sure how you're going to do comparison, so you'll need to fill out this conditional
                            if(data.getName().toLowerCase().contains(charSequence)||data.getDescription().toLowerCase().contains(charSequence))
                            {
                                filterResultsData.add(data);
                            }
                        }
                        results.values = filterResultsData;
                        results.count = filterResultsData.size();
                    }
                    return results;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence contraint, FilterResults results) {
                    filterdList = (ArrayList<Item>) results.values;
                    if (results.count > 0) {
                        notifyDataSetChanged();
                    } else
                    {
                        notifyDataSetInvalidated();
                    }
                }
            };
        }
    }
}
