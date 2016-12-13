package example.chedifier.chedifier.test;

import android.util.Log;

import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookType;

/**
 * Created by chedifier on 2016/12/7.
 */
public class ThreadProxy {

    private static final String TAG = "ThreadProxy";

    @HookAnnotation(
            targetClass = Thread.class,
            targetMethodName = "run",
            targetMethodParams = {},
            hookType = HookType.BEFORE_TARGET)
    public void before_run(){

        Object o = this;
        Thread t = (Thread)o;

        Log.d(TAG,"before_run " + t.getId() + "  " + t.getName());

        return;
    }

    @HookAnnotation(
            targetClass = Thread.class,
            targetMethodName = "run",
            targetMethodParams = {},
            hookType = HookType.POST_TARGET)
    public void post_run(){

        Object o = this;
        Thread t = (Thread)o;

        Log.d(TAG,"post_run " + t.getId() + "  " + t.getName());

        return;
    }

}
