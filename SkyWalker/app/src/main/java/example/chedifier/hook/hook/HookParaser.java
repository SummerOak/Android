package example.chedifier.hook.hook;

import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by chedifier on 2016/10/15.
 */
public class HookParaser {

    private static final String TAG = "HookParaser";


    public static void parseAndHook(Class<?>... classes){

        Log.d(TAG,"parseAndHook");

        if(classes != null && classes.length>0){
            for(Class<?> c:classes){
                Method[] methods = c.getDeclaredMethods();
                if(methods != null && methods.length > 0){
                    for(Method method:methods){
                        if(method.isAnnotationPresent(HookAnnotation.class)){
                            parseWithClassAndParamsType(method,method.getAnnotation(HookAnnotation.class));
                        }else if(method.isAnnotationPresent(HookByDescriptor.class)){
                            parseWithDescriptor(method,method.getAnnotation(HookByDescriptor.class));
                        }
                    }
                }
            }
        }
    }

    private static void parseWithDescriptor(Method method,HookByDescriptor annotation){
        if(annotation != null && method != null){
            try {
                Class<?> targetClass = Class.forName(annotation.className());
                if(targetClass != null){
                    Method[] methods = targetClass.getDeclaredMethods();
                    if(methods != null){
                        for(Method m:methods)if(m.getName().equals(annotation.methodName())){
                            String desc = Utils.calculateMethodSignature(m);
                            if(desc != null && !desc.equals("") && desc.equals(annotation.methodDescriptor())){
                                Log.d(TAG,"tareget >>> " + annotation.methodName() + " " + annotation.methodDescriptor());
                                Hook.hookMethod(targetClass,annotation.methodName(),annotation.methodDescriptor(),Modifier.isStatic(m.getModifiers()),
                                        method.getDeclaringClass(),method.getName(),Utils.calculateMethodSignature(method), Modifier.isStatic(method.getModifiers()),
                                        annotation.hookType().ordinal());

                                return;
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static void parseWithClassAndParamsType(Method method,HookAnnotation hookAnnotation){
        if(hookAnnotation != null){
            if(hookAnnotation.targetClass() != null
                    && hookAnnotation.targetMethodName() != null){
                try {
                    Method target = hookAnnotation.targetClass().getDeclaredMethod(hookAnnotation.targetMethodName(),hookAnnotation.targetMethodParams());
                    if(target != null){
                        String methodName = target.getName();
                        String signature = Utils.calculateMethodSignature(target);
                        Log.d(TAG,"tareget >>> " + methodName + " " + signature);

                        Hook.hookMethod(hookAnnotation.targetClass(),methodName,signature,Modifier.isStatic(target.getModifiers()),
                                method.getDeclaringClass(),method.getName(),Utils.calculateMethodSignature(method), Modifier.isStatic(method.getModifiers()),
                                hookAnnotation.hookType().ordinal());
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
