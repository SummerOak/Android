package example.chedifier.hook;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import example.chedifier.hook.hook.Hook;
import example.chedifier.hook.hook.HookParaser;
import example.chedifier.hook.hook.HookProxyMethod;

/**
 * Created by chedifier on 2016/5/13.
 */
public class HookApplication extends Application {

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        HookParaser.parseAndHook();

        sAppContext = this;
    }

    public static Context getAppContext(){
        return sAppContext;
    }
}
