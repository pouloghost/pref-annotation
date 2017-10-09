package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.LongPreference;

import javax.lang.model.element.Element;

public class LongVisitor extends GenVisitor<LongPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports, Element field, String key, LongPreference annotation) {
        getterSetter.append("  public static long get").append(key).append("(){\n");
        getterSetter.append("    return sPreferences.getLong(\"").append(key).append("\", ").append(annotation.def()).append("L);");
        getterSetter.append("\n  }\n");
        getterSetter.append("  public static void set").append(key).append("(long value){\n").
                append("    sPreferences.edit().putLong(\"").append(key).append("\", value).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(LongPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(LongPreference annotation) {
        return annotation.prefName();
    }
}
