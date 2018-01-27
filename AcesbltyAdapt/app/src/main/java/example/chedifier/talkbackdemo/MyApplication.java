package example.chedifier.talkbackdemo;

import android.app.Application;
import android.content.Context;

import example.chedifier.hook.hook.HookParaser;


/**
 * Created by chedifier on 2016/6/7.
 */
public class MyApplication extends Application {

    private final String TAG = "MyApplication";

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        HookParaser.parseAndHook(HookTalkbackProxy.class);

        sAppContext = this;
    }

    public static Context getAppContext(){
        return sAppContext;
    }

}
