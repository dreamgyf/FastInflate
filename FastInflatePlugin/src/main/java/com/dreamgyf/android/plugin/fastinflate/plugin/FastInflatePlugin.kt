package com.dreamgyf.android.plugin.fastinflate.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class FastInflatePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        println("apply plugin FastInflate for project ${project.name}")

        val variants = if (project.plugins.hasPlugin(AppPlugin::class.java)) {
            project.extensions.getByType(AppExtension::class.java).applicationVariants
        } else if (project.plugins.hasPlugin(LibraryPlugin::class.java)) {
            project.extensions.getByType(LibraryExtension::class.java).libraryVariants
        } else {
            null
        }

        variants?.all { variant ->
            val flavor = variant.flavorName
            val buildType = variant.buildType.name
            val buildVariant = variant.name

            val capBuildVariant = buildVariant.replaceFirstChar { it.uppercase() }
            val packageResourcesTaskName = "package${capBuildVariant}Resources"
            val packageResourcesTask = project.tasks.findByName(packageResourcesTaskName)

            packageResourcesTask?.let { task ->
                val genTaskName = "generate${capBuildVariant}FastInflateLayouts"
                val genTaskProvider = project.tasks.register(
                    genTaskName,
                    GenerateFastInflateLayoutTask::class.java,
                    flavor, buildType, buildVariant
                )
                genTaskProvider.get().dependsOn(task)

                variant.registerJavaGeneratingTask(genTaskProvider, genTaskProvider.get().genDir)

                println("Resister task $genTaskName for buildVariant: $buildVariant")
            }
        }
    }
}