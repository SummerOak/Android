package example.chedifier.hook.testproxy;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookParaser;
import example.chedifier.hook.hook.HookType;

/**
 * Created by wxj_pc on 2016/10/29.
 */
public class MyHandlerProxy{

    @HookAnnotation(
            targetClass = Handler.class,
            targetMethodName = "dispatchMessage",
            targetMethodParams = {Message.class},
            hookType = HookType.POST_TARGET)
    public void dispatchMessage(Message msg){
        Log.d("cqx","dispatchMessage >>> Hooked\n"
                + "  content=" + msg);
        return;
    }

    @HookAnnotation(
            targetClass = HookParaser.class,
            targetMethodName = "parseAndHook",
            targetMethodParams = {Class [].class},
            hookType = HookType.POST_TARGET)
    public void dispatchMessage(Class<?>... classes){
        Log.d("cqx","dispatchMessage >>> Hooked\n"
                + "  content=" + classes);
        return;
    }

}
