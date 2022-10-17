package com.dreamgyf.android.plugin.fastinflate

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import java.io.File
import java.io.FileFilter
import java.util.jar.JarFile
import java.util.zip.ZipEntry


@Suppress("DEPRECATION")
class LayoutTransform : Transform() {

    override fun getName(): String {
        return "LayoutTransform"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_RESOURCES
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.PROJECT_ONLY
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
    }

    override fun transform(
        context: Context,
        inputs: MutableCollection<TransformInput>,
        referencedInputs: MutableCollection<TransformInput>,
        outputProvider: TransformOutputProvider?,
        isIncremental: Boolean
    ) {
        println("transform")
        inputs.forEach { input ->
            input.directoryInputs.forEach { dirInput ->
                dirInput.file.forEachFiles { file ->
                    println(file.name)
                }
            }
            input.jarInputs.forEach { jarInput ->
                if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
                    val jarFile = JarFile(jarInput.file)
                    val enumeration = jarFile.entries()

                    while (enumeration.hasMoreElements()) {
                        val jarEntry = enumeration.nextElement()
                        val classFileName = jarEntry.name
                        val zipEntry = ZipEntry(classFileName)

                        println(classFileName)
                    }
                }
            }
        }
    }
}

fun File.forEachFiles(action: (File) -> Unit) {
    val files = listFiles(FileFileFilter)
    files?.forEach {
        action(it)
    }
}

object FileFileFilter : FileFilter {
    override fun accept(pathname: File?): Boolean {
        return pathname?.isFile == true
    }
}