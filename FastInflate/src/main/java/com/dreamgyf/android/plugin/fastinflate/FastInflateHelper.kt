package com.dreamgyf.android.plugin.fastinflate

import android.view.InflateException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

object FastInflateHelper {

    @Throws(InflateException::class, IOException::class, XmlPullParserException::class)
    fun advanceToRootNode(parser: XmlPullParser) {
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
}