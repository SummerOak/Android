package example.chedifier.hook.hook;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

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

    public static HookResult setHookEnable(boolean isProxyStatic,int hookType,boolean enable,Class proxyC,String proxyName,Class<?>... params){
        Log.d(TAG,"setHookEnable > " + "enable: " + enable + " class: " + proxyC.getName() + " method:" + proxyName);

        try {
            String signature = calculateMethodSignature(proxyC.getDeclaredMethod(proxyName,params));
            Log.d(TAG,"signature: " + signature);
            int ret = setHookEnableNative(proxyC,proxyName,signature,isProxyStatic,hookType,enable);
            if(ret == 0){
                Log.d(TAG,"setHookEnable success.");
                return HookResult.SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG,"setHookEnable failed.");
        return HookResult.FAILED;
    }

    private static final String calculateMethodSignature(Method method){
        Log.d(TAG,"calculateMethodSignature " + method);

        String signature = "";
        if(method != null){
            signature += "(";
            for(Class<?> c:method.getParameterTypes()){
                String Lsig = Array.newInstance(c,1).getClass().getName();
                signature += Lsig.substring(1);
            }
            signature += ")";

            Class<?> returnType = method.getReturnType();
            if(returnType == void.class){
                signature += "V";
            }else{
                signature += Array.newInstance(returnType,1).getClass().getName().substring(1);
            }

            signature = signature.replace('.','/');
        }

        return signature;
    }

    private static native int hookMethodNative(Class c,String name,String sig,boolean isStatic,
                                               Class proxyC,String proxyName,String proxySig,boolean isProxyStatic,
                                               int hookType);

    private static native int setHookEnableNative(Class proxyC,String proxyName,String proxySig,boolean isProxyStatic,
                                               int hookType,boolean enable);

}
