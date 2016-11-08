package example.chedifier.hook.testproxy;

import android.util.Log;

import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookType;
import example.chedifier.hook.test.Class1;

/**
 * Created by wxj_pc on 2016/10/29.
 */
public class MyClassloaderProxy {

    private static final String TAG = "MyClassloaderProxy";

    @HookAnnotation(
            targetClass = ClassLoader.class,
            targetMethodName = "loadClass",
            targetMethodParams = {String.class},
            hookType = HookType.BEFORE_TARGET)
    public void loadClass(String className){
        Log.d(TAG,"loadClass " + className);

//        Object o = this;
//        ClassLoader classLoader = (ClassLoader)o;
//
//        Class<?> ret = null;
//        try {
//            Hook.setHookEnable(false,HookType.REPLACE_TARGET.ordinal(),false,MyClassloaderProxy.class,"loadClass",String.class);
//            Log.d(TAG,"result " + 1);
//            ret = classLoader.loadClass(className);
//            Hook.setHookEnable(false,HookType.REPLACE_TARGET.ordinal(),true,MyClassloaderProxy.class,"loadClass",String.class);
//            Log.d(TAG,"result " + 2);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return ret;
    }


    @HookAnnotation(
            targetClass = Class1.class,
            targetMethodName = "func1",
            targetMethodParams = {String.class},
            hookType = HookType.BEFORE_TARGET)
    public void func1(String className){
        Log.d(TAG,"loadClass " + className);
    }
}
