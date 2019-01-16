package com.moc.byscote.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moc.byscote.R;

import static com.moc.byscote.MainActivity.drawer;

public class PaymentSelectFragment extends Fragment {

    View view,dialog_view;
    Button button;
    Fragment fragment;
    CardView card_shop,card_mpu,card_online;
    ImageView img_back;
    int select_id=0;
    LinearLayout l1,l2,l3;
    Button btn_continue;


    public PaymentSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_payment_select, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);

        img_back = view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });


        card_shop = view.findViewById(R.id.card_shop);
        card_mpu = view.findViewById(R.id.card_mpu);
        card_online= view.findViewById(R.id.card_online);

        l1 = view.findViewById(R.id.layout_shop);
        l2 = view.findViewById(R.id.layout_mpu);
        l3= view.findViewById(R.id.layout_online);
        btn_continue = view.findViewById(R.id.btn_continue);

        btn_continue.setVisibility(View.INVISIBLE);
        btn_continue.setEnabled(false);

        card_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_id = 1;
                l1.setBackground(getResources().getDrawable(R.drawable.card_background_select));
                l2.setBackground(getResources().getDrawable(R.drawable.card_background));
                l3.setBackground(getResources().getDrawable(R.drawable.card_background));

                btn_continue.setVisibility(View.VISIBLE);
                btn_continue.setEnabled(true);

            }
        });

        card_mpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_id = 2;
                l2.setBackground(getResources().getDrawable(R.drawable.card_background_select));
                l1.setBackground(getResources().getDrawable(R.drawable.card_background));
                l3.setBackground(getResources().getDrawable(R.drawable.card_background));

                btn_continue.setVisibility(View.VISIBLE);
                btn_continue.setEnabled(true);


            }
        });

        card_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                select_id = 3;
                l3.setBackground(getResources().getDrawable(R.drawable.card_background_select));
                l2.setBackground(getResources().getDrawable(R.drawable.card_background));
                l1.setBackground(getResources().getDrawable(R.drawable.card_background));

                btn_continue.setVisibility(View.VISIBLE);
                btn_continue.setEnabled(true);


            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(select_id==1){

                    fragment = new PaymentShopFragment();

                    if (fragment != null) {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        ft.replace(R.id.mainFrame, fragment).addToBackStack("Shop");
                        ft.commit();

                    }
                }else if(select_id==2){

                    fragment = new PaymentMPUFragment();

                    if (fragment != null) {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        ft.replace(R.id.mainFrame, fragment).addToBackStack("MPU");
                        ft.commit();

                    }
                }else if(select_id==3){

                    fragment = new PaymentOnlineFragment();

                    if (fragment != null) {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                        ft.replace(R.id.mainFrame, fragment).addToBackStack("Online");
                        ft.commit();

                    }
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
