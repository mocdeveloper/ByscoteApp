package com.moc.byscote.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moc.byscote.R;
import com.moc.byscote.model.SeriesList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sithuaung on 7/9/17.
 */

public class SeriesListAdapter5 extends RecyclerView.Adapter<SeriesListAdapter5.PersonViewHolder> {

    List<SeriesList> seriesList_Items;
    List<SeriesList> newList;
    PersonViewHolder pvh;
    Context context;


    public SeriesListAdapter5(Context context , List<SeriesList> seriesList_Items) {

        this.context = context;
        this.seriesList_Items = seriesList_Items;
        this.newList = new ArrayList<SeriesList>();
        this.newList.addAll(seriesList_Items);
    }


    @Override
    public int getItemCount() {
        return seriesList_Items.size();
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.series_list_item, viewGroup, false);
        pvh = new PersonViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {


      //  personViewHolder.tv_category.setBackgroundResource(categoryList_Items.get(i).getImg_src());
        if(i==0){
            Picasso.get().load(R.mipmap.a14).into(personViewHolder.img_series);
            personViewHolder.tv_series_title.setText("WILLIAM CAREY");
        }else if(i==1){
            Picasso.get().load(R.mipmap.a15).into(personViewHolder.img_series);
            personViewHolder.tv_series_title.setText("Bring Him Home");
        }else  if(i==2){
            Picasso.get().load(R.mipmap.a2).into(personViewHolder.img_series);
            personViewHolder.tv_series_title.setText("Five Story");
        }else {
            Picasso.get().load(seriesList_Items.get(i-2).getImg_src()).into(personViewHolder.img_series);
            personViewHolder.tv_series_title.setText(seriesList_Items.get(i).getSeries_name());
        }

     //   Glide.with(context).load(seriesList_Items.get(i).getImg_src()).into(personViewHolder.img_series);


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        //  CardView cv;
        TextView tv_series_title;
        ImageView img_series;

        PersonViewHolder(View itemView) {

            super(itemView);

            tv_series_title = (TextView) itemView.findViewById(R.id.tv_series_title);
            img_series = (ImageView) itemView.findViewById(R.id.img_series);

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