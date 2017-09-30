package gt.tools.preference.annotation;

public @interface BooleanPreference {
    String prefName() default "";

    String key() default "";

    boolean def() default false;
}
