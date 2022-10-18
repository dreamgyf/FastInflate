package com.dreamgyf.android.plugin.fastinflate.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileFilter
import javax.inject.Inject

open class GenerateFastInflateLayoutTask
@Inject constructor(private val flavor: String) : DefaultTask() {

    private val layoutDir = File("${project.buildDir}/intermediates/packaged_res/$flavor/layout")

    @Internal
    val genDir = File("${project.buildDir}/generated/source/fastinflate")

    @TaskAction
    fun doTask() {
        if (!layoutDir.exists() || !layoutDir.isDirectory) {
            return
        }

        layoutDir.forEachFiles { file ->
            val fileName = file.name

            val suffixIndex = fileName.indexOf(".xml")
            if (suffixIndex == -1) {
                println("$fileName isn't xml file, skip it.")
                return@forEachFiles
            }

            val layoutName = fileName.substring(0, suffixIndex)
            val genName = "FastInflate_Layout_$layoutName"

            println("Generating $genName...")

            val genFile = FastInflateLayoutGenerator.generate(genName, file)

            if (!genDir.exists()) {
                genDir.mkdirs()
            }

            genFile.writeTo(genDir)
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