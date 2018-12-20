package com.moc.byscote.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.moc.byscote.R;

import static com.moc.byscote.MainActivity.drawer;

public class MyAccountFragment extends Fragment {

    View view;
    ImageView img_left_menu ;


    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_my_account, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);

        img_left_menu = view.findViewById(R.id.img_left_menu);
        img_left_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        return view;


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

}
