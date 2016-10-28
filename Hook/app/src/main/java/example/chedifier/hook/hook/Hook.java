package example.chedifier.hook.hook;

import android.util.Log;

/**
 * Created by chedifier on 2016/5/13.
 */
public class Hook {

    private static final String TAG = Hook.class.getSimpleName();

    static {
        System.loadLibrary("chedifier_art_method");
    }

    public static HookResult hookMethod(Class c,String name,String sig,boolean isStatic,
                                        Class proxyC,String proxyName,String proxySig,boolean isProxyStatic,int hookType){
        Log.d(TAG,"hookMethod > class: " + c.getName() + " method: " + name
        + " to: " + "class: " + proxyC.getName() + " method:" + proxyName);

        int ret = hookMethodNative(c, name, sig,isStatic,
                proxyC,proxyName,proxySig,isProxyStatic,hookType);
        if(ret == 0){
            Log.d(TAG,"hook success.");
            return HookResult.SUCCESS;
        }

        Log.d(TAG,"hook failed.");
        return HookResult.FAILED;
    }

    private static native int hookMethodNative(Class c,String name,String sig,boolean isStatic,
                                               Class proxyC,String proxyName,String proxySig,boolean isProxyStatic,
                                               int hookType);

    private static native int hookMethodNativeWithLiBieGou(Class c,String name,String sig,boolean isStatic,
                                                           Class proxyC,String proxyName,String proxySig,boolean isProxyStatic);
}
