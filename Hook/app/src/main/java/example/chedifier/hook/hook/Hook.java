package example.chedifier.hook.hook;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

import example.chedifier.hook.HookApplication;

/**
 * Created by chedifier on 2016/5/13.
 */
public class Hook {

    private static final String TAG = Hook.class.getSimpleName();

    static {
        System.loadLibrary("chedifierHookLib");
    }

    public static HookResult hookMethod(Class c,String name,String sig,boolean isStatic,
                                        Class proxyC,String proxyName,String proxySig,boolean isProxyStatic){
        Log.d(TAG,"hookMethod > class: " + c.getName() + " method: " + name
        + " to: " + "class: " + proxyC.getName() + " method:" + proxyName);

        int ret = hookMethodNative(c, name, sig,isStatic,
                proxyC,proxyName,proxySig,isProxyStatic);
        if(ret == 0){
            return HookResult.SUCCESS;
        }

        return HookResult.FAILED;
    }

    private static native int hookMethodNative(Class c,String name,String sig,boolean isStatic,
                                               Class proxyC,String proxyName,String proxySig,boolean isProxyStatic);

}
