package com.dreamgyf.android.plugin.fastinflate

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.dreamgyf.android.plugin.fastinflate.exception.FastInflateException
import org.jetbrains.annotations.TestOnly
import java.lang.reflect.Method

class FastInflate private constructor(private val appContext: Context) {

    private val supportSdk =
        Build.VERSION.SDK_INT in Build.VERSION_CODES.LOLLIPOP..Build.VERSION_CODES.TIRAMISU

    private val inflateMethodMap = mutableMapOf<Int, Pair<Any, Method>?>()

    fun inflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = (root != null)
    ): View {
        if (!supportSdk) {
            return LayoutInflater.from(appContext).inflate(resource, root, attachToRoot)
        }

        return try {
            forceInflate(resource, root, attachToRoot)
        } catch (t: Throwable) {
            Log.e("FastInflate", "FastInflate failed, fall back to LayoutInflater.")
            LayoutInflater.from(appContext).inflate(resource, root, attachToRoot)
        }
    }

    @TestOnly
    @Throws(Throwable::class)
    fun inflateForTestOnly(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = (root != null)
    ): View {
        return forceInflate(resource, root, attachToRoot)
    }

    @Throws(Throwable::class)
    private fun forceInflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        var pair = inflateMethodMap[resource]
        if (pair == null) {
            if (inflateMethodMap.containsKey(resource)) {
                throw FastInflateException("Cannot find FastInflate method by this resource.")
            }

            try {
                val layoutName = appContext.resources.getResourceEntryName(resource)
                val className = "${GEN_PACKAGE_NAME}.FastInflate_Layout_$layoutName"
                val clz = Class.forName(className)
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
            } catch (_: Throwable) {
                inflateMethodMap[resource] = null
                throw FastInflateException("Cannot find FastInflate method by this resource.")
            }
        }

        val (instance: Any, method: Method) = pair

        return method.invoke(instance, resource, root, attachToRoot) as View
    }

    companion object {

        const val GEN_PACKAGE_NAME = "com.dreamgyf.android.plugin.fastinflate.generate"

        private var instance: FastInflate? = null

        fun from(context: Context): FastInflate {
            if (instance == null) {
                instance = FastInflate(context.applicationContext)
            }
            return instance!!
        }
    }
}