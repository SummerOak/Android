package example.chedifier.hook.hook;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by chedifier on 2016/10/15.
 */
public class HookParaser {

    private static final String TAG = "HookParaser";

    public static void parseAndHook(){

        Log.d(TAG,"parseAndHook");

        Method[] methods = HookProxyMethod.class.getDeclaredMethods();
        if(methods != null && methods.length > 0){
            for(int i=0;i<methods.length;i++){
                Method method = methods[i];
                HookAnnotation hookAnnotation = method.getAnnotation(HookAnnotation.class);
                if(hookAnnotation != null){
                    if(hookAnnotation.targetClass() != null
                            && hookAnnotation.methodName() != null){
                        try {
                            Method target = hookAnnotation.targetClass().getDeclaredMethod(hookAnnotation.methodName(),hookAnnotation.params());
                            if(target != null){
                                String methodName = target.getName();
                                String signature = calculateMethodSignature(target);
                                Log.d(TAG,"tareget >>> " + methodName + " " + signature);

                                Hook.hookMethod(hookAnnotation.targetClass(),methodName,signature,Modifier.isStatic(target.getModifiers()),
                                        method.getDeclaringClass(),method.getName(),calculateMethodSignature(method), Modifier.isStatic(method.getModifiers()));
                            }
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }

        }
    }


    private static String calculateMethodSignature(Method method){
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
                signature += Array.newInstance(returnType,1).getClass().getName();
            }

            signature = signature.replace('.','/');
        }

        return signature;
    }

}
