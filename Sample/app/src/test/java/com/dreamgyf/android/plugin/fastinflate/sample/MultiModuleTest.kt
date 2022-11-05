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
@Config(sdk = [Build.VERSION_CODES.S_V2])
class MultiModuleTest {

    @Test
    fun test() {
        val appContext = RuntimeEnvironment.getApplication().applicationContext

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