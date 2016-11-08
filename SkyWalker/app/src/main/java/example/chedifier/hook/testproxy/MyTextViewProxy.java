package example.chedifier.hook.testproxy;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import example.chedifier.hook.HookApplication;
import example.chedifier.hook.hook.Hook;
import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookParaser;
import example.chedifier.hook.hook.HookResult;
import example.chedifier.hook.hook.HookType;
import example.chedifier.hook.ptrace.PTrace;

/**
 * Created by chedifier on 2016/10/28.
 */
public class MyTextViewProxy {
    private static final String TAG = "MyTextViewProxy";

    protected static void prepare(){
        Log.d(TAG,"MyTextViewProxy prepare ");
    }

    private static boolean runThis = false;
    @HookAnnotation(
            targetClass = TextView.class,
            targetMethodName = "setText",
            targetMethodParams = {CharSequence.class},
            hookType = HookType.REPLACE_TARGET)
    public void setText(String content){
        if(true){
            runThis = true;

            HookResult result = Hook.setHookEnable(false,HookType.REPLACE_TARGET.ordinal(),false,MyTextViewProxy.class,"setText",String.class);
            Object thiz = this;
            TextView textView = (TextView)thiz;
            textView.setText("hooked anyway");

            result = Hook.setHookEnable(false,HookType.REPLACE_TARGET.ordinal(),true,MyTextViewProxy.class,"setText",String.class);

            Log.d("cqx","" + textView);

            Toast.makeText(HookApplication.getAppContext(),
                    "setText >>> Hooked\n"
                            + "  content=" + content
                    , Toast.LENGTH_LONG).show();

//            PTrace.pTrace(212);

        }else{
            runThis = false;
        }

        return;
    }


//    @HookAnnotation(
//            targetClass = TextView.class,
//            targetMethodName = "setText",
//            targetMethodParams = {CharSequence.class},
//            hookType = HookType.REPLACE_TARGET)
//    public void setText(String content){
//
//        if(Hook.setHookEnable(false,HookType.REPLACE_TARGET.ordinal(),false,getClass(),"setText",String.class) == HookResult.SUCCESS){
//            Object thiz = this;
//            TextView textView = (TextView)thiz;
//            textView.setText("hooked anyway");
//
//            HookResult result = Hook.setHookEnable(false,HookType.REPLACE_TARGET.ordinal(),true,getClass(),"setText",String.class);
//            Log.d(TAG,"rehook " + (result==HookResult.SUCCESS?"success":"failed"));
//        }
//
//        return;
//    }

}
