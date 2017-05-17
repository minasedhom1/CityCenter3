package com.av.lenovo.sa3edny.adapters;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.av.lenovo.sa3edny.Assets.Methods;
import com.av.lenovo.sa3edny.MainActivity;
import com.av.lenovo.sa3edny.R;
import com.av.lenovo.sa3edny.classes.Category;
import com.av.lenovo.sa3edny.classes.Subcategory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by mido on 2/6/2017.
 */

public class ExpandListAdpter extends BaseExpandableListAdapter {

    AppCompatActivity activity;

    class ViewHolder {
        Button explore;
        TextView catName, catDetails;
        ImageView categoryIcon;
    }

    ArrayList<Category> catArray;
   // ArrayList<Subcategory> subcatArray;

    public ExpandListAdpter(AppCompatActivity activity,ArrayList<Category> catArray) {
        this.activity=activity;
        this.catArray=catArray;
    }

    @Override
    public int getGroupCount() {
        return catArray.size();
    }
    @Override
    public Object getGroup(int groupPosition) {

        return catArray.get(groupPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return catArray.get(groupPosition).getSub_array().size(); //***;)
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true; //**
    }
    ViewHolder holder;
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        holder = new ViewHolder();
        try {


            if(convertView==null)
            {LayoutInflater inflater = (LayoutInflater)  activity.getSystemService(LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.category_group, parent, false);

                holder.catName = (TextView) convertView.findViewById(R.id.shopNameTextView); //*******kont nasi el view.
                holder.catDetails = (TextView) convertView.findViewById(R.id.category_details_tv);
                holder.categoryIcon = (ImageView) convertView.findViewById(R.id.shopPic);
                holder.explore = (Button) convertView.findViewById(R.id.explore_btn);
                convertView.setTag(holder);
            }
            else {holder = (ViewHolder) convertView.getTag();
            }

            final Category myCat=catArray.get(groupPosition); //final for each element
            holder.catName.setText(Html.fromHtml(Methods.htmlRender(myCat.getName_En())));
           /* Typeface typeface=Typeface.createFromAsset(activity.getAssets(),"fonts/futura-medium.ttf");
            holder.catDetails.setTypeface(typeface);*/
            if(myCat.getDescription_En().toString().matches("null"))
            {holder.catDetails.setVisibility(View.GONE);}
            else
            {
                holder.catDetails.setVisibility(View.VISIBLE);
                holder.catDetails.setText(myCat.getDescription_En());}
            Picasso.with(activity).load(myCat.getLogo()).transform(new RoundedCornersTransformation(25,0)).fit().into(holder.categoryIcon);
            holder.catName.setTextSize(15f);

            holder.explore.setTypeface(MainActivity.font,Typeface.BOLD);
           //  holder.explore.setTextSize(13f);
            //holder.explore.setTypeface(Typeface.DEFAULT_BOLD);
            return convertView;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        try {

            Category myCategory=catArray.get(groupPosition);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subcategory_child, parent, false);
            ArrayList<Subcategory> subcategories =myCategory.getSub_array();
            String sub_name= subcategories.get(childPosition).getSubCat_name();
            TextView subCat= (TextView) convertView.findViewById(R.id.lblListItem);
            ImageView child_icon= (ImageView) convertView.findViewById(R.id.child_icon);
            subCat.setText(Html.fromHtml(sub_name) );
            Picasso.with(activity).load(subcategories.get(childPosition).getSubCat_icon_url()).
            transform(new RoundedCornersTransformation(20,0)).fit().into(child_icon);

/*++++++++++++++++++++++++++++++++++++++++----------------------------------------------------------+++++++++++++++++++++++++++++++++++++++*/

            return convertView;

        } catch (Exception e) {
            return null;
        }
    }


}
