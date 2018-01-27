package com.chedifier.plugin;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * inject plugin classloader into host classloader
 * @author chedifier
 * @date 2015-12-23
 */
public class ClassLoaderInjectHelper {
    
    public static InjectResult inject(ClassLoader parentClassLoader, ClassLoader childClassLoader, String someClass) {

        boolean hasBaseDexClassLoader = true;
        try {
            Class.forName("dalvik.system.BaseDexClassLoader");
        } catch (ClassNotFoundException e) {
            hasBaseDexClassLoader = false;
        }
        if (!hasBaseDexClassLoader) {
            return injectBelowApiLevel14(parentClassLoader, childClassLoader, someClass);
        } else {
            return injectAboveEqualApiLevel14(parentClassLoader, childClassLoader);
        }        
    }


    
    @SuppressLint("NewApi")
	private static InjectResult injectBelowApiLevel14(ClassLoader parentClassLoader, ClassLoader childClassLoader,
            String someClass) {
        InjectResult result = null;
        PathClassLoader pathClassLoader = (PathClassLoader) parentClassLoader;
        DexClassLoader dexClassLoader = (DexClassLoader)childClassLoader;
        try {
            dexClassLoader.loadClass(someClass);
            setField(
                    pathClassLoader,
                    PathClassLoader.class,
                    "mPaths",
                    appendArray(getField(pathClassLoader, PathClassLoader.class, "mPaths"),
                            getField(dexClassLoader, DexClassLoader.class, "mRawDexPath")));
            setField(
                    pathClassLoader,
                    PathClassLoader.class,
                    "mFiles",
                    combineArray(getField(pathClassLoader, PathClassLoader.class, "mFiles"),
                            getField(dexClassLoader, DexClassLoader.class, "mFiles")));
            setField(
                    pathClassLoader,
                    PathClassLoader.class,
                    "mZips",
                    combineArray(getField(pathClassLoader, PathClassLoader.class, "mZips"),
                            getField(dexClassLoader, DexClassLoader.class, "mZips")));
            setField(
                    pathClassLoader,
                    PathClassLoader.class,
                    "mDexs",
                    combineArray(getField(pathClassLoader, PathClassLoader.class, "mDexs"),
                            getField(dexClassLoader, DexClassLoader.class, "mDexs")));

            try {
                @SuppressWarnings("unchecked")
                ArrayList<String> libPaths = (ArrayList<String>) getField(pathClassLoader, PathClassLoader.class,
                        "libraryPathElements");
                String[] libArray = (String[]) getField(dexClassLoader, DexClassLoader.class, "mLibPaths");
                for (String path : libArray) {
                    libPaths.add(path);
                }
            } catch (Exception e) {
                setField(
                        pathClassLoader,
                        PathClassLoader.class,
                        "mLibPaths",
                        combineArray(getField(pathClassLoader, PathClassLoader.class, "mLibPaths"),
                                getField(dexClassLoader, DexClassLoader.class, "mLibPaths")));
            }
        } catch (NoSuchFieldException e) {
            result = makeInjectResult(false, e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result = makeInjectResult(false, e);
            e.printStackTrace();
        } catch (Exception e) {
            result = makeInjectResult(false, e);
            e.printStackTrace();
        }

        if (result == null) {
            result = makeInjectResult(true, null);
        }
        return result;
    }

    
    private static InjectResult injectAboveEqualApiLevel14(ClassLoader parentClassLoader, ClassLoader childClassLoader) {
        PathClassLoader pathClassLoader = (PathClassLoader) parentClassLoader;
        DexClassLoader dexClassLoader = (DexClassLoader) childClassLoader;
        InjectResult result = null;
        try {
            // 注入 dex
            Object dexElements = combineArray(getDexElements(getPathList(pathClassLoader)),
                    getDexElements(getPathList(dexClassLoader)));

            Object pathList = getPathList(pathClassLoader);

            setField(pathList, pathList.getClass(), "dexElements", dexElements);
            
            // 注入 native lib so 目录，需要parent class loader 遍历此目录能够找到。因为注入了以后，不处理这个目录找不到。
            Object dexNativeLibraryDirs = combineArray(getNativeLibraryDirectories(getPathList(pathClassLoader)),
                    getNativeLibraryDirectories(getPathList(dexClassLoader)));

            setField(pathList, pathList.getClass(), "nativeLibraryDirectories", dexNativeLibraryDirs);
        } catch (IllegalArgumentException e) {
            result = makeInjectResult(false, e);
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            result = makeInjectResult(false, e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            result = makeInjectResult(false, e);
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            result = makeInjectResult(false, e);
            e.printStackTrace();
        }
        if (result == null) {
            result = makeInjectResult(true, null);
        }
        return result;
    }

    /**
     * set field
     *
     * @param oObj
     *            object
     * @param aCl
     *            class
     * @param aField
     *            field
     * @param value
     *            value
     * @throws NoSuchFieldException
     *             NoSuchFieldException
     * @throws IllegalArgumentException
     *             IllegalArgumentException
     * @throws IllegalAccessException
     *             IllegalAccessException
     */
    private static void setField(Object oObj, Class<?> aCl, String aField, Object value)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = aCl.getDeclaredField(aField);
        localField.setAccessible(true);
        localField.set(oObj, value);
    }

    /**
     * @param oObj
     *            object
     * @param aCl
     *            class
     * @param aField
     *            field
     * @return field
     * @throws NoSuchFieldException
     *             NoSuchFieldException
     * @throws IllegalArgumentException
     *             IllegalArgumentException
     * @throws IllegalAccessException
     *             IllegalAccessException
     */
    private static Object getField(Object oObj, Class<?> aCl, String aField) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {
        Field localField = aCl.getDeclaredField(aField);
        localField.setAccessible(true);
        return localField.get(oObj);
    }

    /**
     * combine array
     *
     * @param aArrayLhs
     *            array
     * @param aArrayRhs
     *            array
     * @return array
     */
    private static Object combineArray(Object aArrayLhs, Object aArrayRhs) {
        Class<?> localClass = aArrayLhs.getClass().getComponentType();
        int i = Array.getLength(aArrayLhs);
        int j = i + Array.getLength(aArrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(aArrayLhs, k));
            } else {
                Array.set(result, k, Array.get(aArrayRhs, k - i));
            }
        }
        return result;
    }
    

    /**
     * append for array
     *
     * @param aArray
     *            array
     * @param aValue
     *            value
     * @return new array
     */
    private static Object appendArray(Object aArray, Object aValue) {
        Class<?> localClass = aArray.getClass().getComponentType();
        int i = Array.getLength(aArray);
        int j = i + 1;
        Object localObject = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(localObject, k, Array.get(aArray, k));
            } else {
                Array.set(localObject, k, aValue);
            }
        }
        return localObject;
    }

    /**
     * make a inject result
     *
     * @param aResult
     *            result
     * @param aT
     *            throwable
     * @return inject result
     */
    public static InjectResult makeInjectResult(boolean aResult, Throwable aT) {
        InjectResult ir = new InjectResult();
        ir.mIsSuccessful = aResult;
        ir.mErrMsg = (aT != null ? aT.getLocalizedMessage() : null);
        return ir;
    }

    /**
     * @param aBaseDexClassLoader
     *            BaseDexClassLoader
     * @return path list
     * @throws IllegalArgumentException
     *             IllegalArgumentException
     * @throws NoSuchFieldException
     *             NoSuchFieldException
     * @throws IllegalAccessException
     *             IllegalAccessException
     * @throws ClassNotFoundException
     *             ClassNotFoundException
     */
    private static Object getPathList(Object aBaseDexClassLoader) throws IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        return getField(aBaseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }
    
    
    

    /**
     * @param aParamObject
     *            param
     * @return dexElements
     * @throws IllegalArgumentException
     *             IllegalArgumentException
     * @throws NoSuchFieldException
     *             NoSuchFieldException
     * @throws IllegalAccessException
     *             IllegalAccessException
     */
    private static Object getDexElements(Object aParamObject) throws IllegalArgumentException,
            NoSuchFieldException, IllegalAccessException {
        return getField(aParamObject, aParamObject.getClass(), "dexElements");
    }
    
    
    private static Object getNativeLibraryDirectories(Object aParamObject) throws IllegalArgumentException, NoSuchFieldException,
            IllegalAccessException {
        return getField(aParamObject, aParamObject.getClass(), "nativeLibraryDirectories");
    }
    
    /**
     * inject result
     */
    public static class InjectResult {
        /** is successful */
        public boolean mIsSuccessful;
        /** error msg */
        public String mErrMsg;
        
		@Override
		public String toString() {
			return "InjectResult [mIsSuccessful=" + mIsSuccessful
					+ ", mErrMsg=" + mErrMsg + "]";
		}
        
        
    }
}
