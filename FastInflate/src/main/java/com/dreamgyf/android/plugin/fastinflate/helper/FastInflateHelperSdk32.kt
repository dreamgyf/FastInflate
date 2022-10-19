package com.dreamgyf.android.plugin.fastinflate.helper

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

object FastInflateHelperApi32 : IFastInflateHelper {

    @Throws(InflateException::class, IOException::class, XmlPullParserException::class)
    override fun advanceToRootNode(parser: XmlPullParser) {
        // Look for the root node.
        var type: Int
        while (parser.next().also { type = it } != XmlPullParser.START_TAG &&
            type != XmlPullParser.END_DOCUMENT) {
            // Empty
        }
        if (type != XmlPullParser.START_TAG) {
            throw InflateException(
                parser.positionDescription
                        + ": No start tag found!"
            )
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
}