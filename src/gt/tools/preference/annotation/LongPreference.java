package gt.tools.preference.annotation;

public @interface LongPreference {
    String prefName() default "";

    String key() default "";

    long def() default 0L;
}
