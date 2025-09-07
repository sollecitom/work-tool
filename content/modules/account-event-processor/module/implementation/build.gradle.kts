dependencies {
    api(libs.swissknife.service.domain)
    api(libs.swissknife.pulsar.utils)

    implementation(projects.modulesAccountEventProcessorAdaptersDrivingPulsar)
    implementation(projects.modulesAccountEventProcessorAdaptersDrivenPulsar)
    implementation(projects.modulesAccountEventProcessorApplicationModel)
    implementation(libs.swissknife.logger.core)
    implementation(libs.pillar.messaging.domain)

    implementation(libs.swissknife.kotlin.extensions)
}