package com.dreamgyf.android.plugin.fastinflate

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class FastInflate private constructor(private val appContext: Context) {

    fun inflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = (root != null)
    ) {
        val layoutName = appContext.resources.getResourceEntryName(resource)

        try {
            val clz = Class.forName("com.dreamgyf.android.plugin.fastinflate.generate.FastInflate_Layout_$layoutName")
            val instance = clz.getConstructor().newInstance()
            val inflateMethod = clz.getMethod("inflate")
            inflateMethod.invoke(instance)
        } catch (t: Throwable) {
            Log.e("FastInflate", t.message ?: "")
        }
    }

    companion object {

        var instance: FastInflate? = null

        fun from(context: Context): FastInflate {
            if (instance == null) {
                instance = FastInflate(context.applicationContext)
            }
            return instance!!
        }
    }
}