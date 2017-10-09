package gt.tools.preference.annotation;

public @interface JsonPreference {
    String prefName() default "";

    String key() default "";
}
