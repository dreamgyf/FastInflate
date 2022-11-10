<img src="./logo.png" width="66%"/>

![Android](https://img.shields.io/badge/-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/-Kotlin-blueviolet)
[![Gradle](https://img.shields.io/badge/-Gradle-01303a)](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)
[![Maven](https://img.shields.io/badge/-Maven-5f86eb)](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)
[![CI](https://github.com/dreamgyf/FastInflate/actions/workflows/ci.yml/badge.svg)](https://github.com/dreamgyf/FastInflate/actions/workflows/ci.yml)
[![version](https://img.shields.io/github/v/release/dreamgyf/FastInflate.svg?label=version&color=red)](https://github.com/dreamgyf/FastInflate/releases)
[![stars](https://img.shields.io/github/stars/dreamgyf/FastInflate)](https://github.com/dreamgyf/FastInflate/stargazers)
[![LICENSE](https://img.shields.io/github/license/dreamgyf/FastInflate)](./LICENSE)

English | [中文](./README-zh_cn.md)

**This is an Android plugin, which can improve the performance of layout loading by parsing the layout xml file during the compilation period and converting the reflection instantiation in LayoutInflater into a normal instantiation.**

## Get Started

### Dependencies

1. Open the `setting.gradle` file, and make sure that the repositories, `gradlePluginPortal` and `mavenCentral`, are configured.

```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        // other repository
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        mavenLocal()
        // other repository
    }
}
```

2. Open the `build.gradle` file of project, and add the `FastInflate` plugin in the `plugins`.

```groovy
plugins {
    id 'com.dreamgyf.android.fastinflate' version '0.1.0-alpha-07' apply false
}
```

3. Open the `build.gradle` file of module, and add the `FastInflate` plugin in the `plugins`.

```groovy
plugins {
    id 'com.dreamgyf.android.fastinflate'
}
```

4. Open the `build.gradle` file of module, and add the `FastInflate` library in the `dependencies`.

```groovy
dependencies {
    implementation 'com.dreamgyf.android.fastinflate:0.1.0-alpha-07'
}
```

### Usage

Just use `FastInflate` instead `LayoutInflater`.

```kotlin
// LayoutInflater.from(this).inflate(R.layout.activity_main, null)
FastInflate.from(this).inflate(R.layout.activity_main, null)
```

## Notice

- If you want compile this project, please `clone` the parent project [AndroidLibraries](https://github.com/dreamgyf/AndroidLibraries) directly.

- The `<include />` tag doesn't supported to use `theme` attribute.

- The current version is an alpha beta, and have not done compatibility tests for all of the Android versions.

## Performance test

After fixing the context problem, the performance drops to the same level as that of the LayoutInflater.

I'll find ways to improve performance in the future.

## Repositories

- [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)

- [Maven Central](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)

## LICENSE

This project uses the [Apache-2.0](./LICENSE) license.