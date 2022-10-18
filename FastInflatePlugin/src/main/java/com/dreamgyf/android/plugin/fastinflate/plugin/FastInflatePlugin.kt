package com.dreamgyf.android.plugin.fastinflate.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

val resRegex = "package.+Resources".toRegex()

class FastInflatePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("apply plugin FastInflate for project ${project.name}")

        project.tasks.all { task ->
            val taskName = task.name
            if (resRegex.containsMatchIn(taskName)) {
                val flavor = taskName.replace("package", "")
                    .replace("Resources", "")
                    .replaceFirstChar { it.lowercase() }

                val genTaskProvider = project.tasks.register(
                    "generate${flavor.replaceFirstChar { it.uppercase() }}FastInflateLayouts",
                    GenerateFastInflateLayoutTask::class.java, flavor
                )
                genTaskProvider.get().dependsOn(task)

                if (project.plugins.hasPlugin(AppPlugin::class.java)) {
                    project.extensions.getByType(AppExtension::class.java).applicationVariants.all {
                        it.registerJavaGeneratingTask(genTaskProvider, genTaskProvider.get().genDir)
                    }
                } else if (project.plugins.hasPlugin(LibraryPlugin::class.java)) {
                    project.extensions.getByType(LibraryExtension::class.java).libraryVariants.all {
                        it.registerJavaGeneratingTask(genTaskProvider, genTaskProvider.get().genDir)
                    }
                }
            }
        }
    }
}