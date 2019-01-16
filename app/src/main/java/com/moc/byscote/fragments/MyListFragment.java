package com.moc.byscote.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.moc.byscote.adapter.SeriesListAdapter;
import com.moc.byscote.model.SeriesList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.moc.byscote.ByscoteMainActivity.navigation;

public class MyListFragment extends Fragment {

    View view;
    RecyclerView recyclerview;
    SeriesListAdapter recycler_adapter;
    public static ArrayList<SeriesList> Series_List;
    Fragment fragment;
    String series_list_url, category_id;
    TextView tv_category_title;
    String category_title;
    ImageView img_back;


    public MyListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_mylist, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        category_title = getArguments().getString("category_title");
        category_id = getArguments().getString("category_id");
        series_list_url = getResources().getString(R.string.base_url)+"movie_category/series/"+category_id;


        recyclerview = view.findViewById(R.id.recycler_view);
        tv_category_title = view.findViewById(R.id.tv_category_title);

        img_back = view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigation.setSelectedItemId(R.id.navigation_home);

            }
        });

      //  tv_category_title.setText(category_title);
        Series_List = new ArrayList<SeriesList>();

        callSeriesList();

        recyclerview.addOnItemTouchListener(new HomeFragment.RecyclerTouchListener(getActivity(), recyclerview, new HomeFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                fragment = new SeasonsListFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    Bundle args = new Bundle();
                    args.putString("series_id", Series_List.get(position).getSeries_id());
                    args.putString("img_series", Series_List.get(position).getImg_src());

                    fragment.setArguments(args);
                    ft.replace(R.id.mainFrame, fragment).addToBackStack("Seasons");
                    ft.commit();

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;


    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private HomeFragment.ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final HomeFragment.ClickListener clicklistener) {

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
    public void callSeriesList() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, series_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray = null;

                        try {

                            jsonArray = new JSONArray(response);

                            for(int i=0;i< jsonArray.length();i++){

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String name = jsonObject1.getString("name_bu");
                                String img_path = "http://assets.byscote.myanmaronlinecreations.com/movie/series/" + jsonObject1.getString("slide_image");
                                String series_id = jsonObject1.getString("series_id");
                                Log.i("Name : ", name);
                                Log.i("Image Path : ", img_path);

                                SeriesList item = new SeriesList(img_path,name,series_id);
                                Series_List.add(item);

                            }

                            recycler_adapter = new SeriesListAdapter(getActivity(),Series_List);
                          //  recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerview.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                            recycler_adapter.notifyDataSetChanged();
                            recyclerview.setAdapter(recycler_adapter);

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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

}
