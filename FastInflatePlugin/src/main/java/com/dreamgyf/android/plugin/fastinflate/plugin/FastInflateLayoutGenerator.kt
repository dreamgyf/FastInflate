package com.dreamgyf.android.plugin.fastinflate.plugin

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import groovy.xml.XmlParser
import java.io.File

object FastInflateLayoutGenerator {

    private val xmlParser by lazy {
        XmlParser()
    }

    fun generate(name: String, file: File): FileSpec {
        val node = xmlParser.parse(file)
        println(node.name().toString())

        val className = ClassName(
            "com.dreamgyf.android.plugin.fastinflate.generate", name
        )

        val logClz = ClassName("android.util", "Log")

        return FileSpec.builder(
            "com.dreamgyf.android.plugin.fastinflate.generate", name
        ).addType(
            TypeSpec.classBuilder(className).addFunction(
                FunSpec.builder("inflate")
                    .addStatement("%T.i(\"Test\", \"Test\")", logClz)
                    .build()
            ).build()
        ).build()
    }
}