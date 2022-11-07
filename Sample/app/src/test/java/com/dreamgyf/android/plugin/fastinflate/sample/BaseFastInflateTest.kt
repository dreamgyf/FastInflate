package com.dreamgyf.android.plugin.fastinflate.sample

import android.os.Build
import com.dreamgyf.android.plugin.fastinflate.FastInflate
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = Build.VERSION_CODES.LOLLIPOP, maxSdk = Build.VERSION_CODES.TIRAMISU)
class BaseFastInflateTest {

    private val appContext = RuntimeEnvironment.getApplication().applicationContext

    @Test
    fun baseTest() {

        FastInflate.from(appContext).inflateForTestOnly(R.layout.activity_main, null)
    }
}