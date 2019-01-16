package com.moc.byscote;

import android.app.Application;
import android.content.Context;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ByscoteApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/noto_serif_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


}
