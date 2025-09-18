import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    val optIns = listOf("kotlin.time.ExperimentalTime")
    val optInCompilerArguments = optIns.map { "-opt-in=$it" }
    val compilerArgs = optInCompilerArguments + listOf("-Xcontext-parameters", "-Xjsr305=strict", "-Xwarning-level=CONTEXT_RECEIVERS_DEPRECATED:disabled")
    val targetJvmVersion = JvmTarget.JVM_23
    jvm {
        compilerOptions {
            jvmTarget.set(targetJvmVersion)
            javaParameters.set(true)
            freeCompilerArgs.set(compilerArgs)
            progressiveMode.set(true)
        }
    }

    compilerOptions {
        freeCompilerArgs.set(compilerArgs)
        progressiveMode.set(true)
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.swissknife.core.domain)
        }
    }
}


compose.desktop {
    application {
        mainClass = "sollecitom.compose.desktop.example.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "sollecitom.compose.desktop.example"
            packageVersion = "1.0.0"
        }
    }
}
