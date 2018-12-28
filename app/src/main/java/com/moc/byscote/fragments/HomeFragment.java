package com.moc.byscote.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.moc.byscote.R;
import com.moc.byscote.adapter.CategoryListAdapter;
import com.moc.byscote.adapter.PagerAdapter;
import com.moc.byscote.adapter.RVListAdapter;
import com.moc.byscote.adapter.SeriesListAdapter;
import com.moc.byscote.adapter.SeriesListAdapter2;
import com.moc.byscote.adapter.SeriesListAdapter3;
import com.moc.byscote.adapter.SeriesListAdapter4;
import com.moc.byscote.model.CategoryList;
import com.moc.byscote.model.SeriesList;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.kshitij_jain.indicatorview.IndicatorView;

import static com.moc.byscote.MainActivity.drawer;

public class HomeFragment extends Fragment {

    View view, dialog_view;
    ImageView item_image;
    ImageView button;
    String count_url;
    TextView txt1, txt2, txt3, txt4;
    // SliderLayout sliderLayout;
    DiscreteScrollView scrollView;
    RecyclerView recyclerview, recyclerview2, recyclerview3, recyclerview4, recyclerCategory, recycler_category_pop_up;
    RVListAdapter recycler_adapter;
    SeriesListAdapter recycler_adapter2;
    SeriesListAdapter2 recycler_adapter3;
    SeriesListAdapter3 recycler_adapter4;
    SeriesListAdapter4 recycler_adapter5;
    CategoryListAdapter recycler_category_adapter;

    PagerAdapter pagerAdapter;
    ImageView v1, v2, v3, v4;
    TextView t1,t2,t3,t4,t5;

    public static ArrayList<CategoryList> Categories_List;
    public static ArrayList<SeriesList> Series_List;
    Fragment fragment = null;
    String category_list_url, series_list_url, category_id, img_path;
    ImageView img_left_menu;
    IndicatorView mIndicatorView;
    AlertDialog alertDialog;
    LayoutInflater inflate,inflate2;
    Dialog dialog2;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        dialog2 = new Dialog(getActivity(), android.R.style.Theme_Light);
//        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        inflate2 = getActivity().getLayoutInflater();
//        dialog_view = inflate2.inflate(R.layout.loading_dialog, null);
//        dialog2.setContentView(dialog_view);
//        dialog2.show();

        view = inflater.inflate(R.layout.fragment_home, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        category_list_url = getResources().getString(R.string.base_url) + "movie_category/";

        img_left_menu = view.findViewById(R.id.img_left_menu);
        mIndicatorView = view.findViewById(R.id.circle_indicator_view);

        scrollView = view.findViewById(R.id.picker);
        scrollView.setOffscreenItems(0); //Reserve extra space equal to (childSize * count) on each side of the view
        scrollView.setOverScrollEnabled(true);
        scrollView.setItemTransitionTimeMillis(100);
        scrollView.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(1f)
                .build());


        recyclerview = view.findViewById(R.id.recycler_view);
        recyclerview2 = view.findViewById(R.id.recycler_view2);
        recyclerview3 = view.findViewById(R.id.recycler_view3);
        recyclerview4 = view.findViewById(R.id.recycler_view4);
        recyclerCategory = view.findViewById(R.id.recycler_category);

        recyclerCategory.setNestedScrollingEnabled(false);


        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);
        v3 = view.findViewById(R.id.v3);
        v4 = view.findViewById(R.id.v4);

        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        t3 = view.findViewById(R.id.t3);
        t4 = view.findViewById(R.id.t4);
        t5 = view.findViewById(R.id.t5);

        v1.setVisibility(View.INVISIBLE);
        v2.setVisibility(View.INVISIBLE);
        v3.setVisibility(View.INVISIBLE);
        v4.setVisibility(View.INVISIBLE);
        t1.setVisibility(View.INVISIBLE);
        t2.setVisibility(View.INVISIBLE);
        t3.setVisibility(View.INVISIBLE);
        t4.setVisibility(View.INVISIBLE);
        t5.setVisibility(View.INVISIBLE);



        Categories_List = new ArrayList<CategoryList>();
        Series_List = new ArrayList<SeriesList>();

        callCategoryList();
//        inflate = getActivity().getLayoutInflater();
//        dialog_view = inflate.inflate(R.layout.category_dialog,null);
//        button = dialog_view.findViewById(R.id.btn_continue);
//        recycler_category_pop_up = dialog_view.findViewById(R.id.recycler_category_pop_up);


        img_left_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_Light);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                inflate = getActivity().getLayoutInflater();
                dialog_view = inflate.inflate(R.layout.category_dialog, null);
                button = dialog_view.findViewById(R.id.btn_continue);
                recycler_category_pop_up = dialog_view.findViewById(R.id.recycler_category_pop_up);

                callCategoryList2();

                dialog.setContentView(dialog_view);
                dialog.show();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();


                    }
                });

                recycler_category_pop_up.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recycler_category_pop_up, new ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        fragment = new SeriesListFragment();

                        if (fragment != null) {
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                            Bundle args = new Bundle();
                            args.putString("category_id", Categories_List.get(position).getCategory_id());
                            args.putString("category_title", Categories_List.get(position).getCategory_name());

                            fragment.setArguments(args);
                            ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                            ft.commit();

                        }
                        dialog.dismiss();

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));


//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                // Get the layout inflater
//
//                // Inflate and set the layout for the dialog
//                // Pass null as the parent view because its going in the
//                // dialog layout
//                builder.setCancelable(true);
//
//                inflate = getActivity().getLayoutInflater();
//                dialog_view = inflate.inflate(R.layout.category_dialog,null);
//                button = dialog_view.findViewById(R.id.btn_continue);
//                recycler_category_pop_up = dialog_view.findViewById(R.id.recycler_category_pop_up);
//
//                callCategoryList2();
//
//                builder.setView(dialog_view);
//                alertDialog = builder.create();
//
////                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
////                lp.copyFrom(alertDialog.getWindow().getAttributes());
////                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
////                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//                int width = (int)(getResources().getDisplayMetrics().widthPixels);
//                int height = (int)(getResources().getDisplayMetrics().heightPixels);
//
//                alertDialog.getWindow().setLayout(width, height);
//                alertDialog.show();
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        alertDialog.dismiss();
//
//
//                    }
//                });

            }
        });


        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new SeriesListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("category_id", Categories_List.get(0).getCategory_id());
                    args.putString("category_title", "Continue Watching");

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }
            }
        });

        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new SeriesListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("category_id", Categories_List.get(0).getCategory_id());
                    args.putString("category_title", "Recently Added");

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }
            }
        });

        v3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new SeriesListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("category_id", Categories_List.get(0).getCategory_id());
                    args.putString("category_title", "Fantasy");

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }
            }
        });


        v4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new SeriesListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("category_id", Categories_List.get(0).getCategory_id());
                    args.putString("category_title", "Action");

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }
            }
        });


        recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                fragment = new SeasonsListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("series_id", Series_List.get(position).getSeries_id());
                    args.putString("img_series", Series_List.get(position).getImg_src());

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerview2.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                fragment = new SeasonsListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("series_id", Series_List.get(position).getSeries_id());
                    args.putString("img_series", Series_List.get(position).getImg_src());

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerview3.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                fragment = new SeasonsListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("series_id", Series_List.get(position).getSeries_id());
                    args.putString("img_series", Series_List.get(position).getImg_src());

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerview4.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                fragment = new SeasonsListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("series_id", Series_List.get(position).getSeries_id());
                    args.putString("img_series", Series_List.get(position).getImg_src());

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recyclerCategory.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                fragment = new SeriesListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("category_id", Categories_List.get(position).getCategory_id());
                    args.putString("category_title", Categories_List.get(position).getCategory_name());

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        scrollView.addOnItemChangedListener(new DiscreteScrollView.OnItemChangedListener<RecyclerView.ViewHolder>() {
            @Override
            public void onCurrentItemChanged(@Nullable RecyclerView.ViewHolder viewHolder, int adapterPosition) {
                mIndicatorView.setCurrentPage(adapterPosition);
            }
        });


        return view;


    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

    public void callCategoryList2() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, category_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray = null;

                        try {
                            Categories_List = new ArrayList<CategoryList>();

                            jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String name = jsonObject1.getString("name_bu");
                                String img_path = "http://assets.byscote.myanmaronlinecreations.com/movie/category/" + jsonObject1.getString("image");
                                String category_id = jsonObject1.getString("category_id");

                                Log.i("Name : ", name);
                                Log.i("Image Path : ", img_path);
                                Log.i("category_id : ", category_id);

                                CategoryList item = new CategoryList(img_path, name, category_id);
                                Categories_List.add(item);

                            }

                            recycler_category_adapter = new CategoryListAdapter(getActivity(), Categories_List);

                            LinearLayoutManager layoutManager6
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recycler_category_pop_up.setLayoutManager(layoutManager6);
                            recycler_category_adapter.notifyDataSetChanged();
                            recycler_category_pop_up.setAdapter(recycler_category_adapter);

//                            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            recycler_adapter.notifyDataSetChanged();
//                            recyclerview.setAdapter(recycler_adapter);

                            Log.i("Response", response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Log.e("That didn't work!", error.toString());

                        NetworkResponse networkResponse = error.networkResponse;

                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized

                            //   Toast.makeText(getActivity(), "Wrong Token", Toast.LENGTH_LONG).show();


                        } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            // HTTP Status Code: 500 Internal Server Error

                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();


                        } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_NOT_FOUND) {
                            // HTTP Status Code: 404 Not Found

                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();


                        } else if ((networkResponse != null && networkResponse.statusCode == HttpStatus.SC_REQUEST_TIMEOUT)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_GATEWAY)) {


                        } else {


                        }
                    }
                }) {


        };

        try {

            queue.add(stringRequest);

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void callCategoryList() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, category_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray = null;

                        try {

                            jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String name = jsonObject1.getString("name_bu");
                                String img_path = "http://assets.byscote.myanmaronlinecreations.com/movie/category/" + jsonObject1.getString("image");
                                String category_id = jsonObject1.getString("category_id");

                                Log.i("Name : ", name);
                                Log.i("Image Path : ", img_path);
                                Log.i("category_id : ", category_id);

                                CategoryList item = new CategoryList(img_path, name, category_id);
                                Categories_List.add(item);

                            }

                            category_id = Categories_List.get(0).getCategory_id();
                            series_list_url = getResources().getString(R.string.base_url) + "movie_category/series/" + category_id;

                            callSeriesList();

                            recycler_adapter = new RVListAdapter(getActivity(), Categories_List);
                            recycler_category_adapter = new CategoryListAdapter(getActivity(), Categories_List);


//                            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            recycler_adapter.notifyDataSetChanged();
//                            recyclerview.setAdapter(recycler_adapter);

                            Log.i("Response", response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Log.e("That didn't work!", error.toString());

                        NetworkResponse networkResponse = error.networkResponse;

                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized

                            //   Toast.makeText(getActivity(), "Wrong Token", Toast.LENGTH_LONG).show();


                        } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            // HTTP Status Code: 500 Internal Server Error

                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();


                        } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_NOT_FOUND) {
                            // HTTP Status Code: 404 Not Found

                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();


                        } else if ((networkResponse != null && networkResponse.statusCode == HttpStatus.SC_REQUEST_TIMEOUT)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_GATEWAY)) {


                        } else {


                        }
                    }
                }) {


        };

        try {

            queue.add(stringRequest);

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callSeriesList() {

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, series_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray = null;
                        String img_path;
                        try {

                            jsonArray = new JSONArray(response);

                            for (int i = 1; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String name = jsonObject1.getString("name_bu");

                                img_path = "http://assets.byscote.myanmaronlinecreations.com/movie/series/" + jsonObject1.getString("slide_image");
                                String series_id = jsonObject1.getString("series_id");
                                Log.i("Name : ", name);
                                Log.i("Image Path : ", img_path);

                                SeriesList item = new SeriesList(img_path, name, series_id);
                                Series_List.add(item);

                            }

                            recycler_adapter2 = new SeriesListAdapter(getActivity(), Series_List);
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview.setLayoutManager(layoutManager);
                            recycler_adapter2.notifyDataSetChanged();
                            recyclerview.setAdapter(recycler_adapter2);
                            recyclerview.getViewTreeObserver().addOnPreDrawListener(
                                    new ViewTreeObserver.OnPreDrawListener() {

                                        @Override
                                        public boolean onPreDraw() {
                                            recyclerview.getViewTreeObserver().removeOnPreDrawListener(this);

                                            for (int i = 0; i < recyclerview.getChildCount(); i++) {
                                                View v = recyclerview.getChildAt(i);
                                                v.setAlpha(0.0f);
                                                v.animate().alpha(1.0f)
                                                        .setDuration(300)
                                                        .setStartDelay(i * 50)
                                                        .start();
                                            }

                                            return true;
                                        }
                                    });

                            mIndicatorView.setPageIndicators(Series_List.size());
                            pagerAdapter = new PagerAdapter(getActivity(), Series_List);
                            pagerAdapter.notifyDataSetChanged();
                            scrollView.setAdapter(pagerAdapter);
                            scrollView.scrollToPosition(1);
                            mIndicatorView.setCurrentPage(1);

                            recycler_adapter3 = new SeriesListAdapter2(getActivity(), Series_List);
                            LinearLayoutManager layoutManager2
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview2.setLayoutManager(layoutManager2);
                            recyclerview2.setAdapter(recycler_adapter3);

                            recycler_adapter4 = new SeriesListAdapter3(getActivity(), Series_List);
                            LinearLayoutManager layoutManager4
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview3.setLayoutManager(layoutManager4);
                            recyclerview3.setAdapter(recycler_adapter4);

                            recycler_adapter5 = new SeriesListAdapter4(getActivity(), Series_List);
                            LinearLayoutManager layoutManager5
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview4.setLayoutManager(layoutManager5);
                            recyclerview4.setAdapter(recycler_adapter5);

                            LinearLayoutManager layoutManager3
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recyclerCategory.setLayoutManager(layoutManager3);
                            recycler_category_adapter.notifyDataSetChanged();
                            recyclerCategory.setAdapter(recycler_category_adapter);


                            v1.setVisibility(View.VISIBLE);
                            v2.setVisibility(View.VISIBLE);
                            v3.setVisibility(View.VISIBLE);
                            v4.setVisibility(View.VISIBLE);
                            t1.setVisibility(View.VISIBLE);
                            t2.setVisibility(View.VISIBLE);
                            t3.setVisibility(View.VISIBLE);
                            t4.setVisibility(View.VISIBLE);
                            t5.setVisibility(View.VISIBLE);

                            Log.i("Response", response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                     //   dialog2.dismiss();
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //Log.e("That didn't work!", error.toString());

                        NetworkResponse networkResponse = error.networkResponse;

                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            // HTTP Status Code: 401 Unauthorized

                            //   Toast.makeText(getActivity(), "Wrong Token", Toast.LENGTH_LONG).show();


                        } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            // HTTP Status Code: 500 Internal Server Error

                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();


                        } else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_NOT_FOUND) {
                            // HTTP Status Code: 404 Not Found

                            //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();


                        } else if ((networkResponse != null && networkResponse.statusCode == HttpStatus.SC_REQUEST_TIMEOUT)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_SERVICE_UNAVAILABLE)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN)
                                || (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_GATEWAY)) {


                        } else {


                        }
                    }
                }) {


        };

        try {

            queue.add(stringRequest);

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
