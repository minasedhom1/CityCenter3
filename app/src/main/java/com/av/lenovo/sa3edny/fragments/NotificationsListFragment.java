package com.av.lenovo.sa3edny.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.Assets.Variables;
import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.Notification;
import com.av.lenovo.sa3edny.adapters.NotificationCustomAdapter;
import com.av.lenovo.sa3edny.classes.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsListFragment extends Fragment {
    JSONArray jsonArray;
 //List<Notification> notificationList;
    ListView notiListView;
    ProgressBar progressBar;
    NotificationCustomAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notifications_list, container, false);
        notiListView= (ListView) v.findViewById(R.id.notification_list);
        progressBar= (ProgressBar) v.findViewById(R.id.progress_bar);

        Methods.setPath(v,getContext());

        final StringRequest request=new StringRequest(Request.Method.GET,Urls.URL_GET_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JsonElement root=new JsonParser().parse(response);
                            response = root.getAsString();
                            jsonArray=new JSONArray(response);
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject object = jsonArray.getJSONObject(i);
                                Gson gson = new Gson();
                                Notification notification=gson.fromJson(object.toString(),Notification.class);
                                Variables.notificationList.add(notification);
                            }
                            adapter =new NotificationCustomAdapter(getContext(),android.R.layout.simple_list_item_1,Variables.notificationList);
                            notiListView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(Methods.onErrorVolley(error), getContext());

            }});
        if(Variables.notificationList.size()==0)
            VolleySingleton.getInstance().addToRequestQueue(request);
        else { adapter =new NotificationCustomAdapter(getContext(),android.R.layout.simple_list_item_1,Variables.notificationList);
            notiListView.setAdapter(adapter);
            progressBar.setVisibility(View.GONE);}




        notiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Notification notification= (Notification) parent.getAdapter().getItem(position);
                if(notification.getItemID()!=null)
                {  Variables.SINGLE_ITEM_ID = String.valueOf(notification.getItemID());

                Fragment fragment = new SingleItemFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).commit();}
            }
        });


        MainActivity.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notiListView.smoothScrollToPosition(0);
            }}
        );

        notiListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
        return v;
    }


}
