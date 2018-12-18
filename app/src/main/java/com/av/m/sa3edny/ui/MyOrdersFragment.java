package com.av.m.sa3edny.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.av.m.sa3edny.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class MyOrdersFragment extends DialogFragment {


    public MyOrdersFragment() {
        // Required empty public constructor
    }
    public static MyOrdersFragment newInstance(String title) {
        MyOrdersFragment frag = new MyOrdersFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    Button cancel_btn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v=inflater.inflate(R.layout.fragment_my_orders, container, false);
        cancel_btn=v.findViewById(R.id.cancel_btn);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrdersFragment.this.getDialog().dismiss();
            }
        });
         return v;
    }

}
