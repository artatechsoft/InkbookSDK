package com.artatech.inkbooksdk;

import android.app.Application;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

/**
 * Created by Goltstein on 21.12.16.
 */

public class BuilderApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //uncomment if you want to use Chromium
        try {
            InkBookSDK.Companion.addChromiumSupport(this, true);
        } catch (Exception e){
            Log.e("BuilderApplication", "", e);
        }
    }
}
