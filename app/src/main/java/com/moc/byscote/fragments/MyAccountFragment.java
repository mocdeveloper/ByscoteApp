package com.moc.byscote.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moc.byscote.R;

import static com.moc.byscote.MainActivity.drawer;

public class MyAccountFragment extends Fragment {

    View view,dialog_view;
    ImageView img_left_menu;
    Button button;
    Fragment fragment;
    LinearLayout layout_pay;
    AlertDialog alertDialog;


    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_my_account, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);

        layout_pay = view.findViewById(R.id.layout_pay);

        layout_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new PaymentSelectFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment).addToBackStack("My Account");
                    ft.commit();

                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflate = getActivity().getLayoutInflater();
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the
        // dialog layout
        builder.setCancelable(true);
        dialog_view = inflate.inflate(R.layout.dialog,null);
        button = dialog_view.findViewById(R.id.btn_continue);
        builder.setView(dialog_view);
        alertDialog = builder.create();
        alertDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();

            }
        });



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
