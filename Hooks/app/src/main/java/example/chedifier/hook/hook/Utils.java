package example.chedifier.hook.hook;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Method;

/**
 * Created by wxj_pc on 2016/10/29.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static final String calculateMethodSignature(Method method){
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
}
