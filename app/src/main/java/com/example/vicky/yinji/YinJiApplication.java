package com.example.vicky.yinji;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.os.Bundle;

import com.bumptech.glide.load.engine.Resource;

/**
 * Dedicated only to my lover
 * HuZhanYun
 */
public class YinJiApplication extends Application {
    //获取顶部Activity
    private static YinJiApplication mApp;
    private static Activity mActivity;
    @Override
    public void onCreate() {
        super.onCreate();
        mApp=this;
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                mActivity=activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
    public static Application getApplication(){
        return mApp;
    }

    public static Activity getActivity(){
        return mActivity;
    }

    public static Resources getAppResources(){
        return mApp.getResources();
    }
}
