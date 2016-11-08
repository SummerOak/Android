package example.chedifier.hook.ptrace;

/**
 * Created by wxj_pc on 2016/10/2.
 */
public class PTrace {

    private static final String TAG = PTrace.class.getSimpleName();

    static {
        System.loadLibrary("chedifier_ptrace");
    }

    public static int pTrace(int pid){
        return pTraceNative(pid);
    }

    public static int pStop(int pid){
        return pTraceStopNative(pid);
    }

    private static native int pTraceNative(int pid);
    private static native int pTraceStopNative(int pid);
}
