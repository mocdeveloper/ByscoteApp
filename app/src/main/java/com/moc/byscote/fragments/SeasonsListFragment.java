package com.moc.byscote.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import com.downloader.utils.Utils;
import com.moc.byscote.PlayerActivity;
import com.moc.byscote.R;
import com.moc.byscote.adapter.EpisodesListAdapter;
import com.moc.byscote.adapter.SeriesListAdapter;
import com.moc.byscote.model.EpisodesList;
import com.moc.byscote.model.SeriesList;
import com.novoda.downloadmanager.Batch;
import com.novoda.downloadmanager.DownloadBatchIdCreator;
import com.novoda.downloadmanager.DownloadFileIdCreator;
import com.novoda.downloadmanager.DownloadManager;
import com.novoda.downloadmanager.DownloadManagerBuilder;
import com.novoda.downloadmanager.StorageRequirementRuleFactory;
import com.novoda.downloadmanager.StorageRoot;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.moc.byscote.MainActivity.drawer;


public class SeasonsListFragment extends Fragment {

    View view;
    RecyclerView recyclerview;
    EpisodesListAdapter recycler_adapter;
    public static ArrayList<EpisodesList> Episodes_List;
    Fragment fragment;
    TextView txt_description;
    ImageView img_series;
    String description_bu;
    String img_path,img_src;
    String seasons_list_url, series_id , season_id,episodes_list_url;
    String cookies_url,dirPath;
    Button btn_download;
    String mockey,signature,policy,key_pair_id;
    Batch batch;
    DownloadManager downloadManager;


    public SeasonsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_episodes, null);

        //  item_image = (ImageView) view.findViewById(R.id.btn_item_image);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       // Setting timeout globally for the download network requests:

        cookies_url = "https://byscote.com/test42?json=1";

        series_id = getArguments().getString("series_id");
        img_src = getArguments().getString("img_series");
        seasons_list_url = getResources().getString(R.string.base_url)+"movie_category/seasons/"+series_id;


        recyclerview = view.findViewById(R.id.recycler_view);
        recyclerview.setNestedScrollingEnabled(false);
        btn_download = view.findViewById(R.id.btn_download);

        Handler handler = new Handler();
        downloadManager = DownloadManagerBuilder
                .newInstance(getActivity(), handler, R.mipmap.ic_launcher_round)
                .build();

        StorageRoot storageRoot = new StorageRoot() {
            @Override
            public String path() {
                return getActivity().getExternalCacheDir().toString();
            }
        };
        batch = Batch.with(storageRoot, DownloadBatchIdCreator.createSanitizedFrom("batch_id_1"), "batch one title")
                .downloadFrom(getResources().getString(R.string.video_url)).saveTo("foo/bar", "local-filename-5mb.zip").withIdentifier(DownloadFileIdCreator.createFrom("file_id_1")).apply()
                .downloadFrom("http://ipv4.download.thinkbroadband.com/10MB.zip").apply()
                .build();



        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cookieValue = "mockey="+mockey+";CloudFront-Policy="+policy+
                        ";CloudFront-Signature="+signature+
                        ";CloudFront-Key-Pair-Id="+key_pair_id;


                downloadManager.download(batch);

            }
        });

        img_series = view.findViewById(R.id.img_series);
        txt_description = view.findViewById(R.id.txt_description);

        Episodes_List = new ArrayList<EpisodesList>();

        callSeasonsList();

        recyclerview.addOnItemTouchListener(new HomeFragment.RecyclerTouchListener(getActivity(), recyclerview, new HomeFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {


                callForCookies();


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

    public void callForCookies() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, cookies_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonObject = null;
                        try {

                            jsonObject = new JSONObject(response);

                            JSONObject jsonObject1 = jsonObject.getJSONObject("cookies");

                            mockey = jsonObject1.getString("mockey");
                            policy = jsonObject1.getString("CloudFront-Policy");
                            signature = jsonObject1.getString("CloudFront-Signature");
                            key_pair_id = jsonObject1.getString("CloudFront-Key-Pair-Id");

                            Intent i = new Intent(getActivity(),PlayerActivity.class);
                            i.putExtra("mockey",mockey);
                            i.putExtra("policy",policy);
                            i.putExtra("signature",signature);
                            i.putExtra("key_pair_id",key_pair_id);

                            startActivity(i);

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


    public void callSeasonsList() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, seasons_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray = null;

                        try {

                            jsonArray = new JSONArray(response);

                            for(int i=0;i< jsonArray.length();i++){

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String name = jsonObject1.getString("name_bu");
                                img_path = "http://assets.byscote.myanmaronlinecreations.com/movie/seasons/" + jsonObject1.getString("image");
                                season_id = jsonObject1.getString("season_id");

                                Log.i("Name : ", name);
                                Log.i("Image Path : ", img_path);

//                                SeriesList item = new SeriesList(img_path,name,season_id);
//                                Series_List.add(item);

                            }

                            episodes_list_url = getResources().getString(R.string.base_url)+"movie_category/episodes/"+season_id;
                            callEpisodesList();

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

    public void callEpisodesList() {


        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, episodes_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONArray jsonArray = null;

                        try {

                            jsonArray = new JSONArray(response);

                            for(int i=0;i< jsonArray.length();i++){

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String name = jsonObject1.getString("name_bu");
                                description_bu = jsonObject1.getString("description_bu");
                                String img_path = "http://assets.byscote.myanmaronlinecreations.com/movie/episode/" + jsonObject1.getString("image");
                                season_id = jsonObject1.getString("season_id");

                                Log.i("Name : ", name);
                                Log.i("Image Path : ", img_path);

                                EpisodesList item = new EpisodesList(img_path,name,season_id,description_bu,"","");
                                Episodes_List.add(item);

                            }


                            Picasso.get().load(img_src).into(img_series);
                            txt_description.setText(description_bu);
                            recycler_adapter = new EpisodesListAdapter(getActivity(),Episodes_List);
                            GridLayoutManager mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
                            recyclerview.setLayoutManager(mGridLayoutManager);
                           // recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
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
