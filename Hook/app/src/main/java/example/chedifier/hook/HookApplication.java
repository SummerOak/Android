package example.chedifier.hook;

import android.app.Application;
import android.content.Context;

import example.chedifier.hook.hook.HookParaser;
import example.chedifier.hook.testproxy.HookProxyMethod;
import example.chedifier.hook.testproxy.MyClassloaderProxy;
import example.chedifier.hook.testproxy.MyHandlerProxy;
import example.chedifier.hook.testproxy.MyTextViewProxy;

/**
 * Created by chedifier on 2016/5/13.
 */
public class HookApplication extends Application {

    private static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        HookParaser.parseAndHook(
//                HookProxyMethod.class,
                MyTextViewProxy.class
//                MyHandlerProxy.class,
//                MyClassloaderProxy.class
        );

        sAppContext = this;
    }

    public static Context getAppContext(){
        return sAppContext;
    }
}
