package com.moc.byscote.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.moc.byscote.R;
import com.moc.byscote.adapter.NotiAdapter;
import com.moc.byscote.adapter.SeriesListAdapter;
import com.moc.byscote.model.SeriesList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuNotiFragment extends Fragment {

    View view;
    RecyclerView recyclerview;
    NotiAdapter recycler_adapter;
    public static ArrayList<SeriesList> Series_List;
    Fragment fragment;
    String series_list_url, category_id;
    Button btn_pay;
    ImageView img_back;

    public MenuNotiFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_menu_noti, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);

        series_list_url = getResources().getString(R.string.base_url)+"movie_category/series/"+"C596529ca3c1b21.69573066";


        recyclerview = view.findViewById(R.id.recyclerview);
        recyclerview.setNestedScrollingEnabled(false);

        Series_List = new ArrayList<SeriesList>();

        callSeriesList();

        img_back = view.findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


        btn_pay = view.findViewById(R.id.btn_pay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new MenuPaymentFragment();

                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                    ft.replace(R.id.mainFrame, fragment);
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

                            recycler_adapter = new NotiAdapter(getActivity(),Series_List);
                            //  recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
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


}
