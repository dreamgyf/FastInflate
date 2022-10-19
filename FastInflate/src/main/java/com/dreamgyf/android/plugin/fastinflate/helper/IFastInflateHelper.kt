package com.dreamgyf.android.plugin.fastinflate.helper

import android.content.Context
import android.util.AttributeSet
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

interface IFastInflateHelper {

    @Throws(InflateException::class, IOException::class, XmlPullParserException::class)
    fun advanceToRootNode(parser: XmlPullParser)

    fun getInflaterPrivateFactory(inflater: LayoutInflater): LayoutInflater.Factory2?

    fun newBlinkLayout(context: Context, attrs: AttributeSet): View
}