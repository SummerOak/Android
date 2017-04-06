package example.chedifier.chedifier.test;

/**
 * Created by chedifier on 2016/12/13.
 */
public class NativeTest {

    static {
        System.loadLibrary("nativetest");
    }

    public static native void test(String desc);

    public static native int eatMemory(int size);

}
