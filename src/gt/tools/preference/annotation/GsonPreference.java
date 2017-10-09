package gt.tools.preference.annotation;

public @interface GsonPreference {
    String prefName() default "";

    String key() default "";
}
