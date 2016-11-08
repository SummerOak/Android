package example.chedifier.chedifier.test;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Choreographer;
import android.widget.Toast;

import java.lang.reflect.Field;

import example.chedifier.chedifier.MyApplication;
import example.chedifier.hook.HookActivity;
import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookByDescriptor;

/**
 * Created by chedifier on 2016/5/14.
 */
public class HookProxyMethod {

    @HookAnnotation(
            targetClass = SkyWalkerTestActivity.class,
            targetMethodName = "testSimple",
            targetMethodParams = {int.class})
    public void testHookProxySimple(int i){
        Log.d("chedifier_hook","testHookProxySimple");
        Toast.makeText(MyApplication.getAppContext(),
                "testcallHooked >>> \n"
                        + "  i=" + i
                , Toast.LENGTH_LONG).show();
        return;
    }

    @HookAnnotation(
            targetClass = SkyWalkerTestActivity.class,
            targetMethodName = "testCall",
            targetMethodParams = {int.class,int.class,int.class,int.class,int.class,
                    String.class,String.class,
                    byte.class,byte.class,
                    char.class,char.class,
                    float.class,float.class})
    public static void testHookProxy(int i,int i1,int i2,int i3,int i4,String s,String s1,byte b,byte b1,char c,char c1,float f,float f1){
        Log.d("chedifier_hook","testHookProxy!");
        Toast.makeText(MyApplication.getAppContext(),
                "testHookProxy >>> \n"
                        + "  i=" + i + " i1=" + i1 + " i2=" + i2 + " i3=" + i3 + " i4=" + i4 + "\n"
                        + "  s=" + s + " s1=" + s1 + "\n"
                        + "  b=" + b + " b1=" + b1 + "\n"
                        + "  c=" + c + " c1=" + c1 + "\n"
                        + "  f=" + f + " f1=" + f1
                , Toast.LENGTH_LONG).show();
    }

    @HookAnnotation(targetClass = SkyWalkerTestActivity.class,
            targetMethodName = "testCall2",
            targetMethodParams = {String[].class})
    public void testHookProxy2(){

    }

    @HookByDescriptor(
            className = "example.chedifier.chedifier.test.SkyWalkerTestActivity",
            methodName = "testPrivData",
            methodDescriptor = "(Lexample/chedifier/chedifier/test/SkyWalkerTestActivity$PrivData;)V"
    )
    public void testPrivData(Object data) {

        int i1 = 0;
        String s1 = "";
        try {
            Class cPrivData = Class.forName("example.chedifier.chedifier.test.SkyWalkerTestActivity$PrivData");
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

        Toast.makeText(MyApplication.getAppContext(),
                "testPrivData >>> Hooked\n"
                        + "  data.i1=" + i1
                        + "  data.s1=" + s1
                , Toast.LENGTH_LONG).show();
        return;
    }

    @HookAnnotation(
            targetClass = Choreographer.class,
            targetMethodName = "doFrame",
            targetMethodParams = {long.class,int.class})
    public void doFrame(long frameTimeNanos, int frame){
        Log.d("cqx","doFrame >>> Hooked\n"
                        + "  frameTimeNanos=" + frameTimeNanos
                        + "  frame=" + frame);

        return;
    }

//    @HookAnnotation(
//            targetClass = View.class,
//            targetMethodName = "invalidateInternal",
//            targetMethodParams = {int.class,int.class,int.class,int.class,boolean.class,boolean.class})
//    public void invalidateInternal(int l, int t, int r, int b, boolean invalidateCache,
//                       boolean fullInvalidate){
//        Log.d("cqx","invalidateInternal >>> Hooked\n"
//                + "  l=" + l
//                + "  t=" + t
//                + "  r=" + b
//                + "  invalidateCache=" + invalidateCache
//                + "  fullInvalidate=" + fullInvalidate);
//
//        return;
//    }

    @HookAnnotation(
            targetClass = Activity.class,
            targetMethodName = "startActivity",
            targetMethodParams = {Intent.class})
    public void startActivity(Intent intent){
        Log.d("cqx","startActivity >>> Hooked\n"
                + "  intent=" + intent);

        intent.setClass(MyApplication.getAppContext(),HookActivity.class);

        return;
    }

}
