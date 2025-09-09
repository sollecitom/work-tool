import com.palantir.gradle.gitversion.GitVersionPlugin
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import sollecitom.plugins.Plugins
import sollecitom.plugins.RepositoryConfiguration
import sollecitom.plugins.conventions.task.dependency.update.DependencyUpdateConvention
import sollecitom.plugins.conventions.task.dependency.version.MinimumDependencyVersion
import sollecitom.plugins.conventions.task.dependency.version.MinimumDependencyVersionConventions
import sollecitom.plugins.conventions.task.maven.publish.MavenPublishConvention
import sollecitom.plugins.conventions.task.test.AggregateTestMetricsConventions
import sollecitom.plugins.conventions.task.test.TestTaskConventions
import java.nio.file.Path
import java.nio.file.Paths

buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath(libs.sollecitom.gradle.plugins.base)
    }
}

repositories {
    RepositoryConfiguration.BuildScript.apply(this)
}

plugins {
    `java-library`
    idea
    alias(libs.plugins.com.palantir.git.version)
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

apply<GitVersionPlugin>()
apply<DependencyUpdateConvention>()

val parentProject = this
val projectGroup: String by properties
val currentVersion: String by project
val versionDetails: Closure<VersionDetails> by extra
val gitVersion = versionDetails()
val libsFolder: Path = rootProject.projectDir.path.let { Paths.get(it) }.resolve("libs")
val servicesFolder: Path = rootProject.projectDir.path.let { Paths.get(it) }.resolve("services")
val toolsFolder: Path = rootProject.projectDir.path.let { Paths.get(it) }.resolve("tools")
val resourceFolder: Path = rootProject.projectDir.path.let { Paths.get(it) }.resolve("resources")

fun Project.isLibrary() = projectDir.path.let { Paths.get(it) }.startsWith(libsFolder)

apply<AggregateTestMetricsConventions>()

allprojects {

    project.extra["gitVersion"] = gitVersion

    group = projectGroup
    version = currentVersion

    repositories {
        RepositoryConfiguration.Modules.apply(this, project)
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
    }

    apply<IdeaPlugin>()
    idea { module { inheritOutputDirs = true } }

    apply<TestTaskConventions>()

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }

    if (isLibrary()) {
        apply<MavenPublishConvention>()
    }

    apply<MinimumDependencyVersionConventions>()
    configure<MinimumDependencyVersionConventions.Extension> {
        val apacheCommonsCompress = MinimumDependencyVersion(group = "org.apache.commons", name = "commons-compress", minimumVersion = "1.26.0")
        knownVulnerableDependencies.set(
            listOf(
                apacheCommonsCompress
            )
        )
    }
}

val containerBasedServiceTest: Task = tasks.register("containerBasedServiceTest").get()