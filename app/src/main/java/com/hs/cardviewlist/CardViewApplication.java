package com.hs.cardviewlist;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import com.activeandroid.ActiveAndroid;

/**
 * Created by mt0013 on 06/08/15.
 */
public class CardViewApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
