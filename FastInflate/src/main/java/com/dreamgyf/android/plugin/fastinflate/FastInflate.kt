package com.dreamgyf.android.plugin.fastinflate

import groovy.xml.XmlParser
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File


class FastInflate : Plugin<Project> {

    private val xmlParser by lazy {
        XmlParser()
    }

    override fun apply(project: Project) {
        println("apply plugin FastInflate for project ${project.name}")

        val projectPath = project.projectDir.absolutePath

        project.tasks.all { task ->
            val taskName = task.name
            if ("package.+Resources".toRegex().containsMatchIn(taskName)) {
                task.doLast {
                    val flavor = taskName.replace("package", "")
                        .replace("Resources", "")
                        .replaceFirstChar { it.lowercase() }

                    val packagedResPath = "${projectPath}/build/intermediates/packaged_res/$flavor"
                    println(packagedResPath)

                    val layoutPath = "$packagedResPath/layout"
                    val layoutDir = File(layoutPath)

                    if (!layoutDir.exists() || !layoutDir.isDirectory) {
                        return@doLast
                    }

                    layoutDir.forEachFiles {
                        println(it.name)

                        val node = xmlParser.parse(it)
                        println(node.name().toString())
                    }
                }
            }
        }
    }
}