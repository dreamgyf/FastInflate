package com.dreamgyf.android.plugin.fastinflate.sample

import android.os.Build
import com.dreamgyf.android.plugin.fastinflate.FastInflate
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.S_V2])
class BaseFastInflateTest {
    @Test
    fun baseTest() {
        val appContext = RuntimeEnvironment.getApplication().applicationContext

        FastInflate.from(appContext).inflateForTestOnly(R.layout.activity_main, null)
    }
}