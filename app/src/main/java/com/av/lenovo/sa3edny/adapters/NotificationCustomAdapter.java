package com.av.lenovo.sa3edny.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.lenovo.sa3edny.Assets.Urls;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.Notification;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by mido on 4/2/2017.
 */

public class NotificationCustomAdapter extends ArrayAdapter<Notification> {

        List <Notification> notificationList2;
         Context context;
        public NotificationCustomAdapter(Context context, int resource, List<Notification> objects) {
            super(context, resource, objects);
            this.notificationList2=objects;
            this.context=context;
        }
        class ViewHolder
        {
            TextView description;
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
                { Picasso.with(getContext()).load(Urls.URL_IMG_PATH+notification.getItem_Photo()).error(R.mipmap.ic_launcher).transform(new RoundedCornersTransformation(20,0)).fit().into(holder.icon);}
                else {Picasso.with(getContext()).load(R.mipmap.notificationicon).transform(new RoundedCornersTransformation(20,0)).fit().into(holder.icon);}
                return convertView;

            } catch (Exception e) {
                return null;
            }
        }
    }

