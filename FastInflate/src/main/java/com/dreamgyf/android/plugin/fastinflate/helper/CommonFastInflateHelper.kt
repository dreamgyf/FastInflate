package com.dreamgyf.android.plugin.fastinflate.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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

    override fun callOnFinishInflate(view: View) {
        val clz = View::class.java
        val method = clz.getDeclaredMethod("onFinishInflate")
        method.isAccessible = true
        method.invoke(view)
    }
}