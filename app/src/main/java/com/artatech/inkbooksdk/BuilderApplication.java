package com.artatech.inkbooksdk;

import android.app.Application;

import com.artatech.dictsdk.DictSDK;

/**
 * Created by Goltstein on 21.12.16.
 */

public class BuilderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DictSDK.init(this,true);
    }
}
