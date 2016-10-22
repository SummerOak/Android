package example.chedifier.hook.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import example.chedifier.hook.HookApplication;
import example.chedifier.hook.MainActivity;

/**
 * Created by chedifier on 2016/5/14.
 */
public class HookProxyMethod {

    @HookAnnotation(
            targetClass = MainActivity.class,
            methodName = "testSimple",
            params = {int.class})
    public void testHookProxySimple(int i){
        Log.d("chedifier_hook","testHookProxySimple");
    }

    @HookAnnotation(
            targetClass = MainActivity.class,
            methodName = "testCall",
            params = {int.class,int.class,int.class,int.class,int.class,
                    String.class,String.class,
                    byte.class,byte.class,
                    char.class,char.class,
                    float.class,float.class})
    public static void testHookProxy(int i,int i1,int i2,int i3,int i4,String s,String s1,byte b,byte b1,char c,char c1,float f,float f1){
        Log.d("chedifier_hook","testHookProxy!");
        Toast.makeText(HookApplication.getAppContext(),
                "testHookProxy >>> \n"
                        + "  i=" + i + " i1=" + i1 + " i2=" + i2 + " i3=" + i3 + " i4=" + i4 + "\n"
                        + "  s=" + s + " s1=" + s1 + "\n"
                        + "  b=" + b + " b1=" + b1 + "\n"
                        + "  c=" + c + " c1=" + c1 + "\n"
                        + "  f=" + f + " f1=" + f1
                , Toast.LENGTH_LONG).show();
    }

    @HookAnnotation(targetClass = MainActivity.class,
            methodName = "testCall2",
            params = {String[].class})
    public void testHookProxy2(){

    }

//    @HookAnnotation(
//            targetClass = MainActivity.class,
//            methodName = "testPrivData",
//            params = {Object.class})

    @HookByDescriptor(
            className = "example.chedifier.hook.MainActivity",
            methodName = "testPrivData",
            methodDescriptor = "(Lexample/chedifier/hook/MainActivity$PrivData;)V"
    )
    public void testPrivData(Object data) {

        int i1 = 0;
        String s1 = "";
        try {
            Class cPrivData = Class.forName("example.chedifier.hook.MainActivity$PrivData");
            Field field = cPrivData.getDeclaredField("i1");
            i1 = (int) field.get(data);
            field = cPrivData.getDeclaredField("s1");
            s1 = (String)field.get(data);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        Toast.makeText(HookApplication.getAppContext(),
                "testPrivData >>> Hooked\n"
                        + "  data.i1=" + i1
                        + "  data.s1=" + s1
                , Toast.LENGTH_LONG).show();
        return;
    }

    @HookAnnotation(
            targetClass = TextView.class,
            methodName = "setText",
            params = {CharSequence.class})
    public void setText(String content){
        Toast.makeText(HookApplication.getAppContext(),
                "setText >>> Hooked\n"
                        + "  content=" + content
                , Toast.LENGTH_LONG).show();

        return;
    }

//    @HookAnnotation(
//            targetClass = Handler.class,
//            methodName = "dispatchMessage",
//            params = {Message.class})
//    public void dispatchMessage(Message msg){
//        Log.d("cqx","dispatchMessage >>> Hooked\n"
//                        + "  content=" + msg);
//        return;
//    }

    @HookAnnotation(
            targetClass = Choreographer.class,
            methodName = "doFrame",
            params = {long.class,int.class})
    public void doFrame(long frameTimeNanos, int frame){
        Log.d("cqx","doFrame >>> Hooked\n"
                        + "  frameTimeNanos=" + frameTimeNanos
                        + "  frame=" + frame);

        return;
    }

    @HookAnnotation(
            targetClass = View.class,
            methodName = "invalidateInternal",
            params = {int.class,int.class,int.class,int.class,boolean.class,boolean.class})
    public void invalidateInternal(int l, int t, int r, int b, boolean invalidateCache,
                       boolean fullInvalidate){
        Log.d("cqx","invalidateInternal >>> Hooked\n"
                + "  l=" + l
                + "  t=" + t
                + "  r=" + b
                + "  invalidateCache=" + invalidateCache
                + "  fullInvalidate=" + fullInvalidate);

        return;
    }

    @HookAnnotation(
            targetClass = Activity.class,
            methodName = "startActivity",
            params = {Intent.class})
    public void startActivity(Intent intent){
        Log.d("cqx","startActivity >>> Hooked\n"
                + "  intent=" + intent);

        intent.setClass(HookApplication.getAppContext(),HookActivity.class);

        return;
    }

}
