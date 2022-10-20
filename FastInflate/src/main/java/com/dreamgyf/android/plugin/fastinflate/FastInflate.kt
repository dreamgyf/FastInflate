package com.dreamgyf.android.plugin.fastinflate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.lang.reflect.Method

class FastInflate private constructor(private val appContext: Context) {

    private val inflateMethodMap = mutableMapOf<Int, Pair<Any, Method>>()

    fun inflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = (root != null)
    ): View {
        try {
            var pair = inflateMethodMap[resource]
            if (pair == null) {
                val layoutName = appContext.resources.getResourceEntryName(resource)
                val clz =
                    Class.forName("com.dreamgyf.android.plugin.fastinflate.generate.FastInflate_Layout_$layoutName")
                val instance = clz.getConstructor(Context::class.java).newInstance(appContext)
                val inflateMethod = clz.getMethod(
                    "inflate",
                    Int::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
                val inflateInfo = Pair(instance, inflateMethod)
                inflateMethodMap[resource] = inflateInfo
                pair = inflateInfo
            }

            val instance = pair.first
            val method = pair.second

            return method.invoke(instance, resource, root, attachToRoot) as View
        } catch (t: Throwable) {
            Log.e("FastInflate", t.message ?: "")
            return LayoutInflater.from(appContext).inflate(resource, root, attachToRoot)
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