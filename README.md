# pref-annotation
用来生成FPM（Field-Preference-Mapping）模板代码的Annotation库。使用Annotation简化将对象的某些字段存入Preference的方法。

与现有的Preference Annotation不同的是：

- 支持自定义Preference定义，可以通过实现SharedPreference抽象不同的KV存储方式
- 支持自定义Preference存储Key的前缀
- 是FPM而非OPM，存储的粒度是Field，而非Object
- 生成快速保存整个对象中做FPM的字段的save方法


# 使用方法

## Gradle集成

1.下载工程，并当做Module引入
2.在需要使用的其他module下，compile project('pref-annotation')

## 使用

### 结构

使用层面，有两类Annotation:

- PreferenceAnnotation 针对类型，表明该class需要
- TypedPreference，例如BooleanPreference、FloatPreference等 针对field，声明field与Preference的对应关系

### Step By Step

- 初始化Context，在使用Preference之前，使用PreferenceAdapter的实现初始化PreferenceContext

```
PreferenceContext.init(new PreferenceAdapter() {
  private SharedPreferences mPreference;

 @Override
 public SharedPreferences getPreferenceByName(String key) {
    if (mPreference == null) {
      mPreference = new CustomSharedPreferences(getAppContext());
 }
    if (PreferenceConfig.DEFAULT_PREFERENCE_NAME.equals(key)) {
      return mPreference;
 }
    return null;
 }

  @Override
 public String getPreferenceKeyPrefix(String key) {
    if (PreferenceConfig.PREFIX_USER_ID.equals(key)) {
      return ME.getId();
 }
    return "";
 }

  @Override
 public <D> D deserialize(String json, Type type) {
    return new Gsons().fromJson(json, type);
 }

  @Override
 public String serialize(Object obj) {
    return new Gsons().toJson(obj);
 }
});

```
 其中
	- getPreferenceByName：入参是TypePreference的prefFile。
	- getPreferenceKeyPrefix：入参是TypePreference的prefixKey，prefixKey代表了前缀的类型，Adapter根据类型返回prefix的真实值，比如userid、sessionid等。
	- deserialize/serialize：ObjectPreference会将对象序列化成String存入Preference，序列化和反序列化使用这两个方法。

- 	optional，声明包名。包名使用gradle编译参数来声明

```
android {
    ...
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = [preferencePackage: 'gt.tools']
            }
        }
    }
...
}
// 日志
afterEvaluate {
    tasks.withType(JavaCompile) {
        options.compilerArgs << "-Xmaxerrs" << "100000"
 }
}

```

- 	为需要处理的类增加PreferenceAnnotation，即如果类中的Field需要存到Preference中，则该类需要打上PreferenceAnnotation。如果不需要save方法，则声明generateSaver = false。
-  为每个需要对应的field声明TypedPreference。 TypedPreference均有如下方法：
	- prefFile Preference的名字。运行时会传入PreferenceAdapter#getPreferenceByName获取Preference实例。
	- key Preference存储使用的key，如果为空，则默认使用格式化后的field名。
	- prefixKey 运行时添加到key前面的前缀key。运行时会传入PreferenceAdapter#getPreferenceKeyPrefix获得真实前缀。
	- removable 是否生成remove方法，默认是false，针对有特殊需求的字段。
	- def default值。其中，ObjectPreference 的def是String类型，经Adapter#deserialize后形成默认对象。BooleanPreference支持int field。 
- 添加TypedPreference后编译。会在build/generated/source/apt目录下生成Helper代码。命名规则是${PreferenceName}Helper，Helper会有四类方法：
	- get${formatedKey} 对应SharedPreferences#getXXX
	- set${formatedKey} 对应SharedPreferences#edit#setXXX
	- remove${formatedKey} 对应SharedPreferences#edit#remove
	- save() 为每个PreferenceAnnotation标注的类声明一个save方法，方便快速保存。
- 使用 直接调用对应Helper的对应方法。




