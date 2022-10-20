package com.dreamgyf.android.plugin.fastinflate.plugin

import com.squareup.kotlinpoet.*
import groovy.util.Node
import groovy.xml.XmlParser
import java.io.File

object FastInflateLayoutGenerator {

    private const val TAG_MERGE = "merge"
    private const val TAG_INCLUDE = "include"
    private const val TAG_1995 = "blink"
    private const val TAG_REQUEST_FOCUS = "requestFocus"
    private const val TAG_TAG = "tag"

    private val xmlParser by lazy {
        XmlParser()
    }

    private val createViewFuncMap = mutableMapOf<String, FunSpec>()
    private val rGenerateFuncList = mutableListOf<FunSpec>()

    fun generate(name: String, file: File): FileSpec {
        createViewFuncMap.clear()
        rGenerateFuncList.clear()

        val root = xmlParser.parse(file)

        val className = ClassName(
            "com.dreamgyf.android.plugin.fastinflate.generate", name
        )

        val inflateFunBuilder = FunSpec.builder("inflate")
            .addParameter("resource", Int::class.java)
            .addParameter("root", viewGroupClz.copy(nullable = true))
            .addParameter("attachToRoot", Boolean::class.java)
            .returns(viewClz)
            .addStatement(
                "privateFactory = %T.getInflaterPrivateFactory(layoutInflater)",
                helperClz
            )
            .addStatement("val res = context.resources")
            .addStatement("val parser = res.getLayout(resource)")
            .addStatement("try {")
            .addStatement("val attrs = %T.asAttributeSet(parser)", xmlClz)
            .addStatement("var result: %T = root", viewClz.copy(nullable = true))
            .addStatement("%T.advanceToRootNode(parser)", helperClz)

        var rootName = root.name().toString()
        if (rootName == TAG_MERGE) {
            //TODO
        } else {
            if (rootName == "view") {
                rootName = root.attribute("class").toString()
            }

            genCreateViewFunc(rootName)

            inflateFunBuilder.addStatement(
                "val temp = createView_${
                    rootName.replace(
                        '.',
                        '_'
                    )
                }(root, context, attrs, false)"
            )
                .addStatement("var params: %T.LayoutParams? = null", viewGroupClz)
                .addStatement("if (root != null) {")
                .addStatement("params = root.generateLayoutParams(attrs)")
                .addStatement("if (!attachToRoot) {")
                .addStatement("temp.layoutParams = params")
                .addStatement("}}")
                .addStatement("inflateChildren(parser, temp, context, attrs)")
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
                .addProperty(
                    PropertySpec.builder("layoutInflater", layoutInflaterClz)
                        .initializer("%T.from(context)", layoutInflaterClz)
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("privateFactory", factory2Clz.copy(nullable = true))
                        .initializer("null")
                        .mutable()
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addProperty(
                    PropertySpec.builder("ATTRS_THEME", intArrayClz)
                        .initializer(
                            "intArrayOf(%T.getResId(\"theme\", \"attr\", \"android\"))",
                            helperClz
                        )
                        .addModifiers(KModifier.PRIVATE)
                        .build()
                )
                .addFunction(inflateFunBuilder.build())
                .addFunction(genInflateChildrenFunc(root))
                .addFunctions(rGenerateFuncList)
                .addFunctions(createViewFuncMap.values)
                .build()
        ).build()
    }

    private fun genInflateChildrenFunc(root: Node): FunSpec {
        val funSpecBuilder = FunSpec.builder("inflateChildren")
            .addParameter("parser", xmlPullParserClz)
            .addParameter("parent", viewClz)
            .addParameter("context", contextClz)
            .addParameter("attrs", attributeSetClz)

        val children = root.children()
        if (children.isEmpty()) {
            return funSpecBuilder.build()
        }

        children.forEachIndexed { index, node ->
            (node as? Node)?.let {
                val funcName = rGenerate("", it, index)
                funSpecBuilder.addStatement("$funcName(parser, parent, context, attrs)")
            }
        }

        return funSpecBuilder.build()
    }

    private fun rGenerate(
        funcPrefix: String,
        node: Node,
        index: Int
    ): String {
        val funcName = "rInflate_${funcPrefix}_${index}"
        val childFuncPrefix = "${funcPrefix}_${index}"

        val funSpecBuilder = FunSpec.builder(funcName)
            .addParameter("parser", xmlPullParserClz)
            .addParameter("parent", viewClz)
            .addParameter("context", contextClz)
            .addParameter("attrs", attributeSetClz)

        funSpecBuilder.addStatement("%T.advanceToNextNode(parser)", helperClz)

        var name = node.name().toString()
        if (name == TAG_REQUEST_FOCUS) {
            funSpecBuilder.addStatement("parent.restoreDefaultFocus()")
        } else if (name == TAG_TAG) {
            //TODO
        } else if (name == TAG_INCLUDE) {
            //TODO
        } else if (name == TAG_MERGE) {
            funSpecBuilder.addStatement(
                "throw %T(\"<merge /> must be the root element\")",
                fastInflateExceptionClz
            )
        } else {
            if (name == "view") {
                name = node.attribute("class").toString()
            }
            genCreateViewFunc(name)

            funSpecBuilder.addStatement(
                "val view = createView_${
                    name.replace(
                        '.',
                        '_'
                    )
                }(parent, context, attrs, false)"
            )
                .addStatement("val viewGroup = parent as %T", viewGroupClz)
                .addStatement("val params = viewGroup.generateLayoutParams(attrs)")

            val children = node.children()
            children.forEachIndexed { i, n ->
                (n as? Node)?.let {
                    val childFuncName = rGenerate(childFuncPrefix, it, i)
                    funSpecBuilder.addStatement("$childFuncName(parser, view, context, attrs)")
                }
            }

            funSpecBuilder.addStatement("viewGroup.addView(view, params)")
        }

        //TODO: include后merge标签不执行以下代码
        funSpecBuilder.addStatement("%T.callOnFinishInflate(parent)", helperClz)

        rGenerateFuncList.add(funSpecBuilder.build())

        return funcName
    }

    private fun genCreateViewFunc(name: String) {
        if (createViewFuncMap.contains(name)) return

        val subFuncName = name.replace('.', '_')
        val fixedName = name.fixViewName()
        val funSpecBuilder = FunSpec.builder("createView_$subFuncName")
            .addParameter("parent", viewClz.copy(nullable = true))
            .addParameter("context", contextClz)
            .addParameter("attrs", attributeSetClz)
            .addParameter("ignoreThemeAttr", Boolean::class.java)
            .returns(viewClz)
            .addStatement("var ctx = context")
            .addStatement("if (!ignoreThemeAttr) {")
            .addStatement("val ta = context.obtainStyledAttributes(attrs, ATTRS_THEME)")
            .addStatement("val themeResId = ta.getResourceId(0, 0)")
            .addStatement(
                "if (themeResId != 0) { ctx = %T(context, themeResId) }",
                contextThemeWrapperClz
            )
            .addStatement("ta.recycle() }")

        if (name == TAG_1995) {
            val funSpec = funSpecBuilder
                .addStatement("return %T.newBlinkLayout(ctx, attrs)", helperClz)
                .build()
            createViewFuncMap[name] = funSpec
            return
        }

        funSpecBuilder
            .addStatement("var view: %T? = null", viewClz)
            .addStatement(
                "if (layoutInflater.factory2 != null) { view = layoutInflater.factory2.onCreateView(parent, %L, ctx, attrs) }",
                "\"$name\""
            )
            .addStatement(
                "else if (layoutInflater.factory != null) { view = layoutInflater.factory.onCreateView(%L, ctx, attrs) }",
                "\"$name\""
            )
            .addStatement(
                "if (view == null) { view = privateFactory?.onCreateView(%L, ctx, attrs) }",
                "\"$name\""
            )
            .addStatement("if (view == null) { view = $fixedName(ctx, attrs) }")
            .apply {
                if (name == "android.view.ViewStub") {
                    addStatement("view.layoutInflater = LayoutInflater.from(context)")
                }
            }
            .addStatement("return view")

        createViewFuncMap[name] = funSpecBuilder.build()
    }

    private fun String.fixViewName(): String {
        return if (indexOf('.') == -1) {
            convertSysView(this)
        } else {
            this
        }
    }
}