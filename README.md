<img src="./logo.png" width="66%"/>

![Android](https://img.shields.io/badge/-Android-brightgreen)
![Kotlin](https://img.shields.io/badge/-Kotlin-blueviolet)
[![Gradle](https://img.shields.io/badge/-Gradle-01303a)](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)
[![Maven](https://img.shields.io/badge/-Maven-5f86eb)](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)
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
    id 'com.dreamgyf.android.fastinflate' version '0.1.0-alpha-03' apply false
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
    implementation 'com.dreamgyf.android.fastinflate:0.1.0-alpha-03'
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

- Multi-module projects are not supported currently.

- Multi-directory of `layout` is not supported currently. (For Example: `layout-v23`)

- The `<include />` tag doesn't supported to use `theme` attribute.

- The current version is an alpha beta, and have not done compatibility tests for all of the Android versions.

- The initialization of the current version of `FastInflate` is slow, this problem will be solved in subsequent versions.

## Performance test

**ps：The following test results are based on `activity_main` layout of this branch.**

### *Pixel 6 pro, Android SDK 33*

- Time consumption of first `inflate` layout（Unit: ms）

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

`FastInflate` takes `22.7ms` on average

`LayoutInflater` takes `29ms` on average

Performance improved by about `27.75%`

- Time consumption of second `inflate` layout（Unit: µs）

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

`FastInflate` takes `1924µs` on average

`LayoutInflater` takes `2912.6µs` on average

Performance improved by about `51.38%`

- Time consumption of `inflate` 1000 times for the same layout（单位：ms 毫秒）

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

`FastInflate` takes `847.3ms` on average

`LayoutInflater` takes `1344.6ms` on average

Performance improved by about `58.69%`

## Repositories

- [Gradle Plugin Portal](https://plugins.gradle.org/plugin/com.dreamgyf.android.fastinflate)

- [Maven Central](https://search.maven.org/artifact/com.dreamgyf.android.plugin/FastInflate)

## LICENSE

This project uses the [Apache-2.0](./LICENSE) license.