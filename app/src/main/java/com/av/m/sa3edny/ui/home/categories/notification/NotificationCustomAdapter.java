package com.av.m.sa3edny.ui.home.categories.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.m.sa3edny.utils.Urls;
import com.av.m.sa3edny.R;
import com.bumptech.glide.Glide;
import java.util.List;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

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
        {  TextView description;
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
                if(notification.getItem_Photo()!=null) {
                    Glide.with(getContext()).load(Urls.URL_IMG_PATH+notification.getItem_Photo()).apply(bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(20,0)))
                            .into(holder.icon);
                }
                else {
                    Glide.with(getContext()).load(R.mipmap.notificationicon).apply(bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(20,0)))
                        .into(holder.icon);}
                    return convertView;
            }

            catch (Exception e) {
                return null;
            }
        }
    }

