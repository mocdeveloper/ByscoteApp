package com.moc.byscote;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Surface;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.drm.UnsupportedDrmException;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.RandomTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.moc.byscote.adapter.SeriesListAdapter;
import com.moc.byscote.adapter.SeriesListAdapter2;
import com.moc.byscote.adapter.SeriesListAdapter3;
import com.moc.byscote.adapter.SeriesListAdapter4;
import com.moc.byscote.adapter.SeriesListAdapter5;
import com.moc.byscote.model.SeriesList;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PlayerActivity extends AppCompatActivity {

    PlayerView playerView;
    SimpleExoPlayer player;
    int currentWindow;
    long playbackPosition;
    boolean playWhenReady;
    private static DefaultBandwidthMeter BANDWIDTH_METER;
    ComponentListener componentListener;
    String mockey, policy, signature, key_pair_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mockey = getIntent().getStringExtra("mockey");
        policy = getIntent().getStringExtra("policy");
        signature = getIntent().getStringExtra("signature");
        key_pair_id = getIntent().getStringExtra("key_pair_id");


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        BANDWIDTH_METER = new DefaultBandwidthMeter();
        playerView = findViewById(R.id.video_view);
        componentListener = new ComponentListener();

    }

    private void initializePlayer() {


        //  player = ExoPlayerFactory.newSimpleInstance(this);

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new
                AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new
                DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(PlayerActivity.this, trackSelector, loadControl);

        playerView.setPlayer(player);
        Uri uri = Uri.parse(getString(R.string.video_url));
        HlsMediaSource mediaSource = buildMediaSource(uri);

        player.addListener(componentListener);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
        player.addVideoDebugListener(componentListener);
        player.addAudioDebugListener(componentListener);


        player.prepare(mediaSource, true, false);

    }


    private HlsMediaSource buildMediaSource(Uri uri) {


        //Cookie Manager
//        Map<String, List<String>> header = new HashMap<>();
//        List<String> value = new ArrayList<>();
//        value.add("3c7b6b4d56ef0ea221778ba75c111000f3a3b698280f94a8ac9224e4230fec61");
//        header.put("mockey",value);
//        URI u1 = URI.create(getResources().getString(R.string.video_url));
//
//        Map<String, List<String>> header2 = new HashMap<>();
//        List<String> value2 = new ArrayList<>();
//        value2.add("eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly9jZG4uYnlzY290ZS5jb20vKiIsIkNvbmRpdGlvbiI6eyJEYXRlTGVzc1RoYW4iOnsiQVdTOkVwb2NoVGltZSI6MTU0NDYwMTM5MH19fV19");
//        header2.put("CloudFront-Policy",value2);
//
//        Map<String, List<String>> header3 = new HashMap<>();
//        List<String> value3 = new ArrayList<>();
//        value3.add("bIh5dwnvplao8HI8abDcmR2e~U16YfsntkqwuuELISHFZ8yf8Y1m~xrd0ePLgpQR-N7Kf25Hr-H8KWLNcvDtNHiNXQhDzyaRIudmnmjXooxqo20mN0q7teWku7e9T-VFAgO5Ket6uOgeRsGEsI6O-crZmLOFeM-C~RAc-2rl565FbvGx90LJiv5aJ6ou7oBXwPmNlAbaw4oNH9qTXV2hR4mCBUbcov7IBd3Obi5vAeBlbahYILvl8-6eVUstHjlDqfYqiGwDFZdCFgDeKIBRD26x~A1juk3QobO8MObUF1evqxWHut6DVqRqm6Rr8mzg6ts4R4HCAS6KMkVvH45TUA__");
//        header3.put("CloudFront-Signature:",value3);
//
//        Map<String, List<String>> header4 = new HashMap<>();
//        List<String> value4 = new ArrayList<>();
//        value4.add("APKAI6UWMU4FMDXMV6EA");
//        header4.put("CloudFront-Key-Pair-Id",value4);
//
//
//        CookieManager DEFAULT_COOKIE_MANAGER = new CookieManager();
//        try {
//            DEFAULT_COOKIE_MANAGER.put(u1, header);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            DEFAULT_COOKIE_MANAGER.put(u1, header2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            DEFAULT_COOKIE_MANAGER.put(u1, header3);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            DEFAULT_COOKIE_MANAGER.put(u1, header4);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        CookieHandler.setDefault(DEFAULT_COOKIE_MANAGER);
//        return new DefaultHttpDataSource.Factory(
//                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
//                createMediaSource(uri);


        //DefaultHttpDataSource
//        DefaultHttpDataSource defaultHttpDataSource = new DefaultHttpDataSource(Util.getUserAgent(PlayerActivity.this,"ByscoteApp"), null, null);
//      //  defaultHttpDataSource.setRequestProperty("mockey", "3c7b6b4d56ef0ea221778ba75c111000f3a3b698280f94a8ac9224e4230fec61");
////        defaultHttpDataSource.setRequestProperty("CloudFront-Policy" ,"eyJTdGF0ZW1lbnQiOlt7IlJlc291cmNlIjoiaHR0cHM6Ly9jZG4uYnlzY290ZS5jb20vKiIsIkNvbmRpdGlvbiI6eyJEYXRlTGVzc1RoYW4iOnsiQVdTOkVwb2NoVGltZSI6MTU0NDYwMTM5MH19fV19 " );
////        defaultHttpDataSource.setRequestProperty("CloudFront-Signature" , "EN9XnCnBa5U9qRC98KLK2cLRjdWDtg2E5EDGeZa~KxewYrXKA5niyZgYEV4hKDF-QyDLdO74C6ksjRzSDVQppEiTL1W-YU-gG04jJjv8OaBATUiBgcFgunpwp~c9f-sjF8UtoVUu5OtbTb1D94p0-Qei9YaoY5kQ1PHYpFj7W17ECtsTZ4BoT5A-IYQVMuSCp6LoUIjSZDWc5HCcVFBA3BGQECSNUcPxc1MgE2Q2D5BYVezbDOAtmFt~ZCPeqPpBSUkmntvHOMywzMIO08LPD17RIAgKZQkCR3RIA2~GWg595wxz1O5T~RO-52o5WDP8M20SiCZL-8m6CqsDviEDcA__");
////        defaultHttpDataSource.setRequestProperty("CloudFront-Key-Pair-Id", "APKAI6UWMU4FMDXMV6EA");
//
//        defaultHttpDataSource.setRequestProperty("mockey", mockey);
//        defaultHttpDataSource.setRequestProperty("CloudFront-Policy" ,policy );
//        defaultHttpDataSource.setRequestProperty("CloudFront-Signature" , signature);
//        defaultHttpDataSource.setRequestProperty("CloudFront-Key-Pair-Id", key_pair_id);
//
//
//        DefaultHttpDataSourceFactory httpDataSourceFactory = new DefaultHttpDataSourceFactory(Util.getUserAgent(PlayerActivity.this,"ByscoteApp"), null, defaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, defaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true);
//        httpDataSourceFactory.setDefaultRequestProperty("mockey", mockey);
//        httpDataSourceFactory.setDefaultRequestProperty("CloudFront-Policy" ,policy );
//        httpDataSourceFactory.setDefaultRequestProperty("CloudFront-Signature" , signature);
//        httpDataSourceFactory.setDefaultRequestProperty("CloudFront-Key-Pair-Id", key_pair_id);
//
//        Log.i("mockey :",mockey);
//        Log.i("policy :",policy);
//        Log.i("signature :",signature);
//        Log.i("key pair id :",key_pair_id);
//
//
//
//      //  DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(PlayerActivity.this, null, httpDataSourceFactory);
//        httpDataSourceFactory.createDataSource();
//
//        HlsMediaSource mVideoSource = new HlsMediaSource(uri, httpDataSourceFactory, 1, null, null);

        Handler mainHandler = new Handler();
        HlsMediaSource hlsMediaSource = null;
        // 1. Create a default TrackSelector

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DefaultHttpDataSourceFactory MGSource = new DefaultHttpDataSourceFactory(
                Util.getUserAgent(PlayerActivity.this, "ByscoteApp"), null);
              //  DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS, true );

        String cookieValue = "mockey="+mockey+";CloudFront-Policy="+policy+
                ";CloudFront-Signature="+signature+
                ";CloudFront-Key-Pair-Id="+key_pair_id;

        MGSource.setDefaultRequestProperty("Cookie", cookieValue);

        Log.i("mockey :", mockey);
        Log.i("policy :", policy);
        Log.i("signature :", signature);
        Log.i("key pair id :", key_pair_id);
        Log.i("Url :", uri.toString());

        // This is the MediaSource representing the media to be played. new AdaptiveMediaSourceEventListener()
        hlsMediaSource = new HlsMediaSource(uri, MGSource, mainHandler, new AdaptiveMediaSourceEventListener() {


            @Override
            public void onMediaPeriodCreated(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

            }

            @Override
            public void onMediaPeriodReleased(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

            }

            @Override
            public void onLoadStarted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

            }

            @Override
            public void onLoadCompleted(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

            }

            @Override
            public void onLoadCanceled(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData) {

            }

            @Override
            public void onLoadError(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, LoadEventInfo loadEventInfo, MediaLoadData mediaLoadData, IOException error, boolean wasCanceled) {

            }

            @Override
            public void onReadingStarted(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {

            }

            @Override
            public void onUpstreamDiscarded(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

            }

            @Override
            public void onDownstreamFormatChanged(int windowIndex, @Nullable MediaSource.MediaPeriodId mediaPeriodId, MediaLoadData mediaLoadData) {

            }

        });
        return hlsMediaSource;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private class ComponentListener extends Player.DefaultEventListener implements
            VideoRendererEventListener, AudioRendererEventListener {

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            String stateString;
            switch (playbackState) {
                case Player.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    break;
                case Player.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    break;
                case Player.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    break;
                case Player.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    break;
            }
            Log.d("TAG", "changed state to " + stateString + " playWhenReady: " + playWhenReady);
        }

        // Implementing VideoRendererEventListener.

        @Override
        public void onVideoEnabled(DecoderCounters counters) {
            // Do nothing.
        }

        @Override
        public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
            // Do nothing.
        }

        @Override
        public void onVideoInputFormatChanged(Format format) {
            // Do nothing.
        }

        @Override
        public void onDroppedFrames(int count, long elapsedMs) {
            // Do nothing.
        }

        @Override
        public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
            // Do nothing.
        }

        @Override
        public void onRenderedFirstFrame(Surface surface) {
            // Do nothing.
        }

        @Override
        public void onVideoDisabled(DecoderCounters counters) {
            // Do nothing.
        }

        // Implementing AudioRendererEventListener.

        @Override
        public void onAudioEnabled(DecoderCounters counters) {
            // Do nothing.
        }

        @Override
        public void onAudioSessionId(int audioSessionId) {
            // Do nothing.
        }

        @Override
        public void onAudioDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {
            // Do nothing.
        }

        @Override
        public void onAudioInputFormatChanged(Format format) {
            // Do nothing.
        }

        @Override
        public void onAudioSinkUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
            // Do nothing.
        }

        @Override
        public void onAudioDisabled(DecoderCounters counters) {
            // Do nothing.
        }

    }


}
