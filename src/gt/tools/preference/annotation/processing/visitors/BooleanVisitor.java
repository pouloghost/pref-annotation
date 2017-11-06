package gt.tools.preference.annotation.processing.visitors;


import gt.tools.preference.annotation.BooleanPreference;

import javax.lang.model.element.Element;

public class BooleanVisitor extends GenVisitor<BooleanPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports,
                                      Element field, String key, String prefixedKey, BooleanPreference annotation) {

        getterSetter.append("  public static boolean get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getBoolean(").append(prefixedKey).append(", ")
                .append(annotation.def()).append(");");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(boolean value){\n")
                .append("    sPreferences.edit().putBoolean(").append(prefixedKey)
                .append(", value).apply();\n  }\n");
        String type = field.asType().toString();
        if (TYPE_INT.equals(type)) {
            getterSetter.append("  public static void set").append(key).append("(int value){\n")
                    .append("    sPreferences.edit().putBoolean(").append(prefixedKey)
                    .append(", value == 1).apply();\n  }\n");
        }
    }

    @Override
    protected String getAnnotationKey(BooleanPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefFile(BooleanPreference annotation) {
        return annotation.prefFile();
    }

    @Override
    public String getPrefixKey(BooleanPreference annotation) {
        return annotation.prefixKey();
    }

    @Override
    public String[] acceptableFieldTypes() {
        return new String[]{TYPE_BOOLEAN, TYPE_INT};
    }

    @Override
    public boolean removable(BooleanPreference annotation) {
        return annotation.removable();
    }
}