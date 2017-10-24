package gt.tools.preference.annotation.processing;

import gt.tools.preference.annotation.processing.visitors.GenVisitor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class PreferenceTemplate {
    public StringBuilder mImports = new StringBuilder();
    public String mClass;
    public StringBuilder mGetterSetters = new StringBuilder();
    public Map<String, StringBuilder> mSavers = new HashMap<>();

    public PreferenceTemplate(String clazz) {
        mImports.append("import android.content.SharedPreferences;\n").
                append("import gt.tools.preference.annotation.ProviderContext;\n");
        mClass = clazz;
    }

    public void accept(GenVisitor visitor, TypeElement root, Element field, Annotation annotation, boolean needSaver) {
        visitor.visit(this, root, field, annotation, needSaver);
    }

    public String toString(String pkg) {
        StringBuilder builder = new StringBuilder();
        builder.append("package ").append(pkg).append(";\n").append(mImports).append(mClass).append(mGetterSetters);
        for (Map.Entry<String, StringBuilder> entry : mSavers.entrySet()) {
            builder.append(entry.getValue());
        }
        return builder.append("}").toString();
    }
}
