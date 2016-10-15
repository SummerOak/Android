package example.chedifier.hook.hook;

import example.chedifier.hook.MainActivity;

/**
 * Created by chedifier on 2016/5/14.
 */
public class HookProxyMethod {

    @HookAnnotation(
            targetClass = MainActivity.class,
            methodName = "testCall",
            params = {int.class,int.class,int.class,int.class,int.class,
                    String.class,String.class,
                    byte.class,byte.class,
                    char.class,char.class,
                    float.class,float.class})
    public void testHookProxy(){

    }


    @HookAnnotation(targetClass = MainActivity.class,
                    methodName = "testCall2",
                    params = {String[].class})
    public void testHookProxy2(){

    }

}
