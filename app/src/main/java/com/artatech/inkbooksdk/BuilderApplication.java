package com.artatech.inkbooksdk;

import android.app.Application;

import com.artatech.dictsdk.DictSDK;
import com.artatech.sdk.InkBookSDK;

/**
 * Created by Goltstein on 21.12.16.
 */

public class BuilderApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DictSDK.init(this,true);

        //uncomment if you want to use Chromium
        InkBookSDK.addChromiumSupport(this);
    }
}
