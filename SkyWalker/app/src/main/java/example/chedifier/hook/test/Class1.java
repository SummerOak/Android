package example.chedifier.hook.test;

import example.chedifier.hook.hook.HookParaser;
import example.chedifier.hook.testproxy.MyClassloaderProxy;

/**
 * Created by chedifier on 2016/11/6.
 */
public class Class1 {

    public void func1(String s1){
        func2();
    }

    public void func2(){
        HookParaser.parseAndHook(MyClassloaderProxy.class);
    }

}
