package com.av.lenovo.sa3edny.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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

Button loyal_add_points_btn,loyal_claim_reward_btn1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_voucher, container, false);
        ImageView imageView= (ImageView) v.findViewById(R.id.prof_voucher_page);
        TextView textView= (TextView) v.findViewById(R.id.FB_name_voucher_page);
        textView.setText(MainActivity.profile.getName());
        Picasso.with(getContext()).load(MainActivity.profile.getProfilePictureUri(300, 300)).transform(new CropCircleTransformation()).into(imageView);
        loyal_add_points_btn= (Button) v.findViewById(R.id.loyal_add_points_btn);
        loyal_add_points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp();
            }
        });
        loyal_claim_reward_btn1= (Button) v.findViewById(R.id.loyal_claim_reward_btn1);
        loyal_add_points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp();
            }
        });
        return v;

    }
    private void popUp() {
        final Dialog nagDialog = new Dialog(getContext());
        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        nagDialog.setContentView(R.layout.popup_loyalty);
        Button loyal_verifiy_btn= (Button) nagDialog.findViewById(R.id.loyal_verifiy_btn);
        loyal_verifiy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage("Passcode is Wrong!")
                        .setIcon(R.mipmap.staron)
                        .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                alertDialog.show();
            }
        });
        Button loyal_cancel_btn= (Button) nagDialog.findViewById(R.id.loyal_cancel_btn);
        loyal_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nagDialog.dismiss();
            }
        });
        nagDialog.setCancelable(false);
        nagDialog.show();
    }
}
