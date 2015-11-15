package ru.fate.newsviewer.application;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import ru.fate.newsviewer.cache.Cache;

/**
 * Created by i4484 on 15.11.2015.
 */
public class ApplicationExtended extends Application {
    @Override
    public void onCreate (){
        super.onCreate();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));

        Cache.getInstance().init(getApplicationContext());
    }
}
