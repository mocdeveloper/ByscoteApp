package com.moc.byscote.adapter;

import android.content.Context;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.moc.byscote.R;
import com.moc.byscote.model.CategoryList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sithuaung on 7/9/17.
 */

public class RVListAdapter extends RecyclerView.Adapter<RVListAdapter.PersonViewHolder> {

    List<CategoryList> categoryList_Items;
    List<CategoryList> newList;
    PersonViewHolder pvh;
    Context context;


    public RVListAdapter(Context context , List<CategoryList> categoryList_Items) {

        this.context = context;
        this.categoryList_Items = categoryList_Items;
        this.newList = new ArrayList<CategoryList>();
        this.newList.addAll(categoryList_Items);
    }


    @Override
    public int getItemCount() {
        return categoryList_Items.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        pvh = new PersonViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {

        personViewHolder.tv_category.setText(categoryList_Items.get(i).getCategory_name());
      //  personViewHolder.tv_category.setBackgroundResource(categoryList_Items.get(i).getImg_src());
        Picasso.get().load(categoryList_Items.get(i).getImg_src()).into(personViewHolder.img_category);

      //  Glide.with(context).load(categoryList_Items.get(i).getImg_src()).into(personViewHolder.img_category);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        //  CardView cv;
        TextView tv_category;
        ImageView img_category;

        PersonViewHolder(View itemView) {

            super(itemView);

            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
            img_category = (ImageView) itemView.findViewById(R.id.img_category);

        }
    }


//    public int filter(String search) {
//        search = search.toLowerCase(Locale.getDefault());
//        listDetail_items.clear();
//        if (search.length() == 0) {
//            listDetail_items.addAll(newList);
//        }
//        else
//        {
//            for (ListItems a : newList)
//            {
//                if (a.getDress_name().toLowerCase(Locale.getDefault()).contains(search)||
//                        a.getDress_description().toLowerCase(Locale.getDefault()).contains(search)||
//                        a.getDress_style().toLowerCase(Locale.getDefault()).contains(search)) {
//
//                    listDetail_items.add(a);
//                }
//
//            }
//        }
//        notifyDataSetChanged();
//        return listDetail_items.size();
//    }
}