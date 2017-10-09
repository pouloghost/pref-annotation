package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.IntPreference;

import javax.lang.model.element.Element;

public class IntVisitor extends GenVisitor<IntPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports, Element field, String key, IntPreference annotation) {
        getterSetter.append("  public static int get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getInt(\"").append(key).append("\", ").append(annotation.def()).append(");");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(int value){\n").
                append("    sPreferences.edit().putInt(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(IntPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(IntPreference annotation) {
        return annotation.prefName();
    }
}
