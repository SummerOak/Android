package com.chedifier.baselibrary.utils;

public class DebugLog {
	
	private static boolean isDebug = true;
	
	public static void setIsDebug(boolean b) {
        isDebug = b;
    }

    public static boolean isDebug() {
        return isDebug;
    }
    
    public static void d(String tag, String message) {
        if (isDebug) {
            StackTraceElement stack[] = Thread.currentThread().getStackTrace();
            message =
                    stack[3].getClassName() + "." + stack[3].getMethodName() + "()<"
                            + stack[3].getLineNumber() + "> : " + message;

            android.util.Log.d(tag, message);
        }
    }
	
	public static void i(String tag, String message) {
        if (isDebug) {
            StackTraceElement stack[] = Thread.currentThread().getStackTrace();
            message =
                    stack[3].getClassName() + "." + stack[3].getMethodName() + "()<"
                            + stack[3].getLineNumber() + "> : " + message;

            android.util.Log.i(tag, message);
        }
    }
}
