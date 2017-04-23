package com.av.lenovo.sa3edny.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoucherFragment extends Fragment {


    public VoucherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_voucher, container, false);
        ImageView imageView= (ImageView) v.findViewById(R.id.prof_voucher_page);
        TextView textView= (TextView) v.findViewById(R.id.FB_name_voucher_page);
        textView.setText(MainActivity.profile.getName());
        Picasso.with(getContext()).load(MainActivity.profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(imageView);
        return v;

    }

}
