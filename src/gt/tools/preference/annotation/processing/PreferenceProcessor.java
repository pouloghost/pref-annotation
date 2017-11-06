package gt.tools.preference.annotation.processing;

import gt.tools.preference.annotation.*;
import gt.tools.preference.annotation.processing.visitors.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes({
        "gt.tools.preference.annotation.PreferenceAnnotation",
        "gt.tools.preference.annotation.BooleanPreference",
        "gt.tools.preference.annotation.FloatPreference",
        "gt.tools.preference.annotation.IntPreference",
        "gt.tools.preference.annotation.JsonPreference",
        "gt.tools.preference.annotation.ObjectPreference",
        "gt.tools.preference.annotation.LongPreference"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class PreferenceProcessor extends AbstractProcessor {
    public static final String PACKAGE_NAME = "preferenceHelperPackageName";
    public static String DEFAULT_PACKAGE = "com.smile.gifshow";
    public static String DEFAULT_PREFERENCE_NAME = "DefaultPreferenceHelper";

    private static final Map<Class<? extends Annotation>, GenVisitor> sVisiters = new HashMap<>();

    static {
        sVisiters.put(BooleanPreference.class, new BooleanVisitor());
        sVisiters.put(FloatPreference.class, new FloatVisitor());
        sVisiters.put(IntPreference.class, new IntVisitor());
        sVisiters.put(LongPreference.class, new LongVisitor());
        sVisiters.put(StringPreference.class, new StringVisitor());
        sVisiters.put(ObjectPreference.class, new ObjectVisitor());
    }

    private static Messager sMessager;
    private Filer mFiler;
    private String mPkgName;
    private Map<String, PreferenceTemplate> mPrefs;
    private boolean mHasProcessed;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        sMessager = processingEnv.getMessager();
        mPrefs = new HashMap<>();
        mPkgName = processingEnv.getOptions().get(PACKAGE_NAME);
        if (mPkgName == null || "".equals(mPkgName)) {
            mPkgName = DEFAULT_PACKAGE;
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (mHasProcessed) {
            return true;
        }
        for (Element rootClass : roundEnv.getElementsAnnotatedWith(PreferenceAnnotation.class)) {
            if (rootClass == null || rootClass.getKind() != ElementKind.CLASS) {
                continue;
            }
            PreferenceAnnotation annotation = rootClass.getAnnotation(PreferenceAnnotation.class);
            boolean needSaver = annotation.generateSaver();
            List<? extends Element> elements = rootClass.getEnclosedElements();
            for (Element field : elements) {
                if (field == null || field.getKind() != ElementKind.FIELD) {
                    continue;
                }
                Annotation fieldAnnotation = null;
                for (Class<? extends Annotation> annotationClass : sVisiters.keySet()) {
                    fieldAnnotation = field.getAnnotation(annotationClass);
                    if (fieldAnnotation != null) {
                        break;
                    }
                }
                gen((TypeElement) rootClass, field, fieldAnnotation, needSaver);
            }
        }
        writeToFile();
        mHasProcessed = true;
        return true;
    }

    private void gen(TypeElement rootClass, Element field, Annotation annotation, boolean needSaver) {
        if (annotation == null) {
            return;
        }
        GenVisitor visitor = sVisiters.get(annotation.getClass().getInterfaces()[0]);
        String prefName = visitor.getPrefFile(annotation);
        prefName = "".equals(prefName) ? DEFAULT_PREFERENCE_NAME : prefName;
        initPref(prefName);
        mPrefs.get(prefName).accept(visitor, rootClass, field, annotation, needSaver);
    }

    private void writeToFile() {
        for (String pref : mPrefs.keySet()) {
            try {
                FileObject file = mFiler.createSourceFile(mPkgName + "." + pref);
                Writer writer = file.openWriter();
                writer.write(mPrefs.get(pref).toString(mPkgName));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initPref(String prefFile) {
        if (mPrefs.containsKey(prefFile)) {
            return;
        }
        StringBuilder whole = new StringBuilder();
        whole.append("public final class ").append(prefFile).append("{\n").append(
                "  public static SharedPreferences sPreferences = (SharedPreferences)PreferenceContext.get(\"")
                .append(prefFile).append("\");\n");
        PreferenceTemplate template = new PreferenceTemplate(whole.toString());
        mPrefs.put(prefFile, template);
    }

    public static void debug(Element e, String msg, Object... args) {
        sMessager.printMessage(Diagnostic.Kind.WARNING, String.format(msg, args), e);
    }

    public static void exception(String s, Element e) {
        sMessager.printMessage(Diagnostic.Kind.ERROR, s, e);
        throw new IllegalArgumentException(s);
    }
}
