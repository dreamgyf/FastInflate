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
@Config(minSdk = Build.VERSION_CODES.LOLLIPOP, maxSdk = Build.VERSION_CODES.TIRAMISU)
class MultiModuleTest {

    private val appContext = RuntimeEnvironment.getApplication().applicationContext

    @Test
    fun test() {
        val appLayout = FastInflate.from(appContext).inflateForTestOnly(
            com.dreamgyf.android.plugin.fastinflate.sample.R.layout.layout_app,
            null
        )
        val module1Layout = FastInflate.from(appContext).inflateForTestOnly(
            com.dreamgyf.android.plugin.fastinflate.sample.module1.R.layout.layout_module1,
            null
        )

        val tvApp = appLayout.findViewById<TextView>(R.id.textview)
        val tvModule1 = module1Layout.findViewById<TextView>(R.id.textview)

        assertEquals(tvApp.text, "app")
        assertEquals(tvModule1.text, "Module1")
    }
}