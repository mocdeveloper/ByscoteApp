package com.moc.byscote.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.exoplayer2.ui.PlayerView;
import com.moc.byscote.R;

public class PaymentOnlineFragment extends Fragment {

    View view;
    CardView card_telenor,card_ooredoo,card_welink;
    Fragment fragment;
    ImageView img_back;

    public PaymentOnlineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_payment_online, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);
        card_telenor = view.findViewById(R.id.card_telenor);
        card_ooredoo = view.findViewById(R.id.card_ooredoo);
        card_welink = view.findViewById(R.id.card_welink);


        img_back = view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });

        card_telenor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new PaymentOnlineTelenorFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Shop");
                    ft.commit();

                }
            }
        });

        card_ooredoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new PaymentOnlineOoredooFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Shop");
                    ft.commit();

                }
            }
        });

        card_welink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new PaymentOnlineWeLinkFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Shop");
                    ft.commit();

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
