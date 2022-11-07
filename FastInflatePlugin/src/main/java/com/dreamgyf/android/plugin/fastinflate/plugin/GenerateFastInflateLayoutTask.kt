package com.dreamgyf.android.plugin.fastinflate.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.FileFilter
import javax.inject.Inject

open class GenerateFastInflateLayoutTask
@Inject constructor(flavor: String, buildType: String, buildVariant: String) : DefaultTask() {

    private val resDir =
        File("${project.buildDir}/intermediates/packaged_res/$buildVariant")

    private val layoutDir =
        File("${project.buildDir}/intermediates/packaged_res/$buildVariant/layout")

    @Internal
    val genDir = File(
        StringBuilder("${project.buildDir}/generated/source/fastinflate").apply {
            if (flavor.isNotEmpty()) {
                append("/$flavor")
            }
            append("/$buildType")
        }.toString()
    )

    @TaskAction
    fun doTask() {
        if (!resDir.exists() || !resDir.isDirectory) {
            return
        }

        resDir.listFiles()?.forEach { dir ->
            val dirName = dir.name
            if (dir.isDirectory && dirName.startsWith("layout")) {
                dir.forEachFiles { file ->
                    val fileName = file.name

                    val suffixIndex = fileName.indexOf(".xml")
                    if (suffixIndex == -1) {
                        println("$fileName isn't xml file, skip it.")
                        return@forEachFiles
                    }

                    val layoutName = fileName.substring(0, suffixIndex)

                    println("Generating $layoutName...")

                    val genFile = FastInflateLayoutGenerator.generate(dirName, layoutName, file)

                    if (!genDir.exists()) {
                        genDir.mkdirs()
                    }

                    genFile.writeTo(genDir)
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