import sollecitom.plugins.RepositoryConfiguration

buildscript {
    repositories {
        mavenLocal()
    }

    dependencies {
        classpath(libs.sollecitom.gradle.plugins.kotlin.jvm)
    }
}

repositories {
    RepositoryConfiguration.BuildScript.apply(this)
}

plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    api(libs.swissknife.kotlin.extensions)
}