package example.chedifier.hook.hook;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by chedifier on 2016/10/22.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HookByDescriptor {

    public String className() default "";
    public String methodName() default "";
    public String methodDescriptor() default "";
    public HookType hookType() default HookType.BEFORE_TARGET;
}
