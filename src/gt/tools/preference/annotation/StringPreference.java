package gt.tools.preference.annotation;

public @interface StringPreference {
    String prefName() default "";

    String key() default "";

    String def() default "";
}
