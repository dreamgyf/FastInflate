package com.dreamgyf.android.plugin.fastinflate.plugin

import com.squareup.kotlinpoet.*
import groovy.xml.XmlParser
import java.io.File

object FastInflateLayoutGenerator {

    private const val TAG_MERGE = "merge"
    private const val TAG_INCLUDE = "include"

    private val xmlParser by lazy {
        XmlParser()
    }

    fun generate(name: String, file: File): FileSpec {
        val createViewFuncMap = mutableMapOf<String, FunSpec>()

        val root = xmlParser.parse(file)

        val className = ClassName(
            "com.dreamgyf.android.plugin.fastinflate.generate", name
        )

        val inflateFunBuilder = FunSpec.builder("inflate")
            .addParameter("resource", Int::class.java)
            .addParameter("root", viewGroupClz.copy(nullable = true))
            .addParameter("attachToRoot", Boolean::class.java)
            .returns(viewClz)
            .addStatement("val res = context.resources")
            .addStatement("val parser = res.getLayout(resource)")
            .addStatement("try {")
            .addStatement("val attrs = %T.asAttributeSet(parser)", xmlClz)
            .addStatement("var result: %T = root", viewClz.copy(nullable = true))
            .addStatement("%T.advanceToRootNode(parser)", helperClz)

        var rootName = root.name().toString()
        if (rootName == TAG_MERGE) {

        } else {
            rootName = rootName.fixViewName()

            if (!createViewFuncMap.contains(rootName)) {
                createViewFuncMap[rootName] = genCreateViewFunc(rootName)
            }

            inflateFunBuilder.addStatement(
                "val temp = createView_${
                    rootName.replace(
                        '.',
                        '_'
                    )
                }(context, attrs)"
            )
                .addStatement("var params: %T.LayoutParams? = null", viewGroupClz)
                .addStatement("if (root != null) {")
                .addStatement("params = root.generateLayoutParams(attrs)")
                .addStatement("if (!attachToRoot) {")
                .addStatement("temp.layoutParams = params")
                .addStatement("}}")
                .addStatement("if (root != null && attachToRoot) { root.addView(temp, params) }")
                .addStatement("if (root == null || !attachToRoot) { result = temp }")
                .addStatement("return result!!")
        }

        inflateFunBuilder.addStatement("} finally { parser.close() }")

        return FileSpec.builder(
            "com.dreamgyf.android.plugin.fastinflate.generate", name
        ).addType(
            TypeSpec.classBuilder(className)
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("context", contextClz)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("context", contextClz)
                        .initializer("context")
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addFunction(inflateFunBuilder.build())
                .addFunctions(createViewFuncMap.values)
                .build()
        ).build()
    }

    private fun genCreateViewFunc(name: String): FunSpec {
        val subFuncName = name.replace('.', '_')
        return FunSpec.builder("createView_$subFuncName")
            .addParameter("context", contextClz)
            .addParameter("attrs", attributeSetClz)
            .returns(viewClz)
            .addStatement("val view = $name(context, attrs)")
            .apply {
                if (name == "android.view.ViewStub") {
                    addStatement("view.layoutInflater = LayoutInflater.from(context)")
                }
            }
            .addStatement("return view")
            .build()
    }

    private fun String.fixViewName(): String {
        return if (indexOf('.') == -1) {
            "android.view.$this"
        } else {
            this
        }
    }
}