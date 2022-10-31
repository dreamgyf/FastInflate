package com.dreamgyf.android.plugin.fastinflate.helper

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import org.xmlpull.v1.XmlPullParser

interface IFastInflateHelper {

    fun advanceToRootNode(parser: XmlPullParser) {
        CommonFastInflateHelper.advanceToRootNode(parser)
    }

    fun advanceToNextNode(parser: XmlPullParser) {
        CommonFastInflateHelper.advanceToNextNode(parser)
    }

    fun consumeChildElements(parser: XmlPullParser) {
        CommonFastInflateHelper.consumeChildElements(parser)
    }

    fun getInflaterPrivateFactory(inflater: LayoutInflater): LayoutInflater.Factory2? {
        return CommonFastInflateHelper.getInflaterPrivateFactory(inflater)
    }

    fun newBlinkLayout(context: Context, attrs: AttributeSet): View {
        return CommonFastInflateHelper.newBlinkLayout(context, attrs)
    }

    fun getResId(name: String, defType: String, defPackage: String): Int {
        return CommonFastInflateHelper.getResId(name, defType, defPackage)
    }

    fun callOnFinishInflate(view: View) {
        CommonFastInflateHelper.callOnFinishInflate(view)
    }
}