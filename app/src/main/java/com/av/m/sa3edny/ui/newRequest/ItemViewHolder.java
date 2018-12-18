package com.av.m.sa3edny.ui.newRequest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.av.m.sa3edny.R;


public class ItemViewHolder extends RecyclerView.ViewHolder {


    TextView  itemName;
    EditText numOfitems;
    CheckBox checkBox;
    ItemViewHolder(View itemView) {
        super(itemView);
        itemName =  itemView.findViewById(R.id.item_name_tv);
      //  numOfitems=itemView.findViewById(R.id.num_of_items_et);
       // checkBox=itemView.findViewById(R.id.checkbox);
    }
}
