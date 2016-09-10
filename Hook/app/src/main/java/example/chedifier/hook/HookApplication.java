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
import example.chedifier.hook.hook.HookProxyMethod;

/**
 * Created by chedifier on 2016/5/13.
 */
public class HookApplication extends Application {

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

//        MainActivity.toast(this, "init static from application!");

//        Hook.hookMethod(MainActivity.class, "toast", "(Ljava/lang/String;)V", false,
//                MainActivity.class, "toastHooked", "(Ljava/lang/String;)V", false);

//        Hook.hookMethod(MainActivity.class,"toast","(Landroid/content/Context;Ljava/lang/String;)V",true,
//                MainActivity.class,"toastHooked","(Landroid/content/Context;Ljava/lang/String;)V",true);

        Hook.hookMethod(MainActivity.class,"test","(ILjava/lang/String;BC)V",false,
                MainActivity.class,"testHooked","(I)V",false);

//        Hook.hookMethod(MainActivity.class,"toast","(Landroid/content/Context;Ljava/lang/String;)V",true,
//                MainActivity.class,"toastHooked","(Ljava/lang/String;)V", false);

//        Hook.hookMethod(TextView.class,"setText","(Ljava/lang/CharSequence;)V",false,
//                HookProxyMethod.class,"setText","(Ljava/lang/CharSequence;)V",false);

//        Hook.hookMethod(ActivityManager.class,"forceStopPackage","(Ljava/lang/String;)V",false,
//                MainActivity.class,"logcatHook","(Ljava/lang/String;)V", false);

//        try {
//            Class c = Class.forName("com.android.server.am.ActivityManagerService");
//
//
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }


        sAppContext = this;
    }

    public static Context getAppContext(){
        return sAppContext;
    }
}
