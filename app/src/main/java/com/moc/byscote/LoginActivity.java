package com.moc.byscote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.moc.byscote.Util.FontManager;
import com.moc.byscote.adapter.RVListAdapter;
import com.moc.byscote.model.CategoryList;
import com.moc.byscote.model.User;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private LinearLayout facebookBtn;
    private WebView webview;
    private TextView txtTnc;
    String login_url;
    User user;

    boolean bTncOpened = false;
    String androidID,UUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        FirebaseMessaging.getInstance().subscribeToTopic("BYSCOTE_NEWS");
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        login_url = getResources().getString(R.string.base_url)+"login_api";
        androidID = MOCInstallation.androidID(this);
        UUID = MOCInstallation.UUID(this);

        if(isConnected()) {
//            setContentView(R.layout.loading_view);
//            ImageView loadingImg = (ImageView) findViewById(R.id.img_loading);
//            AnimationDrawable rocketAnimation = (AnimationDrawable) loadingImg.getDrawable();
//            rocketAnimation.start();

            FacebookSdk.sdkInitialize(getApplicationContext());

            new CountDownTimer(2000, 100) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    //LoginManager.getInstance().logOut();
                    if (isFacebookLoggedIn()) {
                        // if user is already FB logged in, skip this activity's view and go to main activity

                        requestUserData();

                    } else {
                        setContentView(R.layout.activity_login);
                        registerUiCompos();
                        setUpViews();
                        webview.getSettings().setJavaScriptEnabled(true);
                        webview.setWebViewClient(new WebViewClient());
                        // apply custom myanmar font to the whole view(top->down)
                        ApplyZawgyi(findViewById(R.id.login_root));

                        // apply font awesome to FB icon textview
                        ApplyFontAwesome(findViewById(R.id.login_txt_fb_icon));

                        callbackManager = CallbackManager.Factory.create();

                        assignListener();

                        profileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                                profileTracker.stopTracking();
                                Profile.setCurrentProfile(newProfile);
                            }
                        };
                        profileTracker.startTracking();
                    }
                }
            }.start();
        }else {
           // displayMessage("No Internet Connection. Please check your Mobile Data!");
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setMessage("No Internet Connection. Please check your Mobile Data!");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            dialog.dismiss();
                            finish();
                        }
                    });
            alertDialog.show();
        }
    }

    void registerUiCompos() {
        // save any ui components' references
        facebookBtn = findViewById(R.id.login_btn_facebook);
        txtTnc = findViewById(R.id.login_txt_tnc);
        webview = findViewById(R.id.login_webview);
    }

    private void setUpViews() {

    }

    private void assignListener() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                requestUserData();
            }

            @Override
            public void onCancel() {
                displayMessage("Facebook login is cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                displayMessage(error.getMessage());
            }
        });

        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this,
                        Arrays.asList("public_profile", "user_birthday", "user_hometown")
                );
            }
        });

        txtTnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String androidID = MOCInstallation.androidID(LoginActivity.this);
                final String UUID = MOCInstallation.UUID(LoginActivity.this);

                bTncOpened = true;
                webview.setVisibility(View.VISIBLE);
                webview.loadUrl("https://byscote.com/webapp/terms?uid=" + UUID + "&did=" + androidID);
                //webview.loadUrl("http://byscote.com/webapp/terms?uid=" + UUID + "&did=" + androidID);

            }
        });
    }

    public void requestUserData() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        JSONObject resp = response.getJSONObject();

                        Gson gson = new GsonBuilder().create();
                        user = gson.fromJson(resp.toString(), User.class);

                        callLogin();
                        //displayMessage(resp.toString());

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email,age_range,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    protected boolean isFacebookLoggedIn(){
        return AccessToken.getCurrentAccessToken() != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // return from facebook login procedure, if it's success and save logged in user's data
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void ApplyFontAwesome(View view) {
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.FONTAWESOME);
        FontManager.markAsIconContainer(view, iconFont);
    }

    protected void ApplyZawgyi(View view) {
        Typeface iconFont = FontManager.getTypeface(getApplicationContext(), FontManager.ZAWGYI);
        FontManager.markAsIconContainer(view, iconFont);
    }

    protected void displayMessage(String msg) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void callLogin() {


        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONObject jsonArray = null;

                        Intent intent = new Intent(LoginActivity.this, ByscoteMainActivity.class);
                        intent.putExtra("UserData", user);
                        startActivity(intent);
                        finish();
                        Log.i("Response", response);

//                        try {
//
//                            jsonArray = new JSONObject(response);
//
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
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

            /**
             * adding parameters to the request
             **/
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id",user.id);
                params.put("first_name",user.first_name );
                params.put("last_name",user.last_name );
                params.put("email",user.email );
                params.put("gender",user.gender );
                params.put("min_agerange",user.age_range.min );
                params.put("max_agerange",user.age_range.max );
                params.put("uid",androidID );
                params.put("androidid",UUID );

                Log.i("Parameters!", "parameters ... " + params);
                return params;
            }

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


    @Override
    public void onBackPressed() {
        if(bTncOpened) {
            webview.setVisibility(View.GONE);
        }else {
            finish();
        }
        return;
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}
