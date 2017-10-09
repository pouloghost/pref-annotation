package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.FloatPreference;

import javax.lang.model.element.Element;

public class FloatVisitor extends GenVisitor<FloatPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports, Element field, String key, FloatPreference annotation) {
        getterSetter.append("  public static float get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getFloat(\"").append(key).append("\", ").append(annotation.def()).append("F);");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(float value){\n").
                append("    sPreferences.edit().putFloat(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(FloatPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(FloatPreference annotation) {
        return annotation.prefName();
    }
}
