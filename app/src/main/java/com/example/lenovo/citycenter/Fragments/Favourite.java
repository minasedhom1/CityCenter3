package com.example.lenovo.citycenter.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.citycenter.Classes.Item;
import com.example.lenovo.citycenter.R;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite extends Fragment {

    private String s;

    Button website, call;
    ImageView image;
    TextView name, description;
    ListView ItemList;
     ArrayList<Item> itemArrayList=new ArrayList<>();
    private ArrayAdapter itemAdapter;
    Item myItem;

    public Favourite() {
        // Required empty public constructor
    }


  /*  public static Favourite newInstance(Item item) {
        Favourite fragment = new Favourite();
        Bundle args = new Bundle();
       //  myItem=item;
        args.putSerializable("key",item);
        fragment.setArguments(args);
        return fragment;

    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     /*   Bundle bundle= getTargetFragment().getArguments();
        myItem= (Item) bundle.getSerializable("key");
*/
      //  this.itemArrayList = ItemsFragment.itemArrayList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

     //   savedInstanceState=this.getArguments();

      //  myItem= (Item) savedInstanceState.getSerializable("key");


        return inflater.inflate(R.layout.fragment_favourite, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // toast(myItem.name);
        this.itemArrayList=ItemsFragment.favouriteList;

        if(itemArrayList.size()!=0)
        { ItemList = (ListView) getActivity().findViewById(R.id.clickedItem_customList);
        View footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, ItemList, false);
        ItemList.addFooterView(footerView);
        itemAdapter = new MyCustomListAdapter(getContext(), android.R.layout.simple_list_item_1, R.id.shopNameTextView,itemArrayList);
        ItemList.setAdapter(itemAdapter);
        itemAdapter.notifyDataSetChanged();}







        //**********
        //this.myItem = ItemsFragment.myItem;
        //    int y=ItemsFragment.itemArrayList.size();
        // ArrayList<Item> itemArrayList2=ItemsFragment.itemArrayList;


        //  itemArrayList.add(new Item("Carrefour","a7la klam","https://sa3ednymalladmin.azurewebsites.net/IMG/636177604271948352carrefour-logo-9D3FDB68F7-seeklogo.com.gif","01275791088","www.google.com",true));
       /*if(ItemsFragment.itemArrayList.size()!=0) {
           for(int i=0; i<itemArrayList.size();i++)
           {
               myItem = ItemsFragment.itemArrayList.get(i);

           if(myItem.isLike())
           {
              // itemArrayList.add(myItem);



           }
               else{
               itemArrayList.remove(myItem);
           }
       }

       }*/
    }

    public class MyCustomListAdapter extends ArrayAdapter<Item> {


        public MyCustomListAdapter(Context context, int resource, int textViewResourceId, List<Item> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            try {

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_clicked, parent, false);

                myItem = itemArrayList.get(position); //get the clicked object (name,des,icon,phone,site,like)

/*----------------------------------find Views------------------------------------------*/
                name = (TextView) view.findViewById(R.id.name2_tv);
                description = (TextView) view.findViewById(R.id.promo2_tv);
                image = (ImageView) view.findViewById(R.id.item_icon);
                call = (Button) view.findViewById(R.id.call_btn);
                website = (Button) view.findViewById(R.id.website_btn);
                ShineButton shineButton = (ShineButton) view.findViewById(R.id.like_btn); //view." not "getActivity."


/*------------------------------------set values and action to views----------------------------------------*/
      //             //new DownLoadImageTask(image).execute(imageUrl);

                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        startActivity(phoneIntent);
                    }
                });

                website.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        startActivity(i);
                    }
                });


                /*-------------------like btn--------------------*/

                shineButton.init(getActivity());
                //toast(String.valueOf(myItem.isLike()));
                shineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {




                    }
                });



                return view;

            } catch (Exception e) {
                return null;
            }

        }
    }
    public void toast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();}
}
