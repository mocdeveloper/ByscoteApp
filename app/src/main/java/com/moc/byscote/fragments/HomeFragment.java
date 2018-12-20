package com.moc.byscote.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.moc.byscote.MainActivity;
import com.moc.byscote.R;
import com.moc.byscote.adapter.PagerAdapter;
import com.moc.byscote.adapter.RVListAdapter;
import com.moc.byscote.adapter.SeriesListAdapter;
import com.moc.byscote.adapter.SeriesListAdapter2;
import com.moc.byscote.adapter.SeriesListAdapter3;
import com.moc.byscote.adapter.SeriesListAdapter4;
import com.moc.byscote.adapter.SeriesListAdapter5;
import com.moc.byscote.model.CategoryList;
import com.moc.byscote.model.SeriesList;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.kshitij_jain.indicatorview.IndicatorView;

import static com.moc.byscote.MainActivity.drawer;

public class HomeFragment extends Fragment {

    View view;
    ImageView item_image;
    String count_url;
    TextView txt1, txt2, txt3, txt4;
   // SliderLayout sliderLayout;
    DiscreteScrollView scrollView;
    RecyclerView recyclerview, recyclerview2, recyclerview3, recyclerview4, recyclerview5;
    RVListAdapter recycler_adapter;
    SeriesListAdapter recycler_adapter2;
    SeriesListAdapter2 recycler_adapter3;
    SeriesListAdapter3 recycler_adapter4;
    SeriesListAdapter4 recycler_adapter5;
    SeriesListAdapter5 recycler_adapter6;
    PagerAdapter pagerAdapter;
    ImageView v1,v2,v3,v4,v5;

    public static ArrayList<CategoryList> Categories_List;
    public static ArrayList<SeriesList> Series_List;
    Fragment fragment = null;
    String category_list_url, series_list_url, category_id, img_path;
    ImageView img_left_menu;
    IndicatorView mIndicatorView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

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
                .setMinScale(0.9f)
                .build());

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


//        sliderLayout = view.findViewById(R.id.imageSlider);
//        sliderLayout.setIndicatorAnimation(SliderLayout.Animations.SWAP); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//        sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :

//        setSliderViews();

        recyclerview = view.findViewById(R.id.recycler_view);
        recyclerview2 = view.findViewById(R.id.recycler_view2);
        recyclerview3 = view.findViewById(R.id.recycler_view3);
        recyclerview4 = view.findViewById(R.id.recycler_view4);
        recyclerview5 = view.findViewById(R.id.recycler_view5);

        v1 = view.findViewById(R.id.v1);
        v2 = view.findViewById(R.id.v2);
        v3 = view.findViewById(R.id.v3);
        v4 = view.findViewById(R.id.v4);
        v5 = view.findViewById(R.id.v5);


        Categories_List = new ArrayList<CategoryList>();
        Series_List = new ArrayList<SeriesList>();

        callCategoryList();



//        int resId = R.anim.layout_animation_from_bottom;
//        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
//        recyclerview.setLayoutAnimation(animation);
//        recyclerview2.setLayoutAnimation(animation);
//        recyclerview3.setLayoutAnimation(animation);
//        recyclerview4.setLayoutAnimation(animation);
//        recyclerview5.setLayoutAnimation(animation);



        v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new SeriesListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("category_id", Categories_List.get(0).getCategory_id());

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

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
                    ft.commit();

                }
            }
        });

        v5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new SeriesListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("category_id", Categories_List.get(0).getCategory_id());

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

        recyclerview5.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerview, new ClickListener() {
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

//    private void setSliderViews() {
//
//        for (int i = 0; i <= 4; i++) {
//
//            SliderView sliderView = new SliderView(getActivity());
//            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
//
//            switch (i) {
//                case 0: {
//                    sliderView.setImageUrl("http://assets.byscote.myanmaronlinecreations.com/movie/series/S5a967ea6e4c0d5_43661710.jpg");
//                    sliderView.setDescription("ေဖ့ေဖ့ မွတ္စု");
//                    sliderView.setDescriptionTextSize(19);
//                }
//                break;
//                case 1: {
//                    sliderView.setImageUrl("http://assets.byscote.myanmaronlinecreations.com/movie/series/S5a8fc88b205434_67068163.jpg");
//                    sliderView.setDescription("ေလ်ာ္ေၾကး");
//                    sliderView.setDescriptionTextSize(19);
//                }
//                break;
//                case 2: {
//                    sliderView.setImageUrl("http://assets.byscote.myanmaronlinecreations.com/movie/series/S5a967e07299935_63190660.jpg");
//                    sliderView.setDescription("အခ်စ္ဦး နိယာမ");
//                    sliderView.setDescriptionTextSize(19);
//                }
//                break;
//                case 3: {
//                    sliderView.setImageUrl("http://assets.byscote.myanmaronlinecreations.com/movie/series/S5a8fc0838a8148_98887113.jpg");
//                    sliderView.setDescription("တို႔ေက်ာင္းက ေႂကြးေၾကာ္သံ");
//                    sliderView.setDescriptionTextSize(19);
//                }
//                break;
//
//                case 4: {
//                    sliderView.setImageUrl("http://assets.byscote.myanmaronlinecreations.com/movie/series/S5a9682746c9e38_35977495.jpg");
//                    sliderView.setDescription("တရားေသာ ေရွ႕ေန");
//                    sliderView.setDescriptionTextSize(19);
//                }
//                break;
//
//
//            }
//
//
//            final int finalI = i;
//            sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
//                @Override
//                public void onSliderClick(SliderView sliderView) {
//
//                    // Toast.makeText(MainActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
//                    fragment = new SeasonsListFragment();
//
//                    if (fragment != null) {
//                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
//
//                        Bundle args = new Bundle();
//                        args.putString("series_id", Series_List.get(0).getSeries_id());
//                        args.putString("img_series", Series_List.get(0).getImg_src());
//
//                        fragment.setArguments(args);
//                        ft.replace(R.id.mainFrame, fragment).addToBackStack("Series");
//                        ft.commit();
//
//                    }
//
//                }
//            });
//
//            //at last add this view in your layout :
//            sliderLayout.addSliderView(sliderView);
//        }
//    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
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

                            mIndicatorView.setPageIndicators(Series_List.size());
                            pagerAdapter = new PagerAdapter(getActivity(), Series_List);
                            scrollView.setAdapter(pagerAdapter);

                            recycler_adapter3 = new SeriesListAdapter2(getActivity(), Series_List);
                            LinearLayoutManager layoutManager2
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview2.setLayoutManager(layoutManager2);
                            recyclerview2.setAdapter(recycler_adapter3);

                            recycler_adapter4 = new SeriesListAdapter3(getActivity(), Series_List);
                            LinearLayoutManager layoutManager3
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview3.setLayoutManager(layoutManager3);
                            recyclerview3.setAdapter(recycler_adapter4);

                            recycler_adapter5 = new SeriesListAdapter4(getActivity(), Series_List);
                            LinearLayoutManager layoutManager4
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview4.setLayoutManager(layoutManager4);
                            recyclerview4.setAdapter(recycler_adapter5);

                            recycler_adapter6 = new SeriesListAdapter5(getActivity(), Series_List);
                            LinearLayoutManager layoutManager5
                                    = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                            recyclerview5.setLayoutManager(layoutManager5);
                            recyclerview5.setAdapter(recycler_adapter6);


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


}
