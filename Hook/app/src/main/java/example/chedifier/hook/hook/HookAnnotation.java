package example.chedifier.hook.hook;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chedifier on 2016/10/15.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HookAnnotation {

    public Class<?> targetClass() default Object.class;
    public String methodName() default "";
    public Class<?>[] params() default {};
}
