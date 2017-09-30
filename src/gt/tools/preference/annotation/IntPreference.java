package gt.tools.preference.annotation;

public @interface IntPreference {
    String prefName() default "";

    String key() default "";

    int def() default 0;
}
