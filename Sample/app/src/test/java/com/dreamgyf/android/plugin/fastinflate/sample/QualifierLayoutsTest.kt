package com.dreamgyf.android.plugin.fastinflate.sample

import android.os.Build
import android.widget.TextView
import com.dreamgyf.android.plugin.fastinflate.FastInflate
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class QualifierLayoutsTest {

    private val appContext = RuntimeEnvironment.getApplication().applicationContext

    @Test
    @Config(minSdk = Build.VERSION_CODES.LOLLIPOP_MR1, maxSdk = Build.VERSION_CODES.TIRAMISU)
    fun testSDKs() {
        val view = FastInflate.from(appContext).inflateForTestOnly(R.layout.layout_qualifiers, null)
        val textView = view.findViewById<TextView>(R.id.textview)
        assertEquals(textView.text, "layout-v${Build.VERSION.SDK_INT}")
    }

    @Test
    @Config(sdk = [Build.VERSION_CODES.LOLLIPOP])
    fun testNoQualifiers() {
        val view = FastInflate.from(appContext).inflateForTestOnly(R.layout.layout_qualifiers, null)
        val textView = view.findViewById<TextView>(R.id.textview)
        assertEquals(textView.text, "layout")
    }
}