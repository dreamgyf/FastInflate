<img src="./logo.png" width="66%"/>

![Android](https://img.shields.io/badge/-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/-Kotlin-blueviolet)
[![Gradle](https://img.shields.io/badge/-Gradle-01303a)](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)
[![Maven](https://img.shields.io/badge/-Maven-5f86eb)](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)
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
    id 'com.dreamgyf.android.fastinflate' version '0.1.0-alpha-03' apply false
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
    implementation 'com.dreamgyf.android.fastinflate:0.1.0-alpha-03'
}
```

### 代码中使用

使用`FastInflate`替代`LayoutInflater`即可

```kotlin
// LayoutInflater.from(this).inflate(R.layout.activity_main, null)
FastInflate.from(this).inflate(R.layout.activity_main, null)
```

## 注意事项

- 编译此项目请直接`clone`父工程 [AndroidLibraries](https://github.com/dreamgyf/AndroidLibraries)

- 目前暂不支持多`module`项目

- 目前暂不支持多`layout`目录（如`layout-v23`）

- `<include />`标签不支持使用`theme`属性

- 当前版本为`alpha`测试版，尚未对所有`Android`版本进行兼容性测试

- 当前版本`FastInflate`初始化较慢，将在后续版本中解决此问题

## 性能测试

**注：以下测试结果均基于此分支`activity_main`布局**

### *Pixel 6 pro, Android SDK 33*

- 同一布局首次`inflate`耗时（单位：ms 毫秒）

| FastInflate | LayoutInflater |
|-------------|----------------|
| 23ms        | 28ms           |
| 23ms        | 29ms           |
| 22ms        | 29ms           |
| 24ms        | 30ms           |
| 21ms        | 29ms           |
| 23ms        | 29ms           |
| 23ms        | 29ms           |
| 23ms        | 29ms           |
| 22ms        | 28ms           |
| 23ms        | 30ms           |

`FastInflate`平均耗时`22.7ms`

`LayoutInflater`平均耗时`29ms`

性能提升约`27.75%`

- 同一布局第二次`inflate`耗时（单位：µs 微秒）

| FastInflate | LayoutInflater |
|-------------|----------------|
| 1895µs      | 2810µs         |
| 2009µs      | 3223µs         |
| 1898µs      | 2659µs         |
| 1904µs      | 2990µs         |
| 1891µs      | 2767µs         |
| 1861µs      | 2802µs         |
| 2051µs      | 3004µs         |
| 1886µs      | 3001µs         |
| 1957µs      | 2930µs         |
| 1888µs      | 2940µs         |

`FastInflate`平均耗时`1924µs`

`LayoutInflater`平均耗时`2912.6µs`

性能提升约`51.38%`

- 同一布局`inflate`1000次耗时（单位：ms 毫秒）

| FastInflate | LayoutInflater |
|-------------|----------------|
| 835ms       | 1318ms         |
| 848ms       | 1330ms         |
| 844ms       | 1343ms         |
| 845ms       | 1338ms         |
| 848ms       | 1344ms         |
| 853ms       | 1343ms         |
| 856ms       | 1354ms         |
| 855ms       | 1363ms         |
| 846ms       | 1369ms         |
| 843ms       | 1344ms         |

`FastInflate`平均耗时`847.3ms`

`LayoutInflater`平均耗时`1344.6ms`

性能提升约`58.69%`

## 仓库

- [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)

- [Maven Central](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)

## LICENSE

本项目使用 [Apache-2.0](./LICENSE) 协议