package example.chedifier.hook.testproxy;

import android.util.Log;

import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookType;

/**
 * Created by wxj_pc on 2016/10/29.
 */
public class MyClassloaderProxy {

    private static final String TAG = "MyClassloaderProxy";

    private static boolean isRunningThis = false;
    @HookAnnotation(
            targetClass = ClassLoader.class,
            targetMethodName = "loadClass",
            targetMethodParams = {String.class},
            hookType = HookType.BEFORE_TARGET)
    public void loadClass(String className){
        Log.d(TAG,"loadClass " + className);
//        if(isRunningThis){
//            isRunningThis = false;
//            return;
//        }


//        Object o = this;
//        ClassLoader classLoader = (ClassLoader)o;
//
//        return classLoader.loadClass(className);
    }
}
