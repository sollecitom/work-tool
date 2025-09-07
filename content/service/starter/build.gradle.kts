import com.palantir.gradle.gitversion.VersionDetails
import sollecitom.plugins.conventions.task.jib.JibDockerBuildConvention

dependencies {
    api(projects.configuration)
    api(libs.swissknife.web.service.domain)
    api(projects.contractsHttp)
    api(libs.pillar.messaging.conventions)

    implementation(libs.pillar.acme.business.domain)
    implementation(projects.configuration)
    implementation(libs.pillar.service.logging)
    implementation(libs.pillar.prometheus.micrometer.registry)
    implementation(libs.swissknife.web.openapi.utils)
    implementation(libs.swissknife.sql.postgres.utils)
    implementation(libs.swissknife.pulsar.utils)
    implementation(libs.swissknife.kotlin.extensions)
    implementation(libs.pillar.web.api.utils)
    implementation(libs.swissknife.opentelemetry.core)
    implementation(libs.swissknife.opentelemetry.exporter.oltp)

    implementation(projects.modulesDepositEventEndpointModuleImplementation)
    implementation(projects.modulesPaymentCommandEndpointModuleImplementation)
}

apply<JibDockerBuildConvention>()

val serviceName = "modulith-example-service"
val dockerRemoteRepository: String by project.extra
val dockerBaseImageParam: String by project.extra
val gitVersion: VersionDetails by project.extra
val mainAppPort = "8081"
val healthAppPort = "8082"
val tmpVolume = "/tmp"
val maxRamPercentage = "70.000000"

configure<JibDockerBuildConvention.Extension> {
    serviceImageName = "$dockerRemoteRepository/$serviceName"
    starterClassFullyQualifiedName = "sollecitom.services.modulith_example.service.starter.StarterKt"
    dockerBaseImage = dockerBaseImageParam
    reproducibleBuild = true
    volumes = listOf(tmpVolume)
    gitVersion.gitHashFull?.let {
        tags = listOf(it)
    }
    args = listOf(
        "-Djava.security.egd=file:/dev/./urandom"
    )
    jvmFlags = listOf(
        "-XX:+UnlockExperimentalVMOptions",
        "-XX:+HeapDumpOnOutOfMemoryError",
        "-XX:HeapDumpPath=$tmpVolume/java_pid<pid>.hprof",
        "-XX:MaxRAMPercentage=$maxRamPercentage",
        "-XX:+UseZGC",
        "-XX:+AlwaysPreTouch",
        "-XX:+UseNUMA"
    )
    environment = mutableMapOf(
        "SERVICE_PORT" to mainAppPort,
        "HEALTH_PORT" to healthAppPort
    )
}