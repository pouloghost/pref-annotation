package gt.tools.preference.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface LongPreference {
    String prefFile() default "";

    String key() default "";

    String prefixKey() default "";

    boolean removable() default false;

    long def() default 0L;
}

