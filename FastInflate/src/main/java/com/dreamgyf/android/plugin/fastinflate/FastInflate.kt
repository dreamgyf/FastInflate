package com.dreamgyf.android.plugin.fastinflate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

class FastInflate private constructor(private val appContext: Context) {

    fun inflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = (root != null)
    ): View {
        val layoutName = appContext.resources.getResourceEntryName(resource)

        return try {
            val clz =
                Class.forName("com.dreamgyf.android.plugin.fastinflate.generate.FastInflate_Layout_$layoutName")
            val instance = clz.getConstructor(Context::class.java).newInstance(appContext)
            val inflateMethod = clz.getMethod(
                "inflate",
                Int::class.java,
                ViewGroup::class.java,
                Boolean::class.java
            )
            inflateMethod.invoke(instance, resource, root, attachToRoot) as View
        } catch (t: Throwable) {
            Log.e("FastInflate", t.message ?: "")
            LayoutInflater.from(appContext).inflate(resource, root, attachToRoot)
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