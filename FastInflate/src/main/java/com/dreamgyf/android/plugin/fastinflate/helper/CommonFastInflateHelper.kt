package com.dreamgyf.android.plugin.fastinflate.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.dreamgyf.android.plugin.fastinflate.exception.FastInflateException
import org.xmlpull.v1.XmlPullParser

object CommonFastInflateHelper: IFastInflateHelper {

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

    @SuppressLint("DiscouragedPrivateApi")
    override fun getInflaterPrivateFactory(inflater: LayoutInflater): LayoutInflater.Factory2? {
        val clz = LayoutInflater::class.java
        val field = clz.getDeclaredField("mPrivateFactory")
        field.isAccessible = true
        return field.get(inflater) as? LayoutInflater.Factory2?
    }

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

    override fun callOnFinishInflate(view: View) {
        val clz = View::class.java
        val method = clz.getDeclaredMethod("onFinishInflate")
        method.isAccessible = true
        method.invoke(view)
    }
}