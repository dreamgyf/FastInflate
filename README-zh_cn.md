<img src="./logo.png" width="66%"/>

![Android](https://img.shields.io/badge/-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/-Kotlin-blueviolet)
[![Gradle](https://img.shields.io/badge/-Gradle-01303a)](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)
[![Maven](https://img.shields.io/badge/-Maven-5f86eb)](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)
[![CI](https://github.com/dreamgyf/FastInflate/actions/workflows/ci.yml/badge.svg)](https://github.com/dreamgyf/FastInflate/actions/workflows/ci.yml)
[![version](https://img.shields.io/github/v/release/dreamgyf/FastInflate.svg?label=version&color=red)](https://github.com/dreamgyf/FastInflate/releases)
[![stars](https://img.shields.io/github/stars/dreamgyf/FastInflate)](https://github.com/dreamgyf/FastInflate/stargazers)
[![LICENSE](https://img.shields.io/github/license/dreamgyf/FastInflate)](./LICENSE)

[English](./README.md) | 中文

**这是一个`Android`插件，通过在编译期时解析布局`xml`文件，将`LayoutInflater`中的反射实例化转成普通的实例化，以此来提高布局加载的性能**

## 如何使用

### 引入依赖

1. 打开`setting.gradle`文件，确保配置了远程仓库`gradlePluginPortal`和`mavenCentral`

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        // 其他仓库
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        mavenLocal()
        // 其他仓库
    }
}
```

2. 打开项目`build.gradle`文件，在`plugins`下添加`FastInflate`插件

```groovy
plugins {
    id 'com.dreamgyf.android.fastinflate' version '0.1.0-alpha-07' apply false
}
```

3. 打开模块`build.gradle`文件，在`plugins`下添加`FastInflate`插件

```groovy
plugins {
    id 'com.dreamgyf.android.fastinflate'
}
```

4. 打开模块`build.gradle`文件，在`dependencies`下添加`FastInflate`库依赖

```groovy
dependencies {
    implementation 'com.dreamgyf.android.fastinflate:0.1.0-alpha-07'
}
```

### 代码中使用

使用`FastInflate`替代`LayoutInflater`即可

```kotlin
// LayoutInflater.from(this).inflate(R.layout.activity_main, null)
FastInflate.from(this).inflate(R.layout.activity_main, null)
```

## 注意事项

- `<include />`标签不支持使用`theme`属性

- 当前版本为`alpha`测试版，尚未对所有`Android`版本进行兼容性测试

## 性能测试

修复`context`问题后，性能降至和`LayoutInflater`相当的水平，后续想办法再提高性能

## 仓库

- [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)

- [Maven Central](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)

## LICENSE

本项目使用 [Apache-2.0](./LICENSE) 协议