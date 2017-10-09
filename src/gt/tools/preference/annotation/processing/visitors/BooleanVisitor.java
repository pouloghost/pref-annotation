package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.BooleanPreference;

import javax.lang.model.element.Element;

public class BooleanVisitor extends GenVisitor<BooleanPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports, Element field, String key, BooleanPreference annotation) {
        getterSetter.append("  public static boolean get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getBoolean(\"").append(key).append("\", ").append(annotation.def()).append(");");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(boolean value){\n").
                append("    sPreferences.edit().putBoolean(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(BooleanPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(BooleanPreference annotation) {
        return annotation.prefName();
    }
}
