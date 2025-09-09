import sollecitom.plugins.RepositoryConfiguration

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
    google {
        mavenContent {
            includeGroupAndSubgroups("androidx")
            includeGroupAndSubgroups("com.android")
            includeGroupAndSubgroups("com.google")
        }
    }
    mavenCentral()
    gradlePluginPortal()
}

plugins {
    alias(libs.plugins.kotlinMultiplatform) // TODO remove?
}