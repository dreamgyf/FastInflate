package com.dreamgyf.android.plugin.fastinflate.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.AttributeSet
import android.view.InflateException
import android.view.LayoutInflater
import android.view.LayoutInflater.Factory2
import android.view.View
import android.view.FastInflateViewStub
import com.dreamgyf.android.plugin.fastinflate.exception.FastInflateException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

object FastInflateHelper : IFastInflateHelper {

    @Throws(InflateException::class, IOException::class, XmlPullParserException::class)
    override fun advanceToRootNode(parser: XmlPullParser) {
        getHelperImpl().advanceToRootNode(parser)
    }

    override fun getInflaterPrivateFactory(inflater: LayoutInflater): Factory2? {
        return getHelperImpl().getInflaterPrivateFactory(inflater)
    }

    override fun newBlinkLayout(context: Context, attrs: AttributeSet): View {
        return getHelperImpl().newBlinkLayout(context, attrs)
    }

    @SuppressLint("DiscouragedApi")
    fun getResId(name: String, defType: String, defPackage: String): Int {
        return Resources.getSystem().getIdentifier(name, defType, defPackage)
    }

    fun callOnFinishInflate(view: View) {
        FastInflateViewStub.callOnFinishInflate(view)
    }

    private fun getHelperImpl(): IFastInflateHelper {
        return when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.S_V2 -> FastInflateHelperApi32
            Build.VERSION_CODES.TIRAMISU -> FastInflateHelperApi33
            else -> throw FastInflateException("Unsupported android API.")
        }
    }
}