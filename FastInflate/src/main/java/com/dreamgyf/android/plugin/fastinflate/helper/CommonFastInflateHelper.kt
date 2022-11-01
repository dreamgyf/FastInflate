package com.dreamgyf.android.plugin.fastinflate.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dreamgyf.android.plugin.fastinflate.FastInflate
import com.dreamgyf.android.plugin.fastinflate.exception.FastInflateException
import org.xmlpull.v1.XmlPullParser

object CommonFastInflateHelper : IFastInflateHelper {

    override fun advanceToRootNode(parser: XmlPullParser) {
        var type: Int
        while (parser.next().also { type = it } != XmlPullParser.START_TAG &&
            type != XmlPullParser.END_DOCUMENT) {
            // Empty
        }
        if (type != XmlPullParser.START_TAG) {
            throw FastInflateException(
                parser.positionDescription
                        + ": No start tag found!"
            )
        }
    }

    override fun advanceToNextNode(parser: XmlPullParser) {
        var type: Int
        while (parser.next().also { type = it } != XmlPullParser.START_TAG &&
            type != XmlPullParser.END_DOCUMENT) {
            // do nothing
        }
    }

    override fun consumeChildElements(parser: XmlPullParser) {
        var type: Int
        val currentDepth = parser.depth
        while (
            (parser.next().also { type = it } != XmlPullParser.END_TAG
                    || parser.depth > currentDepth)
            && type != XmlPullParser.END_DOCUMENT
        ) {
            // do nothing
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    override fun getInflaterPrivateFactory(inflater: LayoutInflater): LayoutInflater.Factory2? {
        val clz = LayoutInflater::class.java
        val field = clz.getDeclaredField("mPrivateFactory")
        field.isAccessible = true
        return field.get(inflater) as? LayoutInflater.Factory2?
    }

    @SuppressLint("PrivateApi")
    override fun newBlinkLayout(context: Context, attrs: AttributeSet): View {
        val clz = Class.forName("android.view.LayoutInflater\$BlinkLayout")
        val constructor = clz.getDeclaredConstructor(Context::class.java, AttributeSet::class.java)
        constructor.isAccessible = true
        return constructor.newInstance(context, attrs) as View
    }

    @SuppressLint("DiscouragedApi")
    override fun getResId(name: String, defType: String, defPackage: String): Int {
        return Resources.getSystem().getIdentifier(name, defType, defPackage)
    }

    private val ATTRS_THEME by lazy {
        intArrayOf(
            getResId("theme", "attr", "android")
        )
    }

    override fun getThemeAttrs(): IntArray {
        return ATTRS_THEME
    }

    private val ATTRS_VIEW_TAG by lazy {
        intArrayOf(
            android.R.attr.id,
            android.R.attr.value
        ).apply {
            sort()
        }
    }

    private val INDEX_VIEW_TAG_ID by lazy {
        ATTRS_VIEW_TAG.binarySearch(android.R.attr.id)
    }

    private val INDEX_VIEW_TAG_VALUE by lazy {
        ATTRS_VIEW_TAG.binarySearch(android.R.attr.value)
    }

    override fun parseViewTag(parser: XmlPullParser, view: View, attrs: AttributeSet) {
        val context = view.context
        val ta = context.obtainStyledAttributes(attrs, ATTRS_VIEW_TAG)
        val key = ta.getResourceId(INDEX_VIEW_TAG_ID, 0)
        val value = ta.getText(INDEX_VIEW_TAG_VALUE)
        view.setTag(key, value)
        ta.recycle()
    }

    private val tempTypedValue by lazy {
        TypedValue()
    }

    private val ATTRS_INCLUDE by lazy {
        intArrayOf(
            android.R.attr.id,
            android.R.attr.visibility
        ).apply {
            sort()
        }
    }

    private val INDEX_INCLUDE_ID by lazy {
        ATTRS_INCLUDE.binarySearch(android.R.attr.id)
    }

    private val INDEX_INCLUDE_VISIBILITY by lazy {
        ATTRS_INCLUDE.binarySearch(android.R.attr.visibility)
    }

    private const val TAG_MERGE = "merge"

    override fun parseInclude(
        parser: XmlPullParser,
        context: Context,
        parent: View,
        attrs: AttributeSet
    ) {
        if (parent !is ViewGroup) {
            throw FastInflateException("<include /> can only be used inside of a ViewGroup")
        }

        var layout = attrs.getAttributeResourceValue(null, "layout", 0)
        if (layout == 0) {
            val value = attrs.getAttributeValue(null, "layout")
            if (value == null || value.isEmpty()) {
                throw FastInflateException(
                    "You must specify a layout in the"
                            + " include tag: <include layout=\"@layout/layoutID\" />"
                )
            }

            layout = getResId(
                value.substring(1),
                "attr",
                context.packageName
            )
        }

        if (layout != 0 && context.theme.resolveAttribute(layout, tempTypedValue, true)) {
            layout = tempTypedValue.resourceId
        }

        if (layout == 0) {
            val value = attrs.getAttributeValue(null, "layout")
            throw FastInflateException(
                "You must specify a valid layout "
                        + "reference. The layout ID " + value + " is not valid."
            )
        }

        FastInflate.from(context).inflate(layout, parent, true)

        val childParser = context.resources.getLayout(layout)
        advanceToRootNode(childParser)
        val childRootNodeName = childParser.name

        // 非 <merge /> 标签
        if (childRootNodeName != TAG_MERGE) {
            val includeView = parent.getChildAt(parent.childCount - 1)

            val ta = context.obtainStyledAttributes(attrs, ATTRS_INCLUDE)
            val id = ta.getResourceId(INDEX_INCLUDE_ID, View.NO_ID)
            val visibility = ta.getInt(INDEX_INCLUDE_VISIBILITY, -1)
            ta.recycle()

            try {
                val params = parent.generateLayoutParams(attrs)
                includeView.layoutParams = params
            } catch (_: Exception) {
            }

            if (id != View.NO_ID) {
                includeView.id = id
            }

            when (visibility) {
                0 -> includeView.visibility = View.VISIBLE
                1 -> includeView.visibility = View.INVISIBLE
                2 -> includeView.visibility = View.GONE
            }
        }
    }

    override fun callOnFinishInflate(view: View) {
        val clz = View::class.java
        val method = clz.getDeclaredMethod("onFinishInflate")
        method.isAccessible = true
        method.invoke(view)
    }
}