package com.dreamgyf.android.plugin.fastinflate.plugin

import com.squareup.kotlinpoet.*
import groovy.util.Node
import groovy.xml.XmlParser
import java.io.File

const val GEN_PACKAGE_NAME = "com.dreamgyf.android.plugin.fastinflate.generate"

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

    fun generate(layoutDirName: String, layoutName: String, file: File): FileSpec {
        createViewFuncMap.clear()
        rGenerateFuncList.clear()

        val genName = "FastInflate_${layoutDirName.replace('-', '_')}_${layoutName}"

        val root = xmlParser.parse(file)

        return FileSpec.builder(
            GEN_PACKAGE_NAME,
            genName
        )
            .addType(generateClass(genName, root))
            .build()
    }

    private fun generateClass(genName: String, root: Node): TypeSpec {
        return TypeSpec.classBuilder(
            ClassName(
                GEN_PACKAGE_NAME, genName
            )
        )
            .primaryConstructor(
                FunSpec.constructorBuilder().build()
            )
            .addProperty(
                PropertySpec.builder("layoutInflater", layoutInflaterClz)
                    .mutable()
                    .addModifiers(KModifier.PRIVATE)
                    .addModifiers(KModifier.LATEINIT)
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
                        "%T.getThemeAttrs()",
                        helperClz
                    )
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
            .addFunction(generateInflateFunc(root))
            .addFunction(genInflateChildrenFunc(root))
            .addFunctions(rGenerateFuncList)
            .addFunctions(createViewFuncMap.values)
            .build()
    }

    private fun generateInflateFunc(root: Node): FunSpec {
        val inflateFunBuilder = FunSpec.builder("inflate")
            .addParameter("context", contextClz)
            .addParameter("resource", Int::class.java)
            .addParameter("root", viewGroupClz.copy(nullable = true))
            .addParameter("attachToRoot", Boolean::class.java)
            .returns(viewClz)
            .addStatement("layoutInflater = %T.from(context)", layoutInflaterClz)
            .addStatement(
                "privateFactory = %T.getInflaterPrivateFactory(layoutInflater)",
                helperClz
            )
            .addStatement("val res = context.resources")
            .addStatement("val parser = res.getLayout(resource)")
            .beginControlFlow("try")
            .addStatement("val attrs = %T.asAttributeSet(parser)", xmlClz)
            .addStatement("var result: %T = root", viewClz.copy(nullable = true))
            .addStatement("%T.advanceToRootNode(parser)", helperClz)

        val rootNodeName = root.name().toString()
        if (rootNodeName == TAG_MERGE) {
            inflateFunBuilder
                .beginControlFlow("if (root == null || !attachToRoot)")
                .addStatement(
                    "throw %T(%S)",
                    fastInflateExceptionClz,
                    "<merge /> can be used only with a valid ViewGroup root and attachToRoot=true"
                )
                .endControlFlow()
                .addStatement("inflateChildren(parser, root, context, attrs)")
        } else {
            inflateFunBuilder.addStatement(
                "val temp = %L(root, context, attrs, false)",
                genCreateViewFunc(root)
            )
                .addStatement("var params: %T.LayoutParams? = null", viewGroupClz)
                .beginControlFlow("if (root != null)")
                .addStatement("params = root.generateLayoutParams(attrs)")
                .beginControlFlow("if (!attachToRoot)")
                .addStatement("temp.layoutParams = params")
                .endControlFlow()
                .endControlFlow()
                .addStatement("inflateChildren(parser, temp, context, attrs)")
                .beginControlFlow("if (root != null && attachToRoot)")
                .addStatement("root.addView(temp, params)")
                .endControlFlow()
                .beginControlFlow("if (root == null || !attachToRoot)")
                .addStatement("result = temp")
                .endControlFlow()
        }

        inflateFunBuilder
            .addStatement("return result!!")
            .nextControlFlow("finally")
            .addStatement("parser.close()")
            .endControlFlow()

        return inflateFunBuilder.build()
    }

    private fun genInflateChildrenFunc(root: Node): FunSpec {
        val funSpecBuilder = FunSpec.builder("inflateChildren")
            .addParameter("parser", xmlPullParserClz)
            .addParameter("parent", viewClz)
            .addParameter("context", contextClz)
            .addParameter("attrs", attributeSetClz)

        var pendingRequestFocus = false
        val rootNodeName = root.name().toString()
        val finishInflate = rootNodeName != TAG_MERGE

        val children = root.children()
        if (children.isEmpty()) {
            return funSpecBuilder.build()
        }

        children.forEachIndexed { index, node ->
            (node as? Node)?.let {
                val nodeName = it.name().toString()
                if (nodeName == TAG_REQUEST_FOCUS) {
                    pendingRequestFocus = true
                }
                val isLastNode = index == children.size - 1
                val funcName = rGenerate(
                    "",
                    it,
                    index,
                    pendingRequestFocus && isLastNode,
                    finishInflate && isLastNode
                )
                funSpecBuilder.addStatement("$funcName(parser, parent, context, attrs)")
            }
        }

        return funSpecBuilder.build()
    }

    private fun rGenerate(
        funcPrefix: String,
        node: Node,
        index: Int,
        requestFocus: Boolean,
        finishInflate: Boolean
    ): String {
        val funcName = "rInflate_${funcPrefix}_${index}"
        val childFuncPrefix = "${funcPrefix}_${index}"

        val funSpecBuilder = FunSpec.builder(funcName)
            .addParameter("parser", xmlPullParserClz)
            .addParameter("parent", viewClz)
            .addParameter("context", contextClz)
            .addParameter("attrs", attributeSetClz)

        funSpecBuilder.addStatement("%T.advanceToNextNode(parser)", helperClz)

        val nodeName = node.name().toString()
        if (nodeName == TAG_REQUEST_FOCUS) {
            funSpecBuilder.addStatement("%T.consumeChildElements(parser)", helperClz)
        } else if (nodeName == TAG_TAG) {
            funSpecBuilder
                .addStatement("%T.parseViewTag(parser, parent, attrs)", helperClz)
                .addStatement("%T.consumeChildElements(parser)", helperClz)
        } else if (nodeName == TAG_INCLUDE) {
            funSpecBuilder
                .addStatement("%T.parseInclude(parser, context, parent, attrs)", helperClz)
                .addStatement("%T.consumeChildElements(parser)", helperClz)
        } else if (nodeName == TAG_MERGE) {
            funSpecBuilder.addStatement(
                "throw %T(%S)",
                fastInflateExceptionClz,
                "<merge /> must be the root element"
            )
        } else {
            funSpecBuilder.addStatement(
                "val view = %L(parent, context, attrs, false)",
                genCreateViewFunc(node)
            )
                .addStatement("val viewGroup = parent as %T", viewGroupClz)
                .addStatement("val params = viewGroup.generateLayoutParams(attrs)")

            val children = node.children()
            var pendingRequestFocus = false
            children.forEachIndexed { i, n ->
                (n as? Node)?.let {
                    val childNodeName = it.name().toString()
                    if (childNodeName == TAG_REQUEST_FOCUS) {
                        pendingRequestFocus = true
                    }
                    val isLastNode = i == children.size - 1
                    val childFuncName = rGenerate(
                        childFuncPrefix,
                        it,
                        i,
                        pendingRequestFocus && isLastNode,
                        isLastNode
                    )
                    funSpecBuilder.addStatement("$childFuncName(parser, view, context, attrs)")
                }
            }

            funSpecBuilder.addStatement("viewGroup.addView(view, params)")
        }

        if (requestFocus) {
            funSpecBuilder.addStatement("parent.restoreDefaultFocus()")
        }

        if (finishInflate) {
            funSpecBuilder.addStatement("%T.callOnFinishInflate(parent)", helperClz)
        }

        rGenerateFuncList.add(funSpecBuilder.build())

        return funcName
    }

    private fun genCreateViewFunc(node: Node): String {
        val nodeName = node.name().toString()
        val className = if (nodeName == "view") {
            node.attribute("class").toString()
        } else {
            nodeName
        }
        val fixedClassName = className.fixViewName()
        val funcName = "createView_${fixedClassName.replace('.', '_')}"

        if (!createViewFuncMap.contains(funcName)) {
            val funSpecBuilder = FunSpec.builder(funcName)
                .addParameter("parent", viewClz.copy(nullable = true))
                .addParameter("context", contextClz)
                .addParameter("attrs", attributeSetClz)
                .addParameter("ignoreThemeAttr", Boolean::class.java)
                .returns(viewClz)
                .addStatement("var ctx = context")
                .beginControlFlow("if (!ignoreThemeAttr)")
                .addStatement("val ta = context.obtainStyledAttributes(attrs, ATTRS_THEME)")
                .addStatement("val themeResId = ta.getResourceId(0, 0)")
                .beginControlFlow("if (themeResId != 0)")
                .addStatement("ctx = %T(context, themeResId)", contextThemeWrapperClz)
                .endControlFlow()
                .addStatement("ta.recycle()")
                .endControlFlow()
            if (nodeName == TAG_1995) {
                funSpecBuilder.addStatement("return %T.newBlinkLayout(ctx, attrs)", helperClz)
            } else {
                funSpecBuilder
                    .addStatement("var view: %T? = null", viewClz)
                    .beginControlFlow("if (layoutInflater.factory2 != null)")
                    .addStatement(
                        "view = layoutInflater.factory2.onCreateView(parent, %S, ctx, attrs)",
                        className
                    )
                    .nextControlFlow("else if (layoutInflater.factory != null)")
                    .addStatement(
                        "view = layoutInflater.factory.onCreateView(%S, ctx, attrs)",
                        className
                    )
                    .endControlFlow()
                    .beginControlFlow("if (view == null)")
                    .addStatement(
                        "view = privateFactory?.onCreateView(%S, ctx, attrs)",
                        className
                    )
                    .endControlFlow()
                    .beginControlFlow("if (view == null)")
                    .addStatement("view = $fixedClassName(ctx, attrs)")
                    .endControlFlow()
                    .apply {
                        if (fixedClassName == "android.view.ViewStub") {
                            addStatement("(view as? android.view.ViewStub?)?.layoutInflater = layoutInflater.cloneInContext(context)")
                        }
                    }
                    .addStatement("return view")
            }

            createViewFuncMap[funcName] = funSpecBuilder.build()
        }

        return funcName
    }

    private fun String.fixViewName(): String {
        return if (indexOf('.') == -1) {
            if (this == TAG_1995) {
                this
            } else {
                convertSysView(this)
            }
        } else {
            this
        }
    }
}