package com.moc.byscote.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.moc.byscote.R;

public class MenuFragment extends Fragment {

    View view;
    RelativeLayout rl_account,rl_payment,rl_promo,rl_noti;
    Fragment fragment = null;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_menu, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);

        rl_account = view.findViewById(R.id.rl_account);
        rl_payment = view.findViewById(R.id.rl_payment);
        rl_promo = view.findViewById(R.id.rl_promo);
        rl_noti = view.findViewById(R.id.rl_noti);

        rl_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new MenuAccountFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Account");
                    ft.commit();

                }
            }
        });

        rl_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new MenuPaymentFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Account");
                    ft.commit();

                }

            }
        });


        rl_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new MenuPromoFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Account");
                    ft.commit();

                }

            }
        });


        rl_noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new MenuNotiFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Account");
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
