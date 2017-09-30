package gt.tools.preference.annotation;

public @interface FloatPreference {
    String prefName() default "";

    String key() default "";

    float def() default 0f;
}
