package com.chedifier.baselibrary.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.util.Log;

public class JavaCalls {
    private final static String LOG_TAG = "JavaCalls";

    private final static HashMap<Class<?>, Class<?>> PRIMITIVE_MAP = new HashMap<Class<?>, Class<?>>();

    static {
        PRIMITIVE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_MAP.put(Character.class, char.class);
        PRIMITIVE_MAP.put(Short.class, short.class);
        PRIMITIVE_MAP.put(Integer.class, int.class);
        PRIMITIVE_MAP.put(Float.class, float.class);
        PRIMITIVE_MAP.put(Long.class, long.class);
        PRIMITIVE_MAP.put(Double.class, double.class);
        PRIMITIVE_MAP.put(boolean.class, boolean.class);
        PRIMITIVE_MAP.put(byte.class, byte.class);
        PRIMITIVE_MAP.put(char.class, char.class);
        PRIMITIVE_MAP.put(short.class, short.class);
        PRIMITIVE_MAP.put(int.class, int.class);
        PRIMITIVE_MAP.put(float.class, float.class);
        PRIMITIVE_MAP.put(long.class, long.class);
        PRIMITIVE_MAP.put(double.class, double.class);
    }

    @SuppressWarnings("unchecked")
    public static class JavaParam<T> {
        public final Class<? extends T> clazz;
        public final T obj;

        public JavaParam(Class<? extends T> clazz, T obj) {
            super();
            this.clazz = clazz;
            this.obj = obj;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T callMethod(Object targetInstance, String methodName, Object... args) {
        try {
            return callMethodOrThrow(targetInstance, methodName, args);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Meet exception when call Method '" + methodName + "' in " + targetInstance, e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T callMethodOrThrow(Object targetInstance, String methodName, Object... args)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        final Class<?> clazz = targetInstance.getClass();

        Method method = getDeclaredMethod(clazz, methodName, getParameterTypes(args));

        T result = (T) method.invoke(targetInstance, getParameters(args));
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T callStaticMethod(String className, String methodName, Object... args) {
        try {
            Class<?> clazz = Class.forName(className);
            return callStaticMethodOrThrow(clazz, methodName, args);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Meet exception when call Method '" + methodName + "' in " + className, e);
            return null;
        }
    }

    private static Method getDeclaredMethod(final Class<?> clazz, String name, Class<?>... parameterTypes)
            throws NoSuchMethodException, SecurityException {

        Method[] methods = clazz.getDeclaredMethods();
        Method method = findMethodByName(methods, name, parameterTypes);

        return method;
    }

    private static Method findMethodByName(Method[] list, String name, Class<?>[] parameterTypes)
            throws NoSuchMethodException {
        if (name == null) {
            throw new NullPointerException("Method name must not be null.");
        }

        for (Method method : list) {
            if (method.getName().equals(name) && compareClassLists(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }

        throw new NoSuchMethodException(name);
    }

    private static boolean compareClassLists(Class<?>[] a, Class<?>[] b) {
        if (a == null) {
            return (b == null) || (b.length == 0);
        }

        int length = a.length;

        if (b == null) {
            return (length == 0);
        }

        if (length != b.length) {
            return false;
        }

        for (int i = length - 1; i >= 0; i--) {
            if (a[i].isAssignableFrom(b[i])
                    || (PRIMITIVE_MAP.containsKey(a[i]) && PRIMITIVE_MAP.get(a[i]).equals(PRIMITIVE_MAP.get(b[i])))) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T callStaticMethodOrThrow(final String className, String methodName, Object... args)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Method method = getDeclaredMethod(clazz, methodName, getParameterTypes(args));

        T result = (T) method.invoke(null, getParameters(args));
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T callStaticMethodOrThrow(final Class<?> clazz, String methodName, Object... args)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        Method method = getDeclaredMethod(clazz, methodName, getParameterTypes(args));

        T result = (T) method.invoke(null, getParameters(args));
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<?> clazz, Object... args) {
        try {
            return getInstanceOrThrow(clazz, args);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Meet exception when make instance as a " + clazz.getSimpleName(), e);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstanceOrThrow(Class<?> clazz, Object... args) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Constructor<?> constructor = clazz.getConstructor(getParameterTypes(args));
        return (T) constructor.newInstance(getParameters(args));
    }

    public static Object getInstance(String className, Object... args) {
        try {
            return getInstanceOrThrow(className, args);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Meet exception when make instance as a " + className, e);
            return null;
        }
    }

    public static Object getInstanceOrThrow(String className, Object... args) throws SecurityException,
            NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
            InvocationTargetException, ClassNotFoundException {
        return getInstanceOrThrow(Class.forName(className), getParameters(args));
    }

    private static Class<?>[] getParameterTypes(Object... args) {
        Class<?>[] parameterTypes = null;

        if (args != null && args.length > 0) {
            parameterTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                Object param = args[i];
                if (param != null && param instanceof JavaParam<?>) {
                    parameterTypes[i] = ((JavaParam<?>) param).clazz;
                } else {
                    parameterTypes[i] = param == null ? null : param.getClass();
                }
            }
        }
        return parameterTypes;
    }

    private static Object[] getParameters(Object... args) {
        Object[] parameters = null;

        if (args != null && args.length > 0) {
            parameters = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object param = args[i];
                if (param != null && param instanceof JavaParam<?>) {
                    parameters[i] = ((JavaParam<?>) param).obj;
                } else {
                    parameters[i] = param;
                }
            }
        }
        return parameters;
    }

    public static Method getDeclaredMethod(Object object, String methodName, Class<?>... parameterTypes) {
        Method method = null;

        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, parameterTypes);
                return method;
            } catch (Exception e) {

            }
        }

        return null;
    }

    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        try {
            if (null != method) {
                method.setAccessible(true);
                return method.invoke(object, parameters);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    /**
     * Attempt to find a {@link Field field} on the supplied {@link Class} with
     * the supplied <code>name</code>. Searches all superclasses up to
     * {@link Object}.
     * @param object the class object to introspect
     * @param name the name of the field
     * @return the corresponding Field object, or <code>null</code> if not found
     */
    public static Field findField(Object object, String name) {
        return findField(object, name, null);
    }

    /**
     * Attempt to find a {@link Field field} on the supplied {@link Class} with
     * the supplied <code>name</code> and/or {@link Class type}. Searches all
     * superclasses up to {@link Object}.
     * @param object   the  class object to introspect
     * @param name the name of the field (may be <code>null</code> if type is specified)
     * @param type the type of the field (may be <code>null</code> if name is specified)
     * @return the corresponding Field object, or <code>null</code> if not found
     */
    public static Field findField(Object object, String name, Class<?> type) {
        Class<?> searchType = object.getClass();
        while (!Object.class.equals(searchType) && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }
    

    /**
     * Set the field represented by the supplied {@link Field field object} on the
     * specified {@link Object target object} to the specified <code>value</code>.
     * In accordance with {@link Field#set(Object, Object)} semantics, the new value
     * is automatically unwrapped if the underlying field has a primitive type.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
     * @param field the field to set
     * @param target the target object on which to set the field
     * @param value the value to set; may be <code>null</code>
     */
    public static void setField(Field field, Object target, Object value) {
        try {
            field.setAccessible(true);
            field.set(target, value);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
                    + ex.getMessage());
        }
    }
    
    public static void setField(Object target, String name, Object value) {
        Field field = findField(target, name);
        if (field != null) {
            setField(field, target, value);
        }
    }
    
    /**
     * Get the field represented by the supplied {@link Field field object} on the
     * specified {@link Object target object}. In accordance with {@link Field#get(Object)}
     * semantics, the returned value is automatically wrapped if the underlying field
     * has a primitive type.
     * <p>Thrown exceptions are handled via a call to {@link #handleReflectionException(Exception)}.
     * @param field the field to get
     * @param target the target object from which to get the field
     * @return the field's current value
     */
    public static Object getField(Field field, Object target) {
        try {
            field.setAccessible(true);
            return field.get(target);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
                    + ex.getMessage());
        }
    }
    
    public static Object getField(Object target, String name) {
        Field field = findField(target, name);
        return getField(field, target);
    }
}
