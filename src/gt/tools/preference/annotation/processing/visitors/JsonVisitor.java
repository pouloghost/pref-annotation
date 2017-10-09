package gt.tools.preference.annotation.processing.visitors;

import gt.tools.preference.annotation.GsonPreference;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public class JsonVisitor extends GenVisitor<GsonPreference> {
    @Override
    protected void appendGetterSetter(StringBuilder getterSetter, StringBuilder imports, Element field, String key, GsonPreference annotation) {
        TypeElement fieldType = (TypeElement) ((DeclaredType) field.asType()).asElement();
        if (imports.indexOf("gt.tools.preference.annotation.Jsons") == -1) {
            imports.append("import gt.tools.preference.annotation.Jsons;\n");
        }
        if (imports.indexOf(fieldType.getQualifiedName().toString()) == -1) {
            imports.append("import ").append(fieldType.getQualifiedName().toString()).append(";\n");
        }
        getterSetter.append("  public static ").append(fieldType.getSimpleName()).append(" get").append(key).append("(){\n")
                .append("    String value = sPreferences.getString(\"").append(key).append("\", null);\n")
                .append("    if(value == null){\n")
                .append("      return null;\n")
                .append("    }\n")
                .append("    return ProviderContext.getInstance().jsons().fromJson(value, ").append(fieldType.getSimpleName()).append(".class);\n")
                .append("  }\n");

        getterSetter.append("  public static void set").append(key).append("(").append(fieldType.getSimpleName()).append(" value){\n").
                append("    sPreferences.edit().putString(\"").append(key).append("\", ProviderContext.getInstance().jsons().toJson(value)).apply();\n  }\n");
    }

    @Override
    protected String getAnnotationKey(GsonPreference annotation) {
        return annotation.key();
    }

    @Override
    public String getPrefName(GsonPreference annotation) {
        return annotation.prefName();
    }
}
