package com.av.m.sa3edny.ui.newRequest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.m.sa3edny.R;
import com.av.m.sa3edny.ui.items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mina on 8/27/2018.
 */

public class MyAdapterTest extends RecyclerView.Adapter<MyAdapterTest.MyViewHolder> {

    List<Item>  loyalItems;
    Map<String,Integer> requestedItemsMap=new HashMap<>();

    static Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  itemName;
        //EditText numOfitems;
        CheckBox checkBox;
        ImageView itemPhoto;
        MyViewHolder(View itemView) {
            super(itemView);

            itemName=itemView.findViewById(R.id.item_name_tv);
            itemPhoto=itemView.findViewById(R.id.item_photo_iv);

           // numOfitems=itemView.findViewById(R.id.num_of_items_et);
          checkBox=itemView.findViewById(R.id.checkbox);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapterTest(Context context) {
        this.loyalItems=loyalItems;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapterTest.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_of_shop_test, parent, false);


        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

    /*    final Item item=loyalItems.get(position);


        holder.itemName.setText(item.getName());
        Glide.with(context).load(Urls.URL_IMG_PATH +item.getPhoto1())
              //  .apply(bitmapTransform(new jp.wasabeef.glide.transformations.RoundedCornersTransformation(25,0)))
                .into(holder.itemPhoto);*//*        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String num=holder.numOfitems.getText().toString();
                if(b && num.equals("")){
                    compoundButton.setChecked(false);
                    Toast.makeText(context,"Please Enter the no. of items first.",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(b){
                        requestedItemsMap.put(item.getName(), Integer.valueOf(num));
                        holder.numOfitems.setEnabled(false);
                        holder.numOfitems.setInputType(InputType.TYPE_NULL);
                    }
                    else
                        requestedItemsMap.remove(item.getName());
                    holder.numOfitems.setEnabled(true);
                    holder.numOfitems.setInputType(InputType.TYPE_CLASS_NUMBER);
                    //itemViewHolder.numOfitems.setText("");
                }

            }
        });
        holder.numOfitems.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(holder.checkBox.isChecked() && !charSequence.toString().equals("")) {
                    requestedItemsMap.put(item.getName(), Integer.valueOf(charSequence.toString()));
                }
                requestedItemsMap.size();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 6;
    }

    public Map<String,Integer> getSelectedItems(){
        return requestedItemsMap;
    }

}
