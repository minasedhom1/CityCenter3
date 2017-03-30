package com.example.lenovo.citycenter.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.lenovo.citycenter.Assets.Methods;
import com.example.lenovo.citycenter.Assets.Urls;
import com.example.lenovo.citycenter.Assets.Variables;
import com.example.lenovo.citycenter.R;
import com.example.lenovo.citycenter.classes.MyItemAdapter;
import com.example.lenovo.citycenter.classes.Notification;
import com.example.lenovo.citycenter.classes.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsListFragment extends Fragment {
    JSONArray jsonArray;
 List<Notification> notificationList;
    ListView notiListView;
    ProgressBar progressBar;
    public NotificationsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationList=new ArrayList<>();
        StringRequest request=new StringRequest(Request.Method.GET,"http://sa3ednyservice.azurewebsites.net/Sodic/Device/GetNotifications/notification",
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
                            CustomAdapter adapter=new CustomAdapter(getContext(),android.R.layout.simple_list_item_1,notificationList);
                            notiListView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Methods.toast(error.toString(),getContext());

            }});
        VolleySingleton.getInstance().addToRequestQueue(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notifications_list, container, false);
        notiListView= (ListView) v.findViewById(R.id.notification_list);
        progressBar= (ProgressBar) v.findViewById(R.id.progress_bar);
        notiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              Notification notification= (Notification) parent.getAdapter().getItem(position);
                if(notification.getItemID()!=null)
                {  Variables.SINGLE_ITEM_ID = String.valueOf(notification.getItemID());
                Fragment fragment = new SingleItemFragment();
                getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragment).addToBackStack("tag").commit();}
            }
        });
        return v;
    }

    public class CustomAdapter extends ArrayAdapter<Notification>
    {
      List<Notification>notificationList2;
        public CustomAdapter(Context context, int resource, List<Notification> objects) {
            super(context, resource, objects);
            this.notificationList2=objects;
        }
        class ViewHolder
        {
            TextView  description;
            ImageView icon;
        }
        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            try {

                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.custom_notification, parent, false);
                    holder.description = (TextView) convertView.findViewById(R.id.notif_desc_tv);
                    holder.icon= (ImageView) convertView.findViewById(R.id.noti_icon);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
               final Notification notification=notificationList2.get(position);
                holder.description.setText(notification.getDescription());
               if(notification.getItem_Photo()!=null)
               { Picasso.with(getContext()).load(Urls.URL_IMG_PATH+notification.getItem_Photo()).error(R.mipmap.ic_launcher).into(holder.icon);}
               else {Picasso.with(getContext()).load(R.mipmap.notificationicon).into(holder.icon);}
                return convertView;

            } catch (Exception e) {
                return null;
            }
        }
    }
}
