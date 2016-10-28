package example.chedifier.hook.testproxy;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import example.chedifier.hook.HookApplication;
import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookType;

/**
 * Created by chedifier on 2016/10/28.
 */
public class MyTextViewProxy {

    private static boolean runThis = false;
    @HookAnnotation(
            targetClass = TextView.class,
            targetMethodName = "setText",
            targetMethodParams = {CharSequence.class},
            hookType = HookType.POST_TARGET)
    public void setText(String content){

        if(!runThis){
            runThis = true;

            Object thiz = this;
            TextView textView = (TextView)thiz;
            textView.setText("hooked anyway");

            Log.d("cqx","" + textView);

            Toast.makeText(HookApplication.getAppContext(),
                    "setText >>> Hooked\n"
                            + "  content=" + content
                    , Toast.LENGTH_LONG).show();
        }else{
            runThis = false;
        }

        return;
    }

}
