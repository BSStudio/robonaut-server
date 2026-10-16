package hu.bsstudio.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.repositories

class DependencyManagementPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.repositories {
            mavenCentral()
        }

        val libs = project.extensions.getByType<VersionCatalogsExtension>().named("libs")

        project.dependencies {
            add("implementation", platform(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES))
            add("implementation", platform("org.springframework.cloud:spring-cloud-dependencies:2025.1.0"))

            // Security overrides, versions live in gradle/libs.versions.toml
            add("implementation", platform(libs.findLibrary("jackson-bom").get()))
            add("implementation", platform(libs.findLibrary("jackson2-bom").get()))
            constraints {
                add("implementation", libs.findLibrary("amqp-client").get())
            }
        }
    }
}
