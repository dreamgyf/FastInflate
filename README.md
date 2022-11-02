# FastInflate

编译期时解析布局xml文件，将LayoutInflater中的反射实例化转成普通的实例化，以此来提高布局加载的性能

经测试，约能使布局加载的速度提升1.5倍

**当前版本为alpha测试版，仅供学习研究使用，由此插件引发的各种损失，本人概不负责！！！**

**目前不支持多`module`、多`flavor`项目，`<include />`标签不支持使用`theme`属性**


**注：编译此项目请直接 clone 父工程 [AndroidLibraries](https://github.com/dreamgyf/AndroidLibraries)**

## 引入依赖

**注：`gradle plugin`和`maven`仓库上传可能较慢，如果拉取不到依赖，可以自行编译并发布到本地`maven`仓库中使用**

```groovy
plugins {
    id 'com.dreamgyf.android.fastinflate' version '0.1.0-alpha-02'
}

dependencies {
    implementation 'com.dreamgyf.android.fastinflate:0.1.0-alpha-02'
}
```

## 项目中使用

```kotlin
// 使用FastInflate代替LayoutInflater即可，如下
// LayoutInflater.from(this).inflate(R.layout.activity_main, null)
FastInflate.from(this).inflate(R.layout.activity_main, null)
```