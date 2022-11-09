package com.dreamgyf.android.plugin.fastinflate

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.dreamgyf.android.plugin.fastinflate.exception.FastInflateException
import org.jetbrains.annotations.TestOnly
import java.lang.reflect.Method
import java.util.WeakHashMap

class FastInflate private constructor(private val context: Context) {

    fun inflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean = (root != null)
    ): View {
        if (!sSupportSdk) {
            Log.e("FastInflate", "Unsupported Android SDK, fall back to LayoutInflater.")
            return LayoutInflater.from(context).inflate(resource, root, attachToRoot)
        }

        return try {
            forceInflate(resource, root, attachToRoot)
        } catch (t: Throwable) {
            Log.e("FastInflate", "FastInflate failed, fall back to LayoutInflater.")
            LayoutInflater.from(context).inflate(resource, root, attachToRoot)
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

    @SuppressLint("ResourceType")
    @Throws(Throwable::class)
    @Synchronized
    private fun forceInflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        val resources = context.resources
        var pair = sInflateMethodMap[resource]
        if (pair == null) {
            if (sInflateMethodMap.containsKey(resource)) {
                throw FastInflateException("Cannot find FastInflate method by this resource.")
            }

            try {
                val layoutName = resources.getResourceEntryName(resource)
                val layoutPath = resources.getString(resource)
                val splitFiles = layoutPath.split('/')
                val layoutDirName = splitFiles[splitFiles.size - 2]
                val className = "${GEN_PACKAGE_NAME}.FastInflate_${
                    layoutDirName.replace(
                        '-',
                        '_'
                    )
                }_${layoutName}"
                val clz = Class.forName(className)
                val instance = clz.getConstructor().newInstance()
                val inflateMethod = clz.getMethod(
                    "inflate",
                    Context::class.java,
                    Int::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
                val inflateInfo = Pair(instance, inflateMethod)
                sInflateMethodMap[resource] = inflateInfo
                pair = inflateInfo
            } catch (_: Throwable) {
                sInflateMethodMap[resource] = null
                throw FastInflateException("Cannot find FastInflate method by this resource.")
            }
        }

        val (instance: Any, method: Method) = pair

        return method.invoke(instance, context, resource, root, attachToRoot) as View
    }

    companion object {

        private const val GEN_PACKAGE_NAME = "com.dreamgyf.android.plugin.fastinflate.generate"

        private val handler = Handler(Looper.getMainLooper())

        private var registeredLifecycle = false

        private val sSupportSdk =
            Build.VERSION.SDK_INT in Build.VERSION_CODES.LOLLIPOP..Build.VERSION_CODES.TIRAMISU

        private val sInstanceMap = WeakHashMap<Context, FastInflate>()

        private val sInflateMethodMap = mutableMapOf<Int, Pair<Any, Method>?>()

        @Synchronized
        fun from(context: Context): FastInflate {
            registerLifecycleIfNeed(context)

            val instance = sInstanceMap[context]
            if (instance != null) {
                return instance
            }

            val newInstance = FastInflate(context)
            sInstanceMap[context] = newInstance
            return newInstance
        }

        private fun registerLifecycleIfNeed(context: Context) {
            if (registeredLifecycle) {
                return
            }
            registeredLifecycle = true

            val application = context.applicationContext as? Application
            application?.registerActivityLifecycleCallbacks(object :
                Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                    gcForInstance()
                }

                private fun gcForInstance() {
                    System.gc()
                    //延时1s等待Activity Destroy完毕并被gc回收
                    handler.postDelayed({
                        //触发WeakHashMap内部回收机制
                        sInstanceMap.size
                    }, 1000)
                }
            })
        }
    }
}