package com.moc.byscote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.moc.byscote.fragments.HomeFragment;
import com.moc.byscote.fragments.MenuFragment;
import com.moc.byscote.fragments.MyListFragment;
import com.moc.byscote.fragments.SeriesListFragment;
import com.moc.byscote.model.CategoryList;
import com.moc.byscote.model.MenuModel;

import java.util.ArrayList;

public class ByscoteMainActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    public static ArrayList<CategoryList> Categories_List;
    Fragment fragment = null;
    String category_list_url;
    MenuModel childModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home: {
                    fragment = new HomeFragment();

                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.mainFrame, fragment);
                        ft.commit();

                    }
                    return true;
                }
                case R.id.navigation_list: {

                    fragment = new MyListFragment();

                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        Bundle args = new Bundle();
                        args.putString("category_id", "C596529ca3c1b21.69573066");
                        args.putString("category_title", "My List");

                        fragment.setArguments(args);
                        ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                        ft.commit();

                    }
                    return true;
                }
                case R.id.navigation_menu: {

                    fragment = new MenuFragment();

                    if (fragment != null) {
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                        ft.replace(R.id.mainFrame, fragment).addToBackStack("Menu");
                        ft.commit();

                    }
                    return true;
                }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_byscote_main);

        getSupportActionBar().hide();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment = new HomeFragment();

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();

        }
    }

}
