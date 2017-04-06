package example.chedifier.chedifier.test;

import android.util.Log;
import android.view.Choreographer;

import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookByDescriptor;
import example.chedifier.hook.hook.HookType;

/**
 * Created by chedifier on 2016/12/19.
 */
public class Hooks {

    private static final String TAG = "skywalker_hooks";

    @HookAnnotation(
            targetClass = Choreographer.class,
            targetMethodName = "scheduleFrameLocked",
            targetMethodParams = {long.class},
            hookType = HookType.BEFORE_TARGET)
    public void scheduleFrameLocked(){
        Log.d(TAG,"before_run scheduleFrameLocked");

        return;
    }

    @HookByDescriptor(
            className = "android.view.Choreographer$FrameDisplayEventReceiver",
            methodName = "onVsync",
            methodDescriptor = "(JII)V",
            hookType = HookType.BEFORE_TARGET)
    public void onVsync(){
        Log.d(TAG,"before_run onVsync",new Throwable());

        return;
    }

}
