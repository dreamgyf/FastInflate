package com.dreamgyf.android.plugin.fastinflate.plugin

import com.squareup.kotlinpoet.ClassName

val logClz = ClassName("android.util", "Log")
val contextClz = ClassName("android.content", "Context")
val viewGroupClz = ClassName("android.view", "ViewGroup")
val xmlClz = ClassName("android.util", "Xml")
val helperClz = ClassName("com.dreamgyf.android.plugin.fastinflate.helper", "FastInflateHelper")
val attributeSetClz = ClassName("android.util", "AttributeSet")
val viewClz = ClassName("android.view", "View")
val layoutInflaterClz = ClassName("android.view", "LayoutInflater")
val factory2Clz = ClassName("android.view", "LayoutInflater", "Factory2")
val contextThemeWrapperClz = ClassName("android.view", "ContextThemeWrapper")
val fastInflateExceptionClz = ClassName("com.dreamgyf.android.plugin.fastinflate.exception", "FastInflateException")
val xmlPullParserClz = ClassName("org.xmlpull.v1", "XmlPullParser")
val intArrayClz = ClassName("kotlin", "IntArray")