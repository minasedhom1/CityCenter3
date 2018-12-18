package com.av.m.sa3edny.ui.home.categories.notification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.av.m.sa3edny.utils.Methods;
import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.utils.Variables;
import com.av.m.sa3edny.ui.MainActivity;
import com.av.m.sa3edny.R;
import com.av.m.sa3edny.classes.VolleySingleton;
import com.av.m.sa3edny.ui.home.categories.search.SingleItemFragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsListFragment extends Fragment {
    JSONArray jsonArray;
    List<Notification> notificationList=new ArrayList<>();
    ListView notiListView;
    ProgressBar progressBar;
    NotificationCustomAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       // notificationList=new ArrayList<>();
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
                                 notificationList.add(notification);
                            }
                            if(getContext()!=null) {
                                adapter = new NotificationCustomAdapter(getContext(), android.R.layout.simple_list_item_1, notificationList);
                                notiListView.setAdapter(adapter);
                                progressBar.setVisibility(View.GONE);
                            }

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
            //VolleySingleton.getInstance().getRequestQueue().getCache().clear();
        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance().addToRequestQueue(request);





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

    @Override
    public void onResume() {

        super.onResume();
    }
}
