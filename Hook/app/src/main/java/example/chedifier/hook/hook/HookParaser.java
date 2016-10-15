package example.chedifier.hook.hook;

import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
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

                                Hook.hookMethod(hookAnnotation.targetClass(),methodName,signature,hookAnnotation.isStatic(),
                                        method.getDeclaringClass(),method.getName(),calculateMethodSignature(method),hookAnnotation.isStatic());
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


//        Hook.hookMethod(MainActivity.class,"testCall","(IIIIILjava/lang/String;Ljava/lang/String;BBCCFF)V",false,MainActivity.class,"testHooked","(I)V",false);

//        Hook.hookMethod(MainActivity.class, "toast", "(Ljava/lang/String;)V", false,
//                MainActivity.class, "toastHooked", "(Ljava/lang/String;)V", false);

//        Hook.hookMethod(MainActivity.class,"toast","(Landroid/content/Context;Ljava/lang/String;)V",true,
//                MainActivity.class,"toastHooked","(Landroid/content/Context;Ljava/lang/String;)V",true);

//        Hook.hookMethod(MainActivity.class,"toast","(Landroid/content/Context;Ljava/lang/String;)V",true,
//                MainActivity.class,"toastHooked","(Ljava/lang/String;)V", false);

//        Hook.hookMethod(TextView.class,"setText","(Ljava/lang/CharSequence;)V",false,
//                HookProxyMethod.class,"setText","(Ljava/lang/CharSequence;)V",false);

//        Hook.hookMethod(ActivityManager.class,"forceStopPackage","(Ljava/lang/String;)V",false,
//                MainActivity.class,"logcatHook","(Ljava/lang/String;)V", false);

}
