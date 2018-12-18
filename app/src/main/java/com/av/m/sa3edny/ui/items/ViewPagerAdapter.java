package com.av.m.sa3edny.ui.items;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.av.m.sa3edny.R;
import com.bumptech.glide.Glide;
import java.util.List;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> urls;
    private LayoutInflater layoutInflater;


     ViewPagerAdapter(Context context, List<String> urls) {
        this.urls=urls;
        this.context = context;
    }

    @Override
    public int getCount() {
        return urls.size();
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view;
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      //  if(position<urls.size()-1) {
            view = layoutInflater.inflate(R.layout.view_pager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            Glide.with(context).load(urls.get(position)).into(imageView);
      //  }
/*         else {
            view=layoutInflater.inflate(R.layout.popup_video,null);
            final VideoView videoView = (VideoView) view.findViewById(R.id.videoView);
            Uri vidUri = Uri.parse(urls.get(position));
            videoView.setVideoURI(vidUri);
            final MediaController vidControl = new MediaController(context);
            vidControl.setAnchorView(videoView);
            videoView.setMediaController(vidControl);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // This is just to show image when loaded
                    mp.start();
                    mp.pause();

                }
            });
        }*/
            container.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager= (ViewPager) container;
        View view= (View) object;
        viewPager.removeView(view);

    }
}
