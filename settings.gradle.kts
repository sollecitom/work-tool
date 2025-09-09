@file:Suppress("UnstableApiUsage")

rootProject.name = "work-tool"

fun resource(vararg pathSegments: String) = subProject(rootFolder = "resources", pathSegments = pathSegments)

fun library(vararg pathSegments: String) = subProject(rootFolder = "libs", pathSegments = pathSegments)

fun component(vararg pathSegments: String) = subProject(rootFolder = "components", pathSegments = pathSegments)

fun example(vararg pathSegments: String) = subProject(rootFolder = "example", pathSegments = pathSegments, excludeRootFolderFromGroupName = false)

fun subProject(rootFolder: String, vararg pathSegments: String, excludeRootFolderFromGroupName: Boolean = true) {

    val projectName = pathSegments.last()
    val path = listOf(rootFolder) + pathSegments.dropLast(1)
    val group = if (excludeRootFolderFromGroupName) path.minus(rootFolder).joinToString(separator = "-") else path.joinToString(separator = "-", prefix = "${rootProject.name}-")
    val directory = path.joinToString(separator = "/", prefix = "./")
    val fullProjectName = "${if (group.isEmpty()) "" else "$group-"}$projectName"
    println("Michele: $fullProjectName")

    include(fullProjectName)
    project(":$fullProjectName").projectDir = mkdir("$directory/$projectName")
}

fun includeProject(name: String) {

    apply("$name/settings.gradle.kts")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

component("app")