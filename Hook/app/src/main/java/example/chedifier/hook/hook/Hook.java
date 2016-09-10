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

        hookMethodNative(c, name, sig,isStatic,
                proxyC,proxyName,proxySig,isProxyStatic);

        return HookResult.FAILED;
    }

    public void setText(CharSequence content){
        Toast.makeText(HookApplication.getAppContext(), content, Toast.LENGTH_SHORT).show();
    }

    public Object hookProxy(Object... parms){
        Log.d(TAG,"hookProxy");

        if(parms != null){
            Log.d(TAG,"parms.length" + parms.length);

        }



        return null;
    }

    private static native int hookMethodNative(Class c,String name,String sig,boolean isStatic,
                                               Class proxyC,String proxyName,String proxySig,boolean isProxyStatic);

}
