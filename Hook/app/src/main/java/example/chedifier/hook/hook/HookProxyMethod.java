package example.chedifier.hook.hook;

import android.util.Log;
import android.widget.Toast;

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

}
